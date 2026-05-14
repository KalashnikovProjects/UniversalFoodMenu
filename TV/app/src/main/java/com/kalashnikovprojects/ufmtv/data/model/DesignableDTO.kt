package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.Designable
import kotlinx.serialization.Serializable

@Serializable
sealed interface DesignableDTO {
    fun toEntity(): Designable
}