package ir.parvaz.calendar.ui.screens.home

import ir.parvaz.calendar.core.events.Event
import ir.parvaz.calendar.core.prayer.PrayerTimes

data class HomeUiState(
    val weekday: String = "",
    val persianDate: String = "",
    val gregorianDate: String = "",
    val hijriDate: String = "",
    val events: List<Event> = emptyList(),
    val cityName: String = "",
    val prayerTimes: PrayerTimes? = null,
    val showCityPicker: Boolean = false
)
