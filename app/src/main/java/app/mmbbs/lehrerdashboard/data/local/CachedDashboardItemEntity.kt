package app.mmbbs.lehrerdashboard.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_dashboard_items")
data class CachedDashboardItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val kind: String,
    val source: String,
    val status: String,
    val startsAtEpochMillis: Long?,
    val date: String,
    val cachedAtEpochMillis: Long
)
