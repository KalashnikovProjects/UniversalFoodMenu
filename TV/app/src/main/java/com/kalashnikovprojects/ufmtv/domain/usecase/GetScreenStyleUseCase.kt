package com.kalashnikovprojects.ufmtv.domain.usecase

import com.kalashnikovprojects.ufmtv.domain.entity.ScreenStyle
import com.kalashnikovprojects.ufmtv.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class GetScreenStyleUseCase(private val repository: MainRepository) {
    operator fun invoke(): Flow<ScreenStyle> = repository.getScreenStyle()
}