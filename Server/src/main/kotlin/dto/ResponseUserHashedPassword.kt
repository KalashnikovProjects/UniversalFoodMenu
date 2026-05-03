package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserHashedPassword(
    val id: Int,
    val username: String,
    val hashedPassword: String
)