package com.example.ufmcontroller.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
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
import com.example.ufmcontroller.presentation.ui.component.FoodItemFields
import com.example.ufmcontroller.presentation.viewmodel.AddCategoryUiState
import com.example.ufmcontroller.presentation.viewmodel.AddCategoryViewModel
import com.example.ufmcontroller.presentation.viewmodel.AddFoodItemUiState
import com.example.ufmcontroller.presentation.viewmodel.AddFoodItemViewModel
import com.example.ufmcontroller.presentation.viewmodel.EditCategoryUiState
import com.example.ufmcontroller.presentation.viewmodel.EditCategoryViewModel
import com.example.ufmcontroller.presentation.viewmodel.EditFoodItemViewModel
import com.example.ufmcontroller.presentation.viewmodel.EditMenuTab
import com.example.ufmcontroller.presentation.viewmodel.fieldsstates.CategoryFieldsStates
import com.example.ufmcontroller.presentation.viewmodel.fieldsstates.FoodItemFieldsStates

@Composable
fun EditCategoryScreen(
    categoryId: Int,
    viewModel: EditCategoryViewModel = hiltViewModel(
        creationCallback = { factory: EditCategoryViewModel.Factory ->
            factory.create(categoryId)
        }
    ),
    navigateEditFoodItem: (id: Int) -> Unit,
    onBack: () -> Unit,
) {
    val uiState: EditCategoryUiState by viewModel.uiState.collectAsState()

    EditCategoryScreenContent(
        uiState,
        viewModel.categoryFieldsStates,
        navigateEditFoodItem,
        { viewModel.edit() },
        onBack
    )
}

@Composable
fun EditCategoryScreenContent(
    uiState: EditCategoryUiState,
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
            DefaultAppTop("Редактирование категории", onButton = onBack, buttonIsToMenu = false)
            CategoryFields (
                categoryFieldsStates,
                uiState.foodItems,
                navigateEditFoodItem,
            )
        }
        ExtendedFloatingActionButton(
            onClick = onCommit,
            icon = { Icon(Icons.Filled.Check, "OK") },
            text = { Text(text = "Создать") },
            modifier = Modifier.align(Alignment.BottomEnd).padding(30.dp)
        )
    }
}


@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditCategoryScreenContentPreview() {
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            EditCategoryScreenContent (
                uiState = EditCategoryUiState(),
                categoryFieldsStates = CategoryFieldsStates(),
                navigateEditFoodItem = { },
                onCommit = { },
                onBack = { },
            )
        }
    }
}