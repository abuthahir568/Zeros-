package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun SettingsDialog(
    currentRole: UserRole,
    isOfflineMode: Boolean,
    totalShopsCount: Int,
    onDismiss: () -> Unit,
    onToggleRole: () -> Unit,
    onToggleOffline: () -> Unit,
    onReplaySplash: () -> Unit,
    onRequestResetToZero: () -> Unit
) {
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
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = NeonGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "System Settings",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Text(
                    "Zeros Multi-Branch POS & Chain Console",
                    color = TextMuted,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                )

                HorizontalDivider(color = SlateBorder)

                Spacer(Modifier.height(14.dp))

                // Section: App Open Animation
                Text(
                    "DISPLAY & ANIMATION",
                    color = NeonGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onDismiss()
                            onReplaySplash()
                        }
                        .border(1.dp, SlateBorder, RoundedCornerShape(10.dp)),
                    colors = CardDefaults.cardColors(containerColor = SlateCardSurface),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayCircleOutline, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text("Replay App Open Animation", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("Play the cinematic neon brain intro", color = TextMuted, fontSize = 11.sp)
                            }
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Section: Access Mode
                Text(
                    "ACCESS CONTROL & ROLE",
                    color = NeonGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleRole() }
                        .border(1.dp, SlateBorder, RoundedCornerShape(10.dp)),
                    colors = CardDefaults.cardColors(containerColor = SlateCardSurface),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (currentRole == UserRole.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.PointOfSale,
                                contentDescription = null,
                                tint = if (currentRole == UserRole.ADMIN) NeonGold else CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text(
                                    "Current Mode: ${if (currentRole == UserRole.ADMIN) "HQ Admin (All Shops)" else "Shop Staff Terminal"}",
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                                Text("Tap to switch role access", color = TextMuted, fontSize = 11.sp)
                            }
                        }
                        Switch(
                            checked = currentRole == UserRole.ADMIN,
                            onCheckedChange = { onToggleRole() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NeonGold,
                                checkedTrackColor = NeonGold.copy(alpha = 0.3f),
                                uncheckedThumbColor = CyberCyan,
                                uncheckedTrackColor = CyberCyan.copy(alpha = 0.3f)
                            )
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Section: Network & Persistence
                Text(
                    "DATABASE & NETWORK",
                    color = NeonGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleOffline() }
                        .border(1.dp, SlateBorder, RoundedCornerShape(10.dp)),
                    colors = CardDefaults.cardColors(containerColor = SlateCardSurface),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (isOfflineMode) Icons.Default.CloudOff else Icons.Default.CloudSync,
                                contentDescription = null,
                                tint = if (isOfflineMode) AmberPending else EmeraldPaid,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text(
                                    if (isOfflineMode) "Offline Mode (Local Cache)" else "Online Cloud Auto-Sync",
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                                Text("$totalShopsCount Branches Connected", color = TextMuted, fontSize = 11.sp)
                            }
                        }
                        Switch(
                            checked = isOfflineMode,
                            onCheckedChange = { onToggleOffline() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = AmberPending,
                                checkedTrackColor = AmberPending.copy(alpha = 0.3f),
                                uncheckedThumbColor = EmeraldPaid,
                                uncheckedTrackColor = EmeraldPaid.copy(alpha = 0.3f)
                            )
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                HorizontalDivider(color = SlateBorder)

                Spacer(Modifier.height(16.dp))

                // DANGER ZONE: Reset to ZERO
                Text(
                    "DANGER ZONE / DATA RESET",
                    color = CrimsonExpense,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CrimsonExpense.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                    colors = CardDefaults.cardColors(containerColor = CrimsonExpense.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.RestartAlt, contentDescription = null, tint = CrimsonExpense, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Restart Entire App to ZERO",
                                color = CrimsonExpense,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Text(
                            "Wipes today's sales to ₹0 across all shops, clears bills, and resets all expenses back to clean zero.",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        Button(
                            onClick = {
                                onDismiss()
                                onRequestResetToZero()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CrimsonExpense,
                                contentColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("settings_reset_to_zero_button")
                        ) {
                            Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Reset All Data to ZERO", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
