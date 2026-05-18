package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.ScreenTheme
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