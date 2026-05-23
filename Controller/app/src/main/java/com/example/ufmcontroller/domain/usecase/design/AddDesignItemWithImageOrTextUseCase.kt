package com.example.ufmcontroller.domain.usecase.design

import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.repository.DesignRepository
import javax.inject.Inject


// TODO: неуверен, что необходимо отделять это от основого, потом возможно удалить (хотя вряд ли, над post Image и post Text делать)
class AddDesignItemWithImageOrTextUseCase @Inject constructor(
    private val designRepository: DesignRepository
) {
    suspend operator fun invoke(designItem: DesignItem): DesignItem =
        designRepository.addDesignItemWithImageOrText(designItem)
}
