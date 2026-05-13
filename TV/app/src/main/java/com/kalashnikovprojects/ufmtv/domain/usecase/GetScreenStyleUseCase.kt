package com.kalashnikovprojects.ufmtv.domain.usecase

import com.kalashnikovprojects.ufmtv.domain.model.ScreenStyle
import com.kalashnikovprojects.ufmtv.domain.repository.ScreenStyleRepository
import kotlinx.coroutines.flow.Flow

class GetScreenStyleUseCase(private val repository: ScreenStyleRepository) {
    operator fun invoke(): Flow<ScreenStyle> = repository.getScreenStyle()
}