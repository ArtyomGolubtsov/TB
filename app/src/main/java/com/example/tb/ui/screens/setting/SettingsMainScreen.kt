package com.example.tb.ui.screens.setting

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
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
    onSaveSuccess: () -> Unit = {} // Callback при успешном сохранении
) {
    val viewModel: SettingsViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Состояние для диалогов
    var showFrequencyDialog by remember { mutableStateOf(false) }
    var showChannelDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

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

        // Заголовок
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
                    if (state.isValid()) {
                        viewModel.saveSettings {
                            // Показываем Toast или Snackbar
                            // Toast.makeText(context, "Настройки сохранены", Toast.LENGTH_SHORT).show()
                            onSaveSuccess()
                        }
                    } else {
                        errorMessage = "Заполните все обязательные поля корректными числами"
                        showErrorDialog = true
                    }
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
        // Заголовок раздела
        Text(
            text = "Уведомления",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 18.sp,
            modifier = Modifier.padding(start = 11.dp, top = 2.dp)
        )

        // Первый пункт - Частота уведомлений
        NotificationItem(
            text = "Как часто опрашивать о запланированных покупках",
            value = notificationFrequency.displayName,
            onClick = onFrequencyClick,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Второй пункт - Канал уведомлений
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
        // Заголовок раздела
        Text(
            text = "Финансы",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 18.sp,
            modifier = Modifier.padding(start = 11.dp, top = 2.dp)
        )

        // Поле ввода 1 - Сколько готовы откладывать
        FinanceInputField(
            label = "Сколько готовы откладывать в месяц:",
            value = monthlySavings,
            onValueChange = onMonthlySavingsChange,
            placeholder = "₽",
            modifier = Modifier.padding(top = 8.dp)
        )

        // Поле ввода 2 - Доход
        FinanceInputField(
            label = "Введите ваш достаток:",
            value = income,
            onValueChange = onIncomeChange,
            placeholder = "₽",
            modifier = Modifier.padding(top = 15.dp)
        )
    }
}

@Composable
fun FinanceInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

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
        ) {
            BasicTextField(
                value = value,
                onValueChange = { newText ->
                    // Фильтруем только цифры
                    val filtered = newText.filter { it.isDigit() }
                    onValueChange(filtered)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .focusRequester(focusRequester),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color.White,
                    fontSize = 12.sp
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = Color(0xFF888888),
                                fontSize = 12.sp
                            )
                        }
                        innerTextField
                    }
                }
            )
        }
    }
}

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
        // Заголовок раздела
        Text(
            text = "Накопления",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 18.sp,
            modifier = Modifier.padding(start = 11.dp, top = 2.dp)
        )

        // Переключатель
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

            // Кастомный переключатель
            CustomSwitch(
                isChecked = considerSavings,
                onCheckedChange = onConsiderSavingsChange
            )
        }

        // Поле ввода - если переключатель включен
        if (considerSavings) {
            FinanceInputField(
                label = "Введите размер текущих накоплений:",
                value = currentSavings,
                onValueChange = onCurrentSavingsChange,
                placeholder = "₽",
                modifier = Modifier.padding(top = 15.dp)
            )
        }
    }
}

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

// Диалог выбора частоты уведомлений
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

// Диалог выбора канала уведомлений
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

// Диалог ошибки
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