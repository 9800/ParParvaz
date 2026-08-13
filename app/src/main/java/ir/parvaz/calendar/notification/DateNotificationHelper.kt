package ir.parvaz.calendar.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import ir.parvaz.calendar.MainActivity
import ir.parvaz.calendar.R
import ir.parvaz.calendar.core.date.CalendarProvider
import ir.parvaz.calendar.core.date.DateFormatter
import ir.parvaz.calendar.data.NotificationPrefs
import ir.parvaz.calendar.receiver.SystemReceiver
import java.util.Calendar

object DateNotificationHelper {

    const val CHANNEL_ID = "parvaz_date"
    const val NOTIFICATION_ID = 1001

    fun refresh(context: Context) {
        val prefs = NotificationPrefs(context)
        if (!prefs.enabled) {
            cancel(context)
            return
        }
        post(context)
        scheduleMidnight(context)
    }

    fun cancel(context: Context) {
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(NOTIFICATION_ID)
    }

    fun post(context: Context) {
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "نوار تاریخ",
                NotificationManager.IMPORTANCE_MIN
            )
            channel.setShowBadge(false)
            channel.setSound(null, null)
            manager.createNotificationChannel(channel)
        }

        val prefs = NotificationPrefs(context)
        val today = CalendarProvider.today()
        val dayText = fa(today.persian.day)

        val views = RemoteViews(context.packageName, R.layout.notification_date)
        views.setTextViewText(R.id.notif_weekday, today.weekday)
        views.setTextViewText(R.id.notif_persian, DateFormatter.format(today.persian))
        views.setTextViewText(
            R.id.notif_others,
            "${DateFormatter.format(today.gregorian)}  •  " +
                (today.hijri?.let { DateFormatter.format(it) } ?: "")
        )
        views.setTextViewText(R.id.notif_day, dayText)

        views.setInt(R.id.notif_root, "setBackgroundColor", prefs.bgColor)
        views.setTextColor(R.id.notif_weekday, prefs.textColor)
        views.setTextColor(R.id.notif_persian, prefs.textColor)
        views.setTextColor(R.id.notif_others, prefs.textColor)
        views.setInt(R.id.notif_day_box, "setBackgroundColor", prefs.boxColor)
        views.setTextColor(R.id.notif_day, prefs.boxTextColor)

        val openIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(IconCompat.createWithBitmap(dayBitmap(context, dayText, false, 0)))
            .setLargeIcon(dayBitmap(context, dayText, true, prefs.boxColor))
            .setCustomContentView(views)
            .setCustomBigContentView(views)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun dayBitmap(
        context: Context,
        text: String,
        withBackground: Boolean,
        bgColor: Int
    ): Bitmap {
        val density = context.resources.displayMetrics.density
        val size = (128 * density).toInt()
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        if (withBackground) {
            paint.color = bgColor
            canvas.drawRoundRect(
                0f, 0f, size.toFloat(), size.toFloat(),
                24f, 24f, paint
            )
            paint.color = AndroidColor.WHITE
        } else {
            paint.color = AndroidColor.WHITE
        }

        paint.textAlign = Paint.Align.CENTER
        paint.textSize = size * 0.5f

        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val y = size / 2 + bounds.height() / 2

        canvas.drawText(text, size / 2f, y.toFloat(), paint)
        return bitmap
    }

    fun scheduleMidnight(context: Context) {
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 1)

        val intent = Intent(context, SystemReceiver::class.java)
        intent.action = SystemReceiver.ACTION_MIDNIGHT

        val pi = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarm.canScheduleExactAlarms()) {
                alarm.setExactAndAllowWhileIdle(AlarmManager.RTC, cal.timeInMillis, pi)
            } else {
                alarm.set(AlarmManager.RTC, cal.timeInMillis, pi)
            }
        } else {
            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC, cal.timeInMillis, pi)
        }
    }

    private fun fa(n: Int): String {
        return n.toString().map { c -> '۰' + (c - '0') }.joinToString("")
    }
}
