package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class InputUserRawPassword(
    val username: String,
    val rawPassword: String
)