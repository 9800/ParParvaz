package ir.parvaz.calendar.ui.screens.home

import ir.parvaz.calendar.core.prayer.PrayerTimes

data class HomeUiState(
    val weekday: String = "",
    val persianDate: String = "",
    val gregorianDate: String = "",
    val hijriDate: String = "",
    val todayEvents: String = "مناسبت‌ها در مرحله بعدی اضافه می‌شوند",
    val cityName: String = "",
    val prayerTimes: PrayerTimes? = null,
    val showCityPicker: Boolean = false
)
