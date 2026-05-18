package com.kalashnikovprojects.ufmtv.domain.usecase

import com.kalashnikovprojects.ufmtv.domain.entity.LoginEvents
import com.kalashnikovprojects.ufmtv.domain.repository.LoginRepository
import com.kalashnikovprojects.ufmtv.domain.repository.LoginServiceController
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StartListeningLoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val loginServiceController: LoginServiceController
) {
    operator fun invoke(): Flow<LoginEvents> {
        loginServiceController.startService()
        return loginRepository.observeLogin()
    }
}