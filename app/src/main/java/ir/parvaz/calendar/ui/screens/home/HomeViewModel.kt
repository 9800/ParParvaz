package ir.parvaz.calendar.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ir.parvaz.calendar.core.city.Cities
import ir.parvaz.calendar.core.date.CalendarProvider
import ir.parvaz.calendar.core.date.DateFormatter
import ir.parvaz.calendar.core.date.PersianDate
import ir.parvaz.calendar.core.events.EventsRepository
import ir.parvaz.calendar.core.prayer.PrayerTimesCalculator
import ir.parvaz.calendar.data.CityStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val store = CityStore(app)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val monthNames = listOf(
        "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
    )

    init {
        refresh()
        if (store.savedCityId() == null) {
            _uiState.value = _uiState.value.copy(showCityPicker = true)
        }
    }

    fun selectCity(id: String) {
        store.saveCityId(id)
        _uiState.value = _uiState.value.copy(showCityPicker = false)
        refresh()
    }

    fun openCityPicker() {
        _uiState.value = _uiState.value.copy(showCityPicker = true)
    }

    fun closeCityPicker() {
        _uiState.value = _uiState.value.copy(showCityPicker = false)
    }

    private fun refresh() {
        val today = CalendarProvider.today()
        val city = Cities.byId(store.savedCityId())
        val times = city?.let {
            PrayerTimesCalculator.calculate(today.gregorian, it.lat, it.lng)
        }

        val p = today.persian
        val len = CalendarProvider.monthLength(p.year, p.month)
        val lead = CalendarProvider.firstDayOffset(p.year, p.month)

        val cells = (1..len).map { d ->
            val g = CalendarProvider.jalaliToGregorian(p.year, p.month, d)
            val h = CalendarProvider.hijriOf(g)
            val ev = EventsRepository.todayEvents(PersianDate(p.year, p.month, d), h, g)
            DayCell(
                day = d,
                isHoliday = ev.any { it.holiday },
                hasEvent = ev.isNotEmpty(),
                isToday = d == p.day
            )
        }

        _uiState.value = _uiState.value.copy(
            weekday = today.weekday,
            persianDate = DateFormatter.format(p),
            gregorianDate = DateFormatter.format(today.gregorian),
            hijriDate = today.hijri?.let { DateFormatter.format(it) } ?: "—",
            todayDay = p.day,
            monthTitle = "${monthNames[p.month - 1]} ${toFa(p.year)}",
            leadingBlanks = lead,
            monthDays = cells,
            events = EventsRepository.todayEvents(p, today.hijri, today.gregorian),
            cityName = city?.name ?: "",
            prayerTimes = times
        )
    }

    private fun toFa(n: Int): String {
        return n.toString().map { c -> '۰' + (c - '0') }.joinToString("")
    }
}
