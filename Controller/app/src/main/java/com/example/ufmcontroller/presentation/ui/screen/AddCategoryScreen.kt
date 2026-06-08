package com.example.ufmcontroller.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.component.CategoryFields
import com.example.ufmcontroller.presentation.ui.component.DefaultAppTop
import com.example.ufmcontroller.presentation.viewmodel.AddCategoryUiState
import com.example.ufmcontroller.presentation.viewmodel.AddCategoryViewModel
import com.example.ufmcontroller.presentation.viewmodel.fieldsstates.CategoryFieldsStates

@Composable
fun AddCategoryScreen(
    viewModel: AddCategoryViewModel= hiltViewModel<AddCategoryViewModel>(),
    navigateEditFoodItem: (id: Int) -> Unit,
    onBack: () -> Unit,
) {
    val uiState: AddCategoryUiState by viewModel.uiState.collectAsState()

    AddCategoryScreenContent(
        uiState,
        viewModel.categoryFieldsStates,
        navigateEditFoodItem,
        { viewModel.upload() },
        onBack
    )
}

@Composable
fun AddCategoryScreenContent(
    uiState: AddCategoryUiState,
    categoryFieldsStates: CategoryFieldsStates,
    navigateEditFoodItem: (id: Int) -> Unit,
    onCommit: () -> Unit,
    onBack: () -> Unit,
    ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            DefaultAppTop("Создание категории", onButton = onBack, buttonIsToMenu = false)
            CategoryFields (
                categoryFieldsStates,
                uiState.foodItems,
                navigateEditFoodItem,
            )
        }
        ExtendedFloatingActionButton(
            onClick = {
                onCommit()
                onBack()
                      },
            icon = { Icon(Icons.Filled.Check, "OK") },
            text = { Text(text = "Создать") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp)
        )
    }
}


@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddCategoryScreenContentPreview() {
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            AddCategoryScreenContent(
                uiState = AddCategoryUiState(),
                categoryFieldsStates = CategoryFieldsStates(),
                navigateEditFoodItem = { },
                onCommit = { },
                onBack = { },
            )
        }
    }
}