package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.Category
import com.kalashnikovprojects.ufmtv.domain.entity.CategoryWithFoodItems
import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import com.kalashnikovprojects.ufmtv.domain.entity.Designable
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDTO(
    val id: Int,
    val name: String,
    val imageUri: String?,
    val price: Float?,
    val inStock: Boolean?,
) : DesignableDTO {
    override fun toEntity(): CategoryWithFoodItems {
        return CategoryWithFoodItems(category = Category(
                id = id,
                name = name,
                imageUri=imageUri,
                price=price,
                inStock=inStock,
            ),
            foodItems = emptyList(),
        )
    }
}