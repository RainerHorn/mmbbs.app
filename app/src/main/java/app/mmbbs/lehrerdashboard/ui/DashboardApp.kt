package app.mmbbs.lehrerdashboard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mmbbs.lehrerdashboard.ui.theme.LehrerDashboardTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private data class DashboardItem(
    val time: String,
    val title: String,
    val subtitle: String,
    val kind: ItemKind
)

private enum class ItemKind(val label: String, val icon: ImageVector) {
    LESSON("Unterricht", Icons.Default.School),
    APPOINTMENT("Termin", Icons.Default.CalendarMonth),
    TASK("Aufgabe", Icons.Default.TaskAlt),
    EMAIL("E-Mail", Icons.Default.Email),
    MEMO("Memo", Icons.Default.EventNote)
}

private val sampleItems = listOf(
    DashboardItem("08:00", "Mathematik · 8A", "Raum 204 · Lineare Funktionen", ItemKind.LESSON),
    DashboardItem("09:50", "Informatik · 10B", "Raum 118 · Datenbanken", ItemKind.LESSON),
    DashboardItem("11:35", "Fachkonferenz", "Besprechungsraum 2", ItemKind.APPOINTMENT),
    DashboardItem("14:00", "Klassenarbeiten korrigieren", "Noch 12 Arbeiten", ItemKind.TASK),
    DashboardItem("Offen", "3 neue Nachrichten", "Outlook und Teams", ItemKind.EMAIL),
    DashboardItem("Memo", "Elternbrief vorbereiten", "Bis Freitag versenden", ItemKind.MEMO)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardApp() {
    var refreshed by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mein Schultag") },
                actions = {
                    IconButton(onClick = { refreshed = !refreshed }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Aktualisieren")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Default.Add, contentDescription = "Memo hinzufügen")
            }
        }
    ) { innerPadding ->
        DashboardContent(
            modifier = Modifier.padding(innerPadding),
            status = if (refreshed) "Gerade aktualisiert" else "Zuletzt aktualisiert: 07:42"
        )
    }
}

@Composable
private fun DashboardContent(modifier: Modifier = Modifier, status: String) {
    val today = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d. MMMM", Locale.GERMAN))
            .replaceFirstChar { it.uppercase() }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(today, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(status, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            SummaryCard()
        }
        item {
            Text("Heute", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        }
        items(sampleItems) { item -> DashboardCard(item) }
        item { Spacer(Modifier.height(72.dp)) }
    }
}

@Composable
private fun SummaryCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Alles im Blick", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("2 Unterrichtsstunden · 1 Termin · 1 Aufgabe")
            }
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun DashboardCard(item: DashboardItem) {
    Card(shape = RoundedCornerShape(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(item.kind.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.weight(1f)) {
                Text(item.kind.label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(item.subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(item.time, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardPreview() {
    LehrerDashboardTheme(dynamicColor = false) { DashboardApp() }
}
