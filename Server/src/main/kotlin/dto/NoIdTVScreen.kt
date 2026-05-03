package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class NoIdTVScreen(
    val name: String,
    val width: Int,
    val height: Int,
)