package ir.parvaz.calendar.core.date

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField

object CalendarProvider {

    var hijriOffset: Int = 1

    fun today(now: LocalDate = LocalDate.now()): DateInfo {
        return DateInfo(
            persian = persianOf(now),
            gregorian = now,
            hijri = hijriOf(now),
            weekday = weekdayName(now.dayOfWeek)
        )
    }

    fun persianOf(date: LocalDate): PersianDate {
        val gy = date.year.toLong()
        val gm = date.monthValue
        val gd = date.dayOfMonth.toLong()

        val gdm = longArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        val gy2 = if (gm > 2) gy + 1 else gy

        var days = 355666L +
            365L * gy +
            (gy2 + 3) / 4 -
            (gy2 + 99) / 100 +
            (gy2 + 399) / 400 +
            gd +
            gdm[gm - 1]

        var jy = -1595L + 33L * (days / 12053L)
        days %= 12053L
        jy += 4L * (days / 1461L)
        days %= 1461L

        if (days > 365L) {
            jy += (days - 1L) / 365L
            days = (days - 1L) % 365L
        }

        val jm: Long
        val jd: Long

        if (days < 186L) {
            jm = 1L + days / 31L
            jd = 1L + days % 31L
        } else {
            jm = 7L + (days - 186L) / 30L
            jd = 1L + (days - 186L) % 30L
        }

        return PersianDate(jy.toInt(), jm.toInt(), jd.toInt())
    }

    fun hijriOf(date: LocalDate): HijriDate? {
        return try {
            val hijrah = HijrahDate.from(date.plusDays(hijriOffset.toLong()))
            HijriDate(
                hijrah.get(ChronoField.YEAR),
                hijrah.get(ChronoField.MONTH_OF_YEAR),
                hijrah.get(ChronoField.DAY_OF_MONTH)
            )
        } catch (exception: Exception) {
            null
        }
    }

    fun jalaliToGregorian(jy: Int, jm: Int, jd: Int): LocalDate {
        val jy2 = jy + 1595
        var days = -355668L +
            365L * jy2 +
            (jy2 / 33) * 8L +
            ((jy2 % 33 + 3) / 4) +
            jd +
            if (jm < 7) (jm - 1) * 31 else (jm - 7) * 30 + 186

        var gy = (400L * (days / 146097L)).toInt()
        days %= 146097L

        if (days > 36524L) {
            days -= 1
            gy += (100L * (days / 36524L)).toInt()
            days %= 36524L
            if (days >= 365L) days += 1
        }

        gy += (4L * (days / 1461L)).toInt()
        days %= 1461L

        if (days > 365L) {
            gy += ((days - 1) / 365L).toInt()
            days = (days - 1) % 365L
        }

        var gd = (days + 1).toInt()
        val leap = (gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)
        val sal = intArrayOf(0, 31, if (leap) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var gm = 0
        while (gm < 13 && gd > sal[gm]) {
            gd -= sal[gm]
            gm++
        }

        return LocalDate.of(gy, gm, gd)
    }

    fun monthLength(jy: Int, jm: Int): Int {
        return when {
            jm <= 6 -> 31
            jm <= 11 -> 30
            else -> {
                val g = jalaliToGregorian(jy, 12, 30)
                val back = persianOf(g)
                if (back.year == jy && back.month == 12 && back.day == 30) 30 else 29
            }
        }
    }

    fun firstDayOffset(jy: Int, jm: Int): Int {
        val g = jalaliToGregorian(jy, jm, 1)
        return (g.dayOfWeek.value + 1) % 7
    }

    private fun weekdayName(dayOfWeek: DayOfWeek): String {
        return when (dayOfWeek) {
            DayOfWeek.SATURDAY -> "شنبه"
            DayOfWeek.SUNDAY -> "یکشنبه"
            DayOfWeek.MONDAY -> "دوشنبه"
            DayOfWeek.TUESDAY -> "سه‌شنبه"
            DayOfWeek.WEDNESDAY -> "چهارشنبه"
            DayOfWeek.THURSDAY -> "پنجشنبه"
            DayOfWeek.FRIDAY -> "جمعه"
        }
    }
}
