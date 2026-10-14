package com.example.moneyku.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyku.data.model.SplitMember
import com.example.moneyku.ui.components.FormatUtils
import com.example.moneyku.ui.components.StatusBadge
import java.math.BigDecimal

@Composable
fun SplitBillScreen(
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Equal, 1 = By Item
    var peopleCount by remember { mutableIntStateOf(4) }
    var isTaxAndServiceIncluded by remember { mutableStateOf(true) }
    var showConfirmPaymentDialog by remember { mutableStateOf<SplitMember?>(null) }

    var members by remember {
        mutableStateOf(
            listOf(
                SplitMember("1", "Dimas (You / Host)", amountOwed = BigDecimal("60000"), isPaid = true, paymentMethod = "Paid the main bill"),
                SplitMember("2", "Sarah Amalia", amountOwed = BigDecimal("60000"), isPaid = false),
                SplitMember("3", "Budi Santoso", amountOwed = BigDecimal("60000"), isPaid = true, paymentMethod = "BCA • Expense auto-adjusted"),
                SplitMember("4", "Jessica Tan", amountOwed = BigDecimal("60000"), isPaid = false)
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }

                Text("Split Bill", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))

                IconButton(onClick = onShareClick) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }

        // Equal vs By Item Tab Row
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.clip(RoundedCornerShape(16.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Equal", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("By Item", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }

        // Transaction Summary Header
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Food & Drinks • 8 Oct 2026", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        StatusBadge(status = "Synced from Receipt")
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Lunch at Ranch Market", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Bill", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Rp 240.000", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Your Share", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Rp 60.000", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("% Incl. Tax & Service (10%)", style = MaterialTheme.typography.bodySmall)
                        Switch(
                            checked = isTaxAndServiceIncluded,
                            onCheckedChange = { isTaxAndServiceIncluded = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MaterialTheme.colorScheme.primary)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Stepper for number of people
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Number of People", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (peopleCount > 1) peopleCount-- }) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }
                            Text(peopleCount.toString(), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            IconButton(onClick = { peopleCount++ }) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Share per person:", style = MaterialTheme.typography.labelMedium)
                            Text("Rp 60.000/person", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                        }
                    }
                }
            }
        }

        // Members List Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Members (${members.size})", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text("+ Add from Contacts", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable { })
            }
        }

        // Members Item Rows
        items(members) { member ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(member.name.take(1), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(member.name, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                            if (member.paymentMethod != null) {
                                Text(member.paymentMethod, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(FormatUtils.formatRupiah(member.amountOwed), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))

                        Spacer(modifier = Modifier.height(4.dp))

                        if (member.isPaid) {
                            StatusBadge(status = "Paid")
                        } else {
                            OutlinedButton(
                                onClick = { showConfirmPaymentDialog = member },
                                shape = RoundedCornerShape(999.dp),
                                modifier = Modifier.height(32.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp)
                            ) {
                                Text("Mark as Paid", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // WhatsApp Share & Copy Details
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Outlined.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy Details to Clipboard", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onShareClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Send via WhatsApp", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Confirm Payment Dialog
    showConfirmPaymentDialog?.let { member ->
        AlertDialog(
            onDismissRequest = { showConfirmPaymentDialog = null },
            title = { Text("Confirm Payment", fontWeight = FontWeight.Bold) },
            text = { Text("Has ${member.name} paid their share of ${FormatUtils.formatRupiah(member.amountOwed)}?") },
            confirmButton = {
                Button(
                    onClick = {
                        members = members.map { m ->
                            if (m.id == member.id) m.copy(isPaid = true, paymentMethod = "Received via Transfer") else m
                        }
                        showConfirmPaymentDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Yes, Paid")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmPaymentDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
