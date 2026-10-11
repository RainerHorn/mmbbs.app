package app.mmbbs.lehrerdashboard.data

import app.mmbbs.lehrerdashboard.data.local.MemoDao
import app.mmbbs.lehrerdashboard.data.local.MemoEntity
import app.mmbbs.lehrerdashboard.domain.DashboardItemSorter
import app.mmbbs.lehrerdashboard.domain.model.DashboardItem
import app.mmbbs.lehrerdashboard.domain.model.ItemKind
import app.mmbbs.lehrerdashboard.domain.model.ItemSource
import java.time.Clock
import java.time.LocalDate
import java.util.UUID

class RoomDashboardRepository(
    private val remoteData: DashboardRepository,
    private val memoDao: MemoDao,
    private val clock: Clock = Clock.systemDefaultZone()
) : DashboardRepository {
    override suspend fun getItems(date: LocalDate): List<DashboardItem> {
        val memos = memoDao.getAll().map { memo ->
            DashboardItem(
                id = memo.id,
                title = memo.title,
                subtitle = memo.body,
                kind = ItemKind.MEMO,
                source = ItemSource.LOCAL
            )
        }
        return DashboardItemSorter.merge(remoteData.getItems(date), memos)
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
}
