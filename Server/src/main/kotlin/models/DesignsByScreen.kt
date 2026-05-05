package com.kalashnikovprojects.ufmserver.models

import com.kalashnikovprojects.ufmserver.dto.DesignItem
import kotlinx.serialization.Serializable


@Serializable
data class DesignsByScreen(
    val screenId: Int,
    val design: List<DesignItem>,
)