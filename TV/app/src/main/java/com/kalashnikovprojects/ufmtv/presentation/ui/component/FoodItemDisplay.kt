package com.kalashnikovprojects.ufmtv.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import coil3.compose.AsyncImage
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItemDisplayTypeStyle
import com.kalashnikovprojects.ufmtv.domain.entity.NotInStockStyle
import com.kalashnikovprojects.ufmtv.domain.entity.Style

@Composable
fun FoodItemDisplay(
    foodItem: FoodItem,
    style: Style,
) {
    // DEFAULT notInStockStyle - OPACITY (вместе с ним прописываем  || style.notInStockStyle== null)
    if (!foodItem.inStock && style.notInStockStyle== NotInStockStyle.HIDDEN) {
        return
    }
    val isPreview = LocalInspectionMode.current

    val textColor = Color(style.textColorHex?.toLong(16) ?: 0xFFFFFFFF)
    val imageUrl = if (isPreview && foodItem.imageUri != null) android.R.drawable.ic_menu_report_image else foodItem.imageUri
    val imageScale = style.imageScale ?: 1.0F
    val imageAlpha = if (
        !foodItem.inStock &&
        (style.notInStockStyle== NotInStockStyle.OPACITY ||
                style.notInStockStyle== NotInStockStyle.CROSSED_OUT ||
                style.notInStockStyle== null)) 0.3F else 1.0F

    val mainTextAlpha = if (!foodItem.inStock &&
        (style.notInStockStyle== NotInStockStyle.OPACITY ||
        style.notInStockStyle== null)
        ) 0.3F else 1.0F

    val priceAlpha = if (
        !foodItem.inStock &&
        (style.notInStockStyle== NotInStockStyle.OPACITY ||
                style.notInStockStyle== NotInStockStyle.CROSSED_OUT ||
                style.notInStockStyle== null)) 0.3F else 0.85F
    val mainText = if (!foodItem.inStock &&
        (style.notInStockStyle== NotInStockStyle.CROSSED_OUT))
        buildAnnotatedString {
            withStyle(
                SpanStyle(
                    textDecoration = TextDecoration.LineThrough,
                    color = textColor.copy(alpha = 0.43f)
                )
            ) {
                append(foodItem.name)
            }
            withStyle(
                style = SpanStyle(
                    fontSize = 0.8.em,
                    color = textColor.copy(alpha = 0.85f)
                )
            ) {
                append("   Нет\u00A0в\u00A0наличии")
            }
        } else buildAnnotatedString { append(foodItem.name) }

    when (style.foodItemDisplayTypeStyle) {
        is FoodItemDisplayTypeStyle.Row, null -> Row(
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
                        modifier = Modifier.size(30.dp).scale(
                            imageScale
                        ).padding(end = 7.dp),
                        alpha = imageAlpha
                    )
                }
                Text(mainText,
                    fontSize = 13.sp,
                    color=textColor.copy(alpha = mainTextAlpha),
                    modifier = Modifier.padding(vertical = 2.dp),
                    softWrap = true,
                )
            }
            if (style.showPrice == true || style.showPrice == null) {
                Text(foodItem.price.toString(),
                    fontSize = 10.sp,
                    color=textColor.copy(alpha = priceAlpha),
                    modifier = Modifier.padding(start = 5.dp, end=5.dp),
                    textAlign = TextAlign.End,
                    softWrap = false,
                    maxLines = 1
                )
            }
        }
        is FoodItemDisplayTypeStyle.Cell -> Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (imageUrl != null && (style.showImage == true || style.showImage == null)) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp).scale(
                        imageScale
                    ),
                    alpha = imageAlpha
                )
            }
            Text(mainText,
                fontSize = 13.sp,
                color=textColor.copy(alpha = mainTextAlpha),
                textAlign = TextAlign.Center,
                modifier=Modifier
                    .width((120 * (style.itemWidthScale ?: 1.0F)).dp),
                softWrap = true,
            )
            if (style.showPrice == true || style.showPrice == null) {
                Text(foodItem.price.toString(),
                    fontSize = 10.sp,
                    color=textColor.copy(alpha = priceAlpha),
                    modifier = Modifier.padding(top = 1.dp),
                )
            }
        }
    }
}

@Preview
@Composable
fun FoodItemDisplayRowPreviews() {
    Column {
        FoodItemDisplay(
            FoodItem(
                1,
                "Бургер",
                105.9F,
                null,
                true,
            ),
            Style(
                notInStockStyle = NotInStockStyle.CROSSED_OUT,
            )
        )
        FoodItemDisplay(
            FoodItem(
                1,
                "Бургер",
                105.9F,
                "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                true,
            ),
            Style(
                notInStockStyle = NotInStockStyle.CROSSED_OUT,
                showPrice = true,
                showImage = true,
            )
        )
        FoodItemDisplay(
            FoodItem(
                1,
                "Бургер",
                105.9F,
                "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                true,
            ),
            Style(
                foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Row,
                notInStockStyle = NotInStockStyle.CROSSED_OUT,
                showPrice = true,
                showImage = true,
            )
        )
        FoodItemDisplay(
            FoodItem(
                1,
                "Бургер",
                105.9F,
                "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                true,
            ),
            Style(
                imageScale = 1.3F,
                foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Row,
                notInStockStyle = NotInStockStyle.CROSSED_OUT,
                showPrice = false,
            )
        )
        FoodItemDisplay(
            FoodItem(
                1,
                "Бургер с оооочень длинным названием вау",
                105.9F,
                "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                true,
            ),
            Style(
                notInStockStyle = NotInStockStyle.CROSSED_OUT,
                showPrice = true,
                showImage = true,
            )
        )
        FoodItemDisplay(
            FoodItem(
                1,
                "Бургер без картинки",
                105.9F,
                "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                true,
            ),
            Style(
                notInStockStyle = NotInStockStyle.CROSSED_OUT,
                showPrice = true,
                showImage = false,
            )
        )
    }
}


@Preview
@Composable
fun FoodItemDisplayRowNotInStockPreviews() {
    Column {
        FoodItemDisplay(
            FoodItem(
                1,
                "Бургер",
                105.9F,
                null,
                false,
            ),
            Style(
            )
        )
        FoodItemDisplay(
            FoodItem(
                1,
                "Бургер",
                105.9F,
                "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                false,
            ),
            Style(
                notInStockStyle = NotInStockStyle.CROSSED_OUT,
                showPrice = true,
                showImage = true,
            )
        )
        FoodItemDisplay(
            FoodItem(
                1,
                "Бургер",
                105.9F,
                "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                false,
            ),
            Style(
                foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Row,
                notInStockStyle = NotInStockStyle.OPACITY,
                showPrice = true,
                showImage = true,
            )
        )
        FoodItemDisplay(
            FoodItem(
                1,
                "Бургер",
                105.9F,
                "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                false,
            ),
            Style(
                imageScale = 1.3F,
                foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Row,
                notInStockStyle = NotInStockStyle.CROSSED_OUT,
                showPrice = false,
            )
        )
        FoodItemDisplay(
            FoodItem(
                1,
                "Бургер с оооочень длинным названием вау",
                105.9F,
                "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                false,
            ),
            Style(
                showPrice = true,
                showImage = true,
            )
        )
        FoodItemDisplay(
            FoodItem(
                1,
                "Бургер без картинки",
                105.9F,
                "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                false,
            ),
            Style(
                notInStockStyle = NotInStockStyle.OPACITY,
                showPrice = true,
                showImage = false,
                itemWidthScale = 0.5F,
                )
        )
    }
}

@Preview
@Composable
fun FoodItemDisplayCellPreviews() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
    ) {
        item{
            FoodItemDisplay(
                FoodItem(
                    1,
                    "Король бургер 🍔",
                    105.9F,
                    null,
                    true,
                ),
                Style(
                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                )
            )
        }
        item{
            FoodItemDisplay(
                FoodItem(
                    1,
                    "Король бургер 🍔",
                    105.9F,
                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                    true,
                ),
                Style(
                    imageScale = 0.7F,
                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                    notInStockStyle = NotInStockStyle.CROSSED_OUT,
                    showPrice = true,
                    showImage = true,
                )
            )
        }
        item {
            FoodItemDisplay(
                FoodItem(
                    1,
                    "Король бургер 🍔",
                    105.9F,
                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                    true,
                ),
                Style(
                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                    notInStockStyle = NotInStockStyle.OPACITY,
                    showPrice = true,
                    showImage = true,
                )
            )
        }
        item {
            FoodItemDisplay(
                FoodItem(
                    1,
                    "Король бургер 🍔",
                    105.9F,
                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                    true,
                ),
                Style(
                    imageScale = 1.3F,
                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                    notInStockStyle = NotInStockStyle.CROSSED_OUT,
                    showPrice = false,
                )
            )
        }
        item{
            FoodItemDisplay(
                FoodItem(
                    1,
                    "Король бургер 🍔 с очень длинным названием вау",
                    105.9F,
                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                    true,
                ),
                Style(
                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                    showPrice = true,
                    showImage = true,
                )
            )
        }
        item{
            FoodItemDisplay(
                FoodItem(
                    1,
                    "Король бургер 🍔 без картинки",
                    105.9F,
                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                    true,
                ),
                Style(
                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                    notInStockStyle = NotInStockStyle.OPACITY,
                    showPrice = true,
                    showImage = false,
                )
            )
        }
    }
}


@Preview
@Composable
fun FoodItemDisplayCellNotInStockPreviews() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
    ) {
        item{
            FoodItemDisplay(
                FoodItem(
                    1,
                    "Король бургер 🍔",
                    105.9F,
                    null,
                    false,
                ),
                Style(
                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                )
            )
        }
        item{
            FoodItemDisplay(
                FoodItem(
                    1,
                    "Король бургер 🍔",
                    105.9F,
                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                    false,
                ),
                Style(
                    imageScale = 0.7F,
                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                    notInStockStyle = NotInStockStyle.CROSSED_OUT,
                    showPrice = true,
                    showImage = true,
                )
            )
        }
        item {
            FoodItemDisplay(
                FoodItem(
                    1,
                    "Король бургер 🍔",
                    105.9F,
                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                    false,
                ),
                Style(
                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                    notInStockStyle = NotInStockStyle.OPACITY,
                    showPrice = true,
                    showImage = true,
                )
            )
        }
        item {
            FoodItemDisplay(
                FoodItem(
                    1,
                    "Король бургер 🍔",
                    105.9F,
                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                    false,
                ),
                Style(
                    imageScale = 1.3F,
                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                    showPrice = false,
                )
            )
        }
        item{
            FoodItemDisplay(
                FoodItem(
                    1,
                    "Король бургер 🍔 с очень длинным названием вау",
                    105.9F,
                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                    false,
                ),
                Style(
                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                    showPrice = true,
                    showImage = true,
                )
            )
        }
        item{
            FoodItemDisplay(
                FoodItem(
                    1,
                    "Король бургер 🍔 без картинки",
                    105.9F,
                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                    false,
                ),
                Style(
                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                    notInStockStyle = NotInStockStyle.OPACITY,
                    showPrice = true,
                    showImage = false,
                    itemWidthScale = 0.5F,
                )
            )
        }
    }
}
