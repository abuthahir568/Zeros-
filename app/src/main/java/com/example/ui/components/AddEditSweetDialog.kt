package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.SweetItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSweetDialog(
    existingSweet: SweetItem? = null,
    onDismiss: () -> Unit,
    onSaveNew: (name: String, category: String, pricePerKg: Double, stockKg: Double, description: String) -> Unit,
    onUpdatePrice: (sweetId: String, newPrice: Double) -> Unit
) {
    val isEditMode = existingSweet != null

    var name by remember { mutableStateOf(existingSweet?.name ?: "") }
    var category by remember { mutableStateOf(existingSweet?.category ?: "Kaju / Dry Fruit") }
    var priceStr by remember { mutableStateOf(existingSweet?.pricePerKg?.toInt()?.toString() ?: "850") }
    var stockStr by remember { mutableStateOf(existingSweet?.stockKg?.toInt()?.toString() ?: "50") }
    var description by remember { mutableStateOf(existingSweet?.description ?: "") }

    val categories = listOf("Kaju / Dry Fruit", "Pure Desi Ghee", "Mawa Sweets", "Syrup & Ras", "Namkeen & Snacks")

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
                        Icon(Icons.Default.Sell, contentDescription = null, tint = NeonGold, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (isEditMode) "Price Making / Edit Price" else "Add Sweet Food Item",
                            color = NeonGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { if (!isEditMode) name = it },
                    readOnly = isEditMode,
                    label = { Text("Sweet / Food Item Name") },
                    placeholder = { Text("e.g. Shahi Malai Peda") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sweet_name_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = SlateBorder
                    )
                )

                Spacer(Modifier.height(12.dp))

                // Price making field
                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Price Per Kg (₹/kg)") },
                    placeholder = { Text("e.g. 950") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sweet_price_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGold,
                        unfocusedBorderColor = SlateBorder
                    )
                )

                if (!isEditMode) {
                    Spacer(Modifier.height(12.dp))

                    Text("Food Category:", color = TextSecondary, fontSize = 12.sp)
                    Spacer(Modifier.height(4.dp))
                    var catMenuExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = catMenuExpanded,
                        onExpandedChange = { catMenuExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catMenuExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = SlateBorder
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = catMenuExpanded,
                            onDismissRequest = { catMenuExpanded = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
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
                        value = stockStr,
                        onValueChange = { stockStr = it.filter { char -> char.isDigit() || char == '.' } },
                        label = { Text("Initial Stock (in Kg)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = SlateBorder
                        )
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description & Ingredients") },
                        placeholder = { Text("e.g. Pure mawa with elaichi and silver leaf") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = SlateBorder
                        )
                    )
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        val price = priceStr.toDoubleOrNull() ?: 500.0
                        if (isEditMode && existingSweet != null) {
                            onUpdatePrice(existingSweet.id, price)
                        } else {
                            val stock = stockStr.toDoubleOrNull() ?: 50.0
                            if (name.isNotBlank()) {
                                onSaveNew(name, category, price, stock, description)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_sweet_price_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGold, contentColor = SlateDarkBackground)
                ) {
                    Text(
                        if (isEditMode) "Save New Price (₹$priceStr/kg)" else "Create Sweet & Set Price",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
