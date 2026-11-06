package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Bill
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReceiptDialog(
    bill: Bill,
    onDismiss: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val formattedTime = dateFormat.format(Date(bill.timestamp))

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
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (bill.isOfflineQueued) {
                            Icon(Icons.Default.CloudOff, contentDescription = null, tint = AmberPending)
                            Spacer(Modifier.width(6.dp))
                            Text("OFFLINE SAVED", color = AmberPending, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        } else {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPaid)
                            Spacer(Modifier.width(6.dp))
                            Text("CLOUD SYNCED", color = EmeraldPaid, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Brand and Shop Name
                Text(
                    text = bill.shopName.uppercase(),
                    color = NeonGold,
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Central Sweet Chain POS",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = "Invoice #${bill.billNumber}",
                    color = CyberCyan,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = formattedTime,
                    color = TextMuted,
                    fontSize = 11.sp
                )

                HorizontalDivider(
                    color = SlateBorder,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                // Item breakdown
                Column(modifier = Modifier.fillMaxWidth()) {
                    bill.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.sweetItem.name,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "${item.quantity} x ${if (item.weightGrams >= 1000) "${item.weightGrams / 1000}kg" else "${item.weightGrams}g"} @ ₹${item.sweetItem.pricePerKg.toInt()}/kg",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = "₹${String.format("%.2f", item.totalPrice)}",
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color = SlateBorder,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                // Totals
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subtotal", color = TextSecondary, fontSize = 13.sp)
                    Text("₹${String.format("%.2f", bill.subtotal)}", color = TextSecondary, fontSize = 13.sp)
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("GST (5%)", color = TextSecondary, fontSize = 13.sp)
                    Text("₹${String.format("%.2f", bill.taxGst)}", color = TextSecondary, fontSize = 13.sp)
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SlateCardSurface, RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "GRAND TOTAL",
                        color = NeonGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        "₹${String.format("%.2f", bill.grandTotal)}",
                        color = NeonGold,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Paid via ${bill.paymentMethod.name.replace("_", " ")} • Cashier: ${bill.cashierName}",
                    color = TextMuted,
                    fontSize = 11.sp
                )

                Spacer(Modifier.height(16.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("receipt_share_button"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Share")
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("receipt_print_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGold, contentColor = SlateDarkBackground)
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Print", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
