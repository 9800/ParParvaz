package ir.parvaz.calendar.core.date

import java.time.LocalDate

data class PersianDate(
    val year: Int,
    val month: Int,
    val day: Int
)

data class HijriDate(
    val year: Int,
    val month: Int,
    val day: Int
)

data class DateInfo(
    val persian: PersianDate,
    val gregorian: LocalDate,
    val hijri: HijriDate?,
    val weekday: String
)
