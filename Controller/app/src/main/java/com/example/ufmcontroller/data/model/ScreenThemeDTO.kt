package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.ScreenTheme
import kotlinx.serialization.Serializable

@Serializable
enum class ScreenThemeDTO {
    BLACK,
    WHITE,
}

fun ScreenThemeDTO.toEntity(): ScreenTheme = when(this) {
    ScreenThemeDTO.BLACK -> ScreenTheme.BLACK
    ScreenThemeDTO.WHITE -> ScreenTheme.WHITE
}