package com.example.ufmcontroller.domain.entity

import com.example.ufmcontroller.data.model.DesignItemDTO
import com.example.ufmcontroller.data.model.DesignItemWithScreenIdDTO

data class DesignItemWithScreenId(
    val id: Int=0,
    val element: Designable,
    val style: Style,
    val screenId: Int,
)

fun DesignItemWithScreenId.toDTO(): DesignItemWithScreenIdDTO {
    return DesignItemWithScreenIdDTO(
        id = id,
        element = element.toDTO(),
        style = style.toDTO(),
        screenId = screenId,
    )
}

fun DesignItemWithScreenId.toDesignItem() = DesignItem(
    id=id,
    element = element,
    style = style,
)