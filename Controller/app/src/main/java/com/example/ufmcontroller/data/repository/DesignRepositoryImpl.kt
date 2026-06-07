package com.example.ufmcontroller.data.repository

import androidx.core.net.toUri
import com.example.ufmcontroller.data.remote.RemoteCategoryDataSource
import com.example.ufmcontroller.data.remote.RemoteDesignDataSource
import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.DesignItemWithScreenId
import com.example.ufmcontroller.domain.entity.ImageItem
import com.example.ufmcontroller.domain.repository.DesignRepository
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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

    override suspend fun deleteDesignItem(id: Int) {
        return remoteDesignDataSource.deleteDesignItem(id)
    }
}