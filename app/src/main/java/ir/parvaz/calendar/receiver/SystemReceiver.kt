package ir.parvaz.calendar.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ir.parvaz.calendar.data.NotificationPrefs
import ir.parvaz.calendar.notification.DateNotificationHelper

class SystemReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_MIDNIGHT = "ir.parvaz.calendar.MIDNIGHT"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val prefs = NotificationPrefs(context)
        if (!prefs.enabled) return

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            ACTION_MIDNIGHT -> DateNotificationHelper.refresh(context)
        }
    }
}
