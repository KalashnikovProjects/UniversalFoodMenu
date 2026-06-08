package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("category_with_food")
data class CategoryWithFoodItemsDTO(
    val category: CategoryDTO,
    val foodItems: List<FoodItemDTO>,
) : DesignableDTO {
    override fun toEntity(): CategoryWithFoodItems {
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