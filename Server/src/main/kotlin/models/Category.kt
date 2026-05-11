package com.kalashnikovprojects.ufmserver.dto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("category")
data class Category(
    val id: Int,
    val name: String,
    val imageUri: String?,
    val price: Float?,
    val inStock: Boolean,
) : Designable

fun Category.toNoIdCategory() = NoIdCategory(
    name = name,
    imageUri = imageUri,
    price = price,
    inStock = inStock,
)

fun Category.toCategoryWithFoodItems(foodItems: List<FoodItem>) = CategoriesWithFoodItems(
    id=id,
    name = name,
    foodItems = foodItems,
)

@Serializable
data class NoIdCategory(
    val name: String,
    var imageUri: String?,
    val inStock: Boolean,
    val price: Float?
)

fun NoIdCategory.toCategory(id: Int) = Category(
    id=id,
    name = name,
    imageUri = imageUri,
    price = price,
    inStock = inStock,
)