package ir.parvaz.calendar.adhan

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import ir.parvaz.calendar.R
import ir.parvaz.calendar.data.AdhanPrefs

class AdhanReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, AdhanService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
        AdhanScheduler.scheduleAll(context)
    }
}

class AdhanService : Service() {

    private var player: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(2001, buildNotification())
        play(AdhanPrefs(this))
        return START_NOT_STICKY
    }

    private fun buildNotification(): android.app.Notification {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "parvaz_adhan",
                "پخش اذان",
                NotificationManager.IMPORTANCE_LOW
            )
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, "parvaz_adhan")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("در حال پخش اذان")
            .setOngoing(true)
            .build()
    }

    private fun play(prefs: AdhanPrefs) {
        try {
            val resId = resources.getIdentifier(
                "adhan${prefs.soundIndex + 1}",
                "raw",
                packageName
            )

            val mp = if (resId != 0) {
                MediaPlayer.create(this, resId)
            } else {
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                if (uri != null) MediaPlayer.create(this, uri) else null
            }

            player = mp
            mp?.setOnCompletionListener { stopSelf() }
            mp?.start() ?: stopSelf()
        } catch (exception: Exception) {
            stopSelf()
        }

        if (prefs.vibration) {
            try {
                vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vm = getSystemService(VibratorManager::class.java)
                    vm?.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                }
                vibrator?.vibrate(
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 800, 400, 800, 400, 800),
                        0
                    )
                )
            } catch (exception: Exception) {
            }
        }
    }

    override fun onDestroy() {
        try {
            player?.stop()
            player?.release()
            vibrator?.cancel()
        } catch (exception: Exception) {
        }
        super.onDestroy()
    }
}
