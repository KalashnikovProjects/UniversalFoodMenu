package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.DesignItemWithScreenId
import kotlinx.serialization.Serializable

@Serializable
data class DesignItemWithScreenIdDTO(
    val id: Int,
    val element: DesignableDTO,
    @Serializable(with = StyleFromStringSerializer::class)
    val style: StyleDTO,
    val screenId: Int,
)

fun DesignItemWithScreenIdDTO.toEntity() = DesignItemWithScreenId(
    id = id,
    element = element.toEntity(),
    style = style.toEntity(),
    screenId = screenId,
)
