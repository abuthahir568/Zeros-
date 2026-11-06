package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExpenseCategory
import com.example.ui.theme.*
import com.example.viewmodel.SweetPosUiState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExpensesInventoryView(
    uiState: SweetPosUiState,
    onAddExpenseClick: () -> Unit
) {
    val timeFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    val rawMaterialTotal = uiState.expenses.filter {
        it.category in listOf(
            ExpenseCategory.RAW_MATERIAL_GHEE,
            ExpenseCategory.RAW_MATERIAL_MAWA,
            ExpenseCategory.RAW_MATERIAL_DRYFRUITS,
            ExpenseCategory.RAW_MATERIAL_SUGAR_KESAR
        )
    }.sumOf { it.amount }

    val utilitiesAndRentTotal = uiState.expenses.filter {
        it.category in listOf(
            ExpenseCategory.PACKAGING_BOXES,
            ExpenseCategory.ELECTRICITY_UTILITIES,
            ExpenseCategory.SHOP_RENT_MAINTENANCE
        )
    }.sumOf { it.amount }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateDarkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Summary Header Card
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
                                "TOTAL LOGGED EXPENSES",
                                color = CrimsonExpense,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "₹${String.format("%,.0f", uiState.totalExpenses)}",
                                color = TextPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Button(
                            onClick = onAddExpenseClick,
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonExpense, contentColor = TextPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("add_expense_fab")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Log Expense", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    HorizontalDivider(color = SlateBorder, modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Raw Materials (Ghee/Mawa/Kaju)", color = TextMuted, fontSize = 11.sp)
                            Text("₹${String.format("%,.0f", rawMaterialTotal)}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Column {
                            Text("Packaging & Utilities", color = TextMuted, fontSize = 11.sp)
                            Text("₹${String.format("%,.0f", utilitiesAndRentTotal)}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    // Notice banner explaining live P&L link
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SlateCardSurface, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "All logged expenses are deducted dynamically from the Admin Live Dashboard P&L.",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        item {
            Text(
                "RECENT SHOP & INVENTORY EXPENSES",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 0.5.sp
            )
        }

        items(uiState.expenses) { exp ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SlateBorder, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SlateCardSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                exp.shopName,
                                color = NeonGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Text(
                            exp.description,
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text(
                            "Logged by ${exp.loggedBy} • ${timeFormat.format(Date(exp.timestamp))}",
                            color = TextMuted,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "-₹${String.format("%,.0f", exp.amount)}",
                            color = CrimsonExpense,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .background(SlateBorderLight, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                exp.category.name.replace("RAW_MATERIAL_", "").replace("_", " "),
                                color = TextSecondary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
