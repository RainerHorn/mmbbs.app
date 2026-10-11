package app.mmbbs.lehrerdashboard.ui

import app.mmbbs.lehrerdashboard.data.DashboardRepository
import app.mmbbs.lehrerdashboard.domain.model.DashboardItem
import app.mmbbs.lehrerdashboard.domain.model.ItemKind
import app.mmbbs.lehrerdashboard.domain.model.ItemSource
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {
    private val clock = Clock.fixed(Instant.parse("2026-10-11T07:42:00Z"), ZoneOffset.UTC)

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `content state uses injected clock and repository`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val item = DashboardItem("1", "Titel", "Text", ItemKind.MEMO, ItemSource.LOCAL)
        val viewModel = DashboardViewModel(TestRepository(listOf(item)), clock)
        advanceUntilIdle()

        val state = viewModel.uiState.value as DashboardUiState.Content
        assertEquals(LocalDate.of(2026, 10, 11), state.date)
        assertEquals(clock.instant(), state.lastSync)
        assertEquals(listOf(item), state.items)
    }

    @Test
    fun `empty repository creates empty state`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val viewModel = DashboardViewModel(TestRepository(), clock)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is DashboardUiState.Empty)
    }

    @Test
    fun `repository exception creates error state`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val viewModel = DashboardViewModel(TestRepository(error = "Nicht erreichbar"), clock)
        advanceUntilIdle()
        assertEquals("Nicht erreichbar", (viewModel.uiState.value as DashboardUiState.Error).message)
    }

    @Test
    fun `save memo persists and refreshes`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val repository = TestRepository()
        val viewModel = DashboardViewModel(repository, clock)
        advanceUntilIdle()

        viewModel.saveMemo(null, " Neu ", " Text ")
        advanceUntilIdle()

        assertEquals(Triple(null, " Neu ", " Text "), repository.savedMemo)
        assertEquals(2, repository.loadCount)
    }

    private class TestRepository(
        private val items: List<DashboardItem> = emptyList(),
        private val error: String? = null
    ) : DashboardRepository {
        var savedMemo: Triple<String?, String, String>? = null
        var loadCount = 0

        override suspend fun getItems(date: LocalDate): List<DashboardItem> {
            loadCount++
            error?.let { throw IllegalStateException(it) }
            return items
        }

        override suspend fun saveMemo(id: String?, title: String, body: String) {
            savedMemo = Triple(id, title, body)
        }

        override suspend fun deleteMemo(id: String) = Unit
    }
}
