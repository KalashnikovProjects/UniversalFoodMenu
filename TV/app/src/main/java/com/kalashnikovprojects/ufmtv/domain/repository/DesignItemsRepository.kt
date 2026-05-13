package com.kalashnikovprojects.ufmtv.domain.repository

import com.kalashnikovprojects.ufmtv.domain.model.DesignItem
import kotlinx.coroutines.flow.Flow


interface DesignItemsRepository {
    fun getDesignItems(): Flow<List<DesignItem>>
}