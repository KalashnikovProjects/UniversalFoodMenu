package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.defaultCategory
import com.example.ufmcontroller.domain.entity.defaultFoodItem
import com.example.ufmcontroller.domain.usecase.category.DeleteCategoryUseCase
import com.example.ufmcontroller.domain.usecase.category.EditCategoryUseCase
import com.example.ufmcontroller.domain.usecase.category.GetCategoriesByFoodIdUseCase
import com.example.ufmcontroller.domain.usecase.category.GetCategoriesUseCase
import com.example.ufmcontroller.domain.usecase.category.GetCategoryUseCase
import com.example.ufmcontroller.domain.usecase.category.UpdateFoodRelationsForCategoriesUseCase
import com.example.ufmcontroller.domain.usecase.food.DeleteFoodItemUseCase
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
    val openedDeleteConfirmationDialog: Boolean = false
)


@HiltViewModel(assistedFactory = EditCategoryViewModel.Factory::class)
class EditCategoryViewModel @AssistedInject constructor(
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getFoodItemsUseCase: GetFoodItemsUseCase,
    private val getFoodItemsByCategoryIdUseCase: GetFoodItemsByCategoryIdUseCase,

    private val editCategoryUseCase: EditCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    @Assisted private val id: Int
    ) : ViewModel() {

    private var category: Category = defaultCategory()
    private var foodItems: List<FoodItem> = emptyList()
    private val _openedDeleteConfirmationDialog = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(true)
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


    val uiState: StateFlow<EditCategoryUiState> = combine(
        filteredFoodItemsFlow,
        _isLoading,
        _openedDeleteConfirmationDialog
    ) { items, loading, openedDeleteConfirmationDialog ->
        EditCategoryUiState(
            foodItems = items,
            isLoading = loading,
            openedDeleteConfirmationDialog=openedDeleteConfirmationDialog,
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
                replace(0, length, if (category.price != null) category.price.toString() else "")
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
            if (categoryFieldsStates.name.text.toString().isNotEmpty()) {
                editCategoryUseCase(
                    id,
                    Category(
                        id,
                        categoryFieldsStates.name.text.toString(),
                        categoryFieldsStates.imageUri.value,
                        if (priceText.isEmpty()) null else priceText.toFloat(),
                        inStock = null,
                    ),
                    foodItemsIds = categoryFieldsStates.selectedFoodItems.value.toList(),
                    changedImage = categoryFieldsStates.imageChanged.value,
                    )
            }
        }
    }

    fun setOpenedDeleteConfirmationDialog(value: Boolean) {
        viewModelScope.launch {
            _openedDeleteConfirmationDialog.value = value
        }
    }

    fun delete() {
        viewModelScope.launch {
            deleteCategoryUseCase(id)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(id: Int): EditCategoryViewModel
    }
}