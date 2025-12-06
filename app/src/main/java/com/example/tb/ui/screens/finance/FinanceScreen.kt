package com.example.tb.ui.screens.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FinanceScreen(
    onBackClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Переключатель Покупки/История
        PurchaseHistoryToggle()

        // Информация о накоплениях
        SavingsInfo()

        // Список транзакций
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 157.dp)
                .padding(horizontal = 40.dp)
        ) {
            // Транзакция 1 (Потрачено)
            TransactionItem(
                title = "Iphone",
                category = "Техника",
                amountText = "Потрачено",
                amountColor = Color(0xFFEE6B42),
                type = TransactionType.SPENT
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Транзакция 2 (Накоплено)
            TransactionItem(
                title = "Iphone",
                category = "Техника",
                amountText = "Накоплено",
                amountColor = Color(0xFF29BF1F),
                type = TransactionType.SAVED
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Транзакция 3 (Потрачено)
            TransactionItem(
                title = "Кроссовки",
                category = "Одежда",
                amountText = "Потрачено",
                amountColor = Color(0xFFEE6B42),
                type = TransactionType.SPENT
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Транзакция 4 (Накоплено)
            TransactionItem(
                title = "Путешествие",
                category = "Отдых",
                amountText = "Накоплено",
                amountColor = Color(0xFF29BF1F),
                type = TransactionType.SAVED
            )
        }
    }
}

@Composable
fun PurchaseHistoryToggle() {
    var selectedTab by remember { mutableStateOf(0) } // 0 - Покупки, 1 - История

    Box(
        modifier = Modifier
            .padding(top = 59.dp)
            .padding(horizontal = 40.dp)
            .fillMaxWidth()
            .height(30.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color(0xFF616161))
    ) {
        // Активная часть переключателя
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(140.dp)
                .offset(x = if (selectedTab == 0) 0.dp else 140.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Color(0xFF2A64D9))
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Покупки",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.clickable { selectedTab = 0 }
            )
            Text(
                text = "История",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.clickable { selectedTab = 1 }
            )
        }
    }
}

@Composable
fun SavingsInfo() {
    Box(
        modifier = Modifier
            .padding(top = 105.dp)
            .padding(horizontal = 40.dp)
            .fillMaxWidth()
            .height(30.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color(0xFF333333)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "В этом месяце накоплено 12 000 ₽",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

enum class TransactionType {
    SPENT, SAVED
}

@Composable
fun TransactionItem(
    title: String,
    category: String,
    amountText: String,
    amountColor: Color,
    type: TransactionType
) {
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
            text = title,
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
                text = category,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }

        // Сумма/статус справа
        Text(
            text = amountText,
            color = amountColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 11.dp, end = 11.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun FinanceScreenPreview() {
    FinanceScreen()
}