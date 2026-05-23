package com.example.ufmcontroller.domain.entity

sealed interface FoodItemDisplayTypeStyle {
    object Row: FoodItemDisplayTypeStyle

    object Cell: FoodItemDisplayTypeStyle
}