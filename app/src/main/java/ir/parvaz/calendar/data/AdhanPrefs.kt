package ir.parvaz.calendar.data

import android.content.Context
import android.content.SharedPreferences

class AdhanPrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("parvaz_adhan", Context.MODE_PRIVATE)

    var fajrEnabled: Boolean
        get() = prefs.getBoolean("fajr", true)
        set(value) = prefs.edit().putBoolean("fajr", value).apply()

    var dhuhrEnabled: Boolean
        get() = prefs.getBoolean("dhuhr", true)
        set(value) = prefs.edit().putBoolean("dhuhr", value).apply()

    var maghribEnabled: Boolean
        get() = prefs.getBoolean("maghrib", true)
        set(value) = prefs.edit().putBoolean("maghrib", value).apply()

    var soundIndex: Int
        get() = prefs.getInt("sound", 0)
        set(value) = prefs.edit().putInt("sound", value).apply()

    var vibration: Boolean
        get() = prefs.getBoolean("vib", false)
        set(value) = prefs.edit().putBoolean("vib", value).apply()
}
