package com.kalashnikovprojects.ufmtv.domain.entity

data class ScreenStyle(
    val screenTheme: ScreenTheme? = null,
    val backgroundColorHex: String? = null,
    val defaultStyle: Style? = null,
)