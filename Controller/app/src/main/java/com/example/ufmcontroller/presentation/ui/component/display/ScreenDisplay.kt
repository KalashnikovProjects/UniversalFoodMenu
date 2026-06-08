package com.example.ufmcontroller.presentation.ui.component.display

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Devices.TV_720p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.FoodItemDisplayTypeStyle
import com.example.ufmcontroller.domain.entity.ImageItem
import com.example.ufmcontroller.domain.entity.NotInStockStyle
import com.example.ufmcontroller.domain.entity.ScreenStyle
import com.example.ufmcontroller.domain.entity.ScreenTheme
import com.example.ufmcontroller.domain.entity.Style
import com.example.ufmcontroller.domain.entity.TextItem
import com.example.ufmcontroller.domain.entity.withDefaultStyle
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.theme.backgroundDark
import com.example.ufmcontroller.presentation.theme.backgroundLight
import kotlin.collections.forEach
import kotlin.math.roundToInt
import kotlin.text.toLong

private fun dragAmountToFraction(dragPx: Float, containerSize: Int, currentFraction: Float): Float {
    val deltaFraction = dragPx / containerSize
    return (deltaFraction + currentFraction)
}

@Composable
fun ScreenDisplay(
    screenStyle: ScreenStyle,
    designItems: List<DesignItem>,
    basicScale: Float = 1F,
    interactive: Boolean = false,
    onSelectItem: (Int?) -> Unit = {},
    selected: Int? = null,
    onItemMoved: ((id: Int, newX: Float, newY: Float) -> Unit) = { _, _, _ -> }
) {
    val isDark = when (screenStyle.screenTheme) {
        null -> isSystemInDarkTheme()
        ScreenTheme.WHITE -> false
        ScreenTheme.BLACK -> true
    }
    val themeBackgroundColor = if (isDark)
        backgroundDark
    else backgroundLight
    val backgroundColor = if (screenStyle.backgroundColorHex != null)
        Color(screenStyle.backgroundColorHex.toLong(16))
    else themeBackgroundColor

    UFMControllerTheme (darkTheme = isDark) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .then(
                    if (interactive) {
                        Modifier.pointerInput("select") {
                            detectTapGestures {
                                onSelectItem(null)
                            }
                        }
                    } else {
                        Modifier
                    }
                ),
        ) {
            if (interactive) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures {
                                onSelectItem(null)
                            }
                        }
                )
            }
            val containerWidth = constraints.maxWidth
            val containerHeight = constraints.maxHeight

            designItems.forEach { (id, element, style) ->
                key(id) {
                    val finalStyle = if (screenStyle.defaultStyle != null)
                        style.withDefaultStyle(screenStyle.defaultStyle)
                    else style

                    val currentScale =
                        (style.scale ?: screenStyle.defaultStyle?.scale ?: 0.5F) * basicScale

                    val targetXFraction = style.x ?: 0.5f
                    val targetYFraction = style.y ?: 0.5f

                    var dragOffset by remember(
                        id,
                        targetXFraction,
                        targetYFraction
                    ) { mutableStateOf(Offset.Zero) }

                    var mod = Modifier
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            val itemWidth = placeable.width
                            val itemHeight = placeable.height
                            val rawX = (containerWidth * targetXFraction).toInt()
                            val rawY = (containerHeight * targetYFraction).toInt()
                            val topLeftX =
                                rawX - ((itemWidth - (itemWidth * currentScale)) / 2).toInt()
                            val topLeftY =
                                rawY - ((itemHeight - (itemHeight * currentScale)) / 2).toInt()

                            layout(itemWidth, itemHeight) {
                                placeable.place(topLeftX, topLeftY)
                            }
                        }
                        .scale(currentScale)
                        .offset { IntOffset(dragOffset.x.roundToInt(), dragOffset.y.roundToInt()) }

                    if (selected == id) {
                        val borderWidth = with(LocalDensity.current) { (3.dp).toPx() }
                        val borderPadding = with(LocalDensity.current) { (10.dp).toPx() }

                        val offset = borderPadding + borderWidth / 2
                        mod = mod.drawBehind {
                            drawRect(
                                color = Color(0xFF5489F5),
                                topLeft = Offset(-offset, -offset),
                                size = Size(
                                    width = size.width + offset * 2,
                                    height = size.height + offset * 2
                                ),
                                style = Stroke(width = borderWidth)
                            )
                        }
                    }

                    Box(
                        modifier = mod.then(
                            if (interactive) {
                                Modifier
                                    .pointerInput(id) {
                                        detectTapGestures {
                                            onSelectItem(id)
                                        }
                                    }.then(
                                        if (selected == id) {
                                            Modifier.pointerInput(id) {
                                                detectDragGestures(
                                                    onDragStart = {
                                                        dragOffset = Offset.Zero
                                                    },
                                                    onDragEnd = {
                                                        val finalXFraction = dragAmountToFraction(
                                                            dragOffset.x * currentScale,
                                                            containerWidth,
                                                            targetXFraction
                                                        )
                                                        val finalYFraction = dragAmountToFraction(
                                                            dragOffset.y * currentScale,
                                                            containerHeight,
                                                            targetYFraction
                                                        )

                                                        onItemMoved(id, finalXFraction, finalYFraction)
                                                        dragOffset = Offset.Zero
                                                    },
                                                    onDragCancel = {
                                                        dragOffset = Offset.Zero
                                                    },
                                                    onDrag = { change, dragAmount ->
                                                        change.consume()
                                                        dragOffset += dragAmount
                                                    }
                                                )
                                            }
                                        } else {
                                            Modifier
                                        }
                                    )

                            } else Modifier
                        )
                    ) {
                        when (element) {
                            is FoodItem -> FoodItemDisplay(element, finalStyle)
                            is CategoryWithFoodItems -> CategoryDisplay(
                                element,
                                finalStyle,
                                screenStyle.defaultStyle ?: Style()
                            )

                            is ImageItem -> ImageItemDisplay(element, finalStyle)
                            is TextItem -> TextItemDisplay(element, finalStyle)
                            is Category -> {
                                Log.w("UFM", "Category must not in the ScreenDisplay")
                            }
                        }
                    }
                }
            }
        }
    }
}



@Preview(name="1080pLight", group = "light", showSystemUi = true, showBackground = true, device = TV_1080p, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name="720pLight", group = "light", showSystemUi = true, showBackground = true, device = TV_720p, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ScreenDisplayPreview() {
    var x4 by remember { mutableFloatStateOf(0.1F) }
    var y4 by remember { mutableFloatStateOf(0.45F) }
    val onItemMoved = { id: Int, x: Float, y: Float ->
        if (id == 4) {
            Log.d("Drag", "Dragged $x $y")
            x4 = x
            y4 = y
        }
    }
    ScreenDisplay(
        onItemMoved=onItemMoved,
        interactive = true,
        selected = 4,
        designItems = listOf(
            DesignItem(
                1,
                TextItem(
                    1,
                    "Бургерная",
                ),
                Style(
                    x = 0.1F,
                    y = 0.1F,
                    scale = 3F,
                ),
            ),
            DesignItem(
                2,
                FoodItem(
                    1,
                    "Одинокий бургер",
                    105.9F,
                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                    false,
                ),
                Style(
                    x = 0.9F,
                    y = 0.1F,
                    scale = 1.1F,
                ),
            ),
            DesignItem(
                3,
                FoodItem(
                    1,
                    "Самый крутой бургер",
                    105.9F,
                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                    true,
                ),
                Style(
                    x = 0.8F,
                    y = 0.8F,
                    scale = 2F,
                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                ),
            ),
            DesignItem(
                4,
                CategoryWithFoodItems(
                    category = Category(
                        id = 1,
                        name = "Вкусняшки",
                        imageUri = "https://i.pinimg.com/originals/b3/24/cb/b324cbabe17d8913e556722973aa6f09.png",
                        price = 1000.2F,
                        inStock = null,
                    ),
                    foodItems = listOf(
                        FoodItem(
                            1,
                            "Бургер",
                            105.9F,
                            "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                            false,
                        ),
                        FoodItem(
                            1,
                            "Пицца",
                            105.9F,
                            "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                            true,
                        )
                    ),
                ),
                Style(
                    x = x4,
                    y = y4,
                    scale = 1.5F,
                    notInStockStyle = NotInStockStyle.CROSSED_OUT,
                    showPrice = true,
                    categoryItemStyle = Style(
                        notInStockStyle = NotInStockStyle.CROSSED_OUT
                    )
                ),
            ),
            DesignItem(
                5,
                ImageItem(
                    1,
                    "https://yt3.googleusercontent.com/ytc/AIdro_kRmcneiyQOhH6n0KVn7FgbCkAfh0AI0EA9RpgzBnHq3g=s900-c-k-c0x00ffffff-no-rj"
                ),
                Style(
                    x = 0.4F,
                    y = 0.85F,
                    scale = 1.7F,
                ),
            ),
        ),
        screenStyle = ScreenStyle(),
    )
}
