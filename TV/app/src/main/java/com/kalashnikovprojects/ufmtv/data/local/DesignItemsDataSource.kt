package com.kalashnikovprojects.ufmtv.data.local

import com.kalashnikovprojects.ufmtv.domain.model.DesignItem
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DesignItemsDataSource {
    private val _designItems = MutableStateFlow<List<DesignItem>>(emptyList())

    val designItems: StateFlow<List<DesignItem>> = _designItems.asStateFlow()

    fun updateAll(items: List<DesignItem>) {
        _designItems.value = items
    }

    fun updateList(transform: (List<DesignItem>) -> List<DesignItem>) {
        _designItems.value = transform(_designItems.value)
    }
}