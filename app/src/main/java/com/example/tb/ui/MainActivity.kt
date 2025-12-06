package com.example.tb.ui

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.tb.data.CoolingRule
import com.example.tb.data.UserGoal
import com.example.tb.ui.theme.TBTheme
import net.softglobal.pushnotificationtutorial.GoalNotificationService
import net.softglobal.pushnotificationtutorial.NotificationHelper
import java.util.*

class MainActivity : ComponentActivity() {

    internal lateinit var notificationHelper: NotificationHelper
    internal lateinit var goalNotificationService: GoalNotificationService
    private var notificationCounter = 0

    var currentGoal: UserGoal? = null
        private set

    // публичный метод для изменения currentGoal извне
    fun updateGoal(updatedGoal: UserGoal) {
        currentGoal = updatedGoal
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        notificationHelper = NotificationHelper(this)
        goalNotificationService = GoalNotificationService(this)

        setContent {
            TBTheme {
                NotificationPermissionHandler { hasPermission ->
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        if (hasPermission) {
                            MainNavigationScreen()
                        } else {
                            PermissionDeniedScreen()
                        }
                    }
                }
            }
        }

        startGoalChecker()
    }

    private fun startGoalChecker() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                currentGoal?.let { goal ->
                    goalNotificationService.sendDailyProgressNotification(goal, 0.0)
                    goalNotificationService.sendDeadlineWarningNotification(goal, goal.selectedRule)
                    goalNotificationService.sendMotivationNotification(goal)
                    goalNotificationService.sendMilestoneNotification(goal)
                }
                handler.postDelayed(this, 6 * 60 * 60 * 1000L)
            }
        }
        handler.postDelayed(runnable, 1000L)
    }

    fun showTestNotification() {
        notificationCounter++
        notificationHelper.showNotification(
            title = "T-Bank Уведомление #$notificationCounter",
            message = "Проверьте новые операции в вашем аккаунте",
            notificationId = notificationCounter
        )
        Toast.makeText(this, "Уведомление отправлено", Toast.LENGTH_SHORT).show()
    }

    fun createNewGoal(name: String, targetAmount: Double, rule: CoolingRule?) {
        val newGoal = UserGoal(
            id = UUID.randomUUID().toString(),
            name = name,
            targetAmount = targetAmount,
            selectedRule = rule
        )

        currentGoal = newGoal
        goalNotificationService.sendGoalCreatedNotification(newGoal, rule)
        Toast.makeText(this, "Цель '$name' создана!", Toast.LENGTH_SHORT).show()
    }

    fun addToGoal(amount: Double) {
        currentGoal?.let { goal ->
            val updatedGoal = goal.copy(
                currentAmount = goal.currentAmount + amount
            )
            updateGoal(updatedGoal)
            goalNotificationService.sendDailyProgressNotification(updatedGoal, amount)
            Toast.makeText(this, "Добавлено $amount ₽ к цели!", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(this, "Сначала создайте цель", Toast.LENGTH_SHORT).show()
        }
    }
}

fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun MainNavigationScreen() {
    var currentScreen by remember { mutableStateOf("main") }
    val context = LocalContext.current
    val activity = context.findActivity() as? MainActivity

    when (currentScreen) {
        "main" -> FinancialGoalsScreen(
            onNotificationClicked = { activity?.showTestNotification() },
            onOpenCoolingRules = { currentScreen = "cooling_rules" },
            onCreateGoal = { name, amount ->
                activity?.createNewGoal(name, amount, null)
            },
            onAddToGoal = { amount -> activity?.addToGoal(amount) },
            currentGoal = activity?.currentGoal
        )

        "cooling_rules" -> CoolingRulesSettingsScreen(
            goalNotificationService = GoalNotificationService(context),
            onSave = { rule ->
                Toast.makeText(context, "Правило сохранено: ${rule?.timeframe}", Toast.LENGTH_SHORT).show()
                currentScreen = "main"
                // чтение currentGoal — ок
                activity?.currentGoal?.let { goal ->
                    val updatedGoal = goal.copy(selectedRule = rule)
                    // ЗДЕСЬ ИСПРАВЛЕНО: присваивание заменено вызовом updateGoal()
                    activity.updateGoal(updatedGoal)
                }
            },
            onBack = { currentScreen = "main" }
        )
    }
}

@Composable
fun FinancialGoalsScreen(
    onNotificationClicked: () -> Unit,
    onOpenCoolingRules: () -> Unit,
    onCreateGoal: (String, Double) -> Unit,
    onAddToGoal: (Double) -> Unit,
    currentGoal: UserGoal? = null
) {
    var goalName by remember { mutableStateOf("") }
    var goalAmount by remember { mutableStateOf("") }
    var addAmount by remember { mutableStateOf("") }

    val context = LocalContext.current
    val notificationHelper = remember { NotificationHelper(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "T-Bank Финансовые цели",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        currentGoal?.let { goal ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Текущая цель: ${goal.name}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Прогресс: ${goal.currentAmount} ₽ / ${goal.targetAmount} ₽",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    val progress = if (goal.targetAmount > 0) {
                        ((goal.currentAmount / goal.targetAmount) * 100).toInt()
                    } else {
                        0
                    }

                    LinearProgressIndicator(
                        progress = (progress / 100f).coerceIn(0f, 1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )

                    Text(
                        text = "$progress% выполнено",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Новая цель", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = goalName,
                    onValueChange = { goalName = it },
                    label = { Text("Название цели") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = goalAmount,
                    onValueChange = { goalAmount = it },
                    label = { Text("Сумма (₽)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val amount = goalAmount.toDoubleOrNull()
                        if (amount != null && goalName.isNotBlank()) {
                            onCreateGoal(goalName, amount)
                            goalName = ""
                            goalAmount = ""
                        }
                    },
                    enabled = goalName.isNotBlank() && goalAmount.toDoubleOrNull() != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Создать цель")
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Пополнить цель", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = addAmount,
                    onValueChange = { addAmount = it },
                    label = { Text("Сумма пополнения (₽)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val amount = addAmount.toDoubleOrNull()
                        if (amount != null && amount > 0) {
                            onAddToGoal(amount)
                            addAmount = ""
                        }
                    },
                    enabled = addAmount.toDoubleOrNull()?.let { it > 0 } ?: false,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Добавить к цели")
                }
            }
        }

        Button(
            onClick = onOpenCoolingRules,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text("Правила охлаждения")
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = "Тестовые уведомления",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(
            onClick = onNotificationClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Отправить тестовое уведомление")
        }

        OutlinedButton(
            onClick = {
                notificationHelper.showNotification(
                    title = "Баланс обновлён",
                    message = "На вашем счету: 15,430 ₽",
                    notificationId = 100
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Показать баланс")
        }

        OutlinedButton(
            onClick = {
                notificationHelper.showNotification(
                    title = "⚠️ Срочное уведомление",
                    message = "Подозрительная операция обнаружена",
                    notificationId = 200
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Срочное уведомление")
        }
    }
}

@Composable
fun CoolingRulesSettingsScreen(
    goalNotificationService: GoalNotificationService,
    onSave: (CoolingRule?) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextButton(onClick = onBack) {
                Text("← Назад")
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Настройки правил охлаждения",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Здесь будут настройки правил уведомлений",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = { onSave(null) }) {
                    Text("Сохранить правила")
                }
            }
        }
    }
}

@Composable
fun NotificationPermissionHandler(
    content: @Composable (hasPermission: Boolean) -> Unit
) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasNotificationPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

            if (!hasNotificationPermission) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                hasPermission = true
            }
        } else {
            hasPermission = true
        }
    }

    content(hasPermission)
}

@Composable
fun PermissionDeniedScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⚠️ Требуется разрешение",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Для работы уведомлений необходимо разрешение.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            text = "Перейдите в Настройки → Приложения → T-Bank → Разрешения",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:${context.packageName}")
                }
                context.startActivity(intent)
            }
        ) {
            Text("Открыть настройки")
        }
    }
}
