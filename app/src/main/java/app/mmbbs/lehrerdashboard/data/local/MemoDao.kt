package app.mmbbs.lehrerdashboard.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MemoDao {
    @Query("SELECT * FROM memos ORDER BY createdAtEpochMillis DESC")
    suspend fun getAll(): List<MemoEntity>

    @Query("SELECT * FROM memos WHERE id = :id LIMIT 1")
    suspend fun findById(id: String): MemoEntity?

    @Upsert
    suspend fun upsert(memo: MemoEntity)

    @Delete
    suspend fun delete(memo: MemoEntity)
}
