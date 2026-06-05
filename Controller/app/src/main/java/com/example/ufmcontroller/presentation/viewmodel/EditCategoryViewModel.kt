package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.defaultCategory
import com.example.ufmcontroller.domain.entity.defaultFoodItem
import com.example.ufmcontroller.domain.usecase.category.EditCategoryUseCase
import com.example.ufmcontroller.domain.usecase.category.GetCategoriesByFoodIdUseCase
import com.example.ufmcontroller.domain.usecase.category.GetCategoriesUseCase
import com.example.ufmcontroller.domain.usecase.category.GetCategoryUseCase
import com.example.ufmcontroller.domain.usecase.category.UpdateFoodRelationsForCategoriesUseCase
import com.example.ufmcontroller.domain.usecase.food.EditFoodItemUseCase
import com.example.ufmcontroller.domain.usecase.food.GetFoodItemUseCase
import com.example.ufmcontroller.domain.usecase.food.GetFoodItemsByCategoryIdUseCase
import com.example.ufmcontroller.domain.usecase.food.GetFoodItemsUseCase
import com.example.ufmcontroller.domain.usecase.food.UploadFoodItemUseCase
import com.example.ufmcontroller.presentation.viewmodel.fieldsstates.CategoryFieldsStates
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



data class EditCategoryUiState(
    val foodItems: List<FoodItem> = emptyList(),
    val isLoading: Boolean = false,
)


@HiltViewModel(assistedFactory = EditCategoryViewModel.Factory::class)
class EditCategoryViewModel @AssistedInject constructor(
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getFoodItemsUseCase: GetFoodItemsUseCase,
    private val getFoodItemsByCategoryIdUseCase: GetFoodItemsByCategoryIdUseCase,

    private val editCategoryUseCase: EditCategoryUseCase,
    @Assisted private val id: Int
    ) : ViewModel() {

    private var category: Category = defaultCategory()
    private var foodItems: List<FoodItem> = emptyList()

    val categoryFieldsStates: CategoryFieldsStates = CategoryFieldsStates()
    private val _foodItemsFlow = getFoodItemsUseCase()
    private val searchQueryFlow = snapshotFlow { categoryFieldsStates.foodItemsSearch.text }

    private val filteredFoodItemsFlow = combine(
        _foodItemsFlow,
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

    val uiState: StateFlow<EditCategoryUiState> = combine(
        filteredFoodItemsFlow,
        _isLoading
    ) { items, loading ->
        EditCategoryUiState(
            foodItems = items,
            isLoading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EditCategoryUiState()
    )

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            category = getCategoryUseCase(id).first()
            categoryFieldsStates.name.edit {
                replace(0, length, category.name)
            }
            categoryFieldsStates.price.edit {
                replace(0, length, category.price.toString())
            }
            categoryFieldsStates.imageUri.value = category.imageUri
            foodItems = getFoodItemsByCategoryIdUseCase(id).first()

            categoryFieldsStates.selectedFoodItems.value = foodItems.map { it.id }.toSet()
            _isLoading.value = false
        }
    }

    fun edit() {
        viewModelScope.launch {
            val priceText = categoryFieldsStates.price.text.toString()
            editCategoryUseCase(
                id,
                Category(
                    id,
                    categoryFieldsStates.name.text.toString(),
                    categoryFieldsStates.imageUri.value,
                    if (priceText.isEmpty()) null else priceText.toFloat(),
                    inStock = null,
                ),
                foodItems = uiState.value.foodItems.filter {
                    categoryFieldsStates.selectedFoodItems.value.contains(it.id)
                },
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(id: Int): EditCategoryViewModel
    }
}