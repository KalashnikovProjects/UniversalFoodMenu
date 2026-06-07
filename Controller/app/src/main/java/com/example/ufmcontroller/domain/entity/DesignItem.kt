package com.example.ufmcontroller.domain.entity

data class DesignItem(
    val id: Int=0,
    val element: Designable,
    val style: Style,
)


fun DesignItem.toDesignItemWithScreenId(screenId: Int) = DesignItemWithScreenId(
    id=id,
    element = element,
    style = style,
    screenId = screenId,
)