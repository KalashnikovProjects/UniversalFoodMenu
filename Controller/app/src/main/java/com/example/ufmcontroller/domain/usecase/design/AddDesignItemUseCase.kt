package com.example.ufmcontroller.domain.usecase.design

import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.DesignItemWithScreenId
import com.example.ufmcontroller.domain.repository.DesignRepository
import javax.inject.Inject

class AddDesignItemUseCase @Inject constructor(
    private val designRepository: DesignRepository
) {
    suspend operator fun invoke(designItem: DesignItemWithScreenId): DesignItem =
        designRepository.addDesignItem(designItem)
}
