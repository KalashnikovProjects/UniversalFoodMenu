package com.example.ufmcontroller.domain.usecase.screen

import com.example.ufmcontroller.domain.entity.TVScreenWithDesignItems
import com.example.ufmcontroller.domain.repository.GetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScreensWithDesignItemsUseCase @Inject constructor(
    private val getRepository: GetRepository
) {
    operator fun invoke(): Flow<List<TVScreenWithDesignItems>> =
        getRepository.getScreensWithDesignItems()
}
