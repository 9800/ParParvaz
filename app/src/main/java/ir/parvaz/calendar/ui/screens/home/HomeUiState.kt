package ir.parvaz.calendar.ui.screens.home

import ir.parvaz.calendar.core.events.Event
import ir.parvaz.calendar.core.prayer.PrayerTimes

data class DayCell(
    val day: Int,
    val isHoliday: Boolean,
    val hasEvent: Boolean,
    val isToday: Boolean
)

data class HomeUiState(
    val weekday: String = "",
    val persianDate: String = "",
    val gregorianDate: String = "",
    val hijriDate: String = "",
    val todayDay: Int = 0,
    val monthTitle: String = "",
    val weekdayNames: List<String> = listOf(
        "شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه"
    ),
    val leadingBlanks: Int = 0,
    val monthDays: List<DayCell> = emptyList(),
    val events: List<Event> = emptyList(),
    val cityName: String = "",
    val prayerTimes: PrayerTimes? = null,
    val showCityPicker: Boolean = false
)
