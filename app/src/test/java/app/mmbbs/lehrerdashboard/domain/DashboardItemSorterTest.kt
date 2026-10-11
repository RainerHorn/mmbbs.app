package app.mmbbs.lehrerdashboard.domain

import app.mmbbs.lehrerdashboard.domain.model.DashboardItem
import app.mmbbs.lehrerdashboard.domain.model.ItemKind
import app.mmbbs.lehrerdashboard.domain.model.ItemSource
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class DashboardItemSorterTest {
    @Test
    fun `merge sorts timed items and removes duplicate ids`() {
        val later = item("later", Instant.parse("2026-10-11T10:00:00Z"))
        val earlier = item("earlier", Instant.parse("2026-10-11T08:00:00Z"))
        val result = DashboardItemSorter.merge(listOf(later), listOf(earlier, later.copy(title = "Duplicate")))
        assertEquals(listOf("earlier", "later"), result.map(DashboardItem::id))
        assertEquals("later", result.last().title)
    }

    @Test
    fun `untimed items follow timed items in stable kind order`() {
        val result = DashboardItemSorter.merge(
            listOf(item("memo", kind = ItemKind.MEMO), item("timed", Instant.parse("2026-10-11T08:00:00Z")), item("task", kind = ItemKind.TASK))
        )
        assertEquals(listOf("timed", "task", "memo"), result.map(DashboardItem::id))
    }

    private fun item(id: String, startsAt: Instant? = null, kind: ItemKind = ItemKind.LESSON) =
        DashboardItem(id, id, "", kind, ItemSource.LOCAL, startsAt = startsAt)
}
