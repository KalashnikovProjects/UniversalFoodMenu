package com.kalashnikovprojects.ufmserver.dto

import com.kalashnikovprojects.ufmserver.data.tables.FoodItems
import kotlinx.serialization.Serializable

@Serializable
data class CategorizedResponse (
    val categories: List<CategoriesWithFoodItems>,
    val noCategoryFoodItems: List<FoodItem>,
)