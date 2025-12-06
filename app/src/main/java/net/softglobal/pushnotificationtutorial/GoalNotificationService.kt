package net.softglobal.pushnotificationtutorial

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.tb.data.CoolingRule
import com.example.tb.data.UserGoal
import java.text.NumberFormat
import java.util.*
import kotlin.math.ceil
import kotlin.math.max

class GoalNotificationService(private val context: Context) {

    companion object {
        const val CHANNEL_GOALS = "tb_goals_channel"
        const val CHANNEL_URGENT = "tb_urgent_channel"

        // Правила охлаждения как на скриншоте
        val COOLING_RULES = listOf(
            CoolingRule("1 день", "до 15 000 ₽", 0.0, 15000.0, 1),
            CoolingRule("1 неделя", "от 15 000 до 50 000 ₽", 15000.0, 50000.0, 7),
            CoolingRule("1 месяц", "от 50 000 ₽", 50000.0, null, 30)
        )
    }

    private val notificationHelper = NotificationHelper(context)
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))

    init {
        createGoalChannel()
    }

    private fun createGoalChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_GOALS,
                "Цели и накопления",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления о прогрессе финансовых целей"
                enableLights(true)
                lightColor = 0xFF4CAF50.toInt()
            }

            // Канал для срочных уведомлений
            val urgentChannel = NotificationChannel(
                CHANNEL_URGENT,
                "Срочные уведомления",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Важные предупреждения"
                enableLights(true)
                lightColor = 0xFFFF0000.toInt()
                enableVibration(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)
            notificationManager.createNotificationChannel(urgentChannel)
        }
    }

    // 1. Уведомление при создании цели
    fun sendGoalCreatedNotification(goal: UserGoal, rule: CoolingRule?) {
        val message = buildString {
            append("Цель: ${goal.name}\n")
            append("Сумма: ${formatCurrency(goal.targetAmount)}\n")
            rule?.let {
                append("Срок: ${it.timeframe}\n")
                append("Ожидаемое достижение: через ${it.targetDays} дней")
            }
        }

        showGoalNotification(
            title = "🎯 Новая цель создана",
            message = message,
            notificationId = goal.id.hashCode()
        )
    }

    // 2. Ежедневный прогресс (на основе правила "охлаждения")
    fun sendDailyProgressNotification(goal: UserGoal, dailyContribution: Double) {
        val progress = (goal.currentAmount / goal.targetAmount) * 100
        val daysLeft = calculateDaysLeft(goal)

        val message = buildString {
            append("Цель: ${goal.name}\n")
            append("Сегодня: +${formatCurrency(dailyContribution)}\n")
            append("Накоплено: ${formatCurrency(goal.currentAmount)} из ${formatCurrency(goal.targetAmount)}\n")
            append("Прогресс: ${"%.1f".format(progress)}%\n")
            if (daysLeft > 0) {
                append("Осталось дней: $daysLeft")
            }
        }

        val title = when {
            progress >= 100 -> "🎉 Цель достигнута!"
            progress >= 90 -> "Почти у цели! ${"%.1f".format(progress)}%"
            progress >= 75 -> "Отлично! ${"%.1f".format(progress)}%"
            progress >= 50 -> "Полпути пройдено!"
            else -> "Прогресс по цели"
        }

        showGoalNotification(
            title = title,
            message = message,
            notificationId = goal.id.hashCode() + 1
        )
    }

    // 3. Предупреждение о сроке (если отстаёте от графика)
    fun sendDeadlineWarningNotification(goal: UserGoal, rule: CoolingRule?) {
        rule?.let { coolingRule ->
            val expectedDaily = goal.targetAmount / coolingRule.targetDays
            val daysPassed = daysSince(goal.startDate)

            if (daysPassed > 0) {
                val actualDaily = goal.currentAmount / daysPassed

                if (actualDaily < expectedDaily * 0.5) {
                    val remainingAmount = goal.targetAmount - goal.currentAmount
                    val daysBehind = ceil(remainingAmount / expectedDaily).toInt()

                    showGoalNotification(
                        title = "⚠️ Вы отстаёте от графика",
                        message = buildString {
                            appendLine("Цель: ${goal.name}")
                            appendLine("Нужно ускориться!")
                            appendLine("Чтобы успеть за ${coolingRule.targetDays} дней:")
                            appendLine("- Требуется в день: ${formatCurrency(expectedDaily)}")
                            appendLine("- Сейчас в день: ${formatCurrency(actualDaily)}")
                            appendLine("- Отставание: $daysBehind дней")
                        },
                        notificationId = goal.id.hashCode() + 2,
                        isUrgent = true
                    )
                }
            }
        }
    }

    // 4. Уведомление о подходящем правиле "охлаждения"
    fun suggestCoolingRule(targetAmount: Double) {
        val suitableRule = COOLING_RULES.find { rule ->
            targetAmount >= rule.minAmount && (rule.maxAmount == null || targetAmount <= rule.maxAmount)
        }

        suitableRule?.let { rule ->
            showGoalNotification(
                title = "📅 Рекомендуемый срок",
                message = """
                Для суммы ${formatCurrency(targetAmount)}:
                • Рекомендуемый срок: ${rule.timeframe}
                • Диапазон: ${rule.amountRange}
                • Ежедневно: ${formatCurrency(targetAmount / rule.targetDays)}
                
                Сохраните это правило для автоматического отслеживания!
                """.trimIndent(),
                notificationId = "suggestion_${targetAmount}".hashCode()
            )
        }
    }

    // 5. Мотивационные уведомления
    fun sendMotivationNotification(goal: UserGoal) {
        val progress = (goal.currentAmount / goal.targetAmount) * 100

        val (title, message) = when {
            progress < 25 -> Pair(
                "Начало положено!",
                "Каждая копейка приближает к цели '${goal.name}'. Продолжайте в том же духе! 💪"
            )
            progress < 50 -> Pair(
                "Стабильный рост!",
                "Вы уже на ${"%.0f".format(progress)}% пути к цели '${goal.name}'. Так держать! 📈"
            )
            progress < 75 -> Pair(
                "Больше половины!",
                "Уже ${"%.0f".format(progress)}%! Осталось всего ${formatCurrency(goal.targetAmount - goal.currentAmount)} до цели '${goal.name}' 🎯"
            )
            else -> Pair(
                "Финальный рывок!",
                "Всего ${formatCurrency(goal.targetAmount - goal.currentAmount)} осталось! Скоро сможете сказать: 'Цель достигнута!' 🏆"
            )
        }

        showGoalNotification(
            title = title,
            message = message,
            notificationId = goal.id.hashCode() + 3
        )
    }

    // 6. Уведомление о достижении контрольных точек
    fun sendMilestoneNotification(goal: UserGoal) {
        val milestones = listOf(0.25, 0.5, 0.75, 0.9)

        milestones.forEach { milestone ->
            val milestoneAmount = goal.targetAmount * milestone
            if (goal.currentAmount >= milestoneAmount &&
                goal.currentAmount - milestoneAmount < goal.targetAmount * 0.05
            ) {

                showGoalNotification(
                    title = when (milestone) {
                        0.25 -> "🏅 25% достигнуто!"
                        0.5 -> "🥈 Половина пути!"
                        0.75 -> "🥉 75% выполнено!"
                        0.9 -> "🎖️ Осталось 10%!"
                        else -> "Веха достигнута!"
                    },
                    message = """
                    Цель: ${goal.name}
                    Достигнуто: ${formatCurrency(goal.currentAmount)} из ${formatCurrency(goal.targetAmount)}
                    Прогресс: ${(milestone * 100).toInt()}%
                    
                    ${getMotivationQuote(milestone)}
                    """.trimIndent(),
                    notificationId = goal.id.hashCode() + (milestone * 1000).toInt()
                )
            }
        }
    }

    // Вспомогательные функции
    private fun calculateDaysLeft(goal: UserGoal): Int {
        goal.selectedRule?.let { rule ->
            val daysPassed = daysSince(goal.startDate)
            return max(0, rule.targetDays - daysPassed)
        }
        return -1
    }

    private fun daysSince(startDate: Long): Int {
        return ((System.currentTimeMillis() - startDate) / (1000 * 60 * 60 * 24)).toInt()
    }

    private fun formatCurrency(amount: Double): String {
        return currencyFormat.apply {
            maximumFractionDigits = 0
        }.format(amount).replace("руб.", "₽")
    }

    private fun getMotivationQuote(milestone: Double): String {
        return when (milestone) {
            0.25 -> "«Маленькие шаги каждый день приводят к большим результатам»"
            0.5 -> "«Дорогу осилит идущий. Вы на полпути к успеху!»"
            0.75 -> "«Упорство превращает мечты в реальность. Осталось совсем немного!»"
            0.9 -> "«Почти у цели! Последний рывок — и вы победитель!»"
            else -> "«Финансовая дисциплина — ключ к свободе»"
        }
    }

    // Метод для показа уведомлений с целью
    private fun showGoalNotification(
        title: String,
        message: String,
        notificationId: Int,
        isUrgent: Boolean = false
    ) {
        notificationHelper.showGoalNotification(
            title = title,
            message = message,
            notificationId = notificationId,
            isUrgent = isUrgent
        )
    }
}