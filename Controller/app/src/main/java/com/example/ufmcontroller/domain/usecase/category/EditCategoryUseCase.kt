package com.example.ufmcontroller.domain.usecase.category

import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.repository.CategoryRepository
import javax.inject.Inject

class EditCategoryUseCase  @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: Int,
                                category: Category,
                                foodItems: List<FoodItem> = emptyList(),
    ) {
        categoryRepository.editCategory(id, category)
        categoryRepository.setCategoryFoodRelations(id, foodItems.map { it.id })
    }
}

