package app.mmbbs.lehrerdashboard.domain.model

import java.time.Instant

data class DashboardItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val kind: ItemKind,
    val source: ItemSource,
    val status: ItemStatus = ItemStatus.OPEN,
    val startsAt: Instant? = null
)

enum class ItemKind { LESSON, APPOINTMENT, TASK, EMAIL, MEMO }
enum class ItemSource { WEBUNTIS, MICROSOFT_GRAPH, LOCAL }
enum class ItemStatus { OPEN, CHANGED, COMPLETED, CANCELLED }
