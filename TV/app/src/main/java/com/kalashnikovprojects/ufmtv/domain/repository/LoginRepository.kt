package com.kalashnikovprojects.ufmtv.domain.repository

import com.kalashnikovprojects.ufmtv.domain.entity.LoginEvents
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun observeLogin(): Flow<LoginEvents>
    suspend fun logout()
    suspend fun disconnect()
}