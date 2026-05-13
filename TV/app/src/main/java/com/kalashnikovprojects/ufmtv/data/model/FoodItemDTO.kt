package com.kalashnikovprojects.ufmtv.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FoodItemDTO(
    val id: Int,
    val name: String,
    val price: Float,
    val imageUri: String?,
    val inStock: Boolean,
)