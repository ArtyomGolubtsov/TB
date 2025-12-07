package com.example.tb.ui.screens.buyers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tb.R

@Composable
fun PurchaseScreen(
    viewModel: PurchaseViewModel = viewModel(),   // ✅ добавлен дефолт
    onBackClick: () -> Unit = {},
    onAddPurchaseClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 - Покупки, 1 - История
    val state by viewModel.state.collectAsState()

    var showCancelDialogFor by remember { mutableStateOf<String?>(null) }
    var showCompleteDialogFor by remember { mutableStateOf<String?>(null) }

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

        // Кнопка "Добавить покупку" - только для вкладки "Покупки"
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

        // Контент вкладок
        when (selectedTab) {
            0 -> PurchasesList(
                purchases = state.activePurchases,
                viewModel = viewModel,
                onShowCancelDialog = { purchaseId -> showCancelDialogFor = purchaseId },
                onShowCompleteDialog = { purchaseId -> showCompleteDialogFor = purchaseId }
            )
            1 -> HistoryList(
                completedPurchases = state.completedPurchases,
                cancelledPurchases = state.cancelledPurchases,
                allPurchases = state.allPurchases,
                viewModel = viewModel
            )
        }
    }

    // Диалог отмены покупки
    if (showCancelDialogFor != null) {
        val purchase = viewModel.getPurchaseById(showCancelDialogFor!!)
        purchase?.let {
            CancelPurchaseDialog(
                purchase = it,
                onConfirm = {
                    viewModel.cancelPurchase(it.id)
                    showCancelDialogFor = null
                },
                onDismiss = {
                    showCancelDialogFor = null
                }
            )
        }
    }

    // Диалог завершения покупки
    if (showCompleteDialogFor != null) {
        val purchase = viewModel.getPurchaseById(showCompleteDialogFor!!)
        purchase?.let {
            CompletePurchaseDialog(
                purchase = it,
                onConfirm = {
                    viewModel.completePurchase(it.id)
                    showCompleteDialogFor = null
                },
                onDismiss = {
                    showCompleteDialogFor = null
                }
            )
        }
    }
}

@Composable
fun PurchasesList(
    purchases: List<Purchase>,
    viewModel: PurchaseViewModel,
    onShowCancelDialog: (String) -> Unit,
    onShowCompleteDialog: (String) -> Unit
) {
    if (purchases.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Нет активных покупок",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(horizontal = 40.dp),
            verticalArrangement = Arrangement.spacedBy(19.dp)
        ) {
            items(
                items = purchases,
                key = { it.id }
            ) { purchase ->
                PurchaseCard(
                    purchase = purchase,
                    viewModel = viewModel,
                    onShowCancelDialog = { onShowCancelDialog(purchase.id) },
                    onShowCompleteDialog = { onShowCompleteDialog(purchase.id) }
                )
            }
        }
    }
}

@Composable
fun HistoryList(
    completedPurchases: List<Purchase>,
    cancelledPurchases: List<Purchase>,
    allPurchases: List<Purchase>,
    viewModel: PurchaseViewModel
) {
    if (allPurchases.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Нет истории транзакций",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(horizontal = 40.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            // Блок с балансом
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Общий баланс
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color(0xFF333333)),
                        contentAlignment = Alignment.Center
                    ) {
                        val netBalance = viewModel.getNetBalance()
                        val balanceColor =
                            if (netBalance >= 0) Color(0xFF29BF1F) else Color(0xFFEE6B42)

                        Text(
                            text = "Баланс: ${viewModel.formatAmount(netBalance)}",
                            color = balanceColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Сэкономлено
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(30.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(Color(0xFF1A3D1A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✓ ${viewModel.formatAmount(viewModel.getTotalSaved())}",
                                color = Color(0xFF29BF1F),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Потрачено
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(30.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(Color(0xFF3D1A1A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✗ ${viewModel.formatAmount(viewModel.getTotalSpent())}",
                                color = Color(0xFFEE6B42),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(25.dp)) }

            // Отменённые покупки (позитивные)
            items(
                items = cancelledPurchases,
                key = { it.id + "_cancelled" }
            ) { purchase ->
                TransactionItem(
                    purchase = purchase,
                    isPositive = true
                )
            }

            // Завершённые покупки (негативные)
            items(
                items = completedPurchases,
                key = { it.id + "_completed" }
            ) { purchase ->
                TransactionItem(
                    purchase = purchase,
                    isPositive = false
                )
            }
        }
    }
}

@Composable
fun TransactionItem(
    purchase: Purchase,
    isPositive: Boolean
) {
    val backgroundColor = if (isPositive) Color(0xFF1A3D1A) else Color(0xFF3D1A1A)
    val iconColor = if (isPositive) Color(0xFF29BF1F) else Color(0xFFEE6B42)
    val iconText = if (isPositive) "✓" else "✗"
    val statusText = if (isPositive) "Отменено" else "Куплено"
    val amountText = if (isPositive) "+${purchase.amount.toInt()} ₽" else "-${purchase.amount.toInt()} ₽"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterStart)
                .padding(start = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iconText,
                color = iconColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = purchase.title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 60.dp)
                .offset(y = (-16).dp)
        )

        Box(
            modifier = Modifier
                .padding(start = 60.dp)
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

        Text(
            text = statusText,
            color = iconColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 11.dp, end = 11.dp)
        )

        Text(
            text = amountText,
            color = iconColor,
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
    viewModel: PurchaseViewModel,
    onShowCancelDialog: () -> Unit,
    onShowCompleteDialog: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF333333)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = purchase.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { showMenu = true },
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    offset = DpOffset(x = (-100).dp, y = 0.dp),
                    modifier = Modifier
                        .background(Color(0xFF333333))
                        .width(200.dp)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "✅ Завершить покупку",
                                color = Color(0xFFEE6B42),
                                fontSize = 14.sp
                            )
                        },
                        onClick = {
                            showMenu = false
                            onShowCompleteDialog()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "❌ Отменить покупку",
                                color = Color(0xFF29BF1F),
                                fontSize = 14.sp
                            )
                        },
                        onClick = {
                            showMenu = false
                            onShowCancelDialog()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (purchase.notificationsEnabled)
                                    "🔔 Уведомления: Вкл"
                                else
                                    "🔕 Уведомления: Выкл",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        },
                        onClick = {
                            showMenu = false
                            viewModel.togglePurchaseNotifications(purchase.id)
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp)
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

            Text(
                text = "Остаток дней: 30",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 95.dp)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 115.dp),
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

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 5.dp)
                    .fillMaxWidth()
            ) {
                val progressValue = viewModel.getProgressValue(purchase)
                val maxValue = 100f

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF616161))
                ) {
                    Box(
                        modifier = Modifier
                            .width((progressValue / maxValue * 280).dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF2A64D9))
                    )

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

@Composable
fun CancelPurchaseDialog(
    purchase: Purchase,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1A3D1A))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "✅ Отменить покупку?",
                    color = Color(0xFF29BF1F),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Молодец! Ты решил не покупать \"${purchase.title}\"",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Ты сэкономишь ${purchase.amount.toInt()} ₽!",
                    color = Color(0xFF29BF1F),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "Эта сумма добавится к твоим накоплениям",
                    color = Color(0xFFAAAAAA),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

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
                            text = "Передумал",
                            color = Color(0xFF13B008),
                            fontSize = 14.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF00C853))
                            .clickable(onClick = onConfirm),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Да, отменить!",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompletePurchaseDialog(
    purchase: Purchase,
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
                    text = "⚠️ Завершить покупку?",
                    color = Color(0xFFEE6B42),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Ты уверен, что хочешь купить \"${purchase.title}\"?",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Ты потратишь ${purchase.amount.toInt()} ₽",
                    color = Color(0xFFEE6B42),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "Эта сумма вычтется из твоих накоплений",
                    color = Color(0xFFAAAAAA),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Text(
                    text = "Может, подумаешь ещё?",
                    color = Color(0xFFFFB74D),
                    fontSize = 14.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFB20707))
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Отложить",
                            color = Color.White,
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
                            text = "Да, купить",
                            color = Color(0xFFEE6B42),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PurchaseScreenPreview() {
    PurchaseScreen()   // ✅ теперь viewModel берётся по умолчанию
}
