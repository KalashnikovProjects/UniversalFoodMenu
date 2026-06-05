package com.example.ufmcontroller.presentation.viewmodel.fieldsstates

import androidx.compose.foundation.text.input.TextFieldState
import kotlinx.coroutines.flow.MutableStateFlow

data class FoodItemFieldsStates(
    val name: TextFieldState = TextFieldState(""),
    val price: TextFieldState = TextFieldState(""),
    val imageUri: MutableStateFlow<String?> = MutableStateFlow(null),
    val isExtendedCategoriesSelection: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val categoriesSearch: TextFieldState = TextFieldState(""),
    val selectedCategories: MutableStateFlow<Set<Int>> = MutableStateFlow(emptySet()),
)