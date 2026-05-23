package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.FoodItemDisplayTypeStyle
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface FoodItemDisplayTypeStyleDTO {
    fun toEntity(): FoodItemDisplayTypeStyle

    @SerialName("row")
    @Serializable
    object Row: FoodItemDisplayTypeStyleDTO {
        override fun toEntity() = FoodItemDisplayTypeStyle.Row
    }

    @SerialName("cell")
    @Serializable
    object Cell: FoodItemDisplayTypeStyleDTO {
        override fun toEntity() = FoodItemDisplayTypeStyle.Cell
    }
}