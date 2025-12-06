package com.example.tb.ui.screens.blacklist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tb.R

@Composable
fun BlackListScreen(
    onBackClick: () -> Unit = {},
    onAddCategoryClick: () -> Unit = {}
) {
    var selectedMonth by remember { mutableStateOf("Ноябрь") }
    val viewModel: BlacklistViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
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
            text = "Black list",
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
                .padding(top = 100.dp)
                .padding(horizontal = 16.dp)
        ) {
            // Заголовок раздела
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Расходы по категориям:",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                // Выбор месяца
                Text(
                    text = selectedMonth,
                    color = Color(0xFF2A64D9),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        // Можно добавить диалог выбора месяца
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Основная карточка категории и месяц
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Основная карточка категории
                CategoryMainCard(
                    category = state.selectedCategory,
                    modifier = Modifier.weight(1f)
                )

                // Карточка месяца
                MonthCard(
                    month = selectedMonth,
                    stats = state.monthlyStats.firstOrNull { it.month == selectedMonth },
                    modifier = Modifier.width(73.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Выбранные категории
            Text(
                text = "Выбранные категории",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Список иконок категорий
            CategoryIconsRow(
                categories = state.categories,
                onCategoryClick = { viewModel.selectCategory(it) }
            )
        }

        // Кнопка "Добавить категорию"
        Button(
            onClick = onAddCategoryClick,
            modifier = Modifier
                .width(296.dp)
                .height(106.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 42.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFDD2D),
                contentColor = Color(0xFF141414)
            )
        ) {
            Text(
                text = "Добавить категорию",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CategoryMainCard(
    category: BlacklistCategory?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(99.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF333333)
        )
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            if (category != null) {
                // Иконка категории
                Box(
                    modifier = Modifier
                        .size(20.dp, 18.dp)
                        .align(Alignment.TopStart)
                        .background(Color(0xFF2A64D9))
                )

                // Название категории
                Text(
                    text = category.name,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 28.dp)
                )

                // Статистика
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Встречается",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${category.occurrence}",
                        color = Color(0xFFFFDD2D),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "раз",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Итоговая сумма
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Всего:",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = "-${formatMoney(category.totalSpent)} ₽",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                // Кнопка блокировки
                if (category.isBlocked) {
                    Text(
                        text = "Заблокировано на ${category.daysBlocked} дней",
                        color = Color(0xFFEE6B42),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(Color(0xFF2A64D9))
                            .clickable {
                                // Блокировать категорию - можно добавить диалог
                                // viewModel.blockCategory(category.id, 30)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Блок",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                Text(
                    text = "Выберите категорию",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun MonthCard(
    month: String,
    stats: MonthlyStat?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(99.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF333333)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = month.take(3),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                if (stats != null) {
                    Text(
                        text = "-${formatMoney(stats.spent)} ₽",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "ср. ${formatMoney(stats.average)} ₽",
                        color = Color(0xFFAAAAAA),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryIconsRow(
    categories: List<BlacklistCategory>,
    onCategoryClick: (BlacklistCategory) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        categories.take(5).forEach { category ->
            CategoryIconItem(
                category = category,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@Composable
fun CategoryIconItem(
    category: BlacklistCategory,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFF333333))
                .border(
                    width = if (category.isSelected) 2.dp else 0.dp,
                    color = if (category.isSelected) Color(0xFF2A64D9) else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Иконка категории
            when (category.icon) {
                "clothes" -> Box(
                    modifier = Modifier
                        .size(20.dp, 18.dp)
                        .background(Color(0xFF2A64D9))
                )
                "tech" -> Box(
                    modifier = Modifier
                        .size(22.dp, 20.dp)
                        .background(Color(0xFF2A64D9))
                )
                "games" -> Box(
                    modifier = Modifier
                        .size(22.dp, 12.dp)
                        .background(Color(0xFF2A64D9))
                )
                "cosmetics" -> Box(
                    modifier = Modifier
                        .size(22.dp, 19.dp)
                        .background(Color(0xFF2A64D9))
                )
                "jewelry" -> Box(
                    modifier = Modifier
                        .size(20.dp, 17.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .border(
                            width = 1.dp,
                            color = Color(0xFF2A64D9),
                            shape = RoundedCornerShape(1.dp)
                        )
                )
                else -> Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color(0xFF2A64D9))
                )
            }
        }

        Text(
            text = category.name.split(" ").first(),
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}