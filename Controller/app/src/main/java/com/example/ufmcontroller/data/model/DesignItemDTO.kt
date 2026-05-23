package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.DesignItem
import kotlinx.serialization.Serializable

@Serializable
data class DesignItemDTO(
    val id: Int,
    val element: DesignableDTO,
    val style: StyleDTO,
)

fun DesignItemDTO.toEntity(): DesignItem {
    return DesignItem(
        id = id,
        element = element.toEntity(),
        style = style.toEntity(),
    )
}