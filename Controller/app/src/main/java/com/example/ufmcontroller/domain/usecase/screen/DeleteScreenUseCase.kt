package com.example.ufmcontroller.domain.usecase.screen

import com.example.ufmcontroller.domain.repository.TvScreenRepository
import javax.inject.Inject

class DeleteScreenUseCase @Inject constructor(
    private val tvScreenRepository: TvScreenRepository,
) {
    suspend operator fun invoke(id: Int) =
        tvScreenRepository.deleteScreen(id)
}
