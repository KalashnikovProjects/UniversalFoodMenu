package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import kotlinx.serialization.Serializable

@Serializable
data class DesignItemDTO(
    val id: Int,
    val element: DesignableDTO,
    @Serializable(with = StyleFromStringSerializer::class)
    val style: StyleDTO,
)

fun DesignItemDTO.toEntity(): DesignItem {
    return DesignItem(
        id = id,
        element = element.toEntity(),
        style = style.toEntity(),
    )
}