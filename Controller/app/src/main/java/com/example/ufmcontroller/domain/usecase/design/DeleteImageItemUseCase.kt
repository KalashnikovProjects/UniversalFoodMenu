package com.example.ufmcontroller.domain.usecase.design

import com.example.ufmcontroller.domain.repository.DesignRepository
import javax.inject.Inject

class DeleteImageItemUseCase @Inject constructor(
    private val designRepository: DesignRepository
) {
    suspend operator fun invoke(id: Int) =
        designRepository.deleteImageItem(id)
}
