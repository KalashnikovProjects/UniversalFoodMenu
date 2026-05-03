package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.String

@Serializable
@SerialName("food")
data class FoodItem(
    val id: Int,
    val name: String,
    val price: Float,
    val imageUri: String,
    val inStock: Boolean,
) : Designable

fun FoodItem.toNoIdFoodItem() = NoIdFoodItem(
    name = name,
    price=price,
    imageURI = imageUri,
    inStock = inStock,
)