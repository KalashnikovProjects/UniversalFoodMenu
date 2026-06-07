package com.example.ufmcontroller.domain.usecase.screen

import com.example.ufmcontroller.domain.repository.GetRepository
import com.example.ufmcontroller.domain.repository.TvScreenRepository
import javax.inject.Inject

class GetScreenWithDesignItemsUseCase @Inject constructor(
    private val getRepository: GetRepository
) {
    operator fun invoke(id: Int) = getRepository.getScreenWithDesignItems(id)
}
