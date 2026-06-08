package com.example.ufmcontroller.domain.usecase.category

import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.repository.CategoryRepository
import javax.inject.Inject

class EditCategoryUseCase  @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: Int,
                                category: Category,
                                foodItemsIds: List<Int> = emptyList(),
                                changedImage: Boolean,
    ) {
        categoryRepository.editCategory(id, category, changedImage)
        categoryRepository.setCategoryFoodRelations(id, foodItemsIds)
    }
}

