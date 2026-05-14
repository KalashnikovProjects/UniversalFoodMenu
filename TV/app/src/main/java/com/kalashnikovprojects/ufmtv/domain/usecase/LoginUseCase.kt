package com.kalashnikovprojects.ufmtv.domain.usecase

import com.kalashnikovprojects.ufmtv.domain.entity.LoginEvents
import com.kalashnikovprojects.ufmtv.domain.repository.LoginRepository
import com.kalashnikovprojects.ufmtv.domain.repository.SaveTokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class LoginUseCase(private val loginRepository: LoginRepository,
                   private val saveTokenRepository: SaveTokenRepository) {
    operator fun invoke(): Flow<LoginEvents> {
        return loginRepository.observeLogin()
            .onEach { event ->
                if (event is LoginEvents.TokenReceived) {
                    saveTokenRepository.saveToken(event.token)
                    loginRepository.disconnect()
                }
            }
    }
}