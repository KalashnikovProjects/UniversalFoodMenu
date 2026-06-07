package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.domain.entity.Designable
import com.example.ufmcontroller.domain.entity.FoodItem
import kotlinx.serialization.Serializable

@Serializable
data class CategoryWithFoodItemsDTO(
    val category: CategoryDTO,
    val foodItems: List<FoodItemDTO>,
) : DesignableDTO {
    override fun toEntity(): Designable {
        return CategoryWithFoodItems(
            category = category.toEntity().category,
            foodItems = foodItems.map { it.toEntity() }
        )
    }
}


fun CategoryWithFoodItemsDTO.toDTO(): CategoryWithFoodItemsDTO {
    return CategoryWithFoodItemsDTO(
        category = category,
        foodItems = emptyList(),
    )
}