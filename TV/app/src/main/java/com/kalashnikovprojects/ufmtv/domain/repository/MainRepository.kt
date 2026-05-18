package com.kalashnikovprojects.ufmtv.domain.repository

import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow


interface MainRepository {
    fun observeEvents()
    fun getDesignItems(): Flow<List<DesignItem>>
    fun getScreenStyle(): Flow<ScreenStyle>
    fun getLogoutEvent(): SharedFlow<Unit>
    suspend fun disconnect()
}