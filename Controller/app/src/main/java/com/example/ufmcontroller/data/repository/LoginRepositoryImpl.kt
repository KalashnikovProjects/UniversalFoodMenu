package com.example.ufmcontroller.data.repository

import com.example.ufmcontroller.data.local.UserPreferencesDataSource
import com.example.ufmcontroller.domain.repository.LoginRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : LoginRepository {
    override suspend fun sendLogin(username: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun sendRegister(username: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

}