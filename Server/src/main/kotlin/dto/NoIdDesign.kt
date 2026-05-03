package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class NoIdDesign(
    val element: Designable,
    val style: String,
)