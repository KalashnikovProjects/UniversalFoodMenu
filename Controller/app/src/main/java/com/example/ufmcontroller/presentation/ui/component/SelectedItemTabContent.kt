package com.example.ufmcontroller.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.ImageItem
import com.example.ufmcontroller.domain.entity.TextItem
import com.example.ufmcontroller.presentation.viewmodel.InputStates
import com.example.ufmcontroller.presentation.viewmodel.OpenedDeleteConfirmationDialog
import com.example.ufmcontroller.presentation.viewmodel.TvScreenUiState

@Composable
fun SelectedItemTabContent(
    uiState: TvScreenUiState,
    inputStates: InputStates,
    setOpenedDeleteConfirmationDialog: (OpenedDeleteConfirmationDialog) -> Unit,
    editCurrentDesignItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(start=10.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.padding(start = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Элемент: ${
                        when (uiState.selected?.element) {
                            is CategoryWithFoodItems -> "категория"
                            is ImageItem -> "изображение"
                            is TextItem -> "текст"
                            is FoodItem -> "позиция меню"
                            else -> "не выбран"
                        }
                    }",
                    color = colorScheme.onBackground,
                    fontSize = 17.sp,
                    fontWeight = FontWeight(600),
                    modifier=Modifier.weight(1F),
                )
                IconButton(
                    onClick = { setOpenedDeleteConfirmationDialog(OpenedDeleteConfirmationDialog.DELETE_DESIGN_ITEM) },
                    colors = IconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = colorScheme.error,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = colorScheme.error.copy(alpha = 0.5F),
                    ),
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }

            StyleSettingsColumn(
                styleInputStates = inputStates.selectedStyleInputStates,
                styleType = when (uiState.selected?.element) {
                    is FoodItem -> StyleType.FOOD_ITEM
                    is CategoryWithFoodItems -> StyleType.CATEGORY
                    is ImageItem -> StyleType.IMAGE
                    is TextItem -> StyleType.TEXT
                    else -> StyleType.TEXT // такого не должно произойти
                },
                editCurrentDesignItem,
            )
            if (uiState.selected?.element is CategoryWithFoodItems) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = colorScheme.surfaceContainerHigh
                    ),
                    modifier = Modifier
                        .padding(start = 3.dp, end = 3.dp, top = 10.dp, bottom = 3.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
                    ) {
                        Text(
                            "Стиль элементов категории",
                            color = colorScheme.onBackground,
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                        )
                        StyleSettingsColumn(
                            styleInputStates = inputStates.childStyleInputStates,
                            styleType = StyleType.FOOD_ITEM_IN_CATEGORY,
                            editCurrentDesignItem,
                        )
                    }
                }

            }
        }
    }
}