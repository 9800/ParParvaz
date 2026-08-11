package ir.parvaz.calendar.ui.screens.home

data class HomeUiState(
    val weekday: String = "",
    val persianDate: String = "",
    val gregorianDate: String = "",
    val hijriDate: String = "",
    val todayEvents: String = "مناسبت‌ها در مرحله بعدی اضافه می‌شوند"
)
