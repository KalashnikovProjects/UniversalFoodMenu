package com.example.ufmcontroller.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FoodItemsCategorizedDTO (
    val categories: List<CategoryWithFoodItemsDTO>,
    val noCategoryFoodItems: List<FoodItemDTO>,
)