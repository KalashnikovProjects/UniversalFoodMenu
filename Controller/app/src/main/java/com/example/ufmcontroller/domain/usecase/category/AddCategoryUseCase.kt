package com.example.ufmcontroller.domain.usecase.category

import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.repository.CategoryRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Category =
        categoryRepository.addCategory(category)
}
