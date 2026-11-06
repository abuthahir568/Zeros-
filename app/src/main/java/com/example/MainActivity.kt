package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.UserRole
import com.example.ui.components.*
import com.example.ui.screens.AdminDashboardView
import com.example.ui.screens.EmployeePayrollView
import com.example.ui.screens.ExpensesInventoryView
import com.example.ui.screens.PosBillingView
import com.example.ui.theme.*
import com.example.viewmodel.SweetPosViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SweetPosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var showSplash by remember { mutableStateOf(true) }

                Crossfade(
                    targetState = showSplash,
                    animationSpec = tween(600),
                    label = "splash_crossfade"
                ) { isSplashVisible ->
                    if (isSplashVisible) {
                        AppOpenSplashAnimation(
                            onAnimationFinished = { showSplash = false }
                        )
                    } else {
                        SweetPosApp(
                            viewModel = viewModel,
                            onReplaySplash = { showSplash = true }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SweetPosApp(
    viewModel: SweetPosViewModel,
    onReplaySplash: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SlateDarkBackground,
        topBar = {
            Column(modifier = Modifier.background(SlateCardSurfaceElevated)) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onReplaySplash() }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF151515))
                                    .border(1.dp, NeonGold.copy(alpha = 0.6f), RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.zeros_logo),
                                    contentDescription = "Zeros Logo",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "Zeros",
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(NeonGold.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 5.dp, vertical = 2.dp)
                                    ) {
                                        Text("POS", color = NeonGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Text(
                                    if (uiState.currentRole == UserRole.ADMIN) "${uiState.shops.size} Branches HQ Multi-Sync" else "Branch Terminal • ${uiState.currentShop.code}",
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    },
                    actions = {
                        // System Settings Icon Button (containing Reset to Zero & System Options)
                        IconButton(
                            onClick = { viewModel.setShowSettingsDialog(true) },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("settings_button")
                        ) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = NeonGold,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Role Switcher Toggle
                        OutlinedButton(
                            onClick = {
                                val nextRole = if (uiState.currentRole == UserRole.ADMIN) UserRole.STAFF else UserRole.ADMIN
                                viewModel.setRole(nextRole)
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (uiState.currentRole == UserRole.ADMIN) NeonGold.copy(alpha = 0.15f) else CyberCyan.copy(alpha = 0.15f),
                                contentColor = if (uiState.currentRole == UserRole.ADMIN) NeonGold else CyberCyan
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (uiState.currentRole == UserRole.ADMIN) NeonGold.copy(alpha = 0.5f) else CyberCyan.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .testTag("role_switcher_button")
                        ) {
                            Icon(
                                if (uiState.currentRole == UserRole.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.PointOfSale,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                if (uiState.currentRole == UserRole.ADMIN) "HQ Admin" else "Shop Staff",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = SlateCardSurfaceElevated
                    )
                )

                // Sync Notification Banner
                AnimatedVisibility(visible = uiState.syncNotification != null) {
                    uiState.syncNotification?.let { msg ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CyberCyan.copy(alpha = 0.15f))
                                .border(1.dp, CyberCyan.copy(alpha = 0.3f))
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CloudSync, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(msg, color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                            IconButton(
                                onClick = { viewModel.clearSyncNotification() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = CyberCyan, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = SlateCardSurfaceElevated,
                contentColor = TextPrimary,
                tonalElevation = 8.dp
            ) {
                val navItems = if (uiState.currentRole == UserRole.ADMIN) {
                    listOf(
                        Triple(0, "Dashboard", Icons.Default.Analytics),
                        Triple(1, "POS", Icons.Default.PointOfSale),
                        Triple(2, "Payroll", Icons.Default.Badge),
                        Triple(3, "Expenses", Icons.Default.ReceiptLong)
                    )
                } else {
                    listOf(
                        Triple(0, "Billing", Icons.Default.PointOfSale),
                        Triple(1, "Expenses", Icons.Default.ReceiptLong),
                        Triple(2, "Staff", Icons.Default.Badge),
                        Triple(3, "HQ View", Icons.Default.Analytics)
                    )
                }

                navItems.forEach { (tabIndex, label, icon) ->
                    val isSelected = uiState.activeNavTab == tabIndex
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (uiState.currentRole == UserRole.STAFF && tabIndex == 3) {
                                viewModel.setRole(UserRole.ADMIN)
                            } else {
                                viewModel.setActiveNavTab(tabIndex)
                            }
                        },
                        icon = {
                            Icon(
                                icon,
                                contentDescription = label,
                                tint = if (isSelected) NeonGold else TextMuted
                            )
                        },
                        label = {
                            Text(
                                label,
                                color = if (isSelected) NeonGold else TextMuted,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = NeonGold.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_$tabIndex")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentRole) {
                UserRole.ADMIN -> {
                    when (uiState.activeNavTab) {
                        0 -> AdminDashboardView(
                            uiState = uiState,
                            onTimeFilterSelected = { viewModel.setTimeFilter(it) },
                            onSimulateBranchSale = { viewModel.simulateIncomingBranchSale() },
                            onSelectBill = { /* active receipt view */ },
                            onAddExpenseClick = { viewModel.setShowAddExpenseDialog(true) },
                            onAddShopClick = { viewModel.setShowAddShopDialog(true) },
                            onDeleteShop = { viewModel.deleteShop(it) }
                        )
                        1 -> PosBillingView(
                            uiState = uiState,
                            onShopSelected = { viewModel.setSelectedShop(it) },
                            onCategorySelected = { viewModel.setSelectedCategory(it) },
                            onSearchChanged = { viewModel.setSearchQuery(it) },
                            onAddToCart = { item, grams -> viewModel.addToCart(item, grams) },
                            onUpdateCartQty = { idx, delta -> viewModel.updateCartItemQuantity(idx, delta) },
                            onClearCart = { viewModel.clearCart() },
                            onToggleOffline = { viewModel.toggleOfflineMode() },
                            onSyncOffline = { viewModel.syncOfflineBills() },
                            onCheckout = { method, phone -> viewModel.generateBill(method, phone) },
                            onAddSweetClick = { viewModel.setShowAddSweetDialog(true) },
                            onEditPriceClick = { viewModel.setSweetToEditPrice(it) },
                            onDeleteSweetClick = { viewModel.deleteSweetItem(it) }
                        )
                        2 -> EmployeePayrollView(
                            uiState = uiState,
                            onToggleSalaryPaid = { viewModel.toggleSalaryStatus(it) },
                            onAddEmployeeClick = { viewModel.setShowAddEmployeeDialog(true) },
                            onDeleteEmployee = { viewModel.deleteEmployee(it) },
                            onSearchStaffChanged = { viewModel.setStaffSearchQuery(it) }
                        )
                        3 -> ExpensesInventoryView(
                            uiState = uiState,
                            onAddExpenseClick = { viewModel.setShowAddExpenseDialog(true) }
                        )
                    }
                }
                UserRole.STAFF -> {
                    when (uiState.activeNavTab) {
                        0 -> PosBillingView(
                            uiState = uiState,
                            onShopSelected = { viewModel.setSelectedShop(it) },
                            onCategorySelected = { viewModel.setSelectedCategory(it) },
                            onSearchChanged = { viewModel.setSearchQuery(it) },
                            onAddToCart = { item, grams -> viewModel.addToCart(item, grams) },
                            onUpdateCartQty = { idx, delta -> viewModel.updateCartItemQuantity(idx, delta) },
                            onClearCart = { viewModel.clearCart() },
                            onToggleOffline = { viewModel.toggleOfflineMode() },
                            onSyncOffline = { viewModel.syncOfflineBills() },
                            onCheckout = { method, phone -> viewModel.generateBill(method, phone) },
                            onAddSweetClick = { viewModel.setShowAddSweetDialog(true) },
                            onEditPriceClick = { viewModel.setSweetToEditPrice(it) },
                            onDeleteSweetClick = { viewModel.deleteSweetItem(it) }
                        )
                        1 -> ExpensesInventoryView(
                            uiState = uiState,
                            onAddExpenseClick = { viewModel.setShowAddExpenseDialog(true) }
                        )
                        2 -> EmployeePayrollView(
                            uiState = uiState,
                            onToggleSalaryPaid = { viewModel.toggleSalaryStatus(it) },
                            onAddEmployeeClick = { viewModel.setShowAddEmployeeDialog(true) },
                            onDeleteEmployee = { viewModel.deleteEmployee(it) },
                            onSearchStaffChanged = { viewModel.setStaffSearchQuery(it) }
                        )
                        3 -> AdminDashboardView(
                            uiState = uiState,
                            onTimeFilterSelected = { viewModel.setTimeFilter(it) },
                            onSimulateBranchSale = { viewModel.simulateIncomingBranchSale() },
                            onSelectBill = { },
                            onAddExpenseClick = { viewModel.setShowAddExpenseDialog(true) },
                            onAddShopClick = { viewModel.setShowAddShopDialog(true) },
                            onDeleteShop = { viewModel.deleteShop(it) }
                        )
                    }
                }
            }

            // Receipt Dialog on Bill Generation
            uiState.activeReceipt?.let { bill ->
                ReceiptDialog(
                    bill = bill,
                    onDismiss = { viewModel.dismissReceipt() }
                )
            }

            // Add Employee Dialog (with mobile phone)
            if (uiState.showAddEmployeeDialog) {
                AddEmployeeDialog(
                    shops = uiState.shops,
                    onDismiss = { viewModel.setShowAddEmployeeDialog(false) },
                    onConfirm = { name, age, shopId, role, salary, month, phone ->
                        viewModel.addEmployee(name, age, shopId, role, salary, month, phone)
                    }
                )
            }

            // Add Expense Dialog
            if (uiState.showAddExpenseDialog) {
                AddExpenseDialog(
                    shops = uiState.shops,
                    defaultShopId = uiState.selectedShopId,
                    onDismiss = { viewModel.setShowAddExpenseDialog(false) },
                    onConfirm = { shopId, category, description, amount, loggedBy ->
                        viewModel.addExpense(shopId, category, description, amount, loggedBy)
                    }
                )
            }

            // Add Shop Dialog
            if (uiState.showAddShopDialog) {
                AddShopDialog(
                    onDismiss = { viewModel.setShowAddShopDialog(false) },
                    onConfirm = { name, code, locality, phone, manager, target ->
                        viewModel.addShop(name, code, locality, phone, manager, target)
                    }
                )
            }

            // Add / Edit Sweet Item & Price Making Dialog
            if (uiState.showAddSweetDialog || uiState.sweetToEditPrice != null) {
                AddEditSweetDialog(
                    existingSweet = uiState.sweetToEditPrice,
                    onDismiss = {
                        viewModel.setShowAddSweetDialog(false)
                        viewModel.setSweetToEditPrice(null)
                    },
                    onSaveNew = { name, category, price, stock, desc ->
                        viewModel.addSweetItem(name, category, price, stock, desc)
                    },
                    onUpdatePrice = { id, newPrice ->
                        viewModel.updateSweetPrice(id, newPrice)
                    }
                )
            }

            // Restart to Zero Confirmation Dialog
            if (uiState.showResetDialog) {
                ResetConfirmDialog(
                    onDismiss = { viewModel.setShowResetDialog(false) },
                    onConfirmReset = { viewModel.resetAllToZero() }
                )
            }

            // System Settings Dialog (Contains Reset All to ZERO, Animation Replay, Role & Sync)
            if (uiState.showSettingsDialog) {
                SettingsDialog(
                    currentRole = uiState.currentRole,
                    isOfflineMode = uiState.isOfflineMode,
                    totalShopsCount = uiState.shops.size,
                    onDismiss = { viewModel.setShowSettingsDialog(false) },
                    onToggleRole = {
                        val nextRole = if (uiState.currentRole == UserRole.ADMIN) UserRole.STAFF else UserRole.ADMIN
                        viewModel.setRole(nextRole)
                    },
                    onToggleOffline = { viewModel.toggleOfflineMode() },
                    onReplaySplash = onReplaySplash,
                    onRequestResetToZero = { viewModel.setShowResetDialog(true) }
                )
            }
        }
    }
}
