package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.SweetShop
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    shops: List<SweetShop>,
    defaultShopId: String,
    onDismiss: () -> Unit,
    onConfirm: (shopId: String, category: String, description: String, amount: Double, loggedBy: String) -> Unit
) {
    var selectedShopId by remember { mutableStateOf(defaultShopId) }
    var isCustomCategoryMode by remember { mutableStateOf(false) }
    var selectedPresetCategory by remember { mutableStateOf("Pure Desi Ghee") }
    var customCategoryText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var loggedBy by remember { mutableStateOf("") }

    val presetCategories = listOf(
        "Pure Desi Ghee",
        "Fresh Mawa / Khoya",
        "Cashews & Pistachios",
        "Sugar & Kashmiri Kesar",
        "Custom Packaging Boxes",
        "Commercial Electricity",
        "Shop Rent & Lease",
        "Gas & LPG Cylinders",
        "Staff Food & Refreshment",
        "Vehicle Transport & Logistics"
    )

    val finalCategory = if (isCustomCategoryMode) customCategoryText.trim() else selectedPresetCategory

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SlateCardSurfaceElevated,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .border(1.dp, SlateBorderLight, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = CrimsonExpense, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Log Shop Expense",
                            color = CrimsonExpense,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Shop Selector
                Text("Branch Location:", color = TextSecondary, fontSize = 12.sp)
                Spacer(Modifier.height(4.dp))
                var shopMenuExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = shopMenuExpanded,
                    onExpandedChange = { shopMenuExpanded = it }
                ) {
                    val currentShopName = shops.find { it.id == selectedShopId }?.name ?: "Select Shop"
                    OutlinedTextField(
                        value = currentShopName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = shopMenuExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CrimsonExpense,
                            unfocusedBorderColor = SlateBorder
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = shopMenuExpanded,
                        onDismissRequest = { shopMenuExpanded = false }
                    ) {
                        shops.forEach { shop ->
                            DropdownMenuItem(
                                text = { Text("${shop.name} (${shop.code})") },
                                onClick = {
                                    selectedShopId = shop.id
                                    shopMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Expense Category with Writer Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Expense Category:", color = TextSecondary, fontSize = 12.sp)

                    // Writer Icon Toggle Button
                    OutlinedButton(
                        onClick = { isCustomCategoryMode = !isCustomCategoryMode },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isCustomCategoryMode) CyberCyan.copy(alpha = 0.15f) else SlateCardSurface,
                            contentColor = if (isCustomCategoryMode) CyberCyan else TextSecondary
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCustomCategoryMode) CyberCyan else SlateBorder
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier
                            .height(28.dp)
                            .testTag("toggle_custom_category_writer_button")
                    ) {
                        Icon(
                            if (isCustomCategoryMode) Icons.Default.FormatListBulleted else Icons.Default.DriveFileRenameOutline,
                            contentDescription = "Writer Icon",
                            modifier = Modifier.size(13.dp),
                            tint = if (isCustomCategoryMode) CyberCyan else NeonGold
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            if (isCustomCategoryMode) "Preset List" else "Write Custom",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))

                if (isCustomCategoryMode) {
                    // Writer mode: input field with writer icon
                    OutlinedTextField(
                        value = customCategoryText,
                        onValueChange = { customCategoryText = it },
                        label = { Text("Write Category Name") },
                        placeholder = { Text("e.g. Gas Cylinder Refill, Festival Lights, Diesel...") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.DriveFileRenameOutline,
                                contentDescription = "Writer Icon",
                                tint = CyberCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_category_writer_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = SlateBorder
                        )
                    )
                } else {
                    // Dropdown mode
                    var catMenuExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = catMenuExpanded,
                        onExpandedChange = { catMenuExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedPresetCategory,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catMenuExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CrimsonExpense,
                                unfocusedBorderColor = SlateBorder
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = catMenuExpanded,
                            onDismissRequest = { catMenuExpanded = false }
                        ) {
                            presetCategories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        selectedPresetCategory = cat
                                        catMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Amount (₹)") },
                    placeholder = { Text("e.g. 15400") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("expense_amount_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CrimsonExpense,
                        unfocusedBorderColor = SlateBorder
                    )
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description / Vendor Note") },
                    placeholder = { Text("e.g. 30 Tins Amul Ghee Invoice #884") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("expense_desc_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CrimsonExpense,
                        unfocusedBorderColor = SlateBorder
                    )
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = loggedBy,
                    onValueChange = { loggedBy = it },
                    label = { Text("Logged By (Staff Name)") },
                    placeholder = { Text("e.g. Store Manager") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CrimsonExpense,
                        unfocusedBorderColor = SlateBorder
                    )
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        val amount = amountStr.toDoubleOrNull() ?: 0.0
                        val catToSave = if (finalCategory.isNotBlank()) finalCategory else "General Expense"
                        if (amount > 0 && description.isNotBlank()) {
                            onConfirm(selectedShopId, catToSave, description, amount, loggedBy)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_expense_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonExpense, contentColor = TextPrimary)
                ) {
                    Text("Record Expense (Deduct from P&L)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
