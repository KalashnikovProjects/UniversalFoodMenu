package com.example.ufmcontroller.domain.usecase.screen

import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.repository.TvScreenRepository
import javax.inject.Inject

class GetScreenUseCase @Inject constructor(
    private val tvScreenRepository: TvScreenRepository
) {
    operator fun invoke(id: Int) = tvScreenRepository.getScreen(id)
}
