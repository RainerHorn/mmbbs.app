package app.mmbbs.lehrerdashboard.data

import app.mmbbs.lehrerdashboard.domain.DashboardItemSorter
import app.mmbbs.lehrerdashboard.domain.model.DashboardItem
import app.mmbbs.lehrerdashboard.domain.model.ItemKind
import app.mmbbs.lehrerdashboard.domain.model.ItemSource
import java.time.LocalDate
import java.time.ZoneId

class FakeDashboardRepository(private val zoneId: ZoneId = ZoneId.systemDefault()) : DashboardRepository {
    override suspend fun getDashboard(date: LocalDate) = DashboardSnapshot(sampleItems(date))

    fun sampleItems(date: LocalDate): List<DashboardItem> {
        fun at(hour: Int, minute: Int) = date.atTime(hour, minute).atZone(zoneId).toInstant()

        val webUntis = listOf(
            DashboardItem("lesson-math-8a", "Mathematik · 8A", "Raum 204 · Lineare Funktionen", ItemKind.LESSON, ItemSource.WEBUNTIS, startsAt = at(8, 0)),
            DashboardItem("lesson-it-10b", "Informatik · 10B", "Raum 118 · Datenbanken", ItemKind.LESSON, ItemSource.WEBUNTIS, startsAt = at(9, 50))
        )
        val microsoft = listOf(
            DashboardItem("appointment-conference", "Fachkonferenz", "Besprechungsraum 2", ItemKind.APPOINTMENT, ItemSource.MICROSOFT_GRAPH, startsAt = at(11, 35)),
            DashboardItem("task-corrections", "Klassenarbeiten korrigieren", "Noch 12 Arbeiten", ItemKind.TASK, ItemSource.MICROSOFT_GRAPH, startsAt = at(14, 0)),
            DashboardItem("email-new", "3 neue Nachrichten", "Outlook und Teams", ItemKind.EMAIL, ItemSource.MICROSOFT_GRAPH)
        )
        return DashboardItemSorter.merge(webUntis, microsoft)
    }

    override suspend fun saveMemo(id: String?, title: String, body: String) = Unit
    override suspend fun deleteMemo(id: String) = Unit
}
