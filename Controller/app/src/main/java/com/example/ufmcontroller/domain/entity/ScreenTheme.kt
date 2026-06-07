package com.example.ufmcontroller.domain.entity

import com.example.ufmcontroller.data.model.ScreenThemeDTO
import kotlinx.serialization.Serializable

enum class ScreenTheme {
    BLACK,
    WHITE,
}

fun ScreenTheme.toDTO(): ScreenThemeDTO = when(this) {
    ScreenTheme.BLACK -> ScreenThemeDTO.BLACK
    ScreenTheme.WHITE -> ScreenThemeDTO.WHITE
}