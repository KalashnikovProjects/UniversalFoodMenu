package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.defaultFoodItem
import com.example.ufmcontroller.domain.usecase.category.GetCategoriesByFoodIdUseCase
import com.example.ufmcontroller.domain.usecase.category.GetCategoriesUseCase
import com.example.ufmcontroller.domain.usecase.category.UpdateFoodRelationsForCategoriesUseCase
import com.example.ufmcontroller.domain.usecase.food.EditFoodItemUseCase
import com.example.ufmcontroller.domain.usecase.food.GetFoodItemUseCase
import com.example.ufmcontroller.domain.usecase.food.UploadFoodItemUseCase
import com.example.ufmcontroller.presentation.viewmodel.fieldsstates.FoodItemFieldsStates
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject



data class EditFoodItemUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
)


@HiltViewModel(assistedFactory = EditFoodItemViewModel.Factory::class)
class EditFoodItemViewModel @AssistedInject constructor(
    private val getFoodItemUseCase: GetFoodItemUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getCategoriesByFoodIdUseCase: GetCategoriesByFoodIdUseCase,

    private val editFoodItemUseCase: EditFoodItemUseCase,
    @Assisted private val id: Int
    ) : ViewModel() {

    private var foodItem: FoodItem = defaultFoodItem()
    private var categories: List<Category> = emptyList()

    val foodItemFieldsStates: FoodItemFieldsStates = FoodItemFieldsStates()

    private val _categoriesFlow = getCategoriesUseCase()
    private val searchQueryFlow = snapshotFlow { foodItemFieldsStates.categoriesSearch.text }

    private val filteredCategoriesFlow = combine(
        _categoriesFlow,
        searchQueryFlow,
    ) { items, query ->
        if (query.isBlank()) {
            items
        } else {
            items.filter {
                it.name.contains(
                    query,
                    ignoreCase = true
                )
            }
        }
    }
    private val _isLoading = MutableStateFlow(true)

    val uiState: StateFlow<EditFoodItemUiState> = combine(
        filteredCategoriesFlow,
        _isLoading
    ) { items, loading ->
        EditFoodItemUiState(
            categories = items,
            isLoading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EditFoodItemUiState()
    )

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            foodItem = getFoodItemUseCase(id).first()
            foodItemFieldsStates.name.edit {
                replace(0, length, foodItem.name)
            }
            foodItemFieldsStates.price.edit {
                replace(0, length, foodItem.price.toString())
            }
            foodItemFieldsStates.imageUri.value = foodItem.imageUri
            categories = getCategoriesByFoodIdUseCase(id).first()

            foodItemFieldsStates.selectedCategories.value = categories.map { it.id }.toSet()
            _isLoading.value = false
        }
    }

    fun edit() {
        viewModelScope.launch {
            editFoodItemUseCase(
                id,
                FoodItem(
                    id,
                    foodItemFieldsStates.name.text.toString(),
                    foodItemFieldsStates.price.text.toString().toFloat(),
                    foodItemFieldsStates.imageUri.value,
                    inStock = true,
                ),
                categories = uiState.value.categories.filter {
                    foodItemFieldsStates.selectedCategories.value.contains(it.id)
                },
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(id: Int): EditFoodItemViewModel
    }
}