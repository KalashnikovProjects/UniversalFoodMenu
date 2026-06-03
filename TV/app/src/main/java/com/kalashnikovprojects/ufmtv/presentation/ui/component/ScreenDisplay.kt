package com.kalashnikovprojects.ufmtv.presentation.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Devices.TV_720p
import androidx.compose.ui.tooling.preview.Preview
import com.kalashnikovprojects.ufmtv.domain.entity.Category
import com.kalashnikovprojects.ufmtv.domain.entity.CategoryWithFoodItems
import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItemDisplayTypeStyle
import com.kalashnikovprojects.ufmtv.domain.entity.ImageItem
import com.kalashnikovprojects.ufmtv.domain.entity.NotInStockStyle
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenStyle
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenTheme
import com.kalashnikovprojects.ufmtv.domain.entity.Style
import com.kalashnikovprojects.ufmtv.domain.entity.TextItem
import com.kalashnikovprojects.ufmtv.domain.entity.withDefaultStyle
import com.kalashnikovprojects.ufmtv.presentation.theme.backgroundDark
import com.kalashnikovprojects.ufmtv.presentation.theme.backgroundLight


@Composable
fun ScreenDisplay(
    screenStyle: ScreenStyle,
    designItems: List<DesignItem>,
    basicScale: Float = 1F,
) {
    val themeBackgroundColor = if (screenStyle.screenTheme == ScreenTheme.WHITE)
        backgroundLight
    else backgroundDark
    val backgroundColor = if (screenStyle.backgroundColorHex != null)
        Color(screenStyle.backgroundColorHex.toLong(16))
    else themeBackgroundColor

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(backgroundColor),
    ) {
        val containerWidth = constraints.maxWidth
        val containerHeight = constraints.maxHeight

        designItems.forEach { (id, element, style) ->
            key(id) {
                val finalStyle = if (screenStyle.defaultStyle != null)
                    style.withDefaultStyle(screenStyle.defaultStyle)
                else style

                val currentScale = (style.scale ?: screenStyle.defaultStyle?.scale ?: 0.5F) * basicScale

                val targetXFraction = style.x ?: 0.5f
                val targetYFraction = style.y ?: 0.5f

                Box(
                    modifier = Modifier
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            val itemWidth = placeable.width
                            val itemHeight = placeable.height
                            val rawX = (containerWidth * targetXFraction).toInt()
                            val rawY = (containerHeight * targetYFraction).toInt()
                            val topLeftX = rawX - ((itemWidth - (itemWidth * currentScale)) / 2).toInt()
                            val topLeftY = rawY - ((itemHeight - (itemHeight * currentScale)) / 2).toInt()

                            layout(itemWidth, itemHeight) {
                                placeable.place(topLeftX, topLeftY)
                            }
                        }
                        .scale(currentScale)
                ) {
                    when (element) {
                        is FoodItem -> FoodItemDisplay(element, finalStyle)
                        is CategoryWithFoodItems -> CategoryDisplay(element, finalStyle, screenStyle.defaultStyle ?: Style())
                        is ImageItem -> ImageItemDisplay(element, finalStyle)
                        is TextItem -> TextItemDisplay(element, finalStyle)
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
    ScreenDisplay(
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
                    x = 0.1F,
                    y = 0.45F,
                    scale = 1.5F,
                    notInStockStyle = NotInStockStyle.CROSSED_OUT,
                    showPrice = true,
                    categoryItemStyle = Style(
                        notInStockStyle = NotInStockStyle.CROSSED_OUT)
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
