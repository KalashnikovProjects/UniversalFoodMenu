package com.example.ufmcontroller.presentation.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ufmcontroller.domain.entity.ScreenTheme
import com.example.ufmcontroller.presentation.viewmodel.InputStates
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.color.ColorDialog
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.models.ColorSelection
import com.maxkeppeler.sheets.color.models.ColorSelectionMode
import com.maxkeppeler.sheets.color.models.SingleColor
import okhttp3.internal.toHexString


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenTabContent(
    inputStates: InputStates,
    editCurrentScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val selectedTheme by inputStates.selectedScreenTheme.collectAsStateWithLifecycle()
    val selectedColor by inputStates.selectedBackgroundColorHex.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(horizontal = 30.dp, vertical = 15.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Название экрана",
            color = colorScheme.onBackground,
            fontSize = 15.sp,
            fontWeight = FontWeight(600),
        )
        OutlinedTextField(
            state = inputStates.screenNameFieldState,
            placeholder = { Text("Название", modifier = Modifier.alpha(0.5F)) },
            modifier = Modifier
                .clip(RoundedCornerShape(7.dp))
                .padding(top = 5.dp, bottom = 12.dp)
        )
        Card(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "Тема экрана",
                    modifier = Modifier.padding(),
                    color = colorScheme.onBackground,
                    fontSize = 17.sp,
                    fontWeight = FontWeight(600),
                )
                val options = listOf<ScreenTheme?>(null, ScreenTheme.BLACK, ScreenTheme.WHITE)
                SingleChoiceSegmentedButtonRow {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = {
                                inputStates.selectedScreenTheme.value = label
                                editCurrentScreen()
                                      },
                            selected = label == selectedTheme,
                            label = { Text(
                                when (label) {
                                    null -> "Не выбрана"
                                    ScreenTheme.BLACK -> "Темная"
                                    ScreenTheme.WHITE -> "Светлая"
                                },
                                fontSize = if (label == null) 13.sp else 15.sp,
                            ) }
                        )
                    }
                }
                Text(
                    "Цвет фона экрана",
                    modifier = Modifier.padding(top=7.dp),
                    color = colorScheme.onBackground,
                    fontSize = 17.sp,
                    fontWeight = FontWeight(600),
                )
                Row {
                    Button(onClick = {
                        showDialog = !showDialog
                    }, shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.padding(end = 10.dp)) {
                        Text("Выбрать цвет")
                    }
                    if (selectedColor != null) {
                        IconButton(
                            onClick = {
                                inputStates.selectedBackgroundColorHex.value = null
                                editCurrentScreen()
                            },
                            colors = IconButtonColors(
                                containerColor = colorScheme.errorContainer,
                                contentColor = colorScheme.onErrorContainer,
                                disabledContainerColor = colorScheme.errorContainer,
                                disabledContentColor = colorScheme.onErrorContainer
                            ),
                        ) {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "Отмена выбора",
                                modifier = Modifier,
                                tint = colorScheme.onBackground,
                            )
                        }
                    }
                }
                if (showDialog) {
                    ColorDialog(
                        state = rememberUseCaseState(visible = true, onCloseRequest = {
                            showDialog = false
                        },
                            onFinishedRequest = {
                                editCurrentScreen()
                            }),

                        selection = ColorSelection(
                            selectedColor = SingleColor(colorHex = "#" + (selectedColor ?: "FFFFFF")),
                            onSelectColor = { inputStates.selectedBackgroundColorHex.value = it.toHexString() },
                        ),
                        config = ColorConfig(
                            defaultDisplayMode = ColorSelectionMode.CUSTOM,
                            allowCustomColorAlphaValues = false
                        ),
                    )
                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = colorScheme.surfaceContainerHigh
                    ),
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        var isExpanded by remember { mutableStateOf(false) }
                        val onHeaderClick = { isExpanded = !isExpanded }
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(30))
                                .clickable { onHeaderClick() }
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = "Expand default style setting",
                                tint = colorScheme.onBackground,
                                modifier = Modifier.padding(end = 7.dp)
                            )
                            Text(
                                "Стиль элементов по умолчанию",
                                color = colorScheme.onBackground,
                                fontSize = 16.sp,
                                fontWeight = FontWeight(500),
                            )
                        }
                        AnimatedVisibility(visible = isExpanded) {
                            StyleSettingsColumn(
                                styleInputStates = inputStates.defaultStyleInputStates,
                                styleType = StyleType.SCREEN_DEFAULT,
                                editCurrentDesignItem = editCurrentScreen,
                            )
                        }
                    }
                }
            }
        }
    }
}