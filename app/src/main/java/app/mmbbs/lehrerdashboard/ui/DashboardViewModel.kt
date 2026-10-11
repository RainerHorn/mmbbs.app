package app.mmbbs.lehrerdashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.mmbbs.lehrerdashboard.data.DashboardRepository
import java.time.Clock
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: DashboardRepository,
    private val clock: Clock = Clock.systemDefaultZone()
) : ViewModel() {
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            val previous = _uiState.value as? DashboardUiState.Content
            _uiState.value = previous?.copy(isRefreshing = true) ?: DashboardUiState.Loading
            _uiState.value = try {
                val date = LocalDate.now(clock)
                val items = repository.getItems(date)
                if (items.isEmpty()) DashboardUiState.Empty(date, clock.instant())
                else DashboardUiState.Content(date, items, clock.instant())
            } catch (exception: Exception) {
                if (previous != null) previous.copy(isRefreshing = false, message = exception.message)
                else DashboardUiState.Error(exception.message ?: "Daten konnten nicht geladen werden.")
            }
        }
    }

    fun saveMemo(id: String?, title: String, body: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.saveMemo(id, title, body)
            refresh()
        }
    }

    fun deleteMemo(id: String) {
        viewModelScope.launch {
            repository.deleteMemo(id)
            refresh()
        }
    }

    class Factory(private val repository: DashboardRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DashboardViewModel(repository) as T
    }
}
