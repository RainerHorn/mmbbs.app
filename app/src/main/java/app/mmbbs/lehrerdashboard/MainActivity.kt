package app.mmbbs.lehrerdashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import app.mmbbs.lehrerdashboard.ui.DashboardApp
import app.mmbbs.lehrerdashboard.ui.DashboardViewModel
import app.mmbbs.lehrerdashboard.ui.theme.LehrerDashboardTheme

class MainActivity : ComponentActivity() {
    private val dashboardViewModel: DashboardViewModel by viewModels {
        DashboardViewModel.Factory((application as LehrerDashboardApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LehrerDashboardTheme {
                DashboardApp(dashboardViewModel)
            }
        }
    }
}
