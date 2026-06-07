package com.example.ufmcontroller.domain.entity

import com.example.ufmcontroller.data.model.FoodItemDisplayTypeStyleDTO

sealed interface FoodItemDisplayTypeStyle {
    fun toDTO(): FoodItemDisplayTypeStyleDTO

    object Row: FoodItemDisplayTypeStyle {
        override fun toDTO() = FoodItemDisplayTypeStyleDTO.Row
    }

    object Cell: FoodItemDisplayTypeStyle {
        override fun toDTO() = FoodItemDisplayTypeStyleDTO.Cell
    }
}