package com.kalashnikovprojects.ufmtv.data.repository

import com.kalashnikovprojects.ufmtv.data.local.UserPreferencesDataSource
import com.kalashnikovprojects.ufmtv.data.model.LoginEventsDTO
import com.kalashnikovprojects.ufmtv.data.remote.LoginWebSocketService
import com.kalashnikovprojects.ufmtv.domain.entity.LoginEvents
import com.kalashnikovprojects.ufmtv.domain.repository.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginWebSocketService: LoginWebSocketService,
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val externalScope: CoroutineScope
) : LoginRepository {
    override fun observeLogin(): Flow<LoginEvents> = flow {
        externalScope.launch {
            loginWebSocketService.events.collect { event ->
                when (event) {
                    is LoginEvents.CodeReceived -> {
                        emit(event)
                    }
                    is LoginEvents.TokenReceived -> {
                        userPreferencesDataSource.saveAuthToken(token=event.token)
                        emit(event)

                    }
                    LoginEvents.Closed -> {
                        emit(event)
                    }
                }
            }
        }
    }

    override suspend fun logOut() {
        userPreferencesDataSource.clearAuthToken()
        userPreferencesDataSource.clearScreenId()
    }

    override suspend fun disconnect() {
        loginWebSocketService.disconnect()
    }
}