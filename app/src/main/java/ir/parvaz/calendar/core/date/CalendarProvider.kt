package ir.parvaz.calendar.core.date

import java.time.DayOfWeek
import java.time.LocalDate

object CalendarProvider {

    fun today(now: LocalDate = LocalDate.now()): DateInfo {
        return DateInfo(
            persian = toPersian(now),
            gregorian = now,
            hijri = toHijri(now),
            weekday = weekdayName(now.dayOfWeek)
        )
    }

    private fun toPersian(date: LocalDate): PersianDate {
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

    private fun toHijri(date: LocalDate): HijriDate {
        val jd = julianDayNumber(date)

        var l = jd - 1948440L + 10632L
        val n = (l - 1L) / 10631L
        l = l - 10631L * n + 354L

        val j = (10985L - l) / 5316L * ((50L * l) / 17719L) +
            (l / 5670L) * ((43L * l) / 15238L)

        l = l - (30L - j) / 15L * ((17719L * j) / 50L) -
            (j / 16L) * ((15238L * j) / 43L) + 29L

        val m = (24L * l) / 709L
        val d = l - (709L * m) / 24L
        val y = 30L * n + j - 30L

        return HijriDate(y.toInt(), m.toInt(), d.toInt())
    }

    private fun julianDayNumber(date: LocalDate): Long {
        val y = date.year.toLong()
        val m = date.monthValue.toLong()
        val d = date.dayOfMonth.toLong()

        val a = (14L - m) / 12L
        val yy = y + 4800L - a
        val mm = m + 12L * a - 3L

        return d +
            (153L * mm + 2L) / 5L +
            365L * yy +
            yy / 4L -
            yy / 100L +
            yy / 400L -
            32045L
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
