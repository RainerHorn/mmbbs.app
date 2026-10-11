package app.mmbbs.lehrerdashboard.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MemoEntity::class, CachedDashboardItemEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
    abstract fun cachedDashboardItemDao(): CachedDashboardItemDao
}
