package ir.parvaz.calendar.core.date

import java.time.LocalDate

object DateFormatter {

    private val persianMonths = listOf(
        "فروردین",
        "اردیبهشت",
        "خرداد",
        "تیر",
        "مرداد",
        "شهریور",
        "مهر",
        "آبان",
        "آذر",
        "دی",
        "بهمن",
        "اسفند"
    )

    private val gregorianMonths = listOf(
        "ژانویه",
        "فوریه",
        "مارس",
        "آوریل",
        "مه",
        "ژوئن",
        "ژوئیه",
        "اوت",
        "سپتامبر",
        "اکتبر",
        "نوامبر",
        "دسامبر"
    )

    private val hijriMonths = listOf(
        "محرم",
        "صفر",
        "ربیع‌الاول",
        "ربیع‌الثانی",
        "جمادی‌الاول",
        "جمادی‌الثانی",
        "رجب",
        "شعبان",
        "رمضان",
        "شوال",
        "ذی‌القعده",
        "ذی‌الحجه"
    )

    fun format(date: PersianDate): String {
        val monthName = persianMonths.getOrNull(date.month - 1) ?: ""
        return "${date.day.toPersianDigits()} $monthName ${date.year.toPersianDigits()}"
    }

    fun format(date: LocalDate): String {
        val monthName = gregorianMonths.getOrNull(date.monthValue - 1) ?: ""
        return "${date.dayOfMonth.toPersianDigits()} $monthName ${date.year.toPersianDigits()}"
    }

    fun format(date: HijriDate): String {
        val monthName = hijriMonths.getOrNull(date.month - 1) ?: ""
        return "${date.day.toPersianDigits()} $monthName ${date.year.toPersianDigits()}"
    }

    private fun String.toPersianDigits(): String {
        return map { char ->
            when (char) {
                '0' -> '۰'
                '1' -> '۱'
                '2' -> '۲'
                '3' -> '۳'
                '4' -> '۴'
                '5' -> '۵'
                '6' -> '۶'
                '7' -> '۷'
                '8' -> '۸'
                '9' -> '۹'
                else -> char
            }
        }.joinToString("")
    }

    private fun Int.toPersianDigits(): String {
        return toString().toPersianDigits()
    }
}
