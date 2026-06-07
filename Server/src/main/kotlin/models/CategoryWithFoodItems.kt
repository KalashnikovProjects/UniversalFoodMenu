package com.kalashnikovprojects.ufmserver.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("category_with_food")
data class CategoryWithFoodItems(
    val category: Category,
    val foodItems: List<FoodItem>
)