package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.usecase.food.AddFoodItemUseCase
import com.example.ufmcontroller.domain.usecase.food.EditFoodItemUseCase
import com.example.ufmcontroller.domain.usecase.food.GetFoodItemsUseCase
import com.example.ufmcontroller.domain.usecase.food.ToggleFoodItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainScreenUiState(
    val foodItems: List<FoodItem> = emptyList(),
    val filteredFoodItems: List<FoodItem> = emptyList<FoodItem>()
)

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    getFoodItemsUseCase: GetFoodItemsUseCase,
    private val toggleFoodItemUseCase: ToggleFoodItemUseCase,
    private val addFoodItemUseCase: AddFoodItemUseCase,
    private val editFoodItemUseCase: EditFoodItemUseCase,
) : ViewModel() {

    private val foodItemsFlow = getFoodItemsUseCase()
    private val searchQueryFlow = snapshotFlow { searchState.text }

    val uiState: StateFlow<MainScreenUiState> = combine(
        foodItemsFlow,
        searchQueryFlow
    ) { items, query ->
        val filtered = if (query.isBlank()) {
            items
        } else {
            items.filter { it.name.contains(query, ignoreCase = true) }
        }

        MainScreenUiState(
            foodItems = items,
            filteredFoodItems = filtered
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainScreenUiState()
    )

    val searchState: TextFieldState = TextFieldState("")

    fun toggleFoodItem(id: Int) {
        viewModelScope.launch {
            toggleFoodItemUseCase(id)
        }
    }

    fun addFoodItem(foodItem: FoodItem) {
        viewModelScope.launch {
            addFoodItemUseCase(foodItem)
        }
    }

    fun editFoodItem(id: Int, foodItem: FoodItem) {
        viewModelScope.launch {
            editFoodItemUseCase(id, foodItem)
        }
    }
}