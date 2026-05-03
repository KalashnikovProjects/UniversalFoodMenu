package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class NoIdUserRawPassword(
    val username: String,
    val rawPassword: String
)