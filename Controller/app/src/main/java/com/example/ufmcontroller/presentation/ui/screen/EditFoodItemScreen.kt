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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.component.DefaultAppTop
import com.example.ufmcontroller.presentation.ui.component.FoodItemFields
import com.example.ufmcontroller.presentation.viewmodel.EditFoodItemUiState
import com.example.ufmcontroller.presentation.viewmodel.EditFoodItemViewModel
import com.example.ufmcontroller.presentation.viewmodel.EditMenuTab
import com.example.ufmcontroller.presentation.viewmodel.OpenedDeleteConfirmationDialog
import com.example.ufmcontroller.presentation.viewmodel.TvScreenViewModel
import com.example.ufmcontroller.presentation.viewmodel.fieldsstates.FoodItemFieldsStates

@Composable
fun EditFoodItemScreen(
    foodId: Int,
    viewModel: EditFoodItemViewModel = hiltViewModel(
        creationCallback = { factory: EditFoodItemViewModel.Factory ->
            factory.create(foodId)
        }
    ),
    onBack: () -> Unit,
) {
    val uiState: EditFoodItemUiState by viewModel.uiState.collectAsState()

    EditFoodItemScreenContent(
        uiState,
        viewModel.foodItemFieldsStates,
        viewModel::setOpenedDeleteConfirmationDialog,
        viewModel::delete,
        { viewModel.edit() },
        onBack
    )
}

@Composable
fun EditFoodItemScreenContent(
    uiState: EditFoodItemUiState,
    foodItemFieldsStates: FoodItemFieldsStates,
    setOpenedDeleteConfirmationDialog: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onCommit: () -> Unit,
    onBack: () -> Unit,
    ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            DefaultAppTop(
                "Редактирование позиции меню",
                onButton = onBack,
                buttonIsToMenu = false,
                actions = {
                    IconButton(
                        onClick = { setOpenedDeleteConfirmationDialog(true) },
                        colors = IconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = colorScheme.error,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = colorScheme.error.copy(alpha = 0.5F),
                        ),
                        modifier = Modifier.size(60.dp)
                    ) {
                        Icon(Icons.Filled.Delete,
                                contentDescription = "Delete")
                    }
                }
                )
            FoodItemFields(
                foodItemFieldsStates,
                uiState.categories,
            )
        }
        ExtendedFloatingActionButton(
            onClick = onCommit,
            icon = { Icon(Icons.Filled.Check, "OK") },
            text = { Text(text = "Сохранить") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp)
        )
        if (uiState.openedDeleteConfirmationDialog) {
            AlertDialog(
                title = {
                    Text(text = "Удалить позицию меню?", textAlign = TextAlign.Center)
                },
                onDismissRequest = {
                    setOpenedDeleteConfirmationDialog(false)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDelete()
                            setOpenedDeleteConfirmationDialog(false)
                        }
                    ) {
                        Text("Да")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            setOpenedDeleteConfirmationDialog(false)
                        }
                    ) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}


@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditFoodItemScreenContentPreview() {
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            EditFoodItemScreenContent(
                uiState = EditFoodItemUiState(
                    openedDeleteConfirmationDialog = false
                ),
                foodItemFieldsStates = FoodItemFieldsStates(),
                setOpenedDeleteConfirmationDialog = {},
                onDelete = {},
                onCommit = { },
                onBack = { },
            )
        }
    }
}