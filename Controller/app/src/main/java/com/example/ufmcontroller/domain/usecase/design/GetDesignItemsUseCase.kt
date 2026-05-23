package com.example.ufmcontroller.domain.usecase.design

import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.repository.DesignRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDesignItemsUseCase @Inject constructor(
    private val designRepository: DesignRepository
) {
    operator fun invoke(): Flow<List<DesignItem>> =
        designRepository.getDesignItems()
}
