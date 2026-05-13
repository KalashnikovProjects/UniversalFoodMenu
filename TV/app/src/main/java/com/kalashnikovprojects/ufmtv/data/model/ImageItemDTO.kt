package com.kalashnikovprojects.ufmtv.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageItemDTO(
    val id: Int,
    val imageUri: String,
) : DesignableDTO
