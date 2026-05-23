package com.example.ufmcontroller.domain.repository

import com.example.ufmcontroller.domain.entity.DesignItem
import kotlinx.coroutines.flow.Flow

interface DesignRepository {
    suspend fun addDesignItem(designItem: DesignItem): DesignItem
    suspend fun addDesignItemWithImageOrText(designItem: DesignItem): DesignItem

    suspend fun editDesignItem(id: Int, designItem: DesignItem)
    suspend fun deleteDesignItem(id: Int)

    fun getDesignItems(): Flow<List<DesignItem>>
}