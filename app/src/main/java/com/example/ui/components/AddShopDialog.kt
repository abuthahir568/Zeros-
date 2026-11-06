package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun AddShopDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, code: String, locality: String, phone: String, manager: String, dailyTarget: Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("SW-11") }
    var locality by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+91 11 ") }
    var manager by remember { mutableStateOf("") }
    var targetStr by remember { mutableStateOf("120000") }

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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Storefront, contentDescription = null, tint = NeonGold, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Add New Sweet Shop",
                            color = NeonGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Shop Name") },
                    placeholder = { Text("e.g. Royal Sweets Extension") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("shop_name_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGold,
                        unfocusedBorderColor = SlateBorder
                    )
                )

                Spacer(Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("Branch Code") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("shop_code_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGold,
                            unfocusedBorderColor = SlateBorder
                        )
                    )

                    OutlinedTextField(
                        value = targetStr,
                        onValueChange = { targetStr = it.filter { char -> char.isDigit() } },
                        label = { Text("Daily Target (₹)") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1.4f)
                            .testTag("shop_target_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGold,
                            unfocusedBorderColor = SlateBorder
                        )
                    )
                }

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = locality,
                    onValueChange = { locality = it },
                    label = { Text("Locality / Market Area") },
                    placeholder = { Text("e.g. Rohini Sector 14") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("shop_locality_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGold,
                        unfocusedBorderColor = SlateBorder
                    )
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = manager,
                    onValueChange = { manager = it },
                    label = { Text("Shop Manager Name") },
                    placeholder = { Text("e.g. Rajesh Kumar") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("shop_manager_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGold,
                        unfocusedBorderColor = SlateBorder
                    )
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Contact Phone") },
                    placeholder = { Text("+91 11 2345 6789") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("shop_phone_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGold,
                        unfocusedBorderColor = SlateBorder
                    )
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        val target = targetStr.toDoubleOrNull() ?: 100000.0
                        if (name.isNotBlank()) {
                            onConfirm(name, code, locality, phone, manager, target)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_shop_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGold, contentColor = SlateDarkBackground)
                ) {
                    Text("Add Branch to Chain Network", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
