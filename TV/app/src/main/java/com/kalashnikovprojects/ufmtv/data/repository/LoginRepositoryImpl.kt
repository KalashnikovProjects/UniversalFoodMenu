package com.kalashnikovprojects.ufmtv.data.repository

import com.kalashnikovprojects.ufmtv.data.remote.LoginWebSocketService
import com.kalashnikovprojects.ufmtv.domain.model.LoginEvent
import com.kalashnikovprojects.ufmtv.domain.repository.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginWebSocketService: LoginWebSocketService,
    private val externalScope: CoroutineScope
) : LoginRepository {

    init {
        observeWebSocketEvents()
    }

    private fun observeWebSocketEvents() {
        externalScope.launch {
            loginWebSocketService.events.collect { event ->
                when (event) {
                    is com.kalashnikovprojects.ufmtv.data.model.LoginEvent.Closed -> TODO()
                    is com.kalashnikovprojects.ufmtv.data.model.LoginEvent.CodeReceived -> TODO()
                    is com.kalashnikovprojects.ufmtv.data.model.LoginEvent.TokenReceived -> TODO()
                }
            }
        }
    }

    override fun observeLogin(): Flow<LoginEvent> {
        TODO("Not yet implemented")
    }

    override fun logOut() {
        TODO("Not yet implemented")
    }

    override suspend fun disconnect() {
        TODO("Not yet implemented")
    }
}