package ir.parvaz.calendar.data

import android.content.Context
import android.content.SharedPreferences

class WeatherPrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("parvaz_weather", Context.MODE_PRIVATE)

    var source: Int
        get() = prefs.getInt("source", 0)
        set(value) = prefs.edit().putInt("source", value).apply()

    var accuWeatherKey: String
        get() = prefs.getString("accu_key", "") ?: ""
        set(value) = prefs.edit().putString("accu_key", value).apply()

    var googleKey: String
        get() = prefs.getString("google_key", "") ?: ""
        set(value) = prefs.edit().putString("google_key", value).apply()
}
