package app.mmbbs.lehrerdashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.mmbbs.lehrerdashboard.ui.DashboardApp
import app.mmbbs.lehrerdashboard.ui.theme.LehrerDashboardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LehrerDashboardTheme {
                DashboardApp()
            }
        }
    }
}
