package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.usecase.category.GetCategoriesUseCase
import com.example.ufmcontroller.domain.usecase.category.UpdateFoodRelationsForCategoriesUseCase
import com.example.ufmcontroller.domain.usecase.food.UploadFoodItemUseCase
import com.example.ufmcontroller.presentation.viewmodel.fieldsstates.FoodItemFieldsStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject



data class AddFoodItemUiState(
    val categories: List<Category> = emptyList()
)


@HiltViewModel
class AddFoodItemViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val uploadFoodItemUseCase: UploadFoodItemUseCase,
    ) : ViewModel() {
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

    val uiState: StateFlow<AddFoodItemUiState> = filteredCategoriesFlow.map { items ->
        AddFoodItemUiState(
            categories = items,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AddFoodItemUiState(
            categories = emptyList(),
        ),
    )

    fun upload() {
        viewModelScope.launch {
            uploadFoodItemUseCase(
                FoodItem(
                    0,
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
}