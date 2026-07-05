package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.ui.theme.*

@Composable
fun HomeScreen(viewModel: PulseViewModel, onNavigateToSettings: () -> Unit = {}) {
    val netProfit by viewModel.netProfit.collectAsStateWithLifecycle()
    val earningsGoal by viewModel.earningsGoal.collectAsStateWithLifecycle()
    val goalProgress by viewModel.goalProgress.collectAsStateWithLifecycle()
    val incomes by viewModel.incomes.collectAsStateWithLifecycle()
    val invoices by viewModel.invoices.collectAsStateWithLifecycle()
    val leads by viewModel.leads.collectAsStateWithLifecycle()
    
    val overdueInvoices = invoices.count { it.status == "Overdue" }
    val activeLeads = leads.count { it.status == "Active" }
    val pipelineValue = leads.sumOf { it.value }
    val totalInvoices = invoices.sumOf { it.amount }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Pulse", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Text("BUSINESS HEALTH", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.secondary, letterSpacing = 1.sp)
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                    .clickable(onClick = onNavigateToSettings),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Main Goal Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text("October Net Profit", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                                Text("$${"%.2f".format(netProfit)}", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                            Box(
                                modifier = Modifier
                                    .background(PulseSuccess, shape = CircleShape)
                                    .padding(horizontal = 12.dp, vertical = 4.dp),
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.TrendingUp, contentDescription = "On Pace", tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("ON PACE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Goal: $${"%.2f".format(earningsGoal)}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("${(goalProgress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { goalProgress },
                            modifier = Modifier.fillMaxWidth().height(12.dp).clip(CircleShape),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f),
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("You need $124.50/day remaining to hit target.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f), fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    }
                }
            }

            // Metric Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Invoices
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Description,
                        iconTint = InvoiceIconColor,
                        title = "INVOICES",
                        value = "$${"%.0f".format(totalInvoices)}",
                        badgeText = if (overdueInvoices > 0) "$overdueInvoices OVERDUE" else null,
                        badgeBg = MaterialTheme.colorScheme.errorContainer,
                        badgeColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                    // Pipeline
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Assessment,
                        iconTint = PulsePrimary,
                        title = "PIPELINE",
                        value = "$${"%.0f".format(pipelineValue)}",
                        topRightText = "$activeLeads Active"
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Outreach
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.AlternateEmail,
                        iconTint = OutreachIconColor,
                        title = "OUTREACH",
                        value = "12/20",
                        customTopRight = {
                            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Box(modifier = Modifier.size(6.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
                                Box(modifier = Modifier.size(6.dp).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), CircleShape))
                                Box(modifier = Modifier.size(6.dp).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), CircleShape))
                            }
                        }
                    )
                    // Streak
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.LocalFireDepartment,
                        iconTint = StreakIconColor,
                        title = "DAILY STREAK",
                        value = "5",
                        valueSuffix = "DAYS",
                        topRightText = "BEST: 14",
                        topRightColor = StreakBestText
                    )
                }
            }
            
            // Spacer to push the quick action strip down if needed
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Quick Action Strip
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(16.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape = RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Row(modifier = Modifier.padding(end = 12.dp)) {
                                CircleLetter("M", MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(2.dp))
                                CircleLetter("T", MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(2.dp))
                                CircleLetter("W", MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)
                            }
                            Column {
                                Text("Logged today?", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                                Text("Keep the streak.", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                            }
                        }
                        Button(
                            onClick = { /* TODO */ },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text("LOG WORK", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CircleLetter(letter: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier.size(24.dp).background(bgColor, CircleShape).border(1.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(letter, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = textColor, fontSize = 10.sp)
    }
}

@Composable
fun MetricCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    value: String,
    valueSuffix: String? = null,
    badgeText: String? = null,
    badgeBg: Color = Color.Transparent,
    badgeColor: Color = Color.Unspecified,
    topRightText: String? = null,
    topRightColor: Color = Color.Unspecified,
    customTopRight: (@Composable () -> Unit)? = null
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
                if (badgeText != null) {
                    Box(modifier = Modifier.background(badgeBg, CircleShape).padding(horizontal = 8.dp, vertical = 2.dp)) {
                        Text(badgeText, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = badgeColor)
                    }
                } else if (customTopRight != null) {
                    customTopRight()
                } else if (topRightText != null) {
                    Text(topRightText, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = if (topRightColor == Color.Unspecified) MaterialTheme.colorScheme.secondary else topRightColor)
                }
            }
            Column {
                Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    if (valueSuffix != null) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(valueSuffix, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(bottom = 4.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancesScreen(viewModel: PulseViewModel) {
    val incomes by viewModel.incomes.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }, modifier = Modifier.testTag("add_income_btn")) {
                Icon(Icons.Filled.Add, "Add Income")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            item {
                Text("Income Log", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(incomes) { income ->
                ListItem(
                    headlineContent = { Text(income.note ?: "No note") },
                    trailingContent = { Text("$${"%.2f".format(income.amount)}") }
                )
                HorizontalDivider()
            }
        }
        
        if (showDialog) {
            var amountStr by remember { mutableStateOf("") }
            var note by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add Income") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = amountStr,
                            onValueChange = { amountStr = it },
                            label = { Text("Amount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            label = { Text("Note") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        amountStr.toDoubleOrNull()?.let {
                            viewModel.addIncome(it, note)
                            showDialog = false
                        }
                    }) { Text("Save") }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
fun PipelineScreen(viewModel: PulseViewModel) {
    val leads by viewModel.leads.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, "Add Lead")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            item {
                Text("Pipeline", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(leads) { lead ->
                ListItem(
                    headlineContent = { Text(lead.name) },
                    supportingContent = { Text(lead.status) },
                    trailingContent = { Text("$${"%.2f".format(lead.value)}") }
                )
                HorizontalDivider()
            }
        }
        
        if (showDialog) {
            var name by remember { mutableStateOf("") }
            var valueStr by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add Lead") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Lead Name") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = valueStr,
                            onValueChange = { valueStr = it },
                            label = { Text("Estimated Value") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        valueStr.toDoubleOrNull()?.let {
                            viewModel.addLead(name, it)
                            showDialog = false
                        }
                    }) { Text("Save") }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
fun InvoicesScreen(viewModel: PulseViewModel) {
    val invoices by viewModel.invoices.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, "Add Invoice")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            item {
                Text("Invoices", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(invoices) { invoice ->
                ListItem(
                    headlineContent = { Text("${invoice.number} - ${invoice.client}") },
                    supportingContent = { Text(invoice.status) },
                    trailingContent = { Text("$${"%.2f".format(invoice.amount)}") },
                    modifier = Modifier.clickable {
                        if (invoice.status != "Paid") {
                            viewModel.updateInvoiceStatus(invoice.id, "Paid")
                        }
                    }
                )
                HorizontalDivider()
            }
        }
        
        if (showDialog) {
            var number by remember { mutableStateOf("") }
            var client by remember { mutableStateOf("") }
            var amountStr by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add Invoice") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = number,
                            onValueChange = { number = it },
                            label = { Text("Invoice Number") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = client,
                            onValueChange = { client = it },
                            label = { Text("Client") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = amountStr,
                            onValueChange = { amountStr = it },
                            label = { Text("Amount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        amountStr.toDoubleOrNull()?.let {
                            viewModel.addInvoice(number, client, it, 30, "")
                            showDialog = false
                        }
                    }) { Text("Save") }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
fun ContentScreen(viewModel: PulseViewModel) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Content AI Generation (P1)")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: PulseViewModel, onBack: () -> Unit) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("Appearance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Theme Mode", style = MaterialTheme.typography.bodyMedium)
            
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = themeMode == 0,
                    onClick = { viewModel.setThemeMode(0) },
                    label = { Text("System") }
                )
                FilterChip(
                    selected = themeMode == 1,
                    onClick = { viewModel.setThemeMode(1) },
                    label = { Text("Light") }
                )
                FilterChip(
                    selected = themeMode == 2,
                    onClick = { viewModel.setThemeMode(2) },
                    label = { Text("Dark") }
                )
            }
        }
    }
}

