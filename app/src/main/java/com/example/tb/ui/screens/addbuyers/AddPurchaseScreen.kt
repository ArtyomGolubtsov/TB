package com.example.tb.ui.screens.addbuyers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tb.R
import com.example.tb.ui.screens.buyers.PurchaseViewModel

@Composable
fun AddPurchaseScreen(
    onBackClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    var link by remember { mutableStateOf(TextFieldValue()) }
    var name by remember { mutableStateOf(TextFieldValue()) }
    var category by remember { mutableStateOf(TextFieldValue()) }
    var amount by remember { mutableStateOf(TextFieldValue()) }

    // Получаем ViewModel
    val viewModel: PurchaseViewModel = viewModel()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Status Bar фон
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(Color.Black)
        )

        // Стрелка назад
        Box(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 43.dp)
                .clickable { onBackClick() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.path1),
                contentDescription = "Стрелка назад",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(12.dp, 24.dp)
                    .align(Alignment.Center)
            )
        }

        // Заголовок
        Text(
            text = "Добавьте покупку",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 41.dp)
        )

        // Основной контент
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Поле "Введите ссылку"
            Column(
                modifier = Modifier
                    .width(278.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Введите ссылку",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                BasicTextField(
                    value = link,
                    onValueChange = { link = it },
                    textStyle = TextStyle(
                        color = if (link.text.isEmpty()) Color(0xFF494949) else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier
                        .width(278.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF333333))
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (link.text.isEmpty()) {
                                Text(
                                    text = "https://...",
                                    color = Color(0xFF494949),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            // Разделитель "или"
            Text(
                text = "или",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Поле "Введите название"
            Column(
                modifier = Modifier
                    .width(278.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Введите название:",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    textStyle = TextStyle(
                        color = if (name.text.isEmpty()) Color(0xFF494949) else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier
                        .width(278.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF333333))
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (name.text.isEmpty()) {
                                Text(
                                    text = "Кроссовки",
                                    color = Color(0xFF494949),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            // Поле "Введите категорию"
            Column(
                modifier = Modifier
                    .width(278.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Введите категорию:",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                BasicTextField(
                    value = category,
                    onValueChange = { category = it },
                    textStyle = TextStyle(
                        color = if (category.text.isEmpty()) Color(0xFF494949) else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier
                        .width(278.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF333333))
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (category.text.isEmpty()) {
                                Text(
                                    text = "Одежда",
                                    color = Color(0xFF494949),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            // Поле "Введите сумму"
            Column(
                modifier = Modifier
                    .width(278.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Введите сумму:",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                BasicTextField(
                    value = amount,
                    onValueChange = {
                        // Фильтруем только цифры
                        val filtered = it.text.filter { char -> char.isDigit() }
                        amount = it.copy(text = filtered)
                    },
                    textStyle = TextStyle(
                        color = if (amount.text.isEmpty()) Color(0xFF494949) else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier
                        .width(278.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF333333))
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (amount.text.isEmpty()) {
                                Text(
                                    text = "2000",
                                    color = Color(0xFF494949),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            } else {
                                Text(
                                    text = "${amount.text} ₽",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        // Кнопка "Добавить" внизу
        Button(
            onClick = {
                // Валидация полей
                if (name.text.isNotEmpty() && category.text.isNotEmpty() && amount.text.isNotEmpty()) {
                    try {
                        val amountValue = amount.text.toDouble()
                        viewModel.addPurchase(
                            title = name.text,
                            category = category.text,
                            amount = amountValue,
                            link = link.text
                        )
                        onAddClick() // Возвращаемся назад
                    } catch (e: NumberFormatException) {
                        // Обработка ошибки преобразования
                    }
                }
            },
            modifier = Modifier
                .width(296.dp)
                .height(96.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 42.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFDD2D),
                contentColor = Color(0xFF141414)
            ),
            contentPadding = PaddingValues(horizontal = 25.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Добавить",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}