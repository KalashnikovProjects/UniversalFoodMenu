package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.usecase.category.UploadCategoryUseCase
import com.example.ufmcontroller.domain.usecase.food.GetFoodItemsUseCase
import com.example.ufmcontroller.presentation.viewmodel.fieldsstates.CategoryFieldsStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AddCategoryUiState(
    val foodItems: List<FoodItem> = emptyList()
)


@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val getFoodItemsUseCase: GetFoodItemsUseCase,
    private val uploadCategoryUseCase: UploadCategoryUseCase,
    ) : ViewModel() {
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

    val uiState: StateFlow<AddCategoryUiState> = filteredFoodItemsFlow.map { items ->
        AddCategoryUiState(
            foodItems = items,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AddCategoryUiState(
            foodItems = emptyList(),
        ),
    )

    fun upload() {
        viewModelScope.launch {
            val priceText = categoryFieldsStates.price.text.toString()
            if (categoryFieldsStates.name.text.toString().isNotEmpty()) {
                uploadCategoryUseCase(
                    Category(
                        0,
                        categoryFieldsStates.name.text.toString(),
                        categoryFieldsStates.imageUri.value,
                        if (!priceText.isEmpty()) priceText.toFloat() else null,
                        inStock = true,
                    ),
                    foodItemsIds = categoryFieldsStates.selectedFoodItems.value.toList(),
                )
            }
        }

    }
}