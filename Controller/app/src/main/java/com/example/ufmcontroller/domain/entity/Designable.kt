package com.example.ufmcontroller.domain.entity

import com.example.ufmcontroller.data.model.DesignableDTO

sealed interface Designable {
    fun toDTO(): DesignableDTO
}