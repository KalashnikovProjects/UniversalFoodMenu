package com.example.ufmcontroller.domain.usecase.category

import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.repository.GetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val getRepository: GetRepository
) {
    operator fun invoke(id: Int): Flow<Category> =
        getRepository.getCategory(id)
}
