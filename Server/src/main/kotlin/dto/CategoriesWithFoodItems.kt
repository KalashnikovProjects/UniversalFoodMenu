package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable


@Serializable
data class CategoriesWithFoodItems(
    val id: Int,
    val name: String,
    val foodItems: List<FoodItem>
)