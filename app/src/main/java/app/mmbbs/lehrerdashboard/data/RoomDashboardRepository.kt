package app.mmbbs.lehrerdashboard.data

import app.mmbbs.lehrerdashboard.data.local.CachedDashboardItemDao
import app.mmbbs.lehrerdashboard.data.local.CachedDashboardItemEntity
import app.mmbbs.lehrerdashboard.data.local.MemoDao
import app.mmbbs.lehrerdashboard.data.local.MemoEntity
import app.mmbbs.lehrerdashboard.domain.DashboardItemSorter
import app.mmbbs.lehrerdashboard.domain.model.DashboardItem
import app.mmbbs.lehrerdashboard.domain.model.ItemKind
import app.mmbbs.lehrerdashboard.domain.model.ItemSource
import app.mmbbs.lehrerdashboard.domain.model.ItemStatus
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

class RoomDashboardRepository(
    private val remoteData: DashboardRepository,
    private val memoDao: MemoDao,
    private val cacheDao: CachedDashboardItemDao,
    private val clock: Clock = Clock.systemDefaultZone()
) : DashboardRepository {
    override suspend fun getDashboard(date: LocalDate): DashboardSnapshot {
        val remoteSnapshot = runCatching { remoteData.getDashboard(date) }
        val externalItems: List<DashboardItem>
        val isOffline: Boolean

        if (remoteSnapshot.isSuccess) {
            externalItems = remoteSnapshot.getOrThrow().items.filter { it.source != ItemSource.LOCAL }
            cacheDao.replaceForDate(date.toString(), externalItems.map { it.toCacheEntity(date) })
            isOffline = remoteSnapshot.getOrThrow().isOffline
        } else {
            externalItems = cacheDao.getForDate(date.toString()).map { it.toDomain() }
            isOffline = true
        }

        val memos = memoDao.getAll().map { memo ->
            DashboardItem(memo.id, memo.title, memo.body, ItemKind.MEMO, ItemSource.LOCAL)
        }
        return DashboardSnapshot(DashboardItemSorter.merge(externalItems, memos), isOffline)
    }

    override suspend fun saveMemo(id: String?, title: String, body: String) {
        val existing = id?.let { memoDao.findById(it) }
        memoDao.upsert(
            MemoEntity(
                id = id ?: UUID.randomUUID().toString(),
                title = title.trim(),
                body = body.trim(),
                createdAtEpochMillis = existing?.createdAtEpochMillis ?: clock.millis()
            )
        )
    }

    override suspend fun deleteMemo(id: String) {
        memoDao.findById(id)?.let { memoDao.delete(it) }
    }

    private fun DashboardItem.toCacheEntity(date: LocalDate) = CachedDashboardItemEntity(
        id = id,
        title = title,
        subtitle = subtitle,
        kind = kind.name,
        source = source.name,
        status = status.name,
        startsAtEpochMillis = startsAt?.toEpochMilli(),
        date = date.toString(),
        cachedAtEpochMillis = clock.millis()
    )

    private fun CachedDashboardItemEntity.toDomain() = DashboardItem(
        id = id,
        title = title,
        subtitle = subtitle,
        kind = ItemKind.valueOf(kind),
        source = ItemSource.valueOf(source),
        status = ItemStatus.valueOf(status),
        startsAt = startsAtEpochMillis?.let(Instant::ofEpochMilli)
    )
}
