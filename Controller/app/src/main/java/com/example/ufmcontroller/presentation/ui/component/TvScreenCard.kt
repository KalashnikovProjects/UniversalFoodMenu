package com.example.ufmcontroller.presentation.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.entity.TVScreenWithDesignItems
import com.example.ufmcontroller.domain.entity.TextItem


@Composable
fun TvScreenCard(
    screenWithDesignItems: TVScreenWithDesignItems,
    onNavigateToScreen: (Int) -> Unit,
    fontSize: TextUnit = 14.sp,
    ) {
    val screenWidth = screenWithDesignItems.tvScreen.width.toFloat()
    val screenHeight = screenWithDesignItems.tvScreen.height.toFloat()

    val ratio: Float = screenWidth / screenHeight
    val size: String = "${screenWithDesignItems.tvScreen.width}x${screenWithDesignItems.tvScreen.height}"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onNavigateToScreen(screenWithDesignItems.tvScreen.id)
            }
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio)
                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)
                .clip(RoundedCornerShape(5.dp)),
        ) {
            val scaleFactor = maxWidth.value / screenWidth
            ScreenDisplay(
                screenStyle = screenWithDesignItems.tvScreen.style,
                designItems = screenWithDesignItems.designItems,
                basicScale = scaleFactor,
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                screenWithDesignItems.tvScreen.name,
                color = colorScheme.onBackground,
                fontSize = fontSize,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight(500),
                modifier = Modifier.weight(1F),
                lineHeight = fontSize,
            )
            Text(
                text=size,
                color = colorScheme.onBackground,
                textAlign = TextAlign.End,
                fontWeight = FontWeight(400),
                fontSize = fontSize * 11 / 14,
                modifier = Modifier.padding(start=15.dp)
            )
        }
    }
}

@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TvScreenCardPreview() {
    Column {
        TvScreenCard(
            TVScreenWithDesignItems(
                TVScreen(
                    0,
                    "Первый экран",
                    800,
                    600,
                    ScreenStyle(),
                ),
                designItems = emptyList()
            ),
            onNavigateToScreen = {}
        )
        Spacer(modifier = Modifier.size(7.dp))
        TvScreenCard(
            TVScreenWithDesignItems(
                TVScreen(
                    2,
                    "Экран Redmi Super Max Ultra 42",
                    1920,
                    1080,
                    ScreenStyle(
                        screenTheme = ScreenTheme.BLACK,
                    ),
                ),
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
                )
            ),
            onNavigateToScreen = {},
        )
    }

}