package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.Designable
import kotlinx.serialization.Serializable

@Serializable
sealed interface DesignableDTO {
    fun toEntity(): Designable
}

