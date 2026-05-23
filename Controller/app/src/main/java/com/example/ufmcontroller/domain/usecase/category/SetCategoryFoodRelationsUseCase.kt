package com.example.ufmcontroller.domain.usecase.category

import com.example.ufmcontroller.domain.repository.CategoryRepository
import javax.inject.Inject

class SetCategoryFoodRelationsUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: Int, foodIds: List<Int>) =
        categoryRepository.setCategoryFoodRelations(id, foodIds)
}
