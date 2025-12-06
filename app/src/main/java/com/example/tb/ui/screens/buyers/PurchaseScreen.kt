package com.example.tb.ui.screens.buyers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tb.R

@Composable
fun PurchaseScreen(
    onBackClick: () -> Unit = {},
    onAddPurchaseClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 - Покупки, 1 - История
    val viewModel: PurchaseViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Стрелка назад
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
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
        }

        // Табы Покупки/История
        Box(
            modifier = Modifier
                .width(280.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Color(0xFF616161))
                .align(Alignment.CenterHorizontally)
        ) {
            // Активный таб фон
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(30.dp)
                    .align(if (selectedTab == 0) Alignment.CenterStart else Alignment.CenterEnd)
                    .clip(
                        if (selectedTab == 0)
                            RoundedCornerShape(topStart = 25.dp, bottomStart = 25.dp)
                        else
                            RoundedCornerShape(topEnd = 25.dp, bottomEnd = 25.dp)
                    )
                    .background(Color(0xFF2A64D9))
            )

            Text(
                text = "Покупки",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 44.dp)
                    .clickable { selectedTab = 0 }
            )

            Text(
                text = "История",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 44.dp)
                    .clickable { selectedTab = 1 }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Добавить покупку" - ТОЛЬКО ДЛЯ ТАБА "ПОКУПКИ"
        if (selectedTab == 0) {
            Button(
                onClick = onAddPurchaseClick,
                modifier = Modifier
                    .width(280.dp)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFDD2D),
                    contentColor = Color(0xFF141414)
                ),
                contentPadding = PaddingValues(horizontal = 25.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Добавить покупку",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // ОТОБРАЖАЕМ РАЗНЫЙ КОНТЕНТ
        when (selectedTab) {
            0 -> PurchasesList(state.activePurchases, viewModel)
            1 -> HistoryList(state.completedPurchases, state.allPurchases, viewModel)
        }
    }
}

@Composable
fun PurchasesList(
    purchases: List<Purchase>,
    viewModel: PurchaseViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.spacedBy(19.dp)
    ) {
        if (purchases.isEmpty()) {
            Text(
                text = "Нет активных покупок",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            purchases.forEach { purchase ->
                PurchaseCard(
                    purchase = purchase,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun HistoryList(
    completedPurchases: List<Purchase>,
    allPurchases: List<Purchase>,
    viewModel: PurchaseViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 40.dp)
    ) {
        // Информация о накоплениях
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFF333333)),
            contentAlignment = Alignment.Center
        ) {
            val totalAmount = completedPurchases.sumOf { it.amount }
            Text(
                text = "Всего накоплено: ${viewModel.formatAmount(totalAmount)}",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Список транзакций
        Column(
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            if (allPurchases.isEmpty()) {
                Text(
                    text = "Нет истории транзакций",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                allPurchases.forEach { purchase ->
                    TransactionItem(purchase = purchase)
                }
            }
        }
    }
}

@Composable
fun TransactionItem(purchase: Purchase) {
    val isCompleted = !purchase.isActive
    val amountColor = if (isCompleted) Color(0xFF29BF1F) else Color(0xFFEE6B42)
    val statusText = if (isCompleted) "Накоплено" else "В процессе"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF333333)),
        contentAlignment = Alignment.CenterStart
    ) {
        // Заголовок
        Text(
            text = purchase.title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 10.dp)
                .offset(y = (-16).dp)
        )

        // Категория с фоном
        Box(
            modifier = Modifier
                .padding(start = 10.dp)
                .offset(y = 16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF2A64D9)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = purchase.category,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }

        // Статус сверху справа
        Text(
            text = statusText,
            color = amountColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 11.dp, end = 11.dp)
        )

        // Сумма снизу справа
        Text(
            text = "${purchase.amount.toInt()} ₽",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 11.dp, end = 11.dp)
        )
    }
}

@Composable
fun PurchaseCard(
    purchase: Purchase,
    viewModel: PurchaseViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF333333)
        )
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Заголовок
            Text(
                text = purchase.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
            )

            // Кнопка меню (три точки) - для завершения покупки
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp)
                    .clickable {
                        viewModel.completePurchase(purchase.id)
                    }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(2.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                }
            }

            // Категория с фоном
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 45.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF2A64D9))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = purchase.category,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            // Сумма
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 72.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${purchase.amount.toInt()} ₽",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Остаток дней (пример)
            Text(
                text = "Остаток дней: 30",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 95.dp)
            )

            // До цели
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 121.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "До цели:",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${(purchase.amount * 0.6).toInt()} ₽",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }

            // Прогресс бар
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                val progressValue = viewModel.getProgressValue(purchase)
                val maxValue = 100f

                // Фон прогресс бара
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF616161))
                ) {
                    // Заполненная часть
                    Box(
                        modifier = Modifier
                            .width((progressValue / maxValue * 280).dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF2A64D9))
                    )

                    // Треугольник на конце прогресса
                    Box(
                        modifier = Modifier
                            .width(21.25.dp)
                            .height(18.dp)
                            .offset(x = (progressValue / maxValue * 280 - 21.25).dp)
                            .background(Color(0xFF2A64D9))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PurchaseScreenPreview() {
    PurchaseScreen()
}