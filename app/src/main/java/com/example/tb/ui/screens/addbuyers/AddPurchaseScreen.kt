package com.example.tb.ui.screens.addbuyers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
    var selectedCoolingTime by remember { mutableStateOf<CoolingTimeOption?>(null) }

    // Получаем ViewModel
    val viewModel: PurchaseViewModel = viewModel()

    // Расчет рекомендованных времен на основе суммы
    val (savingsBasedRecommendation, systemBasedRecommendation) = calculateRecommendations(amount)

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

        // Два блока с рекомендациями
        CoolingTimeRecommendations(
            amount = amount,
            savingsBasedRecommendation = savingsBasedRecommendation,
            systemBasedRecommendation = systemBasedRecommendation,
            selectedTime = selectedCoolingTime,
            onTimeSelected = { selectedCoolingTime = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 160.dp) // Отступ от кнопки "Добавить"
        )

        // Кнопка "Добавить" внизу
        Button(
            onClick = {
                // Валидация полей
                if (name.isNotEmpty() && category.isNotEmpty() && amount.isNotEmpty()) {
                    try {
                        val amountValue = amount.filter { it.isDigit() }.toDouble()
                        val coolingDays = selectedCoolingTime?.days ?: 0

                        viewModel.addPurchase(
                            title = name,
                            category = category,
                            amount = amountValue,
                            link = link
                        )

                        // Сохраняем выбранное время охлаждения
                        println("Время охлаждения: ${selectedCoolingTime?.name}, дней: $coolingDays")

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

// Модель для опций времени охлаждения
data class CoolingTimeOption(
    val id: Int,
    val name: String,
    val description: String,
    val days: Int,
    val type: RecommendationType
)

enum class RecommendationType {
    SAVINGS_BASED,  // На основе накоплений
    SYSTEM_BASED,   // На основе системных правил
    CUSTOM          // Пользовательский выбор
}

// Расчет рекомендованных времен
@Composable
fun calculateRecommendations(amount: String): Pair<CoolingTimeOption, CoolingTimeOption> {
    val amountValue = amount.filter { it.isDigit() }.toDoubleOrNull() ?: 0.0

    // Рекомендация на основе накоплений (чем больше сумма относительно накоплений, тем дольше)
    val savingsBasedDays = when {
        amountValue < 5000 -> 7
        amountValue < 20000 -> 14
        amountValue < 50000 -> 30
        amountValue < 100000 -> 60
        else -> 90
    }

    // Рекомендация на основе системных правил (превышение лимитов)
    val systemBasedDays = when {
        amountValue < 15000 -> 7  // До дневного лимита
        amountValue < 50000 -> 14 // До недельного лимита
        else -> 30                // Превышает месячный лимит
    }

    val savingsOption = CoolingTimeOption(
        id = 1,
        name = "$savingsBasedDays дней",
        description = "С учетом ваших накоплений",
        days = savingsBasedDays,
        type = RecommendationType.SAVINGS_BASED
    )

    val systemOption = CoolingTimeOption(
        id = 2,
        name = "$systemBasedDays дней",
        description = "В связи с ценой больше предела",
        days = systemBasedDays,
        type = RecommendationType.SYSTEM_BASED
    )

    return Pair(savingsOption, systemOption)
}

@Composable
fun CoolingTimeRecommendations(
    amount: String,
    savingsBasedRecommendation: CoolingTimeOption,
    systemBasedRecommendation: CoolingTimeOption,
    selectedTime: CoolingTimeOption?,
    onTimeSelected: (CoolingTimeOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val amountValue = amount.filter { it.isDigit() }.toDoubleOrNull() ?: 0.0
    val isAmountEntered = amountValue > 0

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Заголовок
        Text(
            text = "Рекомендованные периоды охлаждения:",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )

        if (!isAmountEntered) {
            Text(
                text = "Введите сумму, чтобы увидеть рекомендации",
                color = Color(0xFFAAAAAA),
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        } else {
            // Блок 1: Рекомендовано с учетом ваших накоплений
            RecommendationCard(
                recommendation = savingsBasedRecommendation,
                isSelected = selectedTime == savingsBasedRecommendation,
                onClick = { onTimeSelected(savingsBasedRecommendation) },
                showSavingsInfo = true,
                amount = amountValue
            )

            // Блок 2: Рекомендовано системой в связи с ценой больше предела
            RecommendationCard(
                recommendation = systemBasedRecommendation,
                isSelected = selectedTime == systemBasedRecommendation,
                onClick = { onTimeSelected(systemBasedRecommendation) },
                showSavingsInfo = false,
                amount = amountValue
            )

            // Подсказка с объяснением
            Text(
                text = selectedTime?.let {
                    when (it.type) {
                        RecommendationType.SAVINGS_BASED ->
                            "Это время поможет накопить сумму без ущерба для бюджета"
                        RecommendationType.SYSTEM_BASED ->
                            "Время основано на ваших лимитах расходов из правил охлаждения"
                        else -> "Выбрано пользовательское время охлаждения"
                    }
                } ?: "Выберите рекомендуемый период или укажите свой",
                color = Color(0xFFAAAAAA),
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Кнопка для пользовательского выбора
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF333333))
                    .clickable {
                        // Можно добавить диалог для выбора произвольного времени
                        val customOption = CoolingTimeOption(
                            id = 3,
                            name = "Ваш выбор",
                            description = "Нажмите для настройки",
                            days = 0,
                            type = RecommendationType.CUSTOM
                        )
                        onTimeSelected(customOption)
                    }
                    .border(
                        width = 1.dp,
                        color = if (selectedTime?.type == RecommendationType.CUSTOM)
                            Color(0xFF2A64D9) else Color.Transparent,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✎ Указать своё время",
                    color = if (selectedTime?.type == RecommendationType.CUSTOM)
                        Color(0xFF2A64D9) else Color(0xFF767575),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun RecommendationCard(
    recommendation: CoolingTimeOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    showSavingsInfo: Boolean,
    amount: Double
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isSelected) Color(0xFF2A64D9) else Color(0xFF333333)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF4A84F9) else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Первая строка: время и тип рекомендации
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recommendation.name,
                    color = if (isSelected) Color.White else Color(0xFF767575),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                // Метка типа рекомендации
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (isSelected) Color(0xFF4A84F9) else Color(0xFF444444)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (showSavingsInfo) "По накоплениям" else "По системе",
                        color = if (isSelected) Color.White else Color(0xFFAAAAAA),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Описание рекомендации
            Text(
                text = recommendation.description,
                color = if (isSelected) Color(0xFFCCCCCC) else Color(0xFF888888),
                fontSize = 11.sp
            )

            // Дополнительная информация в зависимости от типа
            if (showSavingsInfo) {
                // Информация о накоплениях
                Text(
                    text = "Сумма ${formatMoney(amount)} составит ${calculateSavingsPercentage(amount)}% от ваших накоплений",
                    color = if (isSelected) Color(0xFFDDDDDD) else Color(0xFF777777),
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            } else {
                // Информация о превышении лимитов
                val limitType = when {
                    amount < 15000 -> "дневного"
                    amount < 50000 -> "недельного"
                    else -> "месячного"
                }
                Text(
                    text = "Превышает $limitType лимита из ваших правил охлаждения",
                    color = if (isSelected) Color(0xFFDDDDDD) else Color(0xFF777777),
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // Галочка выбора
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFDD2D)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Вспомогательные функции
fun formatMoney(amount: Double): String {
    return "%,d ₽".format(amount.toInt()).replace(',', ' ')
}

fun calculateSavingsPercentage(amount: Double): Int {
    // Здесь можно подключить реальные данные о накоплениях из ViewModel
    val totalSavings = 100000.0 // Пример: 100 000 ₽ накоплений
    return ((amount / totalSavings) * 100).toInt().coerceAtLeast(1)
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