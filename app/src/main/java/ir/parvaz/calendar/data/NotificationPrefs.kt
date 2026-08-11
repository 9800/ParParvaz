package ir.parvaz.calendar.data

import android.content.Context
import android.content.SharedPreferences

class NotificationPrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("parvaz_notification", Context.MODE_PRIVATE)

    var enabled: Boolean
        get() = prefs.getBoolean("enabled", false)
        set(value) = prefs.edit().putBoolean("enabled", value).apply()

    var bgColor: Int
        get() = prefs.getInt("bg", 0xFF0E6BA8.toInt())
        set(value) = prefs.edit().putInt("bg", value).apply()

    var textColor: Int
        get() = prefs.getInt("text", 0xFFFFFFFF.toInt())
        set(value) = prefs.edit().putInt("text", value).apply()

    var boxColor: Int
        get() = prefs.getInt("box", 0xFFE53935.toInt())
        set(value) = prefs.edit().putInt("box", value).apply()

    var boxTextColor: Int
        get() = prefs.getInt("boxtext", 0xFFFFFFFF.toInt())
        set(value) = prefs.edit().putInt("boxtext", value).apply()
}
