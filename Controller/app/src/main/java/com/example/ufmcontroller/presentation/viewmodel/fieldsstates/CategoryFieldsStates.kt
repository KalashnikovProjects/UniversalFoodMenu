package com.example.ufmcontroller.presentation.viewmodel.fieldsstates

import androidx.compose.foundation.text.input.TextFieldState
import kotlinx.coroutines.flow.MutableStateFlow

data class CategoryFieldsStates(
    val name: TextFieldState = TextFieldState(""),
    val price: TextFieldState = TextFieldState(""),
    val imageUri: MutableStateFlow<String?> = MutableStateFlow(null),
    val isExtendedFoodItemsSelection: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val foodItemsSearch: TextFieldState = TextFieldState(""),
    val selectedFoodItems: MutableStateFlow<Set<Int>> = MutableStateFlow(emptySet()),
)