package com.kalashnikovprojects.ufmtv.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TextItemDTO(
    val id: Int,
    val text: String,
) : DesignableDTO