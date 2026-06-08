package com.kalashnikovprojects.ufmtv.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.tv.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import coil3.compose.AsyncImage
import com.kalashnikovprojects.ufmtv.domain.entity.Category
import com.kalashnikovprojects.ufmtv.domain.entity.CategoryWithFoodItems
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItemDisplayTypeStyle
import com.kalashnikovprojects.ufmtv.domain.entity.NotInStockStyle
import com.kalashnikovprojects.ufmtv.domain.entity.Style
import com.kalashnikovprojects.ufmtv.domain.entity.withDefaultStyle


@Composable
fun CategoryDisplay(
    categoryWithFoodItems: CategoryWithFoodItems,
    style: Style,
    defaultStyle: Style,
) {
    // DEFAULT notInStockStyle - OPACITY (вместе с ним прописываем  || style.notInStockStyle== null)
    val isPreview = LocalInspectionMode.current

    val category = categoryWithFoodItems.category
    val textColor = if (!style.textColorHex.isNullOrBlank()) {
        val hex = style.textColorHex.removePrefix("#")
        val cleanHex = if (hex.length == 6) "FF$hex" else hex
        Color(cleanHex.toLong(16))
    } else {
        colorScheme.onBackground
    }
    val imageUrl = if (isPreview && category.imageUri != null) android.R.drawable.ic_menu_report_image else category.imageUri
    val imageScale = style.imageScale ?: 1.0F
    val imageAlpha = if (
        (category.inStock == false) &&
        (style.notInStockStyle== NotInStockStyle.OPACITY ||
                style.notInStockStyle== NotInStockStyle.CROSSED_OUT ||
                style.notInStockStyle== null)) 0.3F else 1.0F

    val mainTextAlpha = if ((category.inStock == false) &&
        (style.notInStockStyle== NotInStockStyle.OPACITY ||
                style.notInStockStyle== null)
    ) 0.3F else 1.0F

    val priceAlpha = if (
        (category.inStock == false) &&
        (style.notInStockStyle== NotInStockStyle.OPACITY ||
                style.notInStockStyle== NotInStockStyle.CROSSED_OUT ||
                style.notInStockStyle== null)) 0.3F else 0.85F
    val mainText = if ((category.inStock == false) &&
        (style.notInStockStyle== NotInStockStyle.CROSSED_OUT))
        buildAnnotatedString {
            withStyle(
                SpanStyle(
                    textDecoration = TextDecoration.LineThrough,
                    color = textColor.copy(alpha = 0.43f)
                )
            ) {
                append(category.name)
            }
            withStyle(
                style = SpanStyle(
                    fontSize = 0.8.em,
                    color = textColor.copy(alpha = 0.85f)
                )
            ) {
                append("   Нет\u00A0в\u00A0наличии")
            }
        } else buildAnnotatedString { append(category.name) }
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier=Modifier
                .width((250 * (style.itemWidthScale ?: 1.0F)).dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                Modifier.weight(1F),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (imageUrl != null && (style.showImage == true || style.showImage == null)) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp).scale(
                            imageScale
                        ).padding(end = 7.dp),
                        alpha = imageAlpha
                    )
                }
                Text(
                    mainText,
                    fontSize = if (mainText.length > 20) 16.sp else 18.sp,
                    color = textColor.copy(alpha = mainTextAlpha),
                    minLines = if (mainText.length > 20) 2 else 1,
                    modifier = Modifier.padding(vertical = 5.dp),
                )
            }
            if (category.price != null && (style.showPrice == true || style.showPrice == null)) {
                Text(
                    category.price.toString(),
                    fontSize = 10.sp,
                    color = textColor.copy(alpha = priceAlpha),
                    modifier = Modifier.padding(
                        start = 5.dp
                    ),
                    softWrap = false,
                    maxLines = 1
                )
            }
        }
        Column(
            modifier = Modifier.padding(start=10.dp)
        ) {
            val elementStyle = if (style.categoryItemStyle != null)
                style.categoryItemStyle.withDefaultStyle(defaultStyle)
            else defaultStyle

            categoryWithFoodItems.foodItems.forEach {
                FoodItemDisplay(
                    it.copy(inStock = it.inStock && category.inStock != false),
                    elementStyle.copy(
                        foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Row
                    )
                )
            }
        }
    }
}


@Preview
@Composable
fun CategoryDisplayPreviews() {
    val data = CategoryWithFoodItems(
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
                true,
            ),
            FoodItem(
                1,
                "Пицца",
                105.9F,
                "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                true,
            )
        ),
    )
    Column {
        CategoryDisplay(
            data,
            Style(
                notInStockStyle = NotInStockStyle.CROSSED_OUT,
                showPrice = true,
                itemWidthScale = 0.8F
            ),
            Style(
                itemWidthScale = 0.8F
            )
        )

        Spacer(modifier = Modifier.size(15.dp))

        CategoryDisplay(
            data,
            Style(
                showImage = false,
                showPrice = false,
            ),
            Style()
        )

        Spacer(modifier = Modifier.size(15.dp))

        CategoryDisplay(
            data,
            Style(
                showImage = false,
                categoryItemStyle = Style(
                    showPrice = false,
                    showImage = false,
                ),
            ),
            Style()
        )
    }
}

@Preview
@Composable
fun CategoryDisplayNotInStockPreviews() {
    val data = CategoryWithFoodItems(
        category = Category(
            id = 1,
            name = "Вкусняшки",
            imageUri = "https://i.pinimg.com/originals/b3/24/cb/b324cbabe17d8913e556722973aa6f09.png",
            price = 1000.2F,
            inStock = false,
        ),
        foodItems = listOf(
            FoodItem(
                1,
                "Бургер",
                105.9F,
                "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                true,
            ),
            FoodItem(
                1,
                "Пицца",
                105.9F,
                "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                true,
            )
        ),
    )
    Column {
        CategoryDisplay(
            data,
            Style(
                notInStockStyle = NotInStockStyle.CROSSED_OUT,
                showPrice = true,
                itemWidthScale = 0.8F
            ),
            Style(
                itemWidthScale = 0.8F
            )
        )

        Spacer(modifier = Modifier.size(15.dp))

        CategoryDisplay(
            data,
            Style(
                showImage = false,
                showPrice = false,
            ),
            Style()
        )

        Spacer(modifier = Modifier.size(15.dp))

        CategoryDisplay(
            data,
            Style(
                showImage = false,
                categoryItemStyle = Style(
                    showPrice = false,
                    showImage = false,
                ),
            ),
            Style()
        )
    }
}