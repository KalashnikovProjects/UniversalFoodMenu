package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.ScreenStyle
import kotlinx.serialization.Serializable


@Serializable
data class ScreenStyleDTO(
    val screenTheme: ScreenThemeDTO?=null,
    val backgroundColorHex: String?=null,
    @Serializable(with = StyleFromStringSerializer::class)
    val defaultStyle: StyleDTO?=null,
)

fun ScreenStyleDTO.toEntity() = ScreenStyle(
    screenTheme = screenTheme?.toEntity(),
    backgroundColorHex = backgroundColorHex,
    defaultStyle = defaultStyle?.toEntity(),
)