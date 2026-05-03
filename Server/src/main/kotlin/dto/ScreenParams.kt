package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class ScreenParams(
    val screenWidth: Int,
    val screenHeight: Int
)