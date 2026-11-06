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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Bill
import com.example.model.SweetShop
import com.example.model.TimePeriodFilter
import com.example.ui.theme.*
import com.example.viewmodel.SweetPosUiState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminDashboardView(
    uiState: SweetPosUiState,
    onTimeFilterSelected: (TimePeriodFilter) -> Unit,
    onSimulateBranchSale: () -> Unit,
    onSelectBill: (Bill) -> Unit,
    onAddExpenseClick: () -> Unit,
    onAddShopClick: () -> Unit,
    onDeleteShop: (String) -> Unit
) {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateDarkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        // Time Period Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TimePeriodFilter.values().forEach { filter ->
                    val isSelected = uiState.timeFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { onTimeFilterSelected(filter) },
                        label = {
                            Text(
                                when (filter) {
                                    TimePeriodFilter.TODAY -> "Today's Total"
                                    TimePeriodFilter.THIS_WEEK -> "This Week"
                                    TimePeriodFilter.THIS_MONTH -> "This Month"
                                },
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) SlateDarkBackground else TextSecondary
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonGold,
                            containerColor = SlateCardSurface
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

        // Main Revenue & P&L Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        brush = Brush.verticalGradient(
                            listOf(NeonGold.copy(alpha = 0.6f), SlateBorder)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateCardSurfaceElevated)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "CHAIN-WIDE TOTAL REVENUE",
                        color = NeonGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "₹${String.format("%,.0f", uiState.totalRevenue)}",
                            color = TextPrimary,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(EmeraldPaid.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = EmeraldPaid, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("+18.4% Live", color = EmeraldPaid, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    HorizontalDivider(color = SlateBorder, modifier = Modifier.padding(vertical = 14.dp))

                    // Profit & Loss Formula Breakdown
                    Text(
                        "REAL-TIME P&L BREAKDOWN",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Sales", color = TextMuted, fontSize = 11.sp)
                            Text("₹${String.format("%,.0f", uiState.totalRevenue)}", color = EmeraldPaid, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Column {
                            Text("Expenses", color = TextMuted, fontSize = 11.sp)
                            Text("-₹${String.format("%,.0f", uiState.totalExpenses)}", color = CrimsonExpense, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Column {
                            Text("Salaries Paid", color = TextMuted, fontSize = 11.sp)
                            Text("-₹${String.format("%,.0f", uiState.totalSalariesPaid)}", color = CrimsonExpense, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // Net Profit banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        if (uiState.netProfitLoss >= 0) EmeraldPaid.copy(alpha = 0.2f) else CrimsonExpense.copy(alpha = 0.2f),
                                        SlateCardSurface
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                1.dp,
                                if (uiState.netProfitLoss >= 0) EmeraldPaid.copy(alpha = 0.5f) else CrimsonExpense.copy(alpha = 0.5f),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (uiState.netProfitLoss >= 0) Icons.Default.MonetizationOn else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (uiState.netProfitLoss >= 0) EmeraldPaid else CrimsonExpense,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "NET CHAIN PROFIT",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Text(
                            text = "₹${String.format("%,.0f", uiState.netProfitLoss)}",
                            color = if (uiState.netProfitLoss >= 0) EmeraldLight else CrimsonExpense,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        // Sweet Shops Performance Header with Add Shop Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "CHAIN SHOPS NETWORK (${uiState.shops.size})",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        "Add or manage sweet branches",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                // Add Shop Button
                Button(
                    onClick = onAddShopClick,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGold, contentColor = SlateDarkBackground),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("add_shop_button")
                ) {
                    Icon(Icons.Default.AddBusiness, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Add Shop", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Branch Grid Cards with Delete Shop Action
        items(uiState.shopsWithLiveSales) { shop ->
            val progress = (shop.todaySales / shop.dailyTarget).toFloat().coerceIn(0f, 1f)
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .background(SlateBorderLight, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(shop.code, color = NeonGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    shop.name,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Text(
                                "${shop.locality} • Mgr: ${shop.manager}",
                                color = TextMuted,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Text(
                                "Ph: ${shop.phone}",
                                color = CyberCyan,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 1.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.Top) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "₹${String.format("%,.0f", shop.todaySales)}",
                                    color = NeonGold,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                                Text(
                                    "${shop.todayBillsCount} bills today",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(Modifier.width(6.dp))

                            // Delete Shop Icon Button
                            IconButton(
                                onClick = { onDeleteShop(shop.id) },
                                modifier = Modifier
                                    .size(32.dp)
                                    .testTag("delete_shop_${shop.id}")
                            ) {
                                Icon(
                                    Icons.Default.DeleteOutline,
                                    contentDescription = "Delete Shop",
                                    tint = CrimsonExpense.copy(alpha = 0.8f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    // Target progress bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Target: ₹${String.format("%,.0f", shop.dailyTarget)}",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                        Text(
                            "${(progress * 100).toInt()}% Achieved",
                            color = if (progress >= 0.8f) EmeraldPaid else CyberCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (progress >= 0.8f) EmeraldPaid else CyberCyan,
                        trackColor = SlateBorder
                    )
                }
            }
        }

        // Live Feed of Incoming Bills
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "LIVE INCOMING BILL STREAM",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${uiState.bills.size} Bills",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = onSimulateBranchSale,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberCyan.copy(alpha = 0.15f),
                            contentColor = CyberCyan
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(26.dp).testTag("simulate_sale_button")
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(Modifier.width(3.dp))
                        Text("Simulate", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        items(uiState.bills.take(8)) { bill ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectBill(bill) }
                    .border(1.dp, SlateBorder, RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = SlateCardSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                bill.shopName,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                            Spacer(Modifier.width(6.dp))
                            if (bill.isOfflineQueued) {
                                Box(
                                    modifier = Modifier
                                        .background(AmberPending.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text("OFFLINE", color = AmberPending, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Text(
                            "Inv #${bill.billNumber} • ${timeFormat.format(Date(bill.timestamp))} • ${bill.items.size} item(s)",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "₹${String.format("%.2f", bill.grandTotal)}",
                            color = NeonGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            bill.paymentMethod.name.replace("_", " "),
                            color = CyberCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
