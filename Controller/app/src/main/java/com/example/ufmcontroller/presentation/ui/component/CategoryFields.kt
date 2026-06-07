package com.example.ufmcontroller.presentation.ui.component

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.then
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.viewmodel.AddDesignExtended
import com.example.ufmcontroller.presentation.viewmodel.fieldsstates.CategoryFieldsStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@Composable
fun CategoryFields(
    categoryFieldsStates: CategoryFieldsStates,
    foodItems: List<FoodItem>,
    navigateEditFoodItem: (id: Int) -> Unit,
) {
    val onFoodItemToggle =  { id: Int, boolean: Boolean ->
        categoryFieldsStates.selectedFoodItems.update {
            if (it.contains(id)) {
                it - id
            } else {
                it + id
            }
        }
    }
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            categoryFieldsStates.imageUri.value = uri.toString()
        }
    }
    val selectedImageUri by categoryFieldsStates.imageUri.collectAsStateWithLifecycle()
    val isExtendedFoodItemsSelection by categoryFieldsStates.isExtendedFoodItemsSelection.collectAsStateWithLifecycle()
    val selectedFoodItems by categoryFieldsStates.selectedFoodItems.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier=Modifier.width(300.dp),
            // horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Название",
                modifier = Modifier.padding(13.dp),
                color = colorScheme.onBackground,
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
            )
            TextField(
                state=categoryFieldsStates.name,
                placeholder = { Text("Название", modifier = Modifier.alpha(0.5F)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(7.dp)),
            )
            Text("Цена (опционально)",
                modifier = Modifier.padding(13.dp),
                color = colorScheme.onBackground,
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    state = categoryFieldsStates.price,
                    placeholder = { Text("Цена", modifier = Modifier.alpha(0.5F)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(7.dp)),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                    ),
                    inputTransformation = InputTransformation.maxLength(9).then {
                        val currentText = asCharSequence().toString()
                        if (currentText.contains(',')) {
                            val normalized = currentText.replace(',', '.')
                            replace(0, length, normalized)
                        }

                        val updatedText = asCharSequence().toString()

                        val priceRegex = Regex("^\\d*\\.?\\d{0,2}$")

                        if (!updatedText.matches(priceRegex)) {
                            revertAllChanges()
                        }
                    },
                    lineLimits = TextFieldLineLimits.SingleLine,
                )
                IconButton(onClick = {
                    categoryFieldsStates.price.clearText()
                },
                    colors=IconButtonColors(
                        containerColor = colorScheme.errorContainer,
                        contentColor = colorScheme.onErrorContainer,
                        disabledContainerColor = colorScheme.errorContainer,
                        disabledContentColor = colorScheme.onErrorContainer
                    ),
                    modifier = Modifier.padding(start = 10.dp),
                ) {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "Удалить цену",
                        tint = colorScheme.onBackground,
                    )
                }
            }

            Text("Изображение",
                modifier = Modifier.padding(13.dp),
                color = colorScheme.onBackground,
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
            )
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .size(100.dp)
                        .clip(
                            RoundedCornerShape(7.dp)
                        ),
                )
            }
            Row {
                Button(onClick = {
                    pickMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text("Выбрать из галереи")
                }
                if (selectedImageUri != null) {
                    IconButton(onClick = {
                        categoryFieldsStates.imageUri.value = null
                    },
                        colors=IconButtonColors(
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
            Row(
                modifier = Modifier.clickable {
                    categoryFieldsStates.isExtendedFoodItemsSelection.value = !categoryFieldsStates.isExtendedFoodItemsSelection.value
                }
            ) {
                IconButton(
                    onClick = {
                        categoryFieldsStates.isExtendedFoodItemsSelection.value = !categoryFieldsStates.isExtendedFoodItemsSelection.value
                    },
                    modifier= Modifier.padding(end=7.dp)
                ) {
                    Icon(if (!isExtendedFoodItemsSelection) Icons.Filled.ExpandMore else Icons.Filled.ExpandLess,
                        contentDescription = "Expand food items",
                        tint = colorScheme.onBackground,
                    )
                }
                Text("Позиции меню",
                    modifier = Modifier.padding(vertical = 13.dp),
                    color = colorScheme.onBackground,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(600),
                )
            }
            AnimatedVisibility(isExtendedFoodItemsSelection) {
                Column(
                    modifier=Modifier.fillMaxWidth()
                ) {
                    SearchBar(categoryFieldsStates.foodItemsSearch,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth())

                    LazyColumn(
                        modifier=Modifier.fillMaxWidth()
                    ) {
                        items(foodItems) {
                                item ->
                            key("food_items_for_selection${item.id}") {
                                FoodItemRowCard(
                                    item,
                                    isCheckedUp = selectedFoodItems.contains(item.id),
                                    onFoodItemClick = { id, _ -> onFoodItemToggle(id, false) },
                                    onFoodItemLongClick = navigateEditFoodItem,
                                    showSwitch = true,
                                    showNotInStock = false,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CategoryFieldsPreview() {
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            CategoryFields (
                CategoryFieldsStates(
                    name = TextFieldState(""),
                    price = TextFieldState("155.99"),
                    imageUri = MutableStateFlow(null),
                    isExtendedFoodItemsSelection = MutableStateFlow(true),
                ),
                foodItems = listOf(
                    FoodItem(
                        2,
                        "Китайский вок",
                        imageUri = null,
                        price = 99.99F,
                        inStock = false
                    ),
                    FoodItem(
                        1,
                        "Японские роллы",
                        imageUri = null,
                        price = 99.99F,
                        inStock = true
                    )
                ),
                navigateEditFoodItem = {}
            )
        }
    }
}