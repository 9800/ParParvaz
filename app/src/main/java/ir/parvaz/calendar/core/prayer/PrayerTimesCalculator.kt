package ir.parvaz.calendar.core.prayer

import java.time.LocalDate
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

data class PrayerTimes(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val sunset: String,
    val maghrib: String,
    val midnight: String
)

object PrayerTimesCalculator {

    private const val FAJR_ANGLE = 17.7
    private const val MAGHRIB_ANGLE = 4.5
    private const val SUNRISE_ANGLE = 0.833
    private const val TIMEZONE = 3.5

    fun calculate(date: LocalDate, lat: Double, lng: Double): PrayerTimes {
        val t = raw(date, lat, lng)
        val tomorrow = raw(date.plusDays(1), lat, lng)
        val midnight = fixHour((t.sunset + tomorrow.fajr + 24.0) / 2.0)

        return PrayerTimes(
            fajr = format(t.fajr),
            sunrise = format(t.sunrise),
            dhuhr = format(t.dhuhr),
            sunset = format(t.sunset),
            maghrib = format(t.maghrib),
            midnight = format(midnight)
        )
    }

    private data class Raw(
        val fajr: Double,
        val sunrise: Double,
        val dhuhr: Double,
        val sunset: Double,
        val maghrib: Double
    )

    private fun raw(date: LocalDate, lat: Double, lng: Double): Raw {
        val jd = julianDate(date.year, date.monthValue, date.dayOfMonth)
        val d = jd - 2451545.0

        val g = Math.toRadians(fixAngle(357.529 + 0.98560028 * d))
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = Math.toRadians(fixAngle(q + 1.915 * sin(g) + 0.020 * sin(2 * g)))
        val e = Math.toRadians(23.439 - 0.00000036 * d)

        val dec = asin(sin(e) * sin(l))
        val ra = fixHour(Math.toDegrees(atan2(cos(e) * sin(l), cos(l))) / 15.0)
        val eqt = q / 15.0 - ra

        val latRad = Math.toRadians(lat)
        val dhuhr = fixHour(12.0 + TIMEZONE - lng / 15.0 - eqt)

        val tFajr = angleTime(FAJR_ANGLE, latRad, dec)
        val tSun = angleTime(SUNRISE_ANGLE, latRad, dec)
        val tMaghrib = angleTime(MAGHRIB_ANGLE, latRad, dec)

        return Raw(
            fajr = dhuhr - tFajr,
            sunrise = dhuhr - tSun,
            dhuhr = dhuhr,
            sunset = dhuhr + tSun,
            maghrib = dhuhr + tMaghrib
        )
    }

    private fun angleTime(angleDeg: Double, latRad: Double, dec: Double): Double {
        val cosH = (-sin(Math.toRadians(angleDeg)) - sin(latRad) * sin(dec)) /
            (cos(latRad) * cos(dec))
        if (cosH < -1.0 || cosH > 1.0) return 0.0
        return Math.toDegrees(acos(cosH)) / 15.0
    }

    private fun julianDate(y0: Int, m0: Int, d0: Int): Double {
        var y = y0
        var m = m0
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = y / 100
        val b = 2 - a + a / 4
        return (365.25 * (y + 4716)).toInt() +
            (30.6001 * (m + 1)).toInt() +
            d0 + b - 1524.5
    }

    private fun fixAngle(a: Double): Double {
        var r = a % 360.0
        if (r < 0) r += 360.0
        return r
    }

    private fun fixHour(h: Double): Double {
        var r = h % 24.0
        if (r < 0) r += 24.0
        return r
    }

    private fun format(hours: Double): String {
        val h = fixHour(hours)
        var hh = h.toInt()
        var mm = ((h - hh) * 60).toInt()
        if (mm == 60) {
            hh = (hh + 1) % 24
            mm = 0
        }
        return "${toFa(hh)}:${toFa(mm)}"
    }

    private fun toFa(n: Int): String {
        val s = if (n < 10) "0$n" else "$n"
        return s.map { c -> '۰' + (c - '0') }.joinToString("")
    }
}
