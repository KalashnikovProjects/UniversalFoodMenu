package com.example.ufmcontroller.domain.usecase.category

import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesByFoodIdUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(foodId: Int): Flow<List<Category>> =
        categoryRepository.getCategoriesByFoodId(foodId)
}
