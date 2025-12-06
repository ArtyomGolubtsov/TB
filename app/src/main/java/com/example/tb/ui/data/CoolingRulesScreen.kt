package com.example.tb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tb.data.CoolingRule
import net.softglobal.pushnotificationtutorial.GoalNotificationService
import androidx.compose.foundation.border

@Composable
fun CoolingRulesScreen(
    goalNotificationService: GoalNotificationService,
    onSave: (selectedRule: CoolingRule?) -> Unit
) {
    var selectedRuleIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Правила охлаждения",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3A8A),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Можете настроить правила охлаждения - будем соответствовать вашим лимитам и считать время до цели",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Column(
            modifier = Modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Проверка на null для COOLING_RULES
            GoalNotificationService.COOLING_RULES?.forEachIndexed { index, rule ->
                RuleItem(
                    rule = rule,
                    isSelected = selectedRuleIndex == index,
                    onClick = { selectedRuleIndex = index }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // Проверка на null перед использованием
                val selectedRule = GoalNotificationService.COOLING_RULES.getOrNull(selectedRuleIndex)
                if (selectedRule != null) {
                    onSave(selectedRule)
                    goalNotificationService.suggestCoolingRule(selectedRule.maxAmount ?: selectedRule.minAmount)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Text(
                text = "Сохранить",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun RuleItem(
    rule: CoolingRule,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = if (isSelected) Color(0xFF4CAF50) else Color.LightGray
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE8F5E9) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = isSelected,
                    onClick = onClick
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = rule.timeframe,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = rule.amountRange,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Выбрано",
                    tint = Color(0xFF4CAF50)
                )
            }
        }
    }
}
