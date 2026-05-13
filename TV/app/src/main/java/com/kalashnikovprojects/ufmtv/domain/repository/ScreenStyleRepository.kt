package com.kalashnikovprojects.ufmtv.domain.repository

import com.kalashnikovprojects.ufmtv.domain.model.DesignItem
import com.kalashnikovprojects.ufmtv.domain.model.ScreenStyle
import kotlinx.coroutines.flow.Flow

interface ScreenStyleRepository {
    fun getScreenStyle(): Flow<ScreenStyle>
}