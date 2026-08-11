package ir.parvaz.calendar.data

import android.content.Context
import android.content.SharedPreferences

class CityStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("parvaz_settings", Context.MODE_PRIVATE)

    fun savedCityId(): String? = prefs.getString("city_id", null)

    fun saveCityId(id: String) {
        prefs.edit().putString("city_id", id).apply()
    }
}
