package com.kalashnikovprojects.ufmserver.dto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("category")
class Category(
    val id: Int,
    val name: String,
) : Designable

fun Category.toNoIdCategory() = NoIdCategory(
    name = name,
)

fun Category.toCategoryWithFoodItems(foodItems: List<FoodItem>) = CategoriesWithFoodItems(
    id=id,
    name = name,
    foodItems = foodItems,
)

@Serializable
data class NoIdCategory(
    val name: String,
)

fun NoIdCategory.toCategory(id: Int) = Category(
    id=id,
    name = name,
)