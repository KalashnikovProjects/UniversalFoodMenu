package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.model.FoodItem
import com.example.ufmcontroller.domain.usecase.AddFoodItemUseCase
import com.example.ufmcontroller.domain.usecase.EditFoodItemUseCase
import com.example.ufmcontroller.domain.usecase.GetFoodItemsUseCase
import com.example.ufmcontroller.domain.usecase.ToggleFoodItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    getFoodItemsUseCase: GetFoodItemsUseCase,
    private val toggleFoodItemUseCase: ToggleFoodItemUseCase,
    private val addFoodItemUseCase: AddFoodItemUseCase,
    private val editFoodItemUseCase: EditFoodItemUseCase,
) : ViewModel() {
    val foodItems: StateFlow<List<FoodItem>> = getFoodItemsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
    val searchState = TextFieldState("")

    val filteredFoodItems = snapshotFlow { searchState.text }
        .combine(foodItems) { query, items ->
            if (query.isBlank()) {
                items
            } else {
                items.filter { it.name.contains(query, ignoreCase = true) }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
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