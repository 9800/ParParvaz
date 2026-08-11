package ir.parvaz.weather

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class WeatherData(
    val temp: Double,
    val description: String,
    val icon: String,
    val humidity: Int,
    val windSpeed: Double
)

object WeatherService {

    suspend fun fetchOpenMeteo(lat: Double, lon: Double): WeatherData? = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current_weather=true&hourly=relative_humidity_2m,wind_speed_10m")
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 5000
            conn.readTimeout = 5000

            val response = conn.inputStream.bufferedReader().readText()
            val json = JSONObject(response)
            val current = json.getJSONObject("current_weather")
            val hourly = json.getJSONObject("hourly")

            WeatherData(
                temp = current.getDouble("temperature"),
                description = weatherCodeToText(current.getInt("weathercode")),
                icon = weatherCodeToIcon(current.getInt("weathercode")),
                humidity = hourly.getJSONArray("relative_humidity_2m").getInt(0),
                windSpeed = current.getDouble("windspeed")
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchAccuWeather(lat: Double, lon: Double, apiKey: String): WeatherData? = withContext(Dispatchers.IO) {
        try {
            val locationUrl = URL("http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=$apiKey&q=$lat,$lon")
            val locConn = locationUrl.openConnection() as HttpURLConnection
            val locResponse = locConn.inputStream.bufferedReader().readText()
            val locJson = JSONObject(locResponse)
            val locationKey = locJson.getString("Key")

            val weatherUrl = URL("http://dataservice.accuweather.com/currentconditions/v1/$locationKey?apikey=$apiKey")
            val weatherConn = weatherUrl.openConnection() as HttpURLConnection
            val weatherResponse = weatherConn.inputStream.bufferedReader().readText()
            val weatherArray = org.json.JSONArray(weatherResponse)
            val current = weatherArray.getJSONObject(0)

            WeatherData(
                temp = current.getJSONObject("Temperature").getJSONObject("Metric").getDouble("Value"),
                description = current.getJSONObject("WeatherText").getString("Value"),
                icon = current.getString("WeatherIcon"),
                humidity = current.getInt("RelativeHumidity"),
                windSpeed = current.getJSONObject("Wind").getJSONObject("Speed").getJSONObject("Metric").getDouble("Value")
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun weatherCodeToText(code: Int): String {
        return when {
            code == 0 -> "آسمان صاف"
            code in 1..3 -> "کمی ابری"
            code in 45..48 -> "مه‌آلود"
            code in 51..57 -> "نم‌نم باران"
            code in 61..67 -> "بارانی"
            code in 71..77 -> "برفی"
            code in 80..82 -> "رگبار"
            code in 95..99 -> "طوفانی"
            else -> "نامشخص"
        }
    }

    private fun weatherCodeToIcon(code: Int): String {
        return when {
            code == 0 -> "☀️"
            code in 1..3 -> "⛅"
            code in 45..48 -> "🌫️"
            code in 51..67 -> "🌧️"
            code in 71..77 -> "❄️"
            code in 80..82 -> "🌦️"
            code in 95..99 -> "⛈️"
            else -> "🌤️"
        }
    }
}
