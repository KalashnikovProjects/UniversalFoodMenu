package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class NoIdUserHashedPassword(
    val username: String,
    val hashedPassword: String
)