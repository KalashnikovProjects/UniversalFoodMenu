package com.example.ufmcontroller.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ufmcontroller.domain.entity.FoodItemDisplayTypeStyle
import com.example.ufmcontroller.domain.entity.NotInStockStyle
import com.example.ufmcontroller.presentation.viewmodel.StyleInputStates
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.color.ColorDialog
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.models.ColorSelection
import com.maxkeppeler.sheets.color.models.ColorSelectionMode
import com.maxkeppeler.sheets.color.models.SingleColor
import okhttp3.internal.toHexString

enum class StyleType {
    SCREEN_DEFAULT,
    FOOD_ITEM,
    CATEGORY,
    FOOD_ITEM_IN_CATEGORY,
    TEXT,
    IMAGE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyleSettingsColumn(
    styleInputStates: StyleInputStates,
    styleType: StyleType,
    editCurrentDesignItem: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
        if (styleType != StyleType.FOOD_ITEM_IN_CATEGORY && styleType != StyleType.SCREEN_DEFAULT) {
            val scaleSliderValue by styleInputStates.scaleSliderValue.collectAsStateWithLifecycle()
            Text(
                "Размер: %.2fx".format(scaleSliderValue ?: 0.5F),
                modifier = Modifier.padding(),
                color = colorScheme.onBackground,
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
            )
            Slider(
                value = styleInputStates.scaleSliderValue.value ?: 0.5F,
                onValueChange = {
                    styleInputStates.scaleSliderValue.value = it
                    editCurrentDesignItem()
                },
                valueRange = 0.1F..4F,
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 25.dp))
        }
        if (styleType != StyleType.IMAGE) {
            val textColor by styleInputStates.textColorHex.collectAsStateWithLifecycle()
            Text(
                "Цвет текста",
                modifier = Modifier.padding(bottom = 2.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight(600),
                color= if (textColor!=null) Color(("#" + textColor as String).toColorInt()) else colorScheme.onBackground,
            )
            Row {
                Button(onClick = {
                    showDialog = !showDialog
                }, shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(end = 10.dp),
                ) {
                    Text("Выбрать цвет")
                }
                if (textColor != null) {
                    IconButton(
                        onClick = {
                            styleInputStates.textColorHex.value = null
                            editCurrentDesignItem()
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
                            editCurrentDesignItem()
                        }),

                    selection = ColorSelection(
                        selectedColor = SingleColor(colorHex = "#" + (textColor ?: "FFFFFF")),
                        onSelectColor = { styleInputStates.textColorHex.value = it.toHexString() },
                    ),
                    config = ColorConfig(
                        defaultDisplayMode = ColorSelectionMode.CUSTOM,
                        allowCustomColorAlphaValues = false
                    ),
                )
            }
        }


            if (styleType == StyleType.SCREEN_DEFAULT || styleType == StyleType.FOOD_ITEM || styleType == StyleType.FOOD_ITEM_IN_CATEGORY || styleType == StyleType.CATEGORY) {
                val notInStockStyle by styleInputStates.notInStockStyle.collectAsStateWithLifecycle()
                val options = listOf(null, NotInStockStyle.CROSSED_OUT, NotInStockStyle.OPACITY, NotInStockStyle.HIDDEN, NotInStockStyle.NO_CHANGES)

                val names = mapOf<NotInStockStyle?, String>(
                    NotInStockStyle.CROSSED_OUT to "Зачёркнутый текст",
                    NotInStockStyle.OPACITY to "Прозрачность",
                    NotInStockStyle.HIDDEN to "Не отображать",
                    NotInStockStyle.NO_CHANGES to "Не менять отображение",
                )
                Text(
                    "Отображение при отсутствии в наличии",
                    modifier = Modifier.padding(top=10.dp, bottom = 2.dp),
                    color = colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        readOnly = true,
                        value = names[notInStockStyle] ?: "По умолчанию",
                        onValueChange = {},
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = names[item] ?: "По умолчанию",
                                        fontSize = 14.sp
                                    )
                                },
                                onClick = {
                                    styleInputStates.notInStockStyle.value = item
                                    editCurrentDesignItem()
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                }
            }
            }
            if (styleType != StyleType.TEXT && styleType != StyleType.IMAGE) {
                val showImage by styleInputStates.showImage.collectAsStateWithLifecycle()
                Text(
                    "Отображать изображение",
                    modifier = Modifier.padding(top=10.dp, bottom = 2.dp),
                    color = colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                )
                Switch(
                    checked = showImage ?: true,
                    onCheckedChange = {
                        styleInputStates.showImage.value = it
                        editCurrentDesignItem()
                    },
                )
            }
            if (styleType != StyleType.TEXT && styleType != StyleType.IMAGE) {
                val showPrice by styleInputStates.showPrice.collectAsStateWithLifecycle()
                Text(
                    "Отображать цену",
                    modifier = Modifier.padding(top=10.dp, bottom = 2.dp),
                    color = colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                )
                Switch(
                    checked = showPrice ?: true,
                    onCheckedChange = {
                        styleInputStates.showPrice.value = it
                        editCurrentDesignItem()
                    },
                )
            }
            if (styleType == StyleType.FOOD_ITEM || styleType == StyleType.SCREEN_DEFAULT) {
                val foodItemDisplayType by styleInputStates.foodItemDisplayTypeStyle.collectAsStateWithLifecycle()
                val options = listOf(null, FoodItemDisplayTypeStyle.Row, FoodItemDisplayTypeStyle.Cell)

                val names = mapOf<FoodItemDisplayTypeStyle?, String>(
                    FoodItemDisplayTypeStyle.Row to "Строка",
                    FoodItemDisplayTypeStyle.Cell to "Ячейка",
                )
                Text(
                    "Вид элемента меню",
                    modifier = Modifier.padding(top=10.dp, bottom = 2.dp),
                    color = colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                )
                SingleChoiceSegmentedButtonRow {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = {
                                styleInputStates.foodItemDisplayTypeStyle.value = label
                                editCurrentDesignItem()
                            },
                            selected = label == foodItemDisplayType,
                            label = { Text(names[label] ?: "По умолчанию",
                                fontSize = if (label == null) 9.sp else 14.sp,
                            ) }
                        )
                    }
                }
            }
            if (styleType != StyleType.TEXT && styleType != StyleType.IMAGE) {
                val imageScaleSliderValue by styleInputStates.imageScale.collectAsStateWithLifecycle()
                Text(
                    "Размер изображения: %.2fx".format(imageScaleSliderValue ?: 1.0F),
                    modifier = Modifier.padding(top=10.dp, bottom = 2.dp),
                    color = colorScheme.onBackground,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(600),
                )
                Slider(
                    value = styleInputStates.imageScale.value ?: 1F,
                    onValueChange = {
                        styleInputStates.imageScale.value = it
                        editCurrentDesignItem()
                    },
                    valueRange = 0.4F..3F,
                )
            }
            if (styleType != StyleType.TEXT && styleType != StyleType.IMAGE) {
                val itemWidthScale by styleInputStates.itemWidthScale.collectAsStateWithLifecycle()
                Text(
                    "Ширина элемента: %.2fx".format(itemWidthScale ?: 1.0F),
                    modifier = Modifier.padding(top=10.dp, bottom = 2.dp),
                    color = colorScheme.onBackground,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(600),
                )
                Slider(
                    value = styleInputStates.itemWidthScale.value ?: 1F,
                    onValueChange = {
                        styleInputStates.itemWidthScale.value = it
                        editCurrentDesignItem()
                    },
                    valueRange = 0.4F..3F,
                )
            }
    }
}