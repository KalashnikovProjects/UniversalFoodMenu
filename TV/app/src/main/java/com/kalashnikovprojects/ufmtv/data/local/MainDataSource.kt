package com.kalashnikovprojects.ufmtv.data.local

import com.kalashnikovprojects.ufmtv.domain.model.DesignItem
import com.kalashnikovprojects.ufmtv.domain.model.ScreenStyle
import com.kalashnikovprojects.ufmtv.domain.model.defaultScreenStyle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainDataSource {
    private val _designItems = MutableStateFlow<List<DesignItem>>(emptyList())
    val designItems: StateFlow<List<DesignItem>> = _designItems.asStateFlow()

    private val _screenStyle = MutableStateFlow<ScreenStyle>(defaultScreenStyle())
    val screenStyle: StateFlow<ScreenStyle> = _screenStyle.asStateFlow()

    fun updateAll(items: List<DesignItem>) {
        _designItems.value = items
    }

    fun updateList(transform: (List<DesignItem>) -> List<DesignItem>) {
        _designItems.value = transform(_designItems.value)
    }
}