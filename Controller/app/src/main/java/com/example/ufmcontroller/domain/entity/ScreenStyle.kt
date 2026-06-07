package com.example.ufmcontroller.domain.entity

data class ScreenStyle(
    val screenTheme: ScreenTheme? = null,
    val backgroundColorHex: String? = null,
    val defaultStyle: Style? = null,
)

fun ScreenStyle.toDTO(): ScreenStyle = ScreenStyle(
    screenTheme,
    backgroundColorHex,
    defaultStyle
)