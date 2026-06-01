package com.example.ufmcontroller.domain.repository

import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.entity.TVScreenWithDesignItems
import kotlinx.coroutines.flow.Flow

interface TvScreenRepository {
    suspend fun inputCodeForTvAuth(code: String)
    suspend fun editScreen(id: Int, screen: TVScreen)
    suspend fun deleteScreen(id: Int)
    fun getScreen(id: Int): Flow<TVScreen>
    fun getScreensWithDesignItems(): Flow<List<TVScreenWithDesignItems>>
}