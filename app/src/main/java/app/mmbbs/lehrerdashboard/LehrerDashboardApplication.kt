package app.mmbbs.lehrerdashboard

import android.app.Application
import androidx.room.Room
import app.mmbbs.lehrerdashboard.data.DashboardRepository
import app.mmbbs.lehrerdashboard.data.FakeDashboardRepository
import app.mmbbs.lehrerdashboard.data.RoomDashboardRepository
import app.mmbbs.lehrerdashboard.data.local.AppDatabase

class LehrerDashboardApplication : Application() {
    val repository: DashboardRepository by lazy {
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "lehrer-dashboard.db").build()
        RoomDashboardRepository(FakeDashboardRepository(), database.memoDao())
    }
}
