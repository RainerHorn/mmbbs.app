package app.mmbbs.lehrerdashboard.ui

import app.mmbbs.lehrerdashboard.domain.model.DashboardItem
import java.time.Instant
import java.time.LocalDate

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Content(
        val date: LocalDate,
        val items: List<DashboardItem>,
        val lastSync: Instant,
        val isRefreshing: Boolean = false,
        val message: String? = null
    ) : DashboardUiState
    data class Empty(val date: LocalDate, val lastSync: Instant) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}
