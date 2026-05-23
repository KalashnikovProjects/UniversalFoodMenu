package com.example.ufmcontroller.domain.usecase.category

import com.example.ufmcontroller.domain.repository.CategoryRepository
import javax.inject.Inject

class UpdateFoodRelationsForCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: Int, categoriesIds: List<Int>) =
        categoryRepository.updateFoodRelationsForCategories(id, categoriesIds)
}
