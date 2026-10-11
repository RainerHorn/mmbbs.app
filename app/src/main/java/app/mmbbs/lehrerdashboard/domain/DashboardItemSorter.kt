package app.mmbbs.lehrerdashboard.domain

import app.mmbbs.lehrerdashboard.domain.model.DashboardItem
import app.mmbbs.lehrerdashboard.domain.model.ItemKind

object DashboardItemSorter {
    private val untimedKindOrder = mapOf(
        ItemKind.TASK to 0,
        ItemKind.EMAIL to 1,
        ItemKind.MEMO to 2,
        ItemKind.LESSON to 3,
        ItemKind.APPOINTMENT to 4
    )

    fun merge(vararg sources: List<DashboardItem>): List<DashboardItem> =
        sources.asSequence()
            .flatten()
            .distinctBy(DashboardItem::id)
            .sortedWith(
                compareBy<DashboardItem> { it.startsAt == null }
                    .thenBy { it.startsAt }
                    .thenBy { untimedKindOrder.getValue(it.kind) }
                    .thenBy(DashboardItem::id)
            )
            .toList()
}
