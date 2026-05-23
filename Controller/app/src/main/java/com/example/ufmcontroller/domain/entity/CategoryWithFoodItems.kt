package com.example.ufmcontroller.domain.entity

data class CategoryWithFoodItems(
    val category: Category,
    val foodItems: List<FoodItem>,
) : Designable