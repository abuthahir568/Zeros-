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
import com.example.model.SweetShop
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeDialog(
    shops: List<SweetShop>,
    onDismiss: () -> Unit,
    onConfirm: (name: String, age: Int, shopId: String, role: String, salary: Double, month: String, phone: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ageStr by remember { mutableStateOf("30") }
    var phone by remember { mutableStateOf("+91 98") }
    var selectedShopId by remember { mutableStateOf(shops.firstOrNull()?.id ?: "shop_01") }
    var role by remember { mutableStateOf("Master Halwai") }
    var salaryStr by remember { mutableStateOf("45000") }
    var month by remember { mutableStateOf("November 2026") }

    val rolesList = listOf("Master Halwai", "Assistant Chef", "Cashier & Billing", "Store Manager", "Packaging Associate")

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
                        "Register Staff Member",
                        color = NeonGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    placeholder = { Text("e.g. Radheshyam Sharma") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("employee_name_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = SlateBorder
                    )
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Phone Number") },
                    placeholder = { Text("+91 98765 43210") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("employee_phone_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = SlateBorder
                    )
                )

                Spacer(Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = ageStr,
                        onValueChange = { ageStr = it.filter { char -> char.isDigit() } },
                        label = { Text("Age") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("employee_age_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = SlateBorder
                        )
                    )

                    OutlinedTextField(
                        value = salaryStr,
                        onValueChange = { salaryStr = it.filter { char -> char.isDigit() } },
                        label = { Text("Salary (₹/mo)") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("employee_salary_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = SlateBorder
                        )
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text("Assigned Sweet Shop:", color = TextSecondary, fontSize = 12.sp)
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
                            focusedBorderColor = CyberCyan,
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

                Text("Staff Designation / Role:", color = TextSecondary, fontSize = 12.sp)
                Spacer(Modifier.height(4.dp))
                var roleMenuExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = roleMenuExpanded,
                    onExpandedChange = { roleMenuExpanded = it }
                ) {
                    OutlinedTextField(
                        value = role,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleMenuExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = SlateBorder
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = roleMenuExpanded,
                        onDismissRequest = { roleMenuExpanded = false }
                    ) {
                        rolesList.forEach { r ->
                            DropdownMenuItem(
                                text = { Text(r) },
                                onClick = {
                                    role = r
                                    roleMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = month,
                    onValueChange = { month = it },
                    label = { Text("Payroll Month") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = SlateBorder
                    )
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        val age = ageStr.toIntOrNull() ?: 28
                        val salary = salaryStr.toDoubleOrNull() ?: 30000.0
                        if (name.isNotBlank()) {
                            onConfirm(name, age, selectedShopId, role, salary, month, phone)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_employee_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGold, contentColor = SlateDarkBackground)
                ) {
                    Text("Register Staff Member", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
