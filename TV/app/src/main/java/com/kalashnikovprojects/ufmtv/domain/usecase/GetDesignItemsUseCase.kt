package com.kalashnikovprojects.ufmtv.domain.usecase

import com.kalashnikovprojects.ufmtv.domain.repository.MainRepository
import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDesignItemsUseCase @Inject constructor(private val repository: MainRepository) {
    operator fun invoke(): Flow<List<DesignItem>> = repository.getDesignItems()
}