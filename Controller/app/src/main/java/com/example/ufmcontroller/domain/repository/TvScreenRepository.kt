package com.example.ufmcontroller.domain.repository

import com.example.ufmcontroller.domain.entity.TVScreen

interface TvScreenRepository {
    suspend fun inputCodeForTvAuth(code: String): TVScreen
    suspend fun editScreen(id: Int, screen: TVScreen)
    suspend fun deleteScreen(id: Int)
}