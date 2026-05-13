package com.kalashnikovprojects.ufmtv.domain.usecase

import com.kalashnikovprojects.ufmtv.domain.repository.DesignItemsRepository
import com.kalashnikovprojects.ufmtv.domain.model.DesignItem
import kotlinx.coroutines.flow.Flow

class GetDesignItemsUseCase(private val repository: DesignItemsRepository) {
    operator fun invoke(): Flow<List<DesignItem>> = repository.getDesignItems()
}