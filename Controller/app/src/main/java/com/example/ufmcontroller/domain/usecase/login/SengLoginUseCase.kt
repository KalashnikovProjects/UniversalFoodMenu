package com.example.ufmcontroller.domain.usecase.login

import com.example.ufmcontroller.domain.repository.LoginRepository
import javax.inject.Inject


class SengLoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(username: String, password: String) {
        loginRepository.sendLogin(username, password)
    }
}