package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserRawPassword(
    val username: String,
    val rawPassword: String
)