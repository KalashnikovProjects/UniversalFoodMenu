package com.example.ufmcontroller.domain.usecase.screen

import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.repository.TvScreenRepository
import javax.inject.Inject

class EditScreenUseCase @Inject constructor(
    private val tvScreenRepository: TvScreenRepository
) {
    suspend operator fun invoke(id: Int, screen: TVScreen) =
        tvScreenRepository.editScreen(id, screen)
}
