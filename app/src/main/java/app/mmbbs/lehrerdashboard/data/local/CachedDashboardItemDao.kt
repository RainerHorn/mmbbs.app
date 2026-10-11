package app.mmbbs.lehrerdashboard.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CachedDashboardItemDao {
    @Query("SELECT * FROM cached_dashboard_items WHERE date = :date ORDER BY startsAtEpochMillis")
    suspend fun getForDate(date: String): List<CachedDashboardItemEntity>

    @Query("DELETE FROM cached_dashboard_items WHERE date = :date")
    suspend fun deleteForDate(date: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CachedDashboardItemEntity>)

    @Transaction
    suspend fun replaceForDate(date: String, items: List<CachedDashboardItemEntity>) {
        deleteForDate(date)
        insertAll(items)
    }
}
