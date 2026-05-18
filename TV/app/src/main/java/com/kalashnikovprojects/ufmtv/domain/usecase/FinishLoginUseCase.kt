package com.kalashnikovprojects.ufmtv.domain.usecase

import com.kalashnikovprojects.ufmtv.domain.repository.LoginRepository
import com.kalashnikovprojects.ufmtv.domain.repository.LoginServiceController
import javax.inject.Inject

class FinishLoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val loginServiceController: LoginServiceController
) {
    suspend operator fun invoke() {
        loginServiceController.stopService()
        loginRepository.disconnect()
    }
}