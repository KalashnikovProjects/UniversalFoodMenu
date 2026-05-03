package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class InputUserHashedPassword(
    val username: String,
    val hashedPassword: String
)