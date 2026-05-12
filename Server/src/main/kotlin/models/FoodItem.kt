package com.kalashnikovprojects.ufmserver.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.String

@Serializable
@SerialName("food")
data class FoodItem(
    val id: Int,
    val name: String,
    val price: Float,
    val imageUri: String?,
    val inStock: Boolean,
) : Designable

fun FoodItem.toNoIdFoodItem() = NoIdFoodItem(
    name = name,
    price =price,
    imageUri = imageUri,
    inStock = inStock,
)


@Serializable
data class NoIdFoodItem(
    val name: String,
    val price: Float,
    @Transient
    var imageUri: String?,
    val inStock: Boolean,
) : Designable

fun NoIdFoodItem.toFoodItem(id: Int) = FoodItem(
    id =id,
    name = name,
    price =price,
    imageUri = imageUri,
    inStock = inStock,
)