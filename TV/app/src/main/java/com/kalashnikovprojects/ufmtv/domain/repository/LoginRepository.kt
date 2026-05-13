package com.kalashnikovprojects.ufmtv.domain.repository

import com.kalashnikovprojects.ufmtv.domain.model.LoginEvent
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun observeLogin(): Flow<LoginEvent>
    suspend fun disconnect()
}