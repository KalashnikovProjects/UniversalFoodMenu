package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.domain.entity.FoodItemsCategorized
import com.example.ufmcontroller.domain.usecase.category.ToggleCategoryUseCase
import com.example.ufmcontroller.domain.usecase.food.GetCategorizedFoodItemsUseCase
import com.example.ufmcontroller.domain.usecase.food.ToggleFoodItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val items: FoodItemsCategorized = FoodItemsCategorized(
        emptyList(),
        emptyList()
    ),
    val openedIds: Set<Int>,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategorizedFoodItemsUseCase: GetCategorizedFoodItemsUseCase,
    private val toggleFoodItemUseCase: ToggleFoodItemUseCase,
    private val toggleCategoryUseCase: ToggleCategoryUseCase,
    ) : ViewModel() {

    private val itemsFlow = getCategorizedFoodItemsUseCase()
    private val openedIdsFlow = MutableStateFlow(emptySet<Int>())
    val searchState: TextFieldState = TextFieldState("")
    private val searchQueryFlow = snapshotFlow { searchState.text }

    private val filteredItemsFlow = combine(
        itemsFlow,
        searchQueryFlow,
    ) { items, query ->
        if (query.isBlank()) {
            items
        } else {
            FoodItemsCategorized(
                categories = items.categories.map { category ->
                    if (category.category.name.contains(query, ignoreCase = true)) {
                        category
                    } else {
                        CategoryWithFoodItems(
                            category.category,
                            category.foodItems.filter {
                                it.name.contains(
                                    query,
                                    ignoreCase = true
                                )
                            }
                        )
                    }
                },
                noCategoryFoodItems = items.noCategoryFoodItems.filter {
                    it.name.contains(
                        query,
                        ignoreCase = true
                    )
                },
            )
        }
    }

    val uiState: StateFlow<HomeUiState> = combine(
        filteredItemsFlow,
        openedIdsFlow,
        ) { items, openedIds ->
        HomeUiState(
            items = items,
            openedIds=openedIds,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(
            items = FoodItemsCategorized(
                emptyList(),
                emptyList()
            ),
            openedIds=emptySet(),
        )
    )


    fun toggleFoodItem(id: Int) {
        viewModelScope.launch {
            toggleFoodItemUseCase(id)
        }
    }

    fun toggleCategory(id: Int) {
        viewModelScope.launch {
            toggleCategoryUseCase(id)
        }
    }


    fun switchCategoryVisibility(id: Int) {
        viewModelScope.launch {
            openedIdsFlow.update {
                if (it.contains(id)) {
                    it - id
                } else {
                    it + id
                }
            }
        }
    }
}