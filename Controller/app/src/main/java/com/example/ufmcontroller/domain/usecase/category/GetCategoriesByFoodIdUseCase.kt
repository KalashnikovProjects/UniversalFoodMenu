package com.example.ufmcontroller.domain.usecase.category

import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.repository.GetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesByFoodIdUseCase @Inject constructor(
    private val getRepository: GetRepository
) {
    operator fun invoke(foodId: Int): Flow<List<Category>> =
        getRepository.getCategoriesByFoodId(foodId)
}
