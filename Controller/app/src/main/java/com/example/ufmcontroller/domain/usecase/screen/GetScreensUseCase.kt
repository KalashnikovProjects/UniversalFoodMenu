package com.example.ufmcontroller.domain.usecase.screen

import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.entity.TVScreenWithDesignItems
import com.example.ufmcontroller.domain.repository.TvScreenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScreensWithDesignItemsUseCase @Inject constructor(
    private val tvScreenRepository: TvScreenRepository
) {
    operator fun invoke(): Flow<List<TVScreenWithDesignItems>> =
        tvScreenRepository.getScreensWithDesignItems()
}
