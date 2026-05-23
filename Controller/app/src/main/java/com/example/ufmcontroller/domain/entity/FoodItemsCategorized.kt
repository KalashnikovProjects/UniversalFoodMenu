package com.example.ufmcontroller.domain.entity

data class FoodItemsCategorized (
    val categories: List<CategoryWithFoodItems>,
    val noCategoryFoodItems: List<FoodItem>,
)