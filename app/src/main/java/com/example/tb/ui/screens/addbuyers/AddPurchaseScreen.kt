package com.example.tb.ui.screens.addbuyers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
    var link by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

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

                SimpleTextField(
                    value = link,
                    onValueChange = { link = it },
                    placeholder = "https://..."
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

                SimpleTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Кроссовки"
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

                SimpleTextField(
                    value = category,
                    onValueChange = { category = it },
                    placeholder = "Одежда"
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

                SimpleNumericTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = "2000"
                )
            }
        }

        // Кнопка "Добавить" внизу
        Button(
            onClick = {
                // Валидация полей
                if (name.isNotEmpty() && category.isNotEmpty() && amount.isNotEmpty()) {
                    try {
                        val amountValue = amount.filter { it.isDigit() }.toDouble()
                        viewModel.addPurchase(
                            title = name,
                            category = category,
                            amount = amountValue,
                            link = link
                        )
                        onAddClick() // Возвращаемся назад
                    } catch (e: NumberFormatException) {
                        // Обработка ошибки преобразования
                    }
                }
            },
            enabled = name.isNotEmpty() && category.isNotEmpty() && amount.isNotEmpty(),
            modifier = Modifier
                .width(296.dp)
                .height(96.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 42.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (name.isNotEmpty() && category.isNotEmpty() && amount.isNotEmpty())
                    Color(0xFFFFDD2D)
                else
                    Color(0xFF616161),
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

@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    var localValue by remember { mutableStateOf(value) }

    // Синхронизируем с внешним значением
    LaunchedEffect(value) {
        if (localValue != value) {
            localValue = value
        }
    }

    Box(
        modifier = Modifier
            .width(278.dp)
            .height(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF333333))
            .border(
                width = 1.dp,
                color = if (isFocused) Color(0xFF2A64D9) else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { focusRequester.requestFocus() }
    ) {
        BasicTextField(
            value = localValue,
            onValueChange = { newValue ->
                // Для текстовых полей - любой текст
                localValue = newValue
                onValueChange(newValue)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            ),
            singleLine = true,
            cursorBrush = SolidColor(Color.White),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (localValue.isEmpty() && !isFocused) {
                        Text(
                            text = placeholder,
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
}

@Composable
fun SimpleNumericTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    var localValue by remember { mutableStateOf(value) }

    // Синхронизируем с внешним значением
    LaunchedEffect(value) {
        if (localValue != value) {
            localValue = value
        }
    }

    Box(
        modifier = Modifier
            .width(278.dp)
            .height(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF333333))
            .border(
                width = 1.dp,
                color = if (isFocused) Color(0xFF2A64D9) else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                focusRequester.requestFocus()
                isFocused = true
            }
    ) {
        BasicTextField(
            value = if (isFocused) localValue else {
                if (localValue.isEmpty()) {
                    ""
                } else {
                    val cleaned = localValue.filter { it.isDigit() }
                    val number = cleaned.toLongOrNull()
                    if (number != null) {
                        "%,d ₽".format(number).replace(',', ' ')
                    } else {
                        localValue
                    }
                }
            },
            onValueChange = { newValue ->
                // Только цифры
                val filtered = newValue.filter { it.isDigit() }
                localValue = filtered
                onValueChange(filtered)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (!focusState.isFocused) {
                        // При потере фокуса сохраняем только цифры
                        val cleaned = localValue.filter { it.isDigit() }
                        localValue = cleaned
                    }
                },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            ),
            singleLine = true,
            cursorBrush = SolidColor(Color.White),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (!isFocused && localValue.isEmpty()) {
                        Text(
                            text = placeholder,
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
}