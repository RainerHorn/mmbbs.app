package app.mmbbs.lehrerdashboard.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import app.mmbbs.lehrerdashboard.data.DashboardRepository
import app.mmbbs.lehrerdashboard.data.FakeDashboardRepository
import java.time.Clock
import java.time.LocalDate

class DashboardViewModel(
    private val repository: DashboardRepository = FakeDashboardRepository(),
    private val clock: Clock = Clock.systemDefaultZone()
) : ViewModel() {
    private val _uiState = mutableStateOf<DashboardUiState>(DashboardUiState.Loading)
    val uiState: State<DashboardUiState> = _uiState

    init { refresh() }

    fun refresh() {
        _uiState.value = DashboardUiState.Loading
        _uiState.value = try {
            val date = LocalDate.now(clock)
            val items = repository.getItems(date)
            if (items.isEmpty()) DashboardUiState.Empty(date, clock.instant())
            else DashboardUiState.Content(date, items, clock.instant())
        } catch (exception: Exception) {
            DashboardUiState.Error(exception.message ?: "Daten konnten nicht geladen werden.")
        }
    }
}
