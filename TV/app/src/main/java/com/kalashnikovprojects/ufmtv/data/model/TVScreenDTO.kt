package com.kalashnikovprojects.ufmtv.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TVScreenDTO(
    val id: Int,
    val name: String,
    val width: Int,
    val height: Int,
    val style: String,
)