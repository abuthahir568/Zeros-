package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SweetRepository
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class SweetPosUiState(
    val currentRole: UserRole = UserRole.ADMIN,
    val selectedShopId: String = "shop_01",
    val activeNavTab: Int = 0, // 0: Dashboard/POS, 1: Employees, 2: Expenses, 3: Branches/Bills
    val shops: List<SweetShop> = SweetRepository.allShops,
    val catalog: List<SweetItem> = SweetRepository.sweetCatalog,
    val bills: List<Bill> = SweetRepository.initialBills(),
    val offlineQueue: List<Bill> = emptyList(),
    val isOfflineMode: Boolean = false,
    val employees: List<Employee> = SweetRepository.initialEmployees(),
    val expenses: List<Expense> = SweetRepository.initialExpenses(),
    val cartItems: List<CartItem> = emptyList(),
    val timeFilter: TimePeriodFilter = TimePeriodFilter.TODAY,
    val activeReceipt: Bill? = null,
    val showAddExpenseDialog: Boolean = false,
    val showAddEmployeeDialog: Boolean = false,
    val showAddShopDialog: Boolean = false,
    val showAddSweetDialog: Boolean = false,
    val sweetToEditPrice: SweetItem? = null,
    val showResetDialog: Boolean = false,
    val showSettingsDialog: Boolean = false,
    val searchQuery: String = "",
    val staffSearchQuery: String = "",
    val selectedCategory: String = "All",
    val syncNotification: String? = null
) {
    val currentShop: SweetShop
        get() {
            val base = shops.find { it.id == selectedShopId } ?: shops.firstOrNull() ?: SweetShop(
                id = "shop_default",
                name = "Main Sweet Hub",
                code = "SW-01",
                locality = "Central City",
                phone = "+91 98000 00000",
                manager = "Store Manager",
                dailyTarget = 100000.0
            )
            val shopBills = bills.filter { it.shopId == base.id }
            return base.copy(
                todaySales = shopBills.sumOf { it.grandTotal },
                todayBillsCount = shopBills.size
            )
        }

    val shopsWithLiveSales: List<SweetShop>
        get() = shops.map { s ->
            val shopBills = bills.filter { it.shopId == s.id }
            s.copy(
                todaySales = shopBills.sumOf { it.grandTotal },
                todayBillsCount = shopBills.size
            )
        }

    // Pure Dynamic Calculations: Starts at ZERO on app open, auto-calculates on every bill
    val totalRevenue: Double
        get() = bills.sumOf { it.grandTotal }

    val totalExpenses: Double
        get() = expenses.sumOf { it.amount }

    val totalSalariesPaid: Double
        get() = employees.filter { it.isPaid }.sumOf { it.salaryAmount }

    val totalSalariesPending: Double
        get() = employees.filter { !it.isPaid }.sumOf { it.salaryAmount }

    val netProfitLoss: Double
        get() = totalRevenue - totalExpenses - totalSalariesPaid

    val cartSubtotal: Double
        get() = cartItems.sumOf { it.totalPrice }

    val cartGst: Double
        get() = cartSubtotal * 0.05

    val cartGrandTotal: Double
        get() = cartSubtotal + cartGst
}

class SweetPosViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SweetPosUiState())
    val uiState: StateFlow<SweetPosUiState> = _uiState.asStateFlow()

    fun setRole(role: UserRole) {
        _uiState.update { it.copy(currentRole = role, activeNavTab = 0) }
    }

    fun setSelectedShop(shopId: String) {
        _uiState.update { it.copy(selectedShopId = shopId) }
    }

    fun setActiveNavTab(tabIndex: Int) {
        _uiState.update { it.copy(activeNavTab = tabIndex) }
    }

    fun setTimeFilter(filter: TimePeriodFilter) {
        _uiState.update { it.copy(timeFilter = filter) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setStaffSearchQuery(query: String) {
        _uiState.update { it.copy(staffSearchQuery = query) }
    }

    fun setSelectedCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun toggleOfflineMode() {
        val nextMode = !_uiState.value.isOfflineMode
        _uiState.update { it.copy(isOfflineMode = nextMode) }
        if (!nextMode && _uiState.value.offlineQueue.isNotEmpty()) {
            syncOfflineBills()
        }
    }

    fun syncOfflineBills() {
        viewModelScope.launch {
            val queued = _uiState.value.offlineQueue
            if (queued.isEmpty()) return@launch

            val syncedBills = queued.map { it.copy(isOfflineQueued = false, isSynced = true) }
            _uiState.update { current ->
                current.copy(
                    bills = syncedBills + current.bills,
                    offlineQueue = emptyList(),
                    syncNotification = "Auto-Synced ${queued.size} offline bill(s) to Central Cloud Database!"
                )
            }
        }
    }

    fun clearSyncNotification() {
        _uiState.update { it.copy(syncNotification = null) }
    }

    // Shop Management (Add & Delete)
    fun setShowAddShopDialog(show: Boolean) {
        _uiState.update { it.copy(showAddShopDialog = show) }
    }

    fun addShop(name: String, code: String, locality: String, phone: String, manager: String, dailyTarget: Double) {
        val newShop = SweetShop(
            id = "shop_${System.currentTimeMillis()}",
            name = name,
            code = code.ifBlank { "SW-${_uiState.value.shops.size + 1}" },
            locality = locality,
            phone = phone.ifBlank { "+91 11 2000 0000" },
            manager = manager.ifBlank { "Store Manager" },
            dailyTarget = if (dailyTarget > 0) dailyTarget else 100000.0,
            todaySales = 0.0,
            todayBillsCount = 0
        )
        _uiState.update {
            it.copy(
                shops = it.shops + newShop,
                showAddShopDialog = false,
                syncNotification = "New Shop Added: ${newShop.name} (${newShop.code})"
            )
        }
    }

    fun deleteShop(shopId: String) {
        _uiState.update { state ->
            val target = state.shops.find { it.id == shopId }
            val updatedShops = state.shops.filter { it.id != shopId }
            val newSelectedId = if (state.selectedShopId == shopId) {
                updatedShops.firstOrNull()?.id ?: ""
            } else {
                state.selectedShopId
            }
            state.copy(
                shops = updatedShops,
                selectedShopId = newSelectedId,
                syncNotification = "Deleted Shop: ${target?.name ?: "Branch"}"
            )
        }
    }

    // Sweet Catalog & Price Making
    fun setShowAddSweetDialog(show: Boolean) {
        _uiState.update { it.copy(showAddSweetDialog = show) }
    }

    fun setSweetToEditPrice(sweet: SweetItem?) {
        _uiState.update { it.copy(sweetToEditPrice = sweet) }
    }

    fun addSweetItem(name: String, category: String, pricePerKg: Double, stockKg: Double, description: String) {
        val newItem = SweetItem(
            id = "sw_${System.currentTimeMillis()}",
            name = name,
            category = category,
            pricePerKg = pricePerKg,
            stockKg = stockKg,
            unitSnippet = "per kg",
            description = description
        )
        _uiState.update {
            it.copy(
                catalog = listOf(newItem) + it.catalog,
                showAddSweetDialog = false,
                syncNotification = "Added Sweet Item: ${newItem.name} at ₹${newItem.pricePerKg.toInt()}/kg"
            )
        }
    }

    fun updateSweetPrice(sweetId: String, newPrice: Double) {
        _uiState.update { state ->
            val updated = state.catalog.map { item ->
                if (item.id == sweetId) item.copy(pricePerKg = newPrice) else item
            }
            state.copy(
                catalog = updated,
                sweetToEditPrice = null,
                syncNotification = "Price updated to ₹${newPrice.toInt()}/kg"
            )
        }
    }

    fun deleteSweetItem(sweetId: String) {
        _uiState.update { state ->
            val target = state.catalog.find { it.id == sweetId }
            val updated = state.catalog.filter { it.id != sweetId }
            state.copy(
                catalog = updated,
                syncNotification = "Removed ${target?.name ?: "Sweet item"}"
            )
        }
    }

    // Cart Operations
    fun addToCart(item: SweetItem, weightGrams: Int) {
        _uiState.update { state ->
            val existingIndex = state.cartItems.indexOfFirst {
                it.sweetItem.id == item.id && it.weightGrams == weightGrams
            }
            val updated = if (existingIndex >= 0) {
                state.cartItems.toMutableList().apply {
                    val current = this[existingIndex]
                    this[existingIndex] = current.copy(quantity = current.quantity + 1)
                }
            } else {
                state.cartItems + CartItem(sweetItem = item, weightGrams = weightGrams, quantity = 1)
            }
            state.copy(cartItems = updated)
        }
    }

    fun updateCartItemQuantity(index: Int, delta: Int) {
        _uiState.update { state ->
            if (index !in state.cartItems.indices) return@update state
            val item = state.cartItems[index]
            val newQty = item.quantity + delta
            val updatedList = if (newQty <= 0) {
                state.cartItems.filterIndexed { i, _ -> i != index }
            } else {
                state.cartItems.toMutableList().apply {
                    this[index] = item.copy(quantity = newQty)
                }
            }
            state.copy(cartItems = updatedList)
        }
    }

    fun clearCart() {
        _uiState.update { it.copy(cartItems = emptyList()) }
    }

    // Billing Checkout
    fun generateBill(paymentMethod: PaymentMethod, customerPhone: String = "+91 98*** *****") {
        val state = _uiState.value
        if (state.cartItems.isEmpty()) return

        val shop = state.currentShop
        val billNum = "SW-${shop.code.replace("SW-", "")}-${(1000..9999).random()}"
        val subtotal = state.cartSubtotal
        val tax = state.cartGst
        val total = state.cartGrandTotal

        val newBill = Bill(
            id = UUID.randomUUID().toString(),
            billNumber = billNum,
            shopId = shop.id,
            shopName = shop.name,
            customerPhone = if (customerPhone.isBlank()) "+91 98765 00000" else customerPhone,
            items = state.cartItems,
            subtotal = subtotal,
            taxGst = tax,
            discount = 0.0,
            grandTotal = total,
            paymentMethod = paymentMethod,
            cashierName = shop.manager,
            timestamp = System.currentTimeMillis(),
            isOfflineQueued = state.isOfflineMode,
            isSynced = !state.isOfflineMode
        )

        _uiState.update { current ->
            if (current.isOfflineMode) {
                current.copy(
                    offlineQueue = listOf(newBill) + current.offlineQueue,
                    cartItems = emptyList(),
                    activeReceipt = newBill
                )
            } else {
                val updatedShops = current.shops.map { s ->
                    if (s.id == shop.id) s.copy(
                        todaySales = s.todaySales + total,
                        todayBillsCount = s.todayBillsCount + 1
                    ) else s
                }
                current.copy(
                    bills = listOf(newBill) + current.bills,
                    shops = updatedShops,
                    cartItems = emptyList(),
                    activeReceipt = newBill
                )
            }
        }
    }

    fun dismissReceipt() {
        _uiState.update { it.copy(activeReceipt = null) }
    }

    // Employee & Payroll Management (Add, Delete, Search, Phone)
    fun toggleSalaryStatus(employeeId: String) {
        _uiState.update { state ->
            val updated = state.employees.map { emp ->
                if (emp.id == employeeId) {
                    val nextPaid = !emp.isPaid
                    emp.copy(
                        isPaid = nextPaid,
                        paidTimestamp = if (nextPaid) System.currentTimeMillis() else null
                    )
                } else emp
            }
            state.copy(employees = updated)
        }
    }

    fun addEmployee(name: String, age: Int, shopId: String, role: String, salary: Double, month: String, phone: String) {
        val targetShop = _uiState.value.shops.find { it.id == shopId } ?: _uiState.value.shops.first()
        val newEmp = Employee(
            id = "emp_${System.currentTimeMillis()}",
            name = name,
            age = age,
            shopId = targetShop.id,
            shopName = targetShop.name,
            role = role,
            salaryAmount = salary,
            workingMonth = month,
            isPaid = false,
            phone = phone.ifBlank { "+91 98765 43210" }
        )
        _uiState.update {
            it.copy(
                employees = listOf(newEmp) + it.employees,
                showAddEmployeeDialog = false,
                syncNotification = "Staff Registered: ${newEmp.name} (${newEmp.role})"
            )
        }
    }

    fun deleteEmployee(employeeId: String) {
        _uiState.update { state ->
            val target = state.employees.find { it.id == employeeId }
            val updated = state.employees.filter { it.id != employeeId }
            state.copy(
                employees = updated,
                syncNotification = "Removed Staff: ${target?.name ?: "Employee"}"
            )
        }
    }

    fun setShowAddEmployeeDialog(show: Boolean) {
        _uiState.update { it.copy(showAddEmployeeDialog = show) }
    }

    // Expenses & Inventory
    fun addExpense(shopId: String, category: ExpenseCategory, description: String, amount: Double, loggedBy: String) {
        val targetShop = _uiState.value.shops.find { it.id == shopId } ?: _uiState.value.shops.first()
        val newExp = Expense(
            id = "exp_${System.currentTimeMillis()}",
            shopId = targetShop.id,
            shopName = targetShop.name,
            category = category,
            description = description,
            amount = amount,
            loggedBy = if (loggedBy.isBlank()) "Counter Staff" else loggedBy,
            timestamp = System.currentTimeMillis()
        )
        _uiState.update {
            it.copy(expenses = listOf(newExp) + it.expenses, showAddExpenseDialog = false)
        }
    }

    fun deleteExpense(expenseId: String) {
        _uiState.update { state ->
            val updated = state.expenses.filter { it.id != expenseId }
            state.copy(expenses = updated, syncNotification = "Expense entry removed")
        }
    }

    fun setShowAddExpenseDialog(show: Boolean) {
        _uiState.update { it.copy(showAddExpenseDialog = show) }
    }

    fun setShowSettingsDialog(show: Boolean) {
        _uiState.update { it.copy(showSettingsDialog = show) }
    }

    // RESTART APP ALL OVER TO ZERO
    fun setShowResetDialog(show: Boolean) {
        _uiState.update { it.copy(showResetDialog = show) }
    }

    fun resetAllToZero() {
        _uiState.update { state ->
            // Reset all shops sales to 0.0
            val zeroedShops = state.shops.map { it.copy(todaySales = 0.0, todayBillsCount = 0) }
            // Reset employee salaries to unpaid
            val resetEmployees = state.employees.map { it.copy(isPaid = false, paidTimestamp = null) }

            state.copy(
                shops = zeroedShops,
                bills = emptyList(),
                offlineQueue = emptyList(),
                expenses = emptyList(),
                employees = resetEmployees,
                cartItems = emptyList(),
                showResetDialog = false,
                syncNotification = "RESET COMPLETE: All sales, bills, and expenses cleared to pure ZERO!"
            )
        }
    }

    // Live Stream Simulator
    fun simulateIncomingBranchSale() {
        if (_uiState.value.shops.isEmpty()) return
        val otherShops = _uiState.value.shops.filter { it.id != _uiState.value.selectedShopId }
        val randomShop = if (otherShops.isNotEmpty()) otherShops.random() else _uiState.value.shops.random()
        val catalogItem = _uiState.value.catalog.random()
        val weight = listOf(500, 1000, 2000).random()
        val qty = (1..2).random()
        val cartItem = CartItem(catalogItem, weight, qty)
        val subtotal = cartItem.totalPrice
        val tax = subtotal * 0.05
        val grandTotal = subtotal + tax

        val simulatedBill = Bill(
            id = UUID.randomUUID().toString(),
            billNumber = "SW-${randomShop.code.replace("SW-", "")}-${(1000..9999).random()}",
            shopId = randomShop.id,
            shopName = randomShop.name,
            customerPhone = "+91 98110 ${(10000..99999).random()}",
            items = listOf(cartItem),
            subtotal = subtotal,
            taxGst = tax,
            discount = 0.0,
            grandTotal = grandTotal,
            paymentMethod = PaymentMethod.UPI_QR,
            cashierName = randomShop.manager,
            timestamp = System.currentTimeMillis(),
            isOfflineQueued = false,
            isSynced = true
        )

        _uiState.update { state ->
            val updatedShops = state.shops.map { s ->
                if (s.id == randomShop.id) s.copy(
                    todaySales = s.todaySales + grandTotal,
                    todayBillsCount = s.todayBillsCount + 1
                ) else s
            }
            state.copy(
                bills = listOf(simulatedBill) + state.bills,
                shops = updatedShops,
                syncNotification = "Live Stream: New bill ₹${String.format("%.0f", grandTotal)} generated at ${randomShop.name}!"
            )
        }
    }
}
