package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.CategoryWithFoodItems
import com.kalashnikovprojects.ufmtv.domain.entity.Designable
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