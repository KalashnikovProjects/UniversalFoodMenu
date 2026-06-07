package com.example.ufmcontroller.domain.usecase.screen

import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.repository.TvScreenRepository
import javax.inject.Inject

class InputCodeUseCase @Inject constructor(
    private val tvScreenRepository: TvScreenRepository
) {
    suspend operator fun invoke(code: String): TVScreen =
        tvScreenRepository.inputCodeForTvAuth(code)
}
