package com.example.model

enum class UserRole {
    ADMIN,
    STAFF
}

data class SweetShop(
    val id: String,
    val name: String,
    val code: String,
    val locality: String,
    val phone: String,
    val manager: String,
    val dailyTarget: Double,
    val todaySales: Double = 0.0,
    val todayBillsCount: Int = 0
)

data class SweetItem(
    val id: String,
    val name: String,
    val category: String, // "Kaju / Dry Fruit", "Pure Desi Ghee", "Mawa Sweets", "Syrup & Ras", "Namkeen & Snacks"
    val pricePerKg: Double,
    val stockKg: Double,
    val unitSnippet: String = "per kg",
    val description: String = ""
)

data class CartItem(
    val sweetItem: SweetItem,
    val weightGrams: Int, // 250, 500, 1000, 2000
    val quantity: Int = 1
) {
    val totalPrice: Double
        get() = (sweetItem.pricePerKg * (weightGrams / 1000.0)) * quantity
}

enum class PaymentMethod {
    UPI_QR,
    CASH,
    CARD
}

data class Bill(
    val id: String,
    val billNumber: String,
    val shopId: String,
    val shopName: String,
    val customerPhone: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val taxGst: Double, // 5%
    val discount: Double,
    val grandTotal: Double,
    val paymentMethod: PaymentMethod,
    val cashierName: String,
    val timestamp: Long,
    val isOfflineQueued: Boolean = false,
    val isSynced: Boolean = true
)

data class Employee(
    val id: String,
    val name: String,
    val age: Int,
    val shopId: String,
    val shopName: String,
    val role: String, // "Master Halwai", "Assistant Chef", "Cashier & Billing", "Store Manager", "Packaging Associate"
    val salaryAmount: Double,
    val workingMonth: String, // e.g. "November 2026"
    val isPaid: Boolean,
    val paidTimestamp: Long? = null,
    val phone: String = "+91 98765 43210"
)

enum class ExpenseCategory {
    RAW_MATERIAL_MAWA,
    RAW_MATERIAL_GHEE,
    RAW_MATERIAL_DRYFRUITS,
    RAW_MATERIAL_SUGAR_KESAR,
    PACKAGING_BOXES,
    ELECTRICITY_UTILITIES,
    SHOP_RENT_MAINTENANCE
}

data class Expense(
    val id: String,
    val shopId: String,
    val shopName: String,
    val category: String,
    val description: String,
    val amount: Double,
    val loggedBy: String,
    val timestamp: Long
)

enum class TimePeriodFilter {
    TODAY,
    THIS_WEEK,
    THIS_MONTH
}
