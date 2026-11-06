package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Employee
import com.example.ui.theme.*
import com.example.viewmodel.SweetPosUiState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EmployeePayrollView(
    uiState: SweetPosUiState,
    onToggleSalaryPaid: (String) -> Unit,
    onAddEmployeeClick: () -> Unit,
    onDeleteEmployee: (String) -> Unit,
    onSearchStaffChanged: (String) -> Unit
) {
    var statusFilter by remember { mutableStateOf("All") } // "All", "Pending", "Paid"
    var selectedShopFilter by remember { mutableStateOf("All") }
    val timeFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    val filteredEmployees = uiState.employees.filter { emp ->
        val matchesStatus = when (statusFilter) {
            "Paid" -> emp.isPaid
            "Pending" -> !emp.isPaid
            else -> true
        }
        val matchesShop = selectedShopFilter == "All" || emp.shopId == selectedShopFilter
        val matchesSearch = uiState.staffSearchQuery.isBlank() ||
                emp.name.contains(uiState.staffSearchQuery, ignoreCase = true) ||
                emp.role.contains(uiState.staffSearchQuery, ignoreCase = true) ||
                emp.shopName.contains(uiState.staffSearchQuery, ignoreCase = true) ||
                emp.phone.contains(uiState.staffSearchQuery)

        matchesStatus && matchesShop && matchesSearch
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateDarkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Summary Header Cards
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SlateBorder, RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SlateCardSurfaceElevated)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "CHAIN PAYROLL OVERVIEW",
                                color = NeonGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "Month: November 2026",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = onAddEmployeeClick,
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGold, contentColor = SlateDarkBackground),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("add_employee_fab")
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Add Staff", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    HorizontalDivider(color = SlateBorder, modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Staff", color = TextMuted, fontSize = 11.sp)
                            Text("${uiState.employees.size} Members", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Column {
                            Text("Salaries Paid", color = TextMuted, fontSize = 11.sp)
                            Text("₹${String.format("%,.0f", uiState.totalSalariesPaid)}", color = EmeraldPaid, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Column {
                            Text("Pending Disbursement", color = TextMuted, fontSize = 11.sp)
                            Text("₹${String.format("%,.0f", uiState.totalSalariesPending)}", color = AmberPending, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Staff Searching Icon & Input Field
        item {
            OutlinedTextField(
                value = uiState.staffSearchQuery,
                onValueChange = onSearchStaffChanged,
                placeholder = { Text("Search staff name, role, shop or phone number...", color = TextMuted, fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search Staff Icon",
                        tint = CyberCyan,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (uiState.staffSearchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchStaffChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextSecondary)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SlateCardSurface,
                    unfocusedContainerColor = SlateCardSurface,
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = SlateBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_staff_input")
            )
        }

        // Filter Pills
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "Pending", "Paid").forEach { filter ->
                        val isSelected = statusFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { statusFilter = filter },
                            label = { Text(filter, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = if (filter == "Paid") EmeraldPaid else if (filter == "Pending") AmberPending else NeonGold,
                                selectedLabelColor = SlateDarkBackground,
                                containerColor = SlateCardSurface,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }

                // Shop selector pills
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedShopFilter == "All",
                            onClick = { selectedShopFilter = "All" },
                            label = { Text("All Shops", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberCyan,
                                selectedLabelColor = SlateDarkBackground,
                                containerColor = SlateCardSurface,
                                labelColor = TextSecondary
                            )
                        )
                    }
                    items(uiState.shops) { shop ->
                        val isSelected = selectedShopFilter == shop.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedShopFilter = shop.id },
                            label = { Text(shop.code, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberCyan,
                                selectedLabelColor = SlateDarkBackground,
                                containerColor = SlateCardSurface,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }
            }
        }

        // Employee Cards List with Mobile Number & Delete Staff Icon
        items(filteredEmployees) { emp ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (emp.isPaid) EmeraldPaid.copy(alpha = 0.4f) else AmberPending.copy(alpha = 0.4f),
                        RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SlateCardSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    emp.name,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "(${emp.age} yrs)",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                emp.role,
                                color = NeonGold,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Text(
                                emp.shopName,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )

                            // Prominent Mobile Phone Display
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(
                                    Icons.Default.Phone,
                                    contentDescription = "Staff Mobile Phone",
                                    tint = CyberCyan,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    emp.phone,
                                    color = CyberCyan,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.Top) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "₹${String.format("%,.0f", emp.salaryAmount)}",
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                                Text(
                                    emp.workingMonth,
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }

                            Spacer(Modifier.width(6.dp))

                            // Delete Staff Icon Button
                            IconButton(
                                onClick = { onDeleteEmployee(emp.id) },
                                modifier = Modifier
                                    .size(32.dp)
                                    .testTag("delete_staff_${emp.id}")
                            ) {
                                Icon(
                                    Icons.Default.PersonRemove,
                                    contentDescription = "Delete Staff",
                                    tint = CrimsonExpense.copy(alpha = 0.8f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = SlateBorder, modifier = Modifier.padding(vertical = 10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (emp.isPaid) EmeraldPaid else AmberPending)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                if (emp.isPaid) "PAID" else "PENDING DISBURSEMENT",
                                color = if (emp.isPaid) EmeraldPaid else AmberPending,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            if (emp.isPaid && emp.paidTimestamp != null) {
                                Text(
                                    " • ${timeFormat.format(Date(emp.paidTimestamp))}",
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        // One-tap status toggle button
                        Button(
                            onClick = { onToggleSalaryPaid(emp.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (emp.isPaid) SlateBorder else EmeraldPaid,
                                contentColor = if (emp.isPaid) TextSecondary else SlateDarkBackground
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("toggle_salary_${emp.id}")
                        ) {
                            Icon(
                                if (emp.isPaid) Icons.Default.Undo else Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                if (emp.isPaid) "Mark Pending" else "Mark as Paid",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
