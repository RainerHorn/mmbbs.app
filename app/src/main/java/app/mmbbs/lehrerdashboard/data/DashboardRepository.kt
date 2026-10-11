package app.mmbbs.lehrerdashboard.data

import app.mmbbs.lehrerdashboard.domain.model.DashboardItem
import java.time.LocalDate

interface DashboardRepository {
    suspend fun getItems(date: LocalDate): List<DashboardItem>
    suspend fun saveMemo(id: String?, title: String, body: String)
    suspend fun deleteMemo(id: String)
}
