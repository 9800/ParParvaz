package ir.parvaz.calendar.data

import android.content.Context
import android.content.SharedPreferences

class AppearancePrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("parvaz_appearance", Context.MODE_PRIVATE)

    var fontSize: Float
        get() = prefs.getFloat("fontsize", 1f)
        set(value) = prefs.edit().putFloat("fontsize", value).apply()

    var notifStyle: Int
        get() = prefs.getInt("notifstyle", 0)
        set(value) = prefs.edit().putInt("notifstyle", value).apply()
}
