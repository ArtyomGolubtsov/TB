package com.example.tb.ui.screens.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tb.R
import com.example.tb.ui.theme.TBTheme

@Composable
fun SettingsMainScreen(
    onBackClick: () -> Unit = {},
    onCoolingRulesClick: () -> Unit = {},
    onSaveSuccess: () -> Unit = {}
) {
    val viewModel: SettingsViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    var showFrequencyDialog by remember { mutableStateOf(false) }
    var showChannelDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopStart)
                .offset(x = 24.dp, y = 43.dp)
                .clickable { onBackClick() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.path1),
                contentDescription = "Стрелка назад",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(6.dp, 12.dp)
                    .align(Alignment.Center)
            )
        }

        Text(
            text = "Настройки",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 41.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 83.dp)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Блок Уведомлений
            NotificationSection(
                notificationFrequency = state.notificationFrequency,
                notificationChannel = state.notificationChannel,
                onFrequencyClick = { showFrequencyDialog = true },
                onChannelClick = { showChannelDialog = true }
            )

            // Блок Финансы
            FinanceSection(
                monthlySavings = state.monthlySavings,
                income = state.income,
                onMonthlySavingsChange = { viewModel.updateMonthlySavings(it) },
                onIncomeChange = { viewModel.updateIncome(it) }
            )

            // Блок Накопления
            SavingsSection(
                considerSavings = state.considerSavings,
                currentSavings = state.currentSavings,
                onConsiderSavingsChange = { viewModel.updateConsiderSavings(it) },
                onCurrentSavingsChange = { viewModel.updateCurrentSavings(it) }
            )

            // Блок Правила охлаждения
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFF333333))
                    .clickable { onCoolingRulesClick() }
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Правила охлаждения",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 18.sp
                        )
                        Text(
                            text = "Настройка лимитов расходов",
                            color = Color(0xFFAAAAAA),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Стрелочка вправо
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .drawBehind {
                                val path = Path().apply {
                                    moveTo(size.width * 0.4f, size.height * 0.3f)
                                    lineTo(size.width * 0.7f, size.height / 2)
                                    lineTo(size.width * 0.4f, size.height * 0.7f)
                                }
                                drawPath(
                                    path = path,
                                    color = Color(0xFF6C6F71),
                                    style = Stroke(
                                        width = 1.5f,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )
                            }
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
                    viewModel.saveSettings(
                        onSuccess = {
                            onSaveSuccess()
                        },
                        onError = { error ->
                            errorMessage = error
                            showErrorDialog = true
                        }
                    )
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

    // Диалог выбора частоты уведомлений
    if (showFrequencyDialog) {
        NotificationFrequencyDialog(
            selectedFrequency = state.notificationFrequency,
            onDismiss = { showFrequencyDialog = false },
            onSelect = { frequency ->
                viewModel.updateNotificationFrequency(frequency)
                showFrequencyDialog = false
            }
        )
    }

    // Диалог выбора канала уведомлений
    if (showChannelDialog) {
        NotificationChannelDialog(
            selectedChannel = state.notificationChannel,
            onDismiss = { showChannelDialog = false },
            onSelect = { channel ->
                viewModel.updateNotificationChannel(channel)
                showChannelDialog = false
            }
        )
    }

    // Диалог ошибки
    if (showErrorDialog) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}

// ========== NotificationSection ==========
@Composable
fun NotificationSection(
    notificationFrequency: NotificationFrequency,
    notificationChannel: NotificationChannel,
    onFrequencyClick: () -> Unit,
    onChannelClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Color(0xFF333333))
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = "Уведомления",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 18.sp,
            modifier = Modifier.padding(start = 11.dp, top = 2.dp)
        )

        NotificationItem(
            text = "Как часто опрашивать о запланированных покупках",
            value = notificationFrequency.displayName,
            onClick = onFrequencyClick,
            modifier = Modifier.padding(top = 8.dp)
        )

        NotificationItem(
            text = "Выберите канал нотификации",
            value = notificationChannel.displayName,
            onClick = onChannelClick,
            modifier = Modifier.padding(top = 15.dp)
        )
    }
}

@Composable
fun NotificationItem(
    text: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = text,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 18.sp
            )
            Text(
                text = value,
                color = Color(0xFFAAAAAA),
                fontSize = 9.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // Стрелочка вниз
        Box(
            modifier = Modifier
                .size(24.dp)
                .drawBehind {
                    val path = Path().apply {
                        moveTo(size.width * 0.3f, size.height * 0.4f)
                        lineTo(size.width / 2, size.height * 0.65f)
                        lineTo(size.width * 0.7f, size.height * 0.4f)
                    }
                    drawPath(
                        path = path,
                        color = Color(0xFF6C6F71),
                        style = Stroke(
                            width = 1.5f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
        )
    }
}

// ========== FinanceSection ==========
@Composable
fun FinanceSection(
    monthlySavings: String,
    income: String,
    onMonthlySavingsChange: (String) -> Unit,
    onIncomeChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Color(0xFF333333))
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = "Финансы",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 18.sp,
            modifier = Modifier.padding(start = 11.dp, top = 2.dp)
        )

        FinanceInputField(
            label = "Сколько готовы откладывать в месяц:",
            value = monthlySavings,
            onValueChange = onMonthlySavingsChange,
            placeholder = "Например: 10 000 ₽",
            modifier = Modifier.padding(top = 8.dp),
            isNumericOnly = true
        )

        FinanceInputField(
            label = "Введите ваш достаток:",
            value = income,
            onValueChange = onIncomeChange,
            placeholder = "Например: 50 000 ₽",
            modifier = Modifier.padding(top = 15.dp),
            isNumericOnly = true
        )
    }
}

// ========== FinanceInputField ==========
@Composable
fun FinanceInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isNumericOnly: Boolean = false
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    var displayText by remember { mutableStateOf(value) }

    // Синхронизируем с внешним значением
    LaunchedEffect(value) {
        if (displayText != value) {
            displayText = value
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 18.sp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Color(0xFF494949))
                .clickable { focusRequester.requestFocus() }
                .border(
                    width = 1.dp,
                    color = if (isFocused) Color(0xFF2A64D9) else Color.Transparent,
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            androidx.compose.foundation.text.BasicTextField(
                value = if (isFocused || !isNumericOnly) {
                    // В режиме редактирования или для нечисловых полей - показываем как есть
                    displayText
                } else {
                    // Для числовых полей вне фокуса - форматируем
                    val cleaned = displayText.filter { it.isDigit() }
                    val number = cleaned.toLongOrNull()
                    if (number != null) {
                        "%,d".format(number).replace(',', ' ') + " ₽"
                    } else {
                        displayText
                    }
                },
                onValueChange = { newValue ->
                    if (isNumericOnly) {
                        // Для числовых полей - только цифры
                        val filtered = newValue.filter { it.isDigit() }
                        displayText = filtered
                        onValueChange(filtered)
                    } else {
                        // Для нечисловых полей - все символы
                        displayText = newValue
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        if (!focusState.isFocused && isNumericOnly) {
                            // При потере фокуса для числовых полей - сохраняем только цифры
                            val cleaned = displayText.filter { it.isDigit() }
                            displayText = cleaned
                        }
                    },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                ),
                singleLine = true,
                cursorBrush = SolidColor(Color.White),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (displayText.isEmpty() && !isFocused) {
                            Text(
                                text = placeholder,
                                color = Color(0xFF888888),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

// ========== SavingsSection ==========
@Composable
fun SavingsSection(
    considerSavings: Boolean,
    currentSavings: String,
    onConsiderSavingsChange: (Boolean) -> Unit,
    onCurrentSavingsChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Color(0xFF333333))
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = "Накопления",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 18.sp,
            modifier = Modifier.padding(start = 11.dp, top = 2.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Учитывать размер текущих накоплений",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp
            )

            CustomSwitch(
                isChecked = considerSavings,
                onCheckedChange = onConsiderSavingsChange
            )
        }

        if (considerSavings) {
            FinanceInputField(
                label = "Введите размер текущих накоплений:",
                value = currentSavings,
                onValueChange = onCurrentSavingsChange,
                placeholder = "Например: 200 000 ₽",
                modifier = Modifier.padding(top = 15.dp),
                isNumericOnly = true
            )
        }
    }
}

// ========== CustomSwitch ==========
@Composable
fun CustomSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .width(29.dp)
            .height(16.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(if (isChecked) Color(0xFF2A64D9) else Color.Gray)
            .clickable { onCheckedChange(!isChecked) },
        contentAlignment = if (isChecked) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(2.dp)
        )
    }
}

// ========== NotificationFrequencyDialog ==========
@Composable
fun NotificationFrequencyDialog(
    selectedFrequency: NotificationFrequency,
    onDismiss: () -> Unit,
    onSelect: (NotificationFrequency) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF333333))
                .padding(16.dp)
        ) {
            Text(
                text = "Выберите частоту",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            NotificationFrequency.values().forEach { frequency ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (frequency == selectedFrequency)
                                Color(0xFF2A64D9)
                            else
                                Color.Transparent
                        )
                        .clickable { onSelect(frequency) }
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = frequency.displayName,
                        color = if (frequency == selectedFrequency) Color.White else Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// ========== NotificationChannelDialog ==========
@Composable
fun NotificationChannelDialog(
    selectedChannel: NotificationChannel,
    onDismiss: () -> Unit,
    onSelect: (NotificationChannel) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF333333))
                .padding(16.dp)
        ) {
            Text(
                text = "Выберите канал уведомлений",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            NotificationChannel.values().forEach { channel ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (channel == selectedChannel)
                                Color(0xFF2A64D9)
                            else
                                Color.Transparent
                        )
                        .clickable { onSelect(channel) }
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = channel.displayName,
                        color = if (channel == selectedChannel) Color.White else Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// ========== ErrorDialog ==========
@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF333333))
                .padding(16.dp)
        ) {
            Text(
                text = "Ошибка",
                color = Color(0xFFEE6B42),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = message,
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2A64D9))
                    .clickable { onDismiss() }
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "OK",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsMainScreenPreview() {
    TBTheme {
        SettingsMainScreen()
    }
}