package ir.parvaz.calendar.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ir.parvaz.calendar.core.city.Cities
import ir.parvaz.calendar.core.date.CalendarProvider
import ir.parvaz.calendar.core.date.DateFormatter
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

        _uiState.value = _uiState.value.copy(
            weekday = today.weekday,
            persianDate = DateFormatter.format(today.persian),
            gregorianDate = DateFormatter.format(today.gregorian),
            hijriDate = today.hijri?.let { DateFormatter.format(it) } ?: "—",
            events = EventsRepository.todayEvents(today.persian, today.hijri, today.gregorian),
            cityName = city?.name ?: "",
            prayerTimes = times
        )
    }
}
