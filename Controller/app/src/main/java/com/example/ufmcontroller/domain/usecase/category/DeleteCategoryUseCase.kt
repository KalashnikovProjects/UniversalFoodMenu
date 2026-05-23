package com.example.ufmcontroller.domain.usecase.category

import com.example.ufmcontroller.domain.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: Int) =
        categoryRepository.deleteCategory(id)
}
