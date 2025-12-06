package com.example.tb.ui.screens.system_cold

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tb.R
import com.example.tb.data.CoolingRule
import com.example.tb.ui.theme.TBTheme

@Composable
fun CoolingRulesScreen(
    onBackClick: () -> Unit = {},
    onSave: (CoolingRule) -> Unit = {}
) {
    var selectedRule by remember { mutableStateOf(0) }

    val coolingRules = listOf(
        CoolingRule(
            timeframe = "1 день",
            amountRange = "до 15 000 ₽",
            minAmount = 0.0,
            maxAmount = 15000.0,
            targetDays = 1
        ),
        CoolingRule(
            timeframe = "1 неделя",
            amountRange = "от 15 000 до 50 000 ₽",
            minAmount = 15000.0,
            maxAmount = 50000.0,
            targetDays = 7
        ),
        CoolingRule(
            timeframe = "1 месяц",
            amountRange = "от 50 000 ₽",
            minAmount = 50000.0,
            maxAmount = null,
            targetDays = 30
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Заголовок
        Text(
            text = "Правила охлаждения",
            color = Color.White,
            fontSize = 20.sp,
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
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Радиокнопки для выбора правила
            coolingRules.forEachIndexed { index, rule ->
                RuleSelectionItem(
                    rule = rule,
                    isSelected = selectedRule == index,
                    onClick = { selectedRule = index }
                )
            }
        }

        // Кнопка Сохранить
        Button(
            onClick = {
                onSave(coolingRules[selectedRule])
                onBackClick()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 42.dp)
                .width(296.dp)
                .height(56.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFDD2D),
                contentColor = Color(0xFF141414)
            )
        ) {
            Text(
                text = "Сохранить",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun RuleSelectionItem(
    rule: CoolingRule,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF444444) else Color(0xFF333333)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = rule.timeframe,
                        color = if (isSelected) Color(0xFFFFEBEB) else Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = rule.amountRange,
                        color = Color(0xFF767575),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Радиокнопка
                RadioButton(
                    selected = isSelected,
                    onClick = onClick,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFFFFDD2D),
                        unselectedColor = Color.Gray
                    )
                )
            }

            // Дополнительная информация
            if (isSelected) {
                Text(
                    text = "Цель: ${rule.targetDays} день(дней)",
                    color = Color(0xFFAAAAAA),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoolingRulesScreenPreview() {
    TBTheme {
        CoolingRulesScreen()
    }
}