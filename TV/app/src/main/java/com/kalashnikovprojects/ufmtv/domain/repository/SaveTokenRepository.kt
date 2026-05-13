package com.kalashnikovprojects.ufmtv.domain.repository

interface SaveTokenRepository {
    suspend fun saveToken(token: String)
}