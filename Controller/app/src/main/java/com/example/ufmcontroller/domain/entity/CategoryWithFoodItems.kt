package com.example.ufmcontroller.domain.entity

import com.example.ufmcontroller.data.model.CategoryWithFoodItemsDTO
import com.example.ufmcontroller.data.model.DesignableDTO

data class CategoryWithFoodItems(
    val category: Category,
    val foodItems: List<FoodItem>,
) : Designable {
    override fun toDTO(): DesignableDTO {
        return CategoryWithFoodItemsDTO(
            category = category.toDTO(),
            foodItems = foodItems.map { it.toDTO() },
        )
    }
}