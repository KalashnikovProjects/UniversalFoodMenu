package com.kalashnikovprojects.ufmserver.models

import kotlinx.serialization.Serializable

@Serializable
data class FoodItemsCategorized (
    val categories: List<CategoriesWithFoodItems>,
    val noCategoryFoodItems: List<FoodItem>,
)