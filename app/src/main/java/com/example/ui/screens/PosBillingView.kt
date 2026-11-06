package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*
import com.example.viewmodel.SweetPosUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosBillingView(
    uiState: SweetPosUiState,
    onShopSelected: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onSearchChanged: (String) -> Unit,
    onAddToCart: (SweetItem, Int) -> Unit,
    onUpdateCartQty: (Int, Int) -> Unit,
    onClearCart: () -> Unit,
    onToggleOffline: () -> Unit,
    onSyncOffline: () -> Unit,
    onCheckout: (PaymentMethod, String) -> Unit,
    onAddSweetClick: () -> Unit,
    onEditPriceClick: (SweetItem) -> Unit,
    onDeleteSweetClick: (String) -> Unit
) {
    var showCartSheet by remember { mutableStateOf(false) }
    var selectedPaymentMethod by remember { mutableStateOf(PaymentMethod.UPI_QR) }
    var customerPhone by remember { mutableStateOf("") }

    val categories = remember(uiState.catalog) {
        listOf("All") + uiState.catalog.map { it.category }.distinct()
    }

    val filteredItems = uiState.catalog.filter { item ->
        val matchesCategory = uiState.selectedCategory == "All" || item.category == uiState.selectedCategory
        val matchesSearch = uiState.searchQuery.isBlank() || item.name.contains(uiState.searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Box(modifier = Modifier.fillMaxSize().background(SlateDarkBackground)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Branch Picker & Offline Bar
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SlateBorder, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateCardSurfaceElevated)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "ACTIVE SHOP TERMINAL",
                                    color = NeonGold,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    uiState.currentShop.name,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "${uiState.currentShop.locality} • ${uiState.currentShop.code}",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }

                            // Branch Selector Menu
                            var shopDropdownOpen by remember { mutableStateOf(false) }
                            Box {
                                OutlinedButton(
                                    onClick = { shopDropdownOpen = true },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.testTag("switch_shop_button")
                                ) {
                                    Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("Switch Shop", fontSize = 11.sp)
                                }

                                DropdownMenu(
                                    expanded = shopDropdownOpen,
                                    onDismissRequest = { shopDropdownOpen = false },
                                    modifier = Modifier.background(SlateCardSurfaceElevated)
                                ) {
                                    uiState.shops.forEach { s ->
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(s.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                                    Text("${s.locality} (${s.code})", color = TextSecondary, fontSize = 11.sp)
                                                }
                                            },
                                            onClick = {
                                                onShopSelected(s.id)
                                                shopDropdownOpen = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        HorizontalDivider(color = SlateBorder, modifier = Modifier.padding(vertical = 10.dp))

                        // Offline Mode Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(if (uiState.isOfflineMode) AmberPending else EmeraldPaid)
                                )
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text(
                                        if (uiState.isOfflineMode) "OFFLINE MODE ENABLED" else "ONLINE CLOUD AUTO-SYNC",
                                        color = if (uiState.isOfflineMode) AmberPending else EmeraldPaid,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        if (uiState.isOfflineMode) "${uiState.offlineQueue.size} bill(s) queued locally" else "Bills directly push to Firestore",
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (uiState.offlineQueue.isNotEmpty() && !uiState.isOfflineMode) {
                                    Button(
                                        onClick = onSyncOffline,
                                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPaid, contentColor = SlateDarkBackground),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text("Sync (${uiState.offlineQueue.size})", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(Modifier.width(6.dp))
                                }

                                Switch(
                                    checked = uiState.isOfflineMode,
                                    onCheckedChange = { onToggleOffline() },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = AmberPending,
                                        checkedTrackColor = AmberPending.copy(alpha = 0.3f),
                                        uncheckedThumbColor = EmeraldPaid,
                                        uncheckedTrackColor = EmeraldPaid.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier.testTag("offline_mode_toggle")
                                )
                            }
                        }
                    }
                }
            }

            // Sweets & Foods Search Bar (with search icon)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = onSearchChanged,
                        placeholder = { Text("Search sweets, kaju katli, ladoos...", color = TextMuted, fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search Foods Icon",
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchChanged("") }) {
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
                            .weight(1f)
                            .testTag("search_sweets_input")
                    )

                    // Price Making / Add Food Button with Writer Icon
                    Button(
                        onClick = onAddSweetClick,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGold, contentColor = SlateDarkBackground),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
                        modifier = Modifier.testTag("price_making_button")
                    ) {
                        Icon(Icons.Default.DriveFileRenameOutline, contentDescription = "Write Food Category & Price", modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Price Making", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Category Filter Pills with Writer Icon Shortcut
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    // Quick Action: Write Category
                    item {
                        OutlinedButton(
                            onClick = onAddSweetClick,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = CyberCyan.copy(alpha = 0.12f),
                                contentColor = CyberCyan
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("write_food_category_pill")
                        ) {
                            Icon(Icons.Default.DriveFileRenameOutline, contentDescription = "Write Food Category", modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Write Category", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    items(categories) { category ->
                        val isSelected = uiState.selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCategorySelected(category) },
                            label = { Text(category, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonGold,
                                selectedLabelColor = SlateDarkBackground,
                                containerColor = SlateCardSurface,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) NeonGold else SlateBorder
                            )
                        )
                    }
                }
            }

            // Catalog Items List with Price Making & Delete Sweet Icon
            items(filteredItems) { sweet ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SlateBorder, RoundedCornerShape(12.dp)),
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
                                Text(
                                    sweet.name,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    "${sweet.category} • In stock: ${sweet.stockKg} kg",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                                if (sweet.description.isNotBlank()) {
                                    Text(
                                        sweet.description,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.Top) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "₹${sweet.pricePerKg.toInt()}",
                                        color = NeonGold,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 17.sp
                                    )
                                    Text("per kg", color = TextMuted, fontSize = 10.sp)
                                }

                                Spacer(Modifier.width(4.dp))

                                // Price Making Edit Button
                                IconButton(
                                    onClick = { onEditPriceClick(sweet) },
                                    modifier = Modifier
                                        .size(30.dp)
                                        .testTag("edit_price_${sweet.id}")
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Price Making",
                                        tint = CyberCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Delete Sweet Item Button
                                IconButton(
                                    onClick = { onDeleteSweetClick(sweet.id) },
                                    modifier = Modifier
                                        .size(30.dp)
                                        .testTag("delete_sweet_${sweet.id}")
                                ) {
                                    Icon(
                                        Icons.Default.DeleteOutline,
                                        contentDescription = "Delete Sweet",
                                        tint = CrimsonExpense.copy(alpha = 0.8f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        // Quick Add Weight Buttons (250g, 500g, 1kg, 2kg)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(250 to "250g", 500 to "500g", 1000 to "1kg", 2000 to "2kg").forEach { (grams, label) ->
                                val price = (sweet.pricePerKg * (grams / 1000.0)).toInt()
                                OutlinedButton(
                                    onClick = { onAddToCart(sweet, grams) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("add_${sweet.id}_${label}"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = SlateCardSurfaceElevated,
                                        contentColor = TextPrimary
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
                                    contentPadding = PaddingValues(vertical = 6.dp, horizontal = 2.dp)
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("+ $label", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = CyberCyan)
                                        Text("₹$price", fontSize = 10.sp, color = TextSecondary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Floating Bottom Cart Bar
        AnimatedVisibility(
            visible = uiState.cartItems.isNotEmpty(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NeonGold.copy(alpha = 0.8f), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateCardSurfaceElevated),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "${uiState.cartItems.sumOf { it.quantity }} items",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("• Total with GST", color = TextMuted, fontSize = 11.sp)
                        }
                        Text(
                            "₹${String.format("%.2f", uiState.cartGrandTotal)}",
                            color = NeonGold,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = onClearCart, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Clear", tint = TextMuted)
                        }

                        Button(
                            onClick = { showCartSheet = true },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGold, contentColor = SlateDarkBackground),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("view_cart_button")
                        ) {
                            Icon(Icons.Default.ShoppingCartCheckout, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Review & Bill", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Cart & Checkout Bottom Sheet
        if (showCartSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCartSheet = false },
                containerColor = SlateCardSurfaceElevated,
                scrimColor = Color.Black.copy(alpha = 0.7f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Current Cart Bill", color = NeonGold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("${uiState.currentShop.code} • ${uiState.currentShop.name}", color = TextSecondary, fontSize = 12.sp)
                    }

                    HorizontalDivider(color = SlateBorder, modifier = Modifier.padding(vertical = 10.dp))

                    // Items in cart
                    uiState.cartItems.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.sweetItem.name, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text(
                                    "${if (item.weightGrams >= 1000) "${item.weightGrams / 1000}kg" else "${item.weightGrams}g"} box • ₹${item.sweetItem.pricePerKg.toInt()}/kg",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { onUpdateCartQty(index, -1) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = CyberCyan, modifier = Modifier.size(16.dp))
                                }
                                Text("${item.quantity}", color = TextPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp))
                                IconButton(
                                    onClick = { onUpdateCartQty(index, 1) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase", tint = CyberCyan, modifier = Modifier.size(16.dp))
                                }
                            }

                            Text(
                                "₹${String.format("%.2f", item.totalPrice)}",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.width(64.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = SlateBorder, modifier = Modifier.padding(vertical = 10.dp))

                    // Subtotal & GST
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", color = TextSecondary, fontSize = 12.sp)
                        Text("₹${String.format("%.2f", uiState.cartSubtotal)}", color = TextSecondary, fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("GST (5%)", color = TextSecondary, fontSize = 12.sp)
                        Text("₹${String.format("%.2f", uiState.cartGst)}", color = TextSecondary, fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Grand Total", color = NeonGold, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("₹${String.format("%.2f", uiState.cartGrandTotal)}", color = NeonGold, fontWeight = FontWeight.Black, fontSize = 17.sp)
                    }

                    Spacer(Modifier.height(14.dp))

                    // Customer phone input
                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it },
                        label = { Text("Customer Mobile Number (SMS Bill)") },
                        placeholder = { Text("+91 98765 43210") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = SlateBorder
                        )
                    )

                    Spacer(Modifier.height(12.dp))

                    // Payment Method selector
                    Text("Payment Method:", color = TextSecondary, fontSize = 12.sp)
                    Spacer(Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PaymentMethod.values().forEach { method ->
                            val isSelected = selectedPaymentMethod == method
                            OutlinedButton(
                                onClick = { selectedPaymentMethod = method },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) NeonGold else SlateCardSurface,
                                    contentColor = if (isSelected) SlateDarkBackground else TextPrimary
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NeonGold else SlateBorder)
                            ) {
                                Text(
                                    when (method) {
                                        PaymentMethod.UPI_QR -> "UPI QR"
                                        PaymentMethod.CASH -> "Cash"
                                        PaymentMethod.CARD -> "Card"
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            showCartSheet = false
                            onCheckout(selectedPaymentMethod, customerPhone)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("generate_bill_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGold, contentColor = SlateDarkBackground)
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (uiState.isOfflineMode) "Save Offline Bill (Queue Sync)" else "Generate & Print Bill",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}
