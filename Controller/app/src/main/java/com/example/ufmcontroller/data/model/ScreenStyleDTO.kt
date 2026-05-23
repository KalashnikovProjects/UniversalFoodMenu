package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.ScreenStyle
import kotlinx.serialization.Serializable


@Serializable
data class ScreenStyleDTO(
    val screenTheme: ScreenThemeDTO?,
    val backgroundColorHex: String?,
    val defaultStyle: StyleDTO?,
)

fun ScreenStyleDTO.toEntity() = ScreenStyle(
    screenTheme = screenTheme?.toEntity(),
    backgroundColorHex = backgroundColorHex,
    defaultStyle = defaultStyle?.toEntity(),
)