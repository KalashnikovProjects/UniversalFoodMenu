package com.example.ufmcontroller.domain.repository

interface LoginRepository {
    suspend fun sendLogin(username: String, password: String)
    suspend fun sendRegister(username: String, password: String)
    suspend fun logout()
}