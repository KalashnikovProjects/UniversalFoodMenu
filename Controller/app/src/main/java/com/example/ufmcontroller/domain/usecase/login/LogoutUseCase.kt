package com.example.ufmcontroller.domain.usecase.login

import com.example.ufmcontroller.domain.repository.LoginRepository
import javax.inject.Inject


class LogoutUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke() {
        loginRepository.logout()
    }
}