package com.example.ufmcontroller.data.repository

import com.example.ufmcontroller.data.remote.RemoteDesignDataSource
import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.DesignItemWithScreenId
import com.example.ufmcontroller.domain.repository.DesignRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DesignRepositoryImpl @Inject constructor(
    private val remoteDesignDataSource: RemoteDesignDataSource,
    ) : DesignRepository {
    override suspend fun addDesignItem(designItem: DesignItemWithScreenId): DesignItem {
        return remoteDesignDataSource.addDesignItem(designItem)
    }

    override suspend fun addDesignItemWithImage(designItem: DesignItemWithScreenId): DesignItem {
        return remoteDesignDataSource.addDesignItemWithImage(designItem)
    }

    override suspend fun addDesignItemWithText(designItem: DesignItemWithScreenId): DesignItem {
        return remoteDesignDataSource.addDesignItemWithText(designItem)
    }

    override suspend fun editDesignItem(
        id: Int,
        designItem: DesignItemWithScreenId
    ) {
        return remoteDesignDataSource.editDesignItem(id, designItem)
    }

    override suspend fun deleteDesignItem(screeId: Int, id: Int) {
        return remoteDesignDataSource.deleteDesignItem(screeId, id)
    }


    override suspend fun deleteTextItem(id: Int) {
        return remoteDesignDataSource.deleteTextItem(id)
    }

    override suspend fun deleteImageItem(id: Int) {
        return remoteDesignDataSource.deleteImageItem(id)
    }
}