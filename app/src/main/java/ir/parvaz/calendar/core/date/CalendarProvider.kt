package ir.parvaz.calendar.core.date

import android.icu.util.PersianCalendar
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.chrono.HijrahDate
import java.util.Date

object CalendarProvider {

    fun today(now: LocalDate = LocalDate.now()): DateInfo {
        val persian = toPersian(now)
        val hijri = toHijri(now)

        return DateInfo(
            persian = persian,
            gregorian = now,
            hijri = hijri,
            weekday = weekdayName(now.dayOfWeek)
        )
    }

    private fun toPersian(date: LocalDate): PersianDate {
        val instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val calendar = PersianCalendar()
        calendar.time = Date.from(instant)

        val year = calendar.get(PersianCalendar.YEAR)
        val month = calendar.get(PersianCalendar.MONTH) + 1
        val day = calendar.get(PersianCalendar.DAY_OF_MONTH)

        return PersianDate(
            year = year,
            month = month,
            day = day
        )
    }

    private fun toHijri(date: LocalDate): HijriDate? {
        return try {
            val hijrah = HijrahDate.from(date)
            HijriDate(
                year = hijrah.year,
                month = hijrah.monthValue,
                day = hijrah.dayOfMonth
            )
        } catch (exception: Exception) {
            null
        }
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
