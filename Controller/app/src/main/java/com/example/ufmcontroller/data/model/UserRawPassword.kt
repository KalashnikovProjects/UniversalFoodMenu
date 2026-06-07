package com.example.ufmcontroller.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRawPassword(
    val username: String,
    val rawPassword: String
)