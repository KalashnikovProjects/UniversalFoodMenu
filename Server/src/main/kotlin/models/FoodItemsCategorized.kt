package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class FoodItemsCategorized (
    val categories: List<CategoriesWithFoodItems>,
    val noCategoryFoodItems: List<FoodItem>,
)