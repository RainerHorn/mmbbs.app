package app.mmbbs.lehrerdashboard.data

import app.mmbbs.lehrerdashboard.domain.model.DashboardItem
import java.time.LocalDate

fun interface DashboardRepository {
    fun getItems(date: LocalDate): List<DashboardItem>
}
