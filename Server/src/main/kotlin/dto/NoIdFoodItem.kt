package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("food")
data class NoIdFoodItem(
    val name: String,
    val price: Float,
    val inStock: Boolean,
    @Transient
    val imageURI: String,
)