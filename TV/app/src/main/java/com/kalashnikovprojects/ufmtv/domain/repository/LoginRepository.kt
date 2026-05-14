package com.kalashnikovprojects.ufmtv.domain.repository

import com.kalashnikovprojects.ufmtv.domain.entity.LoginEvents
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun observeLogin(): Flow<LoginEvents>
    suspend fun logOut()
    suspend fun disconnect()
}