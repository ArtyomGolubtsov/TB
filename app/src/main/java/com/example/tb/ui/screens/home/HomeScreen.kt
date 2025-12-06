package com.example.tb.ui.screens.home

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
fun HomeScreen(
    onCoolingRulesClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onBlacklistClick: () -> Unit = {},
    onPurchasesClick: () -> Unit = {} // Добавим для навигации на покупки
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0C0C))
    ) {
        // Верхняя белая часть с изображением
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.45f) // 45% высоты экрана
                .background(Color.White)
        ) {
            // Основное изображение
            Image(
                painter = painterResource(id = R.drawable.image_103),
                contentDescription = "Основной баннер",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Стрелка назад
            Image(
                painter = painterResource(id = R.drawable.path2),
                contentDescription = "Стрелка назад",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .clickable { /* Действие при нажатии на стрелку */ }
            )

            // Заголовок в правом верхнем углу
            Text(
                text = "Т-импульс",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
            )
        }

        // Основной контент - нижняя часть
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.55f) // 55% высоты экрана
                .padding(16.dp)
        ) {
            // Сетка карточек
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Первая строка: Покупки и Настройки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Карточка "Покупки" - занимает 40% ширины
                    Box(
                        modifier = Modifier
                            .weight(0.4f)
                            .aspectRatio(1f) // Квадратная
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color(0xFF333333))
                            .clickable { onPurchasesClick() },
                        contentAlignment = Alignment.TopStart
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Покупки",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Image(
                                painter = painterResource(id = R.drawable.bag_2),
                                contentDescription = "Иконка покупок",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(80.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }

                    // Правая колонка: Настройки и Правила охлаждения
                    Column(
                        modifier = Modifier.weight(0.6f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Карточка "Настройки"
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(Color(0xFF333333))
                                .clickable { onSettingsClick() },
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Настройки",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Image(
                                    painter = painterResource(id = R.drawable.settings_future),
                                    contentDescription = "Иконка настроек",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }

                        // Карточка "Правила охлаждения"
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(Color(0xFF333333))
                                .clickable { onCoolingRulesClick() },
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Правила\nохлаждения",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 18.sp
                                )

                                Image(
                                    painter = painterResource(id = R.drawable.temp_cold_line),
                                    contentDescription = "Иконка правил охлаждения",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                        }
                    }
                }

                // Карточка "Blacklist категорий"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color(0xFF333333))
                        .clickable { onBlacklistClick() },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Blacklist категорий",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Image(
                            painter = painterResource(id = R.drawable.category_2),
                            contentDescription = "Иконка добавления",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Нижнее изображение
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(15.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.image_102),
                        contentDescription = "Нижний баннер",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}