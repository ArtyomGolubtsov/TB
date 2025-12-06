package com.example.tb.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
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
import com.example.tb.ui.theme.TBTheme

@Composable
fun SettingsMainScreen(
    onBackClick: () -> Unit = {},
    onCoolingRulesClick: () -> Unit = {},
    onNotificationSettingsClick: () -> Unit = {},
    onFinanceSettingsClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Стрелка назад
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Стрелка назад",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(20.dp, 15.dp)
                .align(Alignment.TopStart)
                .offset(x = 25.dp, y = 35.dp)
                .clickable { onBackClick() }
        )

        // Заголовок
        Text(
            text = "Настройки",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 33.dp)
        )

        // Список настроек
        val settingsItems = listOf(
            SettingsItem(
                title = "Уведомления",
                description = "Настройки оповещений",
                onClick = onNotificationSettingsClick
            ),
            SettingsItem(
                title = "Финансы",
                description = "Лимиты и бюджеты",
                onClick = onFinanceSettingsClick
            ),
            SettingsItem(
                title = "Правила охлаждения",
                description = "Настройка лимитов расходов",
                onClick = onCoolingRulesClick
            ),
            SettingsItem(
                title = "Профиль",
                description = "Личные данные",
                onClick = {}
            ),
            SettingsItem(
                title = "Безопасность",
                description = "Пароль и доступ",
                onClick = {}
            ),
            SettingsItem(
                title = "О приложении",
                description = "Версия и информация",
                onClick = {}
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(settingsItems) { item ->
                SettingsCard(item = item)
            }
        }
    }
}

@Composable
fun SettingsCard(item: SettingsItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF333333)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.description,
                    color = Color(0xFFAAAAAA),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Перейти",
                tint = Color(0xFFFFDD2D)
            )
        }
    }
}

data class SettingsItem(
    val title: String,
    val description: String,
    val onClick: () -> Unit
)

@Preview(showBackground = true)
@Composable
fun SettingsMainScreenPreview() {
    TBTheme {
        SettingsMainScreen()
    }
}