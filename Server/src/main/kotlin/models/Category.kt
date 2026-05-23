package com.kalashnikovprojects.ufmserver.models
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("category")
data class Category(
    val id: Int,
    val name: String,
    val imageUri: String?,
    val price: Float?,
    val inStock: Boolean?,
) : Designable

fun Category.toNoIdCategory() = NoIdCategory(
    name = name,
    imageUri = imageUri,
    price = price,
    inStock = inStock,
)

fun Category.toCategoryWithFoodItems(foodItems: List<FoodItem>) = CategoryWithFoodItems(
    category = this,
    foodItems = foodItems,
)

@Serializable
data class NoIdCategory(
    val name: String,
    var imageUri: String?,
    val price: Float?,
    val inStock: Boolean?,
)

fun NoIdCategory.toCategory(id: Int) = Category(
    id=id,
    name = name,
    imageUri = imageUri,
    price = price,
    inStock = inStock,
)