package app.mmbbs.lehrerdashboard

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import app.mmbbs.lehrerdashboard.data.DashboardRepository
import app.mmbbs.lehrerdashboard.data.FakeDashboardRepository
import app.mmbbs.lehrerdashboard.data.RoomDashboardRepository
import app.mmbbs.lehrerdashboard.data.local.AppDatabase

class LehrerDashboardApplication : Application() {
    private val migration1To2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """CREATE TABLE IF NOT EXISTS `cached_dashboard_items` (
                    `id` TEXT NOT NULL,
                    `title` TEXT NOT NULL,
                    `subtitle` TEXT NOT NULL,
                    `kind` TEXT NOT NULL,
                    `source` TEXT NOT NULL,
                    `status` TEXT NOT NULL,
                    `startsAtEpochMillis` INTEGER,
                    `date` TEXT NOT NULL,
                    `cachedAtEpochMillis` INTEGER NOT NULL,
                    PRIMARY KEY(`id`)
                )""".trimIndent()
            )
        }
    }

    val repository: DashboardRepository by lazy {
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "lehrer-dashboard.db")
            .addMigrations(migration1To2)
            .build()
        RoomDashboardRepository(FakeDashboardRepository(), database.memoDao(), database.cachedDashboardItemDao())
    }
}
