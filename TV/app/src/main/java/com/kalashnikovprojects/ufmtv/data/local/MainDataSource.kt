package com.kalashnikovprojects.ufmtv.data.local

import com.kalashnikovprojects.ufmtv.data.model.TVScreenDTO
import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenStyle
import com.kalashnikovprojects.ufmtv.domain.entity.TVScreen
import com.kalashnikovprojects.ufmtv.domain.entity.defaultScreenStyle
import com.kalashnikovprojects.ufmtv.domain.entity.defaultTVScreen
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainDataSource {
    private val _designItems = MutableStateFlow<List<DesignItem>>(emptyList())
    val designItems: StateFlow<List<DesignItem>> = _designItems.asStateFlow()
    private val _currentScreen = MutableStateFlow(defaultTVScreen())
    val currentScreen: StateFlow<TVScreen> = _currentScreen.asStateFlow()
    private val _foodItems = MutableStateFlow<List<FoodItem>>(emptyList())
    val foodItems = _foodItems.asStateFlow()

    fun updateDesignItemsList(items: List<DesignItem>) {
        _designItems.value = items
    }

    fun updateCurrentScreen(screen: TVScreen) {
        _currentScreen.value = screen
    }

    fun updateFoodItems(foodItems: List<FoodItem>) {
        _foodItems.value = foodItems
    }
}