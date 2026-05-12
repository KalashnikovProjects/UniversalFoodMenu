package com.kalashnikovprojects.ufmserver.models

import kotlinx.serialization.Serializable

@Serializable
data class UserRawPassword(
    val username: String,
    val rawPassword: String
)