package com.example.ufmcontroller.domain.repository

import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.DesignItemWithScreenId

interface DesignRepository {
    suspend fun addDesignItem(designItem: DesignItemWithScreenId): DesignItem
    suspend fun addDesignItemWithImage(designItem: DesignItemWithScreenId): DesignItem
    suspend fun addDesignItemWithText(designItem: DesignItemWithScreenId): DesignItem

    suspend fun editDesignItem(id: Int, designItem: DesignItemWithScreenId)
    suspend fun deleteDesignItem(screenId: Int, id: Int)
    suspend fun deleteTextItem(id: Int)
    suspend fun deleteImageItem(id: Int)
}