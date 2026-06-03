package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.FoodItemsCategorized
import com.example.ufmcontroller.domain.usecase.category.GetCategoriesUseCase
import com.example.ufmcontroller.domain.usecase.food.GetFoodItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

enum class EditMenuTab {
    FOOD, CATEGORIES
}

data class EditMenuUiState(
    val editMenuTab: EditMenuTab = EditMenuTab.FOOD,
    val foodItems: List<FoodItem> = emptyList(),
    val categories: List<Category> = emptyList(),
    )

@HiltViewModel
class EditMenuViewModel @Inject constructor(
    private val getFoodItemsUseCase: GetFoodItemsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,

    ) : ViewModel() {
    private val foodItemsFlow = getFoodItemsUseCase()
    private val categoriesFlow = getCategoriesUseCase()
    private val editMenuTabFlow = MutableStateFlow<EditMenuTab>(EditMenuTab.FOOD)

    val foodSearchState: TextFieldState = TextFieldState("")
    val categoriesSearchState: TextFieldState = TextFieldState("")

    private val foodSearchQueryFlow = snapshotFlow { categoriesSearchState.text }
    private val categoriesQueryFlow = snapshotFlow { categoriesSearchState.text }

    private val filteredFoodItemsFlow = combine(
        foodItemsFlow,
        foodSearchQueryFlow,
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
    private val filteredCategoriesFlow = combine(
        categoriesFlow,
        categoriesQueryFlow,
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

    val uiState: StateFlow<EditMenuUiState> = combine(
        editMenuTabFlow,
        filteredFoodItemsFlow,
        filteredCategoriesFlow,
    ) { tab, food, categories ->
        EditMenuUiState(
            tab,
            food,
            categories,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EditMenuUiState(
            editMenuTab = EditMenuTab.FOOD,
            foodItems = emptyList(),
            categories = emptyList(),
        )
    )

    fun selectTab(tab: EditMenuTab) {
        editMenuTabFlow.value = tab
    }
}