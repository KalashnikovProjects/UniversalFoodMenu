package com.example.ufmcontroller.data.repository

import com.example.ufmcontroller.data.local.UserPreferencesDataSource
import com.example.ufmcontroller.data.remote.RemoteLoginDataSource
import com.example.ufmcontroller.domain.repository.LoginRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val remoteLoginDataSource: RemoteLoginDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : LoginRepository {
    override suspend fun sendLogin(username: String, password: String) {
        remoteLoginDataSource.sendLogin(username, password)
            .also { token ->
                userPreferencesDataSource.saveAuthToken(token)
            }
    }

    override suspend fun sendRegister(username: String, password: String) {
        remoteLoginDataSource.sendRegister(username, password)
            .also { token ->
                userPreferencesDataSource.saveAuthToken(token)
            }
    }

    override suspend fun logout() {
        userPreferencesDataSource.clearAuthToken()
    }
}