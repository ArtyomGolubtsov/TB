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
import androidx.compose.ui.window.Dialog
import com.example.tb.R
import com.example.tb.ui.screens.buyers.PurchaseViewModel
import com.example.tb.ui.screens.cooling.CoolingRulesViewModel
import com.example.tb.ui.screens.setting.SettingsViewModel
import com.example.tb.ui.screens.blacklist.BlacklistViewModel
import com.example.tb.ui.screens.blacklist.BlacklistCategory
import kotlin.math.ceil

// --- Определение классов внутри файла ---

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

data class SavingsInfo(
    val canBuyNow: Boolean,
    val neededAmount: Double,
    val monthsNeeded: Int,
    val comfortableAmount: Double,
    val currentSavings: Double,
    val monthlySavings: Double,
    val considerSavings: Boolean
)

data class SystemLimitInfo(
    val exceedsAnyLimit: Boolean,
    val limitType: String,
    val limitValue: Int
)

@Composable
fun AddPurchaseScreen(
    purchaseViewModel: PurchaseViewModel,
    coolingRulesViewModel: CoolingRulesViewModel,
    settingsViewModel: SettingsViewModel,
    blacklistViewModel: BlacklistViewModel,
    onBackClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    var link by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCoolingTime by remember { mutableStateOf<CoolingTimeOption?>(null) }

    // Состояние для проверки категории
    var categoryError by remember { mutableStateOf<String?>(null) }
    var isCategoryBlocked by remember { mutableStateOf(false) }
    var showBlockedWarning by remember { mutableStateOf(false) }

    // Получаем текущие правила охлаждения
    val coolingRules by coolingRulesViewModel.state.collectAsState()

    // Получаем текущие настройки
    val settings by settingsViewModel.state.collectAsState()

    // Получаем черный список
    val blacklistState by blacklistViewModel.state.collectAsState()

    // Функция проверки категории в черном списке
    fun checkCategoryInBlacklist(categoryName: String): BlacklistCategory? {
        return blacklistState.categories.find {
            it.name.equals(categoryName, ignoreCase = true)
        }
    }

    // При изменении категории проверяем черный список
    LaunchedEffect(category) {
        if (category.isNotEmpty()) {
            val blockedCategory = checkCategoryInBlacklist(category)
            // Используем безопасный вызов
            isCategoryBlocked = blockedCategory?.isBlocked == true

            if (isCategoryBlocked) {
                categoryError = "Эта категория заблокирована на ${blockedCategory?.daysBlocked ?: 0} дней"
            } else if (blockedCategory != null) {
                categoryError = "⚠️ Эта категория в черном списке (трачено: ${formatMoney(blockedCategory.totalSpent)})"
            } else {
                categoryError = null
            }
        } else {
            categoryError = null
            isCategoryBlocked = false
        }
    }

    // Расчет рекомендованных времен на основе суммы, правил и настроек
    val (savingsBasedRecommendation, systemBasedRecommendation) =
        calculateRecommendations(amount, coolingRules, settings)

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

            // Поле "Введите категорию" - с проверкой черного списка
            Column(
                modifier = Modifier
                    .width(278.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Введите категорию:",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    // Иконка предупреждения если категория в черном списке
                    if (categoryError != null) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFDD2D))
                                .clickable { showBlockedWarning = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "!",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                SimpleTextField(
                    value = category,
                    onValueChange = {
                        category = it
                        // Очищаем ошибку при изменении
                        if (categoryError != null) {
                            categoryError = null
                        }
                    },
                    placeholder = "Одежда",
                    isError = categoryError != null
                )

                // Сообщение об ошибке/предупреждении
                if (categoryError != null) {
                    Text(
                        text = categoryError!!,
                        color = if (isCategoryBlocked) Color(0xFFEE6B42) else Color(0xFFFFDD2D),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
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
            coolingRules = coolingRules,
            settings = settings,
            savingsBasedRecommendation = savingsBasedRecommendation,
            systemBasedRecommendation = systemBasedRecommendation,
            selectedTime = selectedCoolingTime,
            onTimeSelected = { selectedCoolingTime = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 160.dp)
        )

        // Кнопка "Добавить" внизу
        Button(
            onClick = {
                if (name.isNotEmpty() && category.isNotEmpty() && amount.isNotEmpty()) {
                    try {
                        val amountValue = amount.filter { it.isDigit() }.toDouble()
                        val coolingDays = selectedCoolingTime?.days ?: 0

                        // Проверяем категорию в черном списке
                        val blockedCategory = checkCategoryInBlacklist(category)

                        // Используем безопасный вызов
                        if (blockedCategory?.isBlocked == true) {
                            // Если категория заблокирована, показываем диалог подтверждения
                            showBlockedWarning = true
                        } else {
                            // Если категория не заблокирована, добавляем покупку
                            purchaseViewModel.addPurchase(
                                title = name,
                                category = category,
                                amount = amountValue,
                                link = link,
                                coolingDays = coolingDays
                            )

                            onAddClick()
                            onBackClick()
                        }
                    } catch (e: NumberFormatException) {
                        // Обработка ошибки
                    }
                }
            },
            enabled = name.isNotEmpty() && category.isNotEmpty() && amount.isNotEmpty() && !isCategoryBlocked,
            modifier = Modifier
                .width(296.dp)
                .height(96.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 42.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (name.isNotEmpty() && category.isNotEmpty() && amount.isNotEmpty() && !isCategoryBlocked)
                    Color(0xFFFFDD2D)
                else
                    Color(0xFF616161),
                contentColor = Color(0xFF141414)
            ),
            contentPadding = PaddingValues(horizontal = 25.dp, vertical = 16.dp)
        ) {
            Text(
                text = if (isCategoryBlocked) "Категория заблокирована" else "Добавить",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // Диалог предупреждения о заблокированной категории
    if (showBlockedWarning) {
        val blockedCategory = checkCategoryInBlacklist(category)
        BlockedCategoryDialog(
            categoryName = category,
            blockedCategory = blockedCategory,
            onConfirm = {
                showBlockedWarning = false
                // Пользователь подтверждает, что понимает риск
                try {
                    val amountValue = amount.filter { it.isDigit() }.toDouble()
                    val coolingDays = selectedCoolingTime?.days ?: 0

                    purchaseViewModel.addPurchase(
                        title = name,
                        category = category,
                        amount = amountValue,
                        link = link,
                        coolingDays = coolingDays
                    )

                    onAddClick()
                    onBackClick()
                } catch (e: NumberFormatException) {
                    // Обработка ошибки
                }
            },
            onDismiss = {
                showBlockedWarning = false
            }
        )
    }
}

// Диалог для заблокированной категории
@Composable
fun BlockedCategoryDialog(
    categoryName: String,
    blockedCategory: BlacklistCategory?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF3D1A1A))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "⚠️ Внимание!",
                    color = Color(0xFFEE6B42),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (blockedCategory != null && blockedCategory.isBlocked) {
                    Text(
                        text = "Категория \"$categoryName\" заблокирована на ${blockedCategory.daysBlocked} дней!",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Вы уже потратили ${formatMoney(blockedCategory.totalSpent)} в этой категории.",
                        color = Color(0xFFAAAAAA),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Действительно хотите добавить покупку в заблокированную категорию?",
                        color = Color(0xFFFFB74D),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                } else if (blockedCategory != null) {
                    Text(
                        text = "Категория \"$categoryName\" находится в черном списке.",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Вы уже потратили ${formatMoney(blockedCategory.totalSpent)} в этой категории.",
                        color = Color(0xFFAAAAAA),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Возможно, стоит выбрать другую категорию.",
                        color = Color(0xFFFFB74D),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                } else {
                    Text(
                        text = "Категория \"$categoryName\"",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Вы действительно хотите добавить покупку?",
                        color = Color(0xFFAAAAAA),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF2A5A2A))
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Изменить категорию",
                            color = Color(0xFF13B008),
                            fontSize = 14.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF653E31))
                            .clickable(onClick = onConfirm),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Всё равно добавить",
                            color = Color(0xFFEE6B42),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

// --- Функции расчета ---

@Composable
fun calculateRecommendations(
    amount: String,
    coolingRules: com.example.tb.ui.screens.cooling.CoolingRulesState,
    settings: com.example.tb.ui.screens.setting.SettingsState
): Pair<CoolingTimeOption, CoolingTimeOption> {
    val amountValue = amount.filter { it.isDigit() }.toDoubleOrNull() ?: 0.0

    // 1. Расчет по накоплениям (рекомендованный период)
    val savingsBasedDays = calculateSavingsBasedCoolingDays(amountValue, settings)

    // 2. Расчет по системным правилам
    val systemBasedDays = calculateSystemBasedCoolingDays(amountValue, coolingRules)

    val savingsOption = CoolingTimeOption(
        id = 1,
        name = formatCoolingPeriod(savingsBasedDays),
        description = "Период накопления до комфортной покупки",
        days = savingsBasedDays,
        type = RecommendationType.SAVINGS_BASED
    )

    val systemOption = CoolingTimeOption(
        id = 2,
        name = formatCoolingPeriod(systemBasedDays),
        description = "По вашим правилам охлаждения",
        days = systemBasedDays,
        type = RecommendationType.SYSTEM_BASED
    )

    return Pair(savingsOption, systemOption)
}

// Форматирование периода охлаждения
fun formatCoolingPeriod(days: Int): String {
    return when {
        days == 0 -> "0 дней (сейчас)"
        days < 30 -> "$days ${getDayWord(days)}"
        else -> {
            val months = days / 30
            val remainingDays = days % 30
            val result = StringBuilder("$months ${getMonthWord(months)}")
            if (remainingDays > 0) {
                result.append(" $remainingDays ${getDayWord(remainingDays)}")
            }
            result.toString()
        }
    }
}

fun getDayWord(days: Int): String = when {
    days == 1 -> "день"
    days in 2..4 -> "дня"
    else -> "дней"
}

fun getMonthWord(months: Int): String = when {
    months == 1 -> "месяц"
    months in 2..4 -> "месяца"
    else -> "месяцев"
}

// Функция расчета периода охлаждения на основе накоплений и настроек
fun calculateSavingsBasedCoolingDays(purchaseAmount: Double, settings: com.example.tb.ui.screens.setting.SettingsState): Int {
    // Получаем данные из настроек
    val monthlySavings = settings.getMonthlySavingsAsInt()?.toDouble() ?: 5000.0
    val currentSavings = if (settings.considerSavings) {
        settings.getCurrentSavingsAsInt()?.toDouble() ?: 0.0
    } else {
        0.0
    }
    val comfortableThreshold = 0.5 // Комфортный порог (остаток 50%)

    // Если покупка бесплатная или очень дешевая
    if (purchaseAmount <= 0) return 0

    // Проверяем, можем ли купить сразу с комфортом
    if (purchaseAmount <= currentSavings * comfortableThreshold) {
        return 0 // Можем купить сразу
    }

    // Если не учитываем накопления или их нет
    if (!settings.considerSavings || currentSavings <= 0) {
        // Простой расчет по ежемесячным накоплениям
        val monthsNeeded = ceil(purchaseAmount / monthlySavings).toInt()
        return monthsNeeded * 30
    }

    // Рассчитываем необходимую дополнительную сумму
    val neededAmount = purchaseAmount - (currentSavings * comfortableThreshold)

    // Если ежемесячные накопления не настроены
    if (monthlySavings <= 0) {
        // Если сумма меньше комфортного порога, но не совсем
        return when {
            neededAmount <= currentSavings * 0.1 -> 7
            neededAmount <= currentSavings * 0.25 -> 14
            neededAmount <= currentSavings * 0.5 -> 30
            neededAmount <= currentSavings -> 60
            else -> 90
        }
    }

    // Рассчитываем количество месяцев для накопления
    val monthsNeeded = ceil(neededAmount / monthlySavings).toInt()

    // Преобразуем в дни (приблизительно 30 дней в месяце)
    val days = monthsNeeded * 30

    // Ограничиваем максимальный период 2 годами
    return days.coerceAtMost(730)
}

// Функция расчета периода охлаждения на основе системных правил
fun calculateSystemBasedCoolingDays(
    purchaseAmount: Double,
    coolingRules: com.example.tb.ui.screens.cooling.CoolingRulesState
): Int {
    // Парсим значения из правил
    val dayLimit = coolingRules.dayLimit.toIntOrNull() ?: 0
    val weekMinLimit = coolingRules.weekMinLimit.toIntOrNull() ?: 0
    val weekMaxLimit = coolingRules.weekMaxLimit.toIntOrNull() ?: 0
    val monthLimit = coolingRules.monthLimit.toIntOrNull() ?: 0

    // Определяем категорию покупки на основе лимитов
    return when {
        monthLimit > 0 && purchaseAmount > monthLimit -> 90
        weekMaxLimit > 0 && purchaseAmount > weekMaxLimit -> 30
        weekMinLimit > 0 && purchaseAmount > weekMinLimit -> 14
        dayLimit > 0 && purchaseAmount > dayLimit -> 7
        else -> when {
            purchaseAmount < 15000 -> 7
            purchaseAmount < 50000 -> 14
            else -> 30
        }
    }
}

// --- Компонент рекомендаций ---

@Composable
fun CoolingTimeRecommendations(
    amount: String,
    coolingRules: com.example.tb.ui.screens.cooling.CoolingRulesState,
    settings: com.example.tb.ui.screens.setting.SettingsState,
    savingsBasedRecommendation: CoolingTimeOption,
    systemBasedRecommendation: CoolingTimeOption,
    selectedTime: CoolingTimeOption?,
    onTimeSelected: (CoolingTimeOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val amountValue = amount.filter { it.isDigit() }.toDoubleOrNull() ?: 0.0
    val isAmountEntered = amountValue > 0

    val savingsInfo = if (isAmountEntered) {
        val monthlySavings = settings.getMonthlySavingsAsInt()?.toDouble() ?: 5000.0
        val currentSavings = if (settings.considerSavings) {
            settings.getCurrentSavingsAsInt()?.toDouble() ?: 0.0
        } else {
            0.0
        }
        val comfortableThreshold = 0.5
        val comfortableAmount = currentSavings * comfortableThreshold
        val canBuyNow = amountValue <= comfortableAmount
        val neededAmount = if (!canBuyNow)
            amountValue - comfortableAmount else 0.0
        val monthsNeeded = if (!canBuyNow && monthlySavings > 0)
            ceil(neededAmount / monthlySavings).toInt() else 0

        SavingsInfo(
            canBuyNow = canBuyNow,
            neededAmount = neededAmount,
            monthsNeeded = monthsNeeded,
            comfortableAmount = comfortableAmount,
            currentSavings = currentSavings,
            monthlySavings = monthlySavings,
            considerSavings = settings.considerSavings
        )
    } else null

    val systemLimitInfo = if (isAmountEntered) {
        getSystemLimitInfo(amountValue, coolingRules)
    } else null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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
            // Если не настроены сбережения, показываем информационное сообщение
            if (savingsInfo != null && !savingsInfo.considerSavings) {
                Text(
                    text = "ℹ️ В настройках отключен учёт текущих накоплений",
                    color = Color(0xFFFFDD2D),
                    fontSize = 10.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            RecommendationCard(
                recommendation = savingsBasedRecommendation,
                isSelected = selectedTime == savingsBasedRecommendation,
                onClick = { onTimeSelected(savingsBasedRecommendation) },
                showSavingsInfo = true,
                savingsInfo = savingsInfo,
                amount = amountValue
            )

            RecommendationCard(
                recommendation = systemBasedRecommendation,
                isSelected = selectedTime == systemBasedRecommendation,
                onClick = { onTimeSelected(systemBasedRecommendation) },
                showSavingsInfo = false,
                systemLimitInfo = systemLimitInfo,
                amount = amountValue
            )

            Text(
                text = selectedTime?.let {
                    when (it.type) {
                        RecommendationType.SAVINGS_BASED ->
                            savingsInfo?.let { info ->
                                if (!info.considerSavings || info.currentSavings <= 0) {
                                    "Покупка по ежемесячным накоплениям: ${formatMoney(info.monthlySavings)}/мес."
                                } else if (info.canBuyNow) {
                                    "Можно купить сейчас! Сумма в пределах комфортного порога"
                                } else {
                                    "Нужно накопить ещё ${formatMoney(info.neededAmount)} за ${info.monthsNeeded} ${getMonthWord(info.monthsNeeded)}"
                                }
                            } ?: "Период накопления до комфортной покупки"

                        RecommendationType.SYSTEM_BASED ->
                            systemLimitInfo?.let { limitInfo ->
                                if (limitInfo.exceedsAnyLimit) {
                                    "Превышает ${limitInfo.limitType} лимит (${formatMoney(limitInfo.limitValue.toDouble())})"
                                } else {
                                    "Укладывается в ваши лимиты охлаждения"
                                }
                            } ?: "На основе ваших правил охлаждения"

                        else -> "Выбрано пользовательское время охлаждения"
                    }
                } ?: "Выберите рекомендуемый период или укажите свой",
                color = Color(0xFFAAAAAA),
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF333333))
                    .clickable {
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

// --- Компонент карточки рекомендации ---

@Composable
fun RecommendationCard(
    recommendation: CoolingTimeOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    showSavingsInfo: Boolean,
    amount: Double,
    savingsInfo: SavingsInfo? = null,
    systemLimitInfo: SystemLimitInfo? = null
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

            Text(
                text = recommendation.description,
                color = if (isSelected) Color(0xFFCCCCCC) else Color(0xFF888888),
                fontSize = 11.sp
            )

            if (showSavingsInfo && savingsInfo != null) {
                if (savingsInfo.canBuyNow) {
                    Text(
                        text = "Можно купить сейчас! Останется ${formatMoney(savingsInfo.comfortableAmount - amount)}",
                        color = if (isSelected) Color(0xFFDDDDDD) else Color(0xFF777777),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                } else {
                    Text(
                        text = "Нужно накопить ещё ${formatMoney(savingsInfo.neededAmount)} за ${savingsInfo.monthsNeeded} мес.",
                        color = if (isSelected) Color(0xFFDDDDDD) else Color(0xFF777777),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            } else if (!showSavingsInfo && systemLimitInfo != null) {
                if (systemLimitInfo.exceedsAnyLimit) {
                    Text(
                        text = "Превышает ${systemLimitInfo.limitType} лимит на ${formatMoney(amount - systemLimitInfo.limitValue.toDouble())}",
                        color = if (isSelected) Color(0xFFDDDDDD) else Color(0xFF777777),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                } else {
                    Text(
                        text = "Укладывается во все установленные лимиты",
                        color = if (isSelected) Color(0xFFDDDDDD) else Color(0xFF777777),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

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

// --- Вспомогательные функции ---

fun formatMoney(amount: Double): String {
    return "%,d ₽".format(amount.toInt()).replace(',', ' ')
}

fun getSystemLimitInfo(
    amount: Double,
    coolingRules: com.example.tb.ui.screens.cooling.CoolingRulesState
): SystemLimitInfo {
    val dayLimit = coolingRules.dayLimit.toIntOrNull() ?: 0
    val weekMinLimit = coolingRules.weekMinLimit.toIntOrNull() ?: 0
    val weekMaxLimit = coolingRules.weekMaxLimit.toIntOrNull() ?: 0
    val monthLimit = coolingRules.monthLimit.toIntOrNull() ?: 0

    return when {
        monthLimit > 0 && amount > monthLimit ->
            SystemLimitInfo(true, "месячный", monthLimit)

        weekMaxLimit > 0 && amount > weekMaxLimit ->
            SystemLimitInfo(true, "недельный максимальный", weekMaxLimit)

        weekMinLimit > 0 && amount > weekMinLimit ->
            SystemLimitInfo(true, "недельный минимальный", weekMinLimit)

        dayLimit > 0 && amount > dayLimit ->
            SystemLimitInfo(true, "дневной", dayLimit)

        else -> SystemLimitInfo(false, "", 0)
    }
}

// --- Компоненты полей ввода ---

@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    var localValue by remember { mutableStateOf(value) }

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
                color = when {
                    isError -> Color(0xFFEE6B42)
                    isFocused -> Color(0xFF2A64D9)
                    else -> Color.Transparent
                },
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { focusRequester.requestFocus() }
    ) {
        BasicTextField(
            value = localValue,
            onValueChange = { newValue ->
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