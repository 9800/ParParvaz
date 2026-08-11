package ir.parvaz.calendar.ui.screens.home

import androidx.lifecycle.ViewModel
import ir.parvaz.calendar.core.date.CalendarProvider
import ir.parvaz.calendar.core.date.DateFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        val today = CalendarProvider.today()

        _uiState.value = HomeUiState(
            weekday = today.weekday,
            persianDate = DateFormatter.format(today.persian),
            gregorianDate = DateFormatter.format(today.gregorian),
            hijriDate = today.hijri?.let { DateFormatter.format(it) } ?: "—"
        )
    }
}
