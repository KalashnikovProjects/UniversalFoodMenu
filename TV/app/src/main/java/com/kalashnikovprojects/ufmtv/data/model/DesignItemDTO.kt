package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.data.model.StyleDTO
import kotlinx.serialization.Serializable

@Serializable
data class DesignItemDTO(
    val id: Int,
    val element: DesignableDTO,
    val style: StyleDTO,
)