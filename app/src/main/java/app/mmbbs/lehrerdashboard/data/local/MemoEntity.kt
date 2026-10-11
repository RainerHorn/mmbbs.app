package app.mmbbs.lehrerdashboard.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memos")
data class MemoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val body: String,
    val createdAtEpochMillis: Long
)
