package ir.parvaz.calendar.adhan

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import ir.parvaz.calendar.core.city.Cities
import ir.parvaz.calendar.core.prayer.PrayerTimesCalculator
import ir.parvaz.calendar.data.AdhanPrefs
import ir.parvaz.calendar.data.CityStore
import java.time.LocalDate
import java.time.ZoneId

object AdhanScheduler {

    private val ZONE = ZoneId.of("Asia/Tehran")
    private val NAMES = listOf("fajr", "dhuhr", "maghrib")

    fun scheduleAll(context: Context) {
        cancelAll(context)

        val city = Cities.byId(CityStore(context).savedCityId()) ?: return
        val adhanPrefs = AdhanPrefs(context)
        val enabled = listOf(
            adhanPrefs.fajrEnabled,
            adhanPrefs.dhuhrEnabled,
            adhanPrefs.maghribEnabled
        )

        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val today = LocalDate.now()

        for (dayOffset in 0..1) {
            val day = today.plusDays(dayOffset.toLong())
            val times = PrayerTimesCalculator.calculate(day, city.lat, city.lng)
            val values = listOf(times.fajr, times.dhuhr, times.maghrib)

            for (i in 0..2) {
                if (!enabled[i]) continue

                val hours = parseHours(values[i])
                val millis = day.atStartOfDay(ZONE).toInstant().toEpochMilli() +
                    (hours * 3600000.0).toLong()

                if (millis <= System.currentTimeMillis()) continue

                val intent = Intent(context, AdhanReceiver::class.java)
                intent.action = "ir.parvaz.calendar.ADHAN_${NAMES[i]}"

                val pi = PendingIntent.getBroadcast(
                    context,
                    dayOffset * 3 + i + 1,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarm.canScheduleExactAlarms()) {
                    alarm.set(AlarmManager.RTC_WAKEUP, millis, pi)
                } else {
                    alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pi)
                }
            }
        }
    }

    private fun cancelAll(context: Context) {
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (dayOffset in 0..1) {
            for (i in 0..2) {
                val intent = Intent(context, AdhanReceiver::class.java)
                intent.action = "ir.parvaz.calendar.ADHAN_${NAMES[i]}"
                val pi = PendingIntent.getBroadcast(
                    context,
                    dayOffset * 3 + i + 1,
                    intent,
                    PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
                )
                if (pi != null) {
                    alarm.cancel(pi)
                    pi.cancel()
                }
            }
        }
    }

    private fun parseHours(fa: String): Double {
        val en = fa.map { c ->
            when (c) {
                in '۰'..'' -> ('0' + (c - '۰'))
                else -> c
            }
        }.joinToString("")
        val parts = en.split(":")
        val h = parts.getOrNull(0)?.toDoubleOrNull() ?: 0.0
        val m = parts.getOrNull(1)?.toDoubleOrNull() ?: 0.0
        return h + m / 60.0
    }
}
