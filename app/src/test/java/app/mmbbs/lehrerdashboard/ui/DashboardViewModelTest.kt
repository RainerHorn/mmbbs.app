package app.mmbbs.lehrerdashboard.ui

import app.mmbbs.lehrerdashboard.data.DashboardRepository
import app.mmbbs.lehrerdashboard.domain.model.DashboardItem
import app.mmbbs.lehrerdashboard.domain.model.ItemKind
import app.mmbbs.lehrerdashboard.domain.model.ItemSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class DashboardViewModelTest {
    private val clock = Clock.fixed(Instant.parse("2026-10-11T07:42:00Z"), ZoneOffset.UTC)

    @Test
    fun `content state uses injected clock and repository`() {
        val item = DashboardItem("1", "Titel", "Text", ItemKind.MEMO, ItemSource.LOCAL)
        val state = DashboardViewModel(DashboardRepository { listOf(item) }, clock).uiState.value as DashboardUiState.Content
        assertEquals(LocalDate.of(2026, 10, 11), state.date)
        assertEquals(clock.instant(), state.lastSync)
        assertEquals(listOf(item), state.items)
    }

    @Test
    fun `empty repository creates empty state`() {
        assertTrue(DashboardViewModel(DashboardRepository { emptyList() }, clock).uiState.value is DashboardUiState.Empty)
    }

    @Test
    fun `repository exception creates error state`() {
        val state = DashboardViewModel(DashboardRepository { error("Nicht erreichbar") }, clock).uiState.value
        assertEquals("Nicht erreichbar", (state as DashboardUiState.Error).message)
    }
}
