package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.ExpenseCategory
import com.example.model.SweetShop
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    shops: List<SweetShop>,
    defaultShopId: String,
    onDismiss: () -> Unit,
    onConfirm: (shopId: String, category: ExpenseCategory, description: String, amount: Double, loggedBy: String) -> Unit
) {
    var selectedShopId by remember { mutableStateOf(defaultShopId) }
    var category by remember { mutableStateOf(ExpenseCategory.RAW_MATERIAL_GHEE) }
    var description by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var loggedBy by remember { mutableStateOf("") }

    val categoryNames = mapOf(
        ExpenseCategory.RAW_MATERIAL_GHEE to "Desi Ghee (Tin/Drum Purchase)",
        ExpenseCategory.RAW_MATERIAL_MAWA to "Fresh Mawa / Khoya Procurement",
        ExpenseCategory.RAW_MATERIAL_DRYFRUITS to "Cashews / Pistachio / Almonds",
        ExpenseCategory.RAW_MATERIAL_SUGAR_KESAR to "Sugar & Kashmiri Kesar",
        ExpenseCategory.PACKAGING_BOXES to "Custom Sweet Gift Boxes & Paper Bags",
        ExpenseCategory.ELECTRICITY_UTILITIES to "Chillers & Commercial Electricity",
        ExpenseCategory.SHOP_RENT_MAINTENANCE to "Shop Lease & Equipment Service"
    )

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Log Shop Expense",
                        color = CrimsonExpense,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(Modifier.height(14.dp))

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

                Spacer(Modifier.height(12.dp))

                Text("Expense Category:", color = TextSecondary, fontSize = 12.sp)
                Spacer(Modifier.height(4.dp))
                var catMenuExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = catMenuExpanded,
                    onExpandedChange = { catMenuExpanded = it }
                ) {
                    OutlinedTextField(
                        value = categoryNames[category] ?: category.name,
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
                        categoryNames.forEach { (cat, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    category = cat
                                    catMenuExpanded = false
                                }
                            )
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
                        if (amount > 0 && description.isNotBlank()) {
                            onConfirm(selectedShopId, category, description, amount, loggedBy)
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
