package com.kalashnikovprojects.ufmtv.domain.repository

import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenStyle
import kotlinx.coroutines.flow.Flow


interface MainRepository {
    fun getDesignItems(): Flow<List<DesignItem>>
    fun getScreenStyle(): Flow<ScreenStyle>
}