package com.kalashnikovprojects.ufmtv.data.repository

import com.kalashnikovprojects.ufmtv.data.local.DesignItemsDataSource
import com.kalashnikovprojects.ufmtv.domain.model.DesignItem
import com.kalashnikovprojects.ufmtv.domain.repository.DesignItemsRepository
import kotlinx.coroutines.flow.Flow


class DesignItemsRepositoryImpl(private val api: DesignItemsDataSource) : DesignItemsRepository {
    override fun getDesignItems(): Flow<List<DesignItem>> {
        TODO("Not yet implemented")
    }
}