package app.mmbbs.lehrerdashboard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Card
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import app.mmbbs.lehrerdashboard.data.FakeDashboardRepository
import app.mmbbs.lehrerdashboard.domain.model.DashboardItem
import app.mmbbs.lehrerdashboard.domain.model.ItemKind
import app.mmbbs.lehrerdashboard.ui.theme.LehrerDashboardTheme
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DashboardApp(viewModel: DashboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    DashboardScreen(
        uiState = uiState,
        onRefresh = viewModel::refresh,
        onSaveMemo = viewModel::saveMemo,
        onDeleteMemo = viewModel::deleteMemo
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreen(
    uiState: DashboardUiState,
    onRefresh: () -> Unit,
    onSaveMemo: (String?, String, String) -> Unit,
    onDeleteMemo: (String) -> Unit
) {
    var editorItem by remember { mutableStateOf<DashboardItem?>(null) }
    var editorOpen by remember { mutableStateOf(false) }
    var detailItem by remember { mutableStateOf<DashboardItem?>(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mein Schultag") },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Aktualisieren")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { editorItem = null; editorOpen = true }) {
                Icon(Icons.Default.Add, contentDescription = "Memo hinzufügen")
            }
        }
    ) { innerPadding ->
        when (uiState) {
            DashboardUiState.Loading -> LoadingState(Modifier.padding(innerPadding))
            is DashboardUiState.Error -> MessageState(uiState.message, Modifier.padding(innerPadding))
            is DashboardUiState.Empty -> MessageState(
                if (uiState.isOffline) "Offline · Für heute sind keine gespeicherten Einträge vorhanden."
                else "Für heute sind keine Einträge vorhanden.",
                Modifier.padding(innerPadding)
            )
            is DashboardUiState.Content -> DashboardContent(
                state = uiState,
                modifier = Modifier.padding(innerPadding),
                onItemClick = { item ->
                    if (item.kind == ItemKind.MEMO) {
                        editorItem = item
                        editorOpen = true
                    } else detailItem = item
                }
            )
        }
    }
    if (editorOpen) {
        MemoEditorDialog(
            item = editorItem,
            onDismiss = { editorOpen = false },
            onSave = { title, body ->
                onSaveMemo(editorItem?.id, title, body)
                editorOpen = false
            },
            onDelete = editorItem?.let { item ->
                { onDeleteMemo(item.id); editorOpen = false }
            }
        )
    }
    detailItem?.let { item ->
        AlertDialog(
            onDismissRequest = { detailItem = null },
            title = { Text(item.title) },
            text = { Text(item.subtitle) },
            confirmButton = { TextButton(onClick = { detailItem = null }) { Text("Schließen") } }
        )
    }
}

@Composable
private fun LoadingState(modifier: Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
}

@Composable
private fun MessageState(message: String, modifier: Modifier) {
    Box(modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Text(message, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun DashboardContent(
    state: DashboardUiState.Content,
    modifier: Modifier = Modifier,
    onItemClick: (DashboardItem) -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d. MMMM", Locale.GERMAN)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.GERMAN)
    val date = state.date.format(dateFormatter).replaceFirstChar { it.uppercase() }
    val zoneId = ZoneId.systemDefault()
    val status = if (state.isRefreshing) "Wird aktualisiert …"
        else "Zuletzt aktualisiert: ${timeFormatter.format(state.lastSync.atZone(zoneId))}"

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(date, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(status, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (state.isOffline) {
                Text("Offline · Gespeicherter Stand", color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.SemiBold)
            }
            state.message?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Spacer(Modifier.height(8.dp))
            SummaryCard(state.items)
        }
        item { Text("Heute", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) }
        items(state.items, key = DashboardItem::id) { item -> DashboardCard(item, zoneId) { onItemClick(item) } }
        item { Spacer(Modifier.height(72.dp)) }
    }
}

@Composable
private fun SummaryCard(items: List<DashboardItem>) {
    val lessons = items.count { it.kind == ItemKind.LESSON }
    val appointments = items.count { it.kind == ItemKind.APPOINTMENT }
    val tasks = items.count { it.kind == ItemKind.TASK }
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
                Text("$lessons Unterricht · $appointments Termin · $tasks Aufgabe")
            }
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun DashboardCard(item: DashboardItem, zoneId: ZoneId, onClick: () -> Unit) {
    Card(modifier = Modifier.clickable(onClick = onClick), shape = RoundedCornerShape(16.dp)) {
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
            Text(item.displayTime(zoneId), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun MemoEditorDialog(
    item: DashboardItem?,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit,
    onDelete: (() -> Unit)?
) {
    var title by remember(item?.id) { mutableStateOf(item?.title.orEmpty()) }
    var body by remember(item?.id) { mutableStateOf(item?.subtitle.orEmpty()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item == null) "Memo hinzufügen" else "Memo bearbeiten") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Titel") }, singleLine = true)
                OutlinedTextField(value = body, onValueChange = { body = it }, label = { Text("Notiz") }, minLines = 3)
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(title, body) }, enabled = title.isNotBlank()) { Text("Speichern") }
        },
        dismissButton = {
            Row {
                onDelete?.let { TextButton(onClick = it) { Text("Löschen") } }
                TextButton(onClick = onDismiss) { Text("Abbrechen") }
            }
        }
    )
}

private val ItemKind.label: String
    get() = when (this) {
        ItemKind.LESSON -> "Unterricht"
        ItemKind.APPOINTMENT -> "Termin"
        ItemKind.TASK -> "Aufgabe"
        ItemKind.EMAIL -> "E-Mail"
        ItemKind.MEMO -> "Memo"
    }

private val ItemKind.icon: ImageVector
    get() = when (this) {
        ItemKind.LESSON -> Icons.Default.School
        ItemKind.APPOINTMENT -> Icons.Default.CalendarMonth
        ItemKind.TASK -> Icons.Default.TaskAlt
        ItemKind.EMAIL -> Icons.Default.Email
        ItemKind.MEMO -> Icons.AutoMirrored.Filled.EventNote
    }

private fun DashboardItem.displayTime(zoneId: ZoneId): String =
    startsAt?.atZone(zoneId)?.format(DateTimeFormatter.ofPattern("HH:mm"))
        ?: if (kind == ItemKind.MEMO) "Memo" else "Offen"

@Preview(showBackground = true)
@Composable
private fun DashboardPreview() {
    val clock = Clock.fixed(Instant.parse("2026-10-11T07:42:00Z"), ZoneId.of("Europe/Berlin"))
    val date = LocalDate.now(clock)
    val state = DashboardUiState.Content(date, FakeDashboardRepository(clock.zone).sampleItems(date), clock.instant())
    LehrerDashboardTheme(dynamicColor = false) {
        DashboardScreen(state, onRefresh = {}, onSaveMemo = { _, _, _ -> }, onDeleteMemo = {})
    }
}
