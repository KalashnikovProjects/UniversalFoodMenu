package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.TVScreen
import kotlinx.serialization.Serializable

@Serializable
data class TVScreenDTO(
    val id: Int,
    val name: String,
    val width: Int,
    val height: Int,
    @Serializable(with = ScreenStyleFromStringSerializer::class)
    val style: ScreenStyleDTO,
)

fun TVScreenDTO.toEntity() = TVScreen(
    id=id,
    name=name,
    width=width,
    height=height,
    style=style.toEntity(),
)