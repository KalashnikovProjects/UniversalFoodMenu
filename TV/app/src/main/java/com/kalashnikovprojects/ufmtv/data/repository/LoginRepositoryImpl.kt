package com.kalashnikovprojects.ufmtv.data.repository

import com.kalashnikovprojects.ufmtv.data.local.UserPreferencesDataSource
import com.kalashnikovprojects.ufmtv.data.remote.LoginWebSocketService
import com.kalashnikovprojects.ufmtv.domain.entity.LoginEvents
import com.kalashnikovprojects.ufmtv.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val loginWebSocketService: LoginWebSocketService,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : LoginRepository {
    override fun observeLogin(): Flow<LoginEvents> = flow {
        loginWebSocketService.events.collect { event ->
            when (event) {
                is LoginEvents.CodeReceived -> {
                    emit(event)
                }
                is LoginEvents.TokenReceived -> {
                    userPreferencesDataSource.saveAuthToken(token=event.token)
                    emit(event)
                    loginWebSocketService.disconnect()
                }
                LoginEvents.ClosedWithError -> {
                    emit(event)
                }
            }
        }
    }

    override suspend fun logout() {
        userPreferencesDataSource.clearAuthToken()
        userPreferencesDataSource.clearScreenId()
    }

    override suspend fun disconnect() {
        loginWebSocketService.disconnect()
    }
}