package com.example.ufmcontroller.domain.usecase.design

import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.repository.GetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDesignItemsByScreenUseCase @Inject constructor(
    private val getRepository: GetRepository
) {
    operator fun invoke(screenId: Int): Flow<List<DesignItem>> =
        getRepository.getDesignItemsByScreenId(screenId)
}
