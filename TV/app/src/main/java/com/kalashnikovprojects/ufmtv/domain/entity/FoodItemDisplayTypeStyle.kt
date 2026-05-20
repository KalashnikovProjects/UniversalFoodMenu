package com.kalashnikovprojects.ufmtv.domain.entity

sealed interface FoodItemDisplayTypeStyle {
    object Row: FoodItemDisplayTypeStyle

    object Cell: FoodItemDisplayTypeStyle
}