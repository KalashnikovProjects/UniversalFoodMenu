package com.example.ufmcontroller.domain.usecase.category

import com.example.ufmcontroller.domain.repository.CategoryRepository
import com.example.ufmcontroller.domain.repository.FoodRepository
import javax.inject.Inject

class ToggleCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: Int) =
        categoryRepository.toggleCategory(id)
}
