package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("food")
data class ResponseFoodItem(
    val id: Int,
    val name: String,
    val price: Float,
    val imageUri: String,
    val inStock: Boolean,
) : InputDesignItem