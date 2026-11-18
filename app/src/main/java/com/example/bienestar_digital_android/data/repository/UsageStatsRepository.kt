package com.example.bienestar_digital_android.data.repository

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.provider.Settings
import java.util.Calendar
import java.util.Locale

data class AppUsageInfo(
    val packageName: String,
    val appName: String,
    val usageTimeMinutes: Long,
    val isSocialMedia: Boolean
)

data class DailyUsageSummary(
    val totalScreenTimeMinutes: Long,
    val socialMediaTimeMinutes: Long,
    val streamingTimeMinutes: Long = 0L,
    val productivityTimeMinutes: Long = 0L,
    val disconnectionTimeMinutes: Long = 0L,
    val topApps: List<AppUsageInfo> = emptyList()
) {
    val totalScreenTimeHours: Float
        get() = totalScreenTimeMinutes / 60f

    val socialMediaTimeHours: Float
        get() = socialMediaTimeMinutes / 60f
}

class UsageStatsRepository(private val context: Context) {

    // Paquetes conocidos de redes sociales
    private val socialMediaPackages = setOf(
        "com.whatsapp",
        "com.instagram.android",
        "com.zhiliaoapp.musically", // TikTok
        "com.ss.android.ugc.trill", // TikTok (variante)
        "com.facebook.katana",
        "com.facebook.orca", // Facebook Messenger
        "com.twitter.android", // X / Twitter
        "com.snapchat.android",
        "com.pinterest",
        "com.reddit.frontpage"
    )

    // Paquetes de ocio y streaming
    private val streamingPackages = setOf(
        "com.google.android.youtube",
        "com.netflix.mediaclient",
        "com.spotify.music",
        "tv.twitch.android",
        "com.disney.disneyplus",
        "com.amazon.avod.thirdpartyclient"
    )

    // Paquetes de productividad y estudio
    private val productivityPackages = setOf(
        "com.google.android.apps.classroom",
        "com.google.android.apps.docs",
        "com.google.android.apps.sheets",
        "com.google.android.apps.slides",
        "com.google.android.gm",
        "com.microsoft.office.word",
        "com.microsoft.office.excel",
        "com.microsoft.teams",
        "com.duolingo",
        "notion.id"
    )

    /**
     * Verifica si el usuario concedió el permiso especial de Acceso a Uso.
     */
    fun hasUsageStatsPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager
            ?: return false
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * Abre la pantalla de ajustes de Android para que el usuario active el permiso de Acceso a Uso.
     */
    fun openUsageAccessSettings() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    /**
     * Obtiene el resumen de uso del día actual (desde las 00:00 hs hasta el momento actual).
     */
    fun getTodayUsageSummary(): DailyUsageSummary {
        if (!hasUsageStatsPermission()) {
            return DailyUsageSummary(0, 0, 0, 0, 0, emptyList())
        }

        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            ?: return DailyUsageSummary(0, 0, 0, 0, 0, emptyList())

        // Definir inicio del día (00:00:00)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val statsMap = usageStatsManager.queryAndAggregateUsageStats(
            startTime,
            endTime
        )

        val packageManager = context.packageManager
        val appsList = mutableListOf<AppUsageInfo>()
        var totalScreenTimeMillis = 0L
        var socialMediaTimeMillis = 0L
        var streamingTimeMillis = 0L
        var productivityTimeMillis = 0L

        for ((pkgName, stats) in statsMap) {
            val timeInForeground = stats.totalTimeInForeground

            // Solo considerar apps que tuvieron interacción real (> 0 ms)
            if (timeInForeground > 0) {
                totalScreenTimeMillis += timeInForeground

                val isSocial = socialMediaPackages.contains(pkgName)
                if (isSocial) {
                    socialMediaTimeMillis += timeInForeground
                } else if (streamingPackages.contains(pkgName)) {
                    streamingTimeMillis += timeInForeground
                } else if (productivityPackages.contains(pkgName)) {
                    productivityTimeMillis += timeInForeground
                }

                // Obtener el nombre legible y amigable de la app (ejemplo: "Instagram", "WhatsApp")
                val rawLabel = try {
                    val appInfo = packageManager.getApplicationInfo(pkgName, 0)
                    packageManager.getApplicationLabel(appInfo).toString()
                } catch (_: Exception) {
                    pkgName
                }

                val friendlyName = when {
                    pkgName.contains("whatsapp", ignoreCase = true) -> "WhatsApp"
                    pkgName.contains("instagram", ignoreCase = true) -> "Instagram"
                    pkgName.contains("tiktok", ignoreCase = true) || pkgName.contains("musically", ignoreCase = true) || pkgName.contains("trill", ignoreCase = true) -> "TikTok"
                    pkgName.contains("twitter", ignoreCase = true) -> "X (Twitter)"
                    pkgName.contains("facebook.orca", ignoreCase = true) -> "Messenger"
                    pkgName.contains("facebook", ignoreCase = true) -> "Facebook"
                    pkgName.contains("snapchat", ignoreCase = true) -> "Snapchat"
                    pkgName.contains("pinterest", ignoreCase = true) -> "Pinterest"
                    pkgName.contains("reddit", ignoreCase = true) -> "Reddit"
                    pkgName.contains("youtube", ignoreCase = true) -> "YouTube"
                    else -> {
                        if (rawLabel.startsWith("com.") || rawLabel.contains(".")) {
                            pkgName.substringAfterLast(".").replaceFirstChar { it.uppercase(Locale.getDefault()) }
                        } else {
                            rawLabel
                        }
                    }
                }

                appsList.add(
                    AppUsageInfo(
                        packageName = pkgName,
                        appName = friendlyName,
                        usageTimeMinutes = timeInForeground / (1000 * 60),
                        isSocialMedia = isSocial
                    )
                )
            }
        }

        val sortedApps = appsList.sortedByDescending { it.usageTimeMinutes }
        val totalScreenMinutes = totalScreenTimeMillis / (1000 * 60)
        val elapsedMinutesToday = ((endTime - startTime) / (1000 * 60)).coerceAtLeast(1L)
        val disconnectionMinutes = (elapsedMinutesToday - totalScreenMinutes).coerceAtLeast(0L)

        return DailyUsageSummary(
            totalScreenTimeMinutes = totalScreenMinutes,
            socialMediaTimeMinutes = socialMediaTimeMillis / (1000 * 60),
            streamingTimeMinutes = streamingTimeMillis / (1000 * 60),
            productivityTimeMinutes = productivityTimeMillis / (1000 * 60),
            disconnectionTimeMinutes = disconnectionMinutes,
            topApps = sortedApps
        )
    }
}
