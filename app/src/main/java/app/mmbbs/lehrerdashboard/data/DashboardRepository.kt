package app.mmbbs.lehrerdashboard.data

import app.mmbbs.lehrerdashboard.domain.model.DashboardItem
import java.time.LocalDate

data class DashboardSnapshot(
    val items: List<DashboardItem>,
    val isOffline: Boolean = false
)

interface DashboardRepository {
    suspend fun getDashboard(date: LocalDate): DashboardSnapshot
    suspend fun saveMemo(id: String?, title: String, body: String)
    suspend fun deleteMemo(id: String)
}
