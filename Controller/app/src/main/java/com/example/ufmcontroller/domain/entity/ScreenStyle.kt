package com.example.ufmcontroller.domain.entity

import com.example.ufmcontroller.data.model.ScreenStyleDTO

data class ScreenStyle(
    val screenTheme: ScreenTheme? = null,
    val backgroundColorHex: String? = null,
    val defaultStyle: Style? = null,
)

fun ScreenStyle.toDTO(): ScreenStyleDTO = ScreenStyleDTO(
    screenTheme?.toDTO(),
    backgroundColorHex,
    defaultStyle?.toDTO(),
)