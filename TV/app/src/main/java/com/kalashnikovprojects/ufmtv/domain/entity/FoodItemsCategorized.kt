package com.kalashnikovprojects.ufmtv.domain.entity

data class FoodItemsCategorized (
    val categories: List<CategoryWithFoodItems>,
    val noCategoryFoodItems: List<FoodItem>,
)