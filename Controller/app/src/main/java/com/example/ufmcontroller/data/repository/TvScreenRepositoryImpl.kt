package com.example.ufmcontroller.data.repository

import com.example.ufmcontroller.data.remote.RemoteCategoryDataSource
import com.example.ufmcontroller.data.remote.RemoteTvScreenDataSource
import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.repository.TvScreenRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvScreenRepositoryImpl @Inject constructor(
    private val remoteTvScreenDataSource: RemoteTvScreenDataSource
    ) : TvScreenRepository {
    override suspend fun inputCodeForTvAuth(code: String): TVScreen {
        return remoteTvScreenDataSource.inputCodeForTvAuth(code)
    }

    override suspend fun editScreen(
        id: Int,
        screen: TVScreen
    ) {
        return remoteTvScreenDataSource.editScreen(id, screen)
    }

    override suspend fun deleteScreen(id: Int) {
        return remoteTvScreenDataSource.deleteScreen(id)
    }
}
