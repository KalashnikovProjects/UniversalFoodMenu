package com.kalashnikovprojects.ufmserver.models

import kotlinx.serialization.Serializable


@Serializable
data class CategoriesWithFoodItems(
    val category: Category,
    val foodItems: List<FoodItem>
)