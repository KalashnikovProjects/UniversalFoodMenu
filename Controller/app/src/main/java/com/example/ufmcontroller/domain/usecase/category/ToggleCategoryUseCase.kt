package com.example.ufmcontroller.domain.usecase.category

import com.example.ufmcontroller.domain.repository.CategoryRepository
import javax.inject.Inject

class ToggleCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: Int, boolean: Boolean) =
        categoryRepository.toggleCategory(id, boolean)
}
