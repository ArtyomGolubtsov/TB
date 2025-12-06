package com.example.tb.ui.screens.cooling

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tb.R

@Composable
fun CoolingRulesScreen(
    onBackClick: () -> Unit = {}
) {
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
            verticalArrangement = Arrangement.spacedBy(52.dp)
        ) {
            // Первая строка (1 день)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "1 день",
                    color = Color(0xFFFFEBEB), // Красноватый текст для выделения
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color(0xFF333333)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "до 15 000 ₽",
                        color = Color(0xFF767575),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                }
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color(0xFF333333)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "от 15 000 до 50 000 ₽",
                        color = Color(0xFF767575),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(start = 15.dp)
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color(0xFF333333)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "от 50 000 ₽",
                        color = Color(0xFF767575),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                }
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

@Preview(showBackground = true)
@Composable
fun CoolingRulesScreenPreview() {
    CoolingRulesScreen()
}