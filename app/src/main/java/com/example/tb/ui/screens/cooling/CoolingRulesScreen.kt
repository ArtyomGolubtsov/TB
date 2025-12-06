package com.example.tb.ui.screens.cooling

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tb.R

@Composable
fun CoolingRulesScreen(
    onBackClick: () -> Unit = {}
) {
    var dayLimit by remember { mutableStateOf("15000") }
    var weekMinLimit by remember { mutableStateOf("15000") }
    var weekMaxLimit by remember { mutableStateOf("50000") }
    var monthLimit by remember { mutableStateOf("50000") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Заголовок
        Text(
            text = "Правила охлаждения",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 33.dp)
        )

        // Описание
        Text(
            text = "Можете настроить правила охлаждения - будем соответствовать вашим лимитам и считать время до цели",
            color = Color(0xFF847676),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp,
            modifier = Modifier
                .width(279.dp)
                .align(Alignment.TopCenter)
                .padding(top = 84.dp)
        )

        // Стрелка назад
        Image(
            painter = painterResource(id = R.drawable.path1),
            contentDescription = "Стрелка назад",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(20.dp, 15.dp)
                .align(Alignment.TopStart)
                .offset(x = 25.dp, y = 35.dp)
                .clickable {
                    onBackClick()
                }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 187.dp)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Первая строка (1 день)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "1 день",
                    color = Color(0xFFFFEBEB),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp,
                )
                CoolingInputField(
                    value = dayLimit,
                    onValueChange = { dayLimit = it },
                    prefix = "до",
                    suffix = "₽"
                )
            }

            // Вторая строка (1 неделя)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "1 неделя",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CoolingInputField(
                        value = weekMinLimit,
                        onValueChange = { weekMinLimit = it },
                        prefix = "от",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "до",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    CoolingInputField(
                        value = weekMaxLimit,
                        onValueChange = { weekMaxLimit = it },
                        suffix = "₽",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Третья строка (1 месяц)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "1 месяц",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp,
                )
                CoolingInputField(
                    value = monthLimit,
                    onValueChange = { monthLimit = it },
                    prefix = "от",
                    suffix = "₽"
                )
            }
        }

        // Кнопка Сохранить
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 42.dp)
                .width(296.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFFFDD2D))
                .clickable {
                    // TODO: Сохранить настройки
                    onBackClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Сохранить",
                color = Color(0xFF141414),
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun CoolingInputField(
    value: String,
    onValueChange: (String) -> Unit,
    prefix: String = "",
    suffix: String = "",
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    var displayText by remember { mutableStateOf(value) }

    // Синхронизируем с внешним значением
    LaunchedEffect(value) {
        if (displayText != value) {
            displayText = value
        }
    }

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color(0xFF333333))
            .border(
                width = 1.dp,
                color = if (isFocused) Color(0xFF2A64D9) else Color.Transparent,
                shape = RoundedCornerShape(25.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (prefix.isNotEmpty() && !isFocused) {
                Text(
                    text = "$prefix ",
                    color = Color(0xFF767575),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp
                )
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                BasicTextField(
                    value = if (isFocused) {
                        displayText
                    } else {
                        val cleaned = displayText.filter { it.isDigit() }
                        val number = cleaned.toLongOrNull()
                        if (number != null) {
                            "%,d".format(number).replace(',', ' ')
                        } else {
                            displayText
                        }
                    },
                    onValueChange = { newValue ->
                        // Разрешаем только цифры
                        val filtered = newValue.filter { it.isDigit() }
                        displayText = filtered
                        onValueChange(filtered)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                            if (!focusState.isFocused) {
                                // При потере фокуса сохраняем только цифры
                                val cleaned = displayText.filter { it.isDigit() }
                                displayText = cleaned
                            }
                        },
                    textStyle = TextStyle(
                        color = if (isFocused) Color.White else Color(0xFF767575),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(Color.White),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (displayText.isEmpty() && !isFocused) {
                                Text(
                                    text = "0",
                                    color = Color(0xFF767575),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 18.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            if (suffix.isNotEmpty() && !isFocused) {
                Text(
                    text = " $suffix",
                    color = Color(0xFF767575),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoolingRulesScreenPreview() {
    CoolingRulesScreen()
}