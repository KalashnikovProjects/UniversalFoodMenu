package com.kalashnikovprojects.ufmserver.models

import kotlinx.serialization.Serializable


@Serializable
data class CategoriesWithFoodItems(
    val id: Int,
    val name: String,
    val imageUri: String?,
    val price: Float?,
    val inStock: Boolean,
    val foodItems: List<FoodItem>
)