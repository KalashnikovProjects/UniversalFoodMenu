package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import kotlinx.serialization.Serializable

@Serializable
data class DesignItemWithScreenIdDTO(
    val id: Int,
    val element: DesignableDTO,
    val style: String,
    val screenId: Int,
)
