package com.example.data

import com.example.model.*
import java.util.UUID

object SweetRepository {

    val allShops = listOf(
        SweetShop("shop_01", "Royal Sweets Flagship", "SW-01", "Connaught Place, Central", "+91 11 2341 5501", "Vikram Rathore", 150000.0, 118400.0, 142),
        SweetShop("shop_02", "Motichoor Heritage Hub", "SW-02", "Chandni Chowk, Old Town", "+91 11 2386 5502", "Harish Agrawal", 120000.0, 94250.0, 118),
        SweetShop("shop_03", "Kesar Confectionery", "SW-03", "Karol Bagh Market", "+91 11 2572 5503", "Rameshwar Dayal", 110000.0, 81500.0, 96),
        SweetShop("shop_04", "Diamond Ghee Bakes", "SW-04", "Lajpat Nagar Central", "+91 11 2984 5504", "Sunil Sharma", 130000.0, 102100.0, 126),
        SweetShop("shop_05", "Golden Gulab Corner", "SW-05", "South Extension Part 2", "+91 11 2462 5505", "Amit Verma", 100000.0, 76300.0, 89),
        SweetShop("shop_06", "Saffron Delight Lounge", "SW-06", "Greater Kailash M-Block", "+91 11 2923 5506", "Rajesh Singhania", 140000.0, 123800.0, 154),
        SweetShop("shop_07", "Silver City Mithai", "SW-07", "Rajouri Garden Main", "+91 11 2541 5507", "Maninder Singh", 115000.0, 88900.0, 104),
        SweetShop("shop_08", "Peda Palace", "SW-08", "Vasant Kunj Promenade", "+91 11 4175 5508", "Pradeep Gupta", 125000.0, 98450.0, 112),
        SweetShop("shop_09", "Crown Rasgulla Bar", "SW-09", "Noida Sector 18 Plaza", "+91 120 456 5509", "Dinesh Chandra", 105000.0, 79200.0, 94),
        SweetShop("shop_10", "Galaxy Sweet Treats", "SW-10", "Gurgaon Cyber Hub Phase 2", "+91 124 489 5510", "Kunal Mehra", 160000.0, 142600.0, 168)
    )

    val sweetCatalog = listOf(
        SweetItem("sw_01", "Kaju Katli Diamond", "Kaju / Dry Fruit", 1050.0, 48.5, "per kg", "Premium cashew paste infused with organic silver varak"),
        SweetItem("sw_02", "Motichoor Ladoo", "Pure Desi Ghee", 680.0, 72.0, "per kg", "Traditional melt-in-mouth tiny boondi pearls in desi ghee"),
        SweetItem("sw_03", "Royal Gulab Jamun", "Syrup & Ras", 540.0, 60.0, "per kg", "Soft mawa dumplings stewed in rose-cardamom syrup"),
        SweetItem("sw_04", "Kesar Mathura Peda", "Mawa Sweets", 720.0, 35.0, "per kg", "Caramelized khoya with authentic saffron strands"),
        SweetItem("sw_05", "Bengali Sponge Rasgulla", "Syrup & Ras", 480.0, 85.0, "per kg", "Chilled airy chenna balls in light saffron sugar syrup"),
        SweetItem("sw_06", "Desi Ghee Milk Cake", "Mawa Sweets", 780.0, 42.0, "per kg", "Granular caramelized milk fudge with roasted aroma"),
        SweetItem("sw_07", "Pista Royal Barfi", "Kaju / Dry Fruit", 950.0, 28.0, "per kg", "Rich Iranian pistachios crushed with pure condensed milk"),
        SweetItem("sw_08", "Saffron Rasmalai", "Syrup & Ras", 650.0, 30.0, "per kg", "Flat cottage cheese dumplings in thickened saffron rabri"),
        SweetItem("sw_09", "Besan Ladoo Supreme", "Pure Desi Ghee", 580.0, 55.0, "per kg", "Slow roasted gram flour with crunchy almonds & ghee"),
        SweetItem("sw_10", "Salted Cashew Namkeen", "Namkeen & Snacks", 1200.0, 40.0, "per kg", "Crisp roasted jumbo cashews with Himalayan pink salt")
    )

    fun initialEmployees(): List<Employee> = listOf(
        Employee("emp_01", "Rameshwar Sen", 46, "shop_01", "Royal Sweets Flagship", "Master Halwai", 48000.0, "November 2026", true, System.currentTimeMillis() - 86400000L * 3),
        Employee("emp_02", "Kishan Lal", 32, "shop_01", "Royal Sweets Flagship", "Cashier & Billing", 28000.0, "November 2026", true, System.currentTimeMillis() - 86400000L * 2),
        Employee("emp_03", "Mahesh Yadav", 28, "shop_01", "Royal Sweets Flagship", "Packaging Associate", 22000.0, "November 2026", false, null),

        Employee("emp_04", "Bhanu Pratap", 50, "shop_02", "Motichoor Heritage Hub", "Master Halwai", 45000.0, "November 2026", true, System.currentTimeMillis() - 86400000L * 4),
        Employee("emp_05", "Pooja Sharma", 26, "shop_02", "Motichoor Heritage Hub", "Cashier & Billing", 27000.0, "November 2026", false, null),

        Employee("emp_06", "Surendra Rawat", 44, "shop_03", "Kesar Confectionery", "Master Halwai", 44000.0, "November 2026", false, null),
        Employee("emp_07", "Deepak Verma", 31, "shop_03", "Kesar Confectionery", "Store Manager", 38000.0, "November 2026", true, System.currentTimeMillis() - 86400000L * 2),

        Employee("emp_08", "Govind Ram", 48, "shop_04", "Diamond Ghee Bakes", "Master Halwai", 47000.0, "November 2026", true, System.currentTimeMillis() - 86400000L * 1),
        Employee("emp_09", "Neha Rajput", 25, "shop_04", "Diamond Ghee Bakes", "Cashier & Billing", 26000.0, "November 2026", true, System.currentTimeMillis() - 86400000L * 3),

        Employee("emp_10", "Murli Manohar", 39, "shop_05", "Golden Gulab Corner", "Master Halwai", 43000.0, "November 2026", false, null),
        Employee("emp_11", "Gaurav Joshi", 29, "shop_06", "Saffron Delight Lounge", "Store Manager", 40000.0, "November 2026", true, System.currentTimeMillis() - 86400000L * 5),
        Employee("emp_12", "Lalit Mohan", 34, "shop_07", "Silver City Mithai", "Cashier & Billing", 28000.0, "November 2026", false, null),
        Employee("emp_13", "Omkar Mishra", 47, "shop_08", "Peda Palace", "Master Halwai", 46000.0, "November 2026", true, System.currentTimeMillis() - 86400000L * 2),
        Employee("emp_14", "Ankit Tiwari", 24, "shop_09", "Crown Rasgulla Bar", "Packaging Associate", 21000.0, "November 2026", false, null),
        Employee("emp_15", "Vipin Chawla", 35, "shop_10", "Galaxy Sweet Treats", "Store Manager", 45000.0, "November 2026", true, System.currentTimeMillis() - 86400000L * 1)
    )

    fun initialExpenses(): List<Expense> = listOf(
        Expense("exp_01", "shop_01", "Royal Sweets Flagship", ExpenseCategory.RAW_MATERIAL_GHEE, "50 Tins Pure Buffalo Desi Ghee (Verka/Amul)", 28500.0, "Vikram Rathore", System.currentTimeMillis() - 7200000L),
        Expense("exp_02", "shop_02", "Motichoor Heritage Hub", ExpenseCategory.RAW_MATERIAL_DRYFRUITS, "Kaju Whole Grade W240 40kg & Iranian Pistachio", 34200.0, "Harish Agrawal", System.currentTimeMillis() - 14400000L),
        Expense("exp_03", "shop_03", "Kesar Confectionery", ExpenseCategory.RAW_MATERIAL_MAWA, "Fresh Mawa / Khoya 80kg from dairy farm", 18400.0, "Rameshwar Dayal", System.currentTimeMillis() - 21600000L),
        Expense("exp_04", "shop_04", "Diamond Ghee Bakes", ExpenseCategory.PACKAGING_BOXES, "2000 Rigid Gold Embossed 1kg Sweet Gift Boxes", 16000.0, "Sunil Sharma", System.currentTimeMillis() - 28800000L),
        Expense("exp_05", "shop_06", "Saffron Delight Lounge", ExpenseCategory.ELECTRICITY_UTILITIES, "Commercial Display Chillers & Air Conditioning Bill", 24500.0, "Rajesh Singhania", System.currentTimeMillis() - 36000000L),
        Expense("exp_06", "shop_10", "Galaxy Sweet Treats", ExpenseCategory.RAW_MATERIAL_SUGAR_KESAR, "Refined Sulphurless Sugar 500kg & Kashmiri Kesar 100g", 27800.0, "Kunal Mehra", System.currentTimeMillis() - 43200000L)
    )

    fun initialBills(): List<Bill> = listOf(
        Bill("bill_101", "INV-SW10-0941", "shop_10", "Galaxy Sweet Treats", "+91 98112 34567",
            listOf(CartItem(sweetCatalog[0], 1000, 2), CartItem(sweetCatalog[3], 500, 1)),
            2460.0, 123.0, 0.0, 2583.0, PaymentMethod.UPI_QR, "Vipin Chawla", System.currentTimeMillis() - 180000L),
        Bill("bill_102", "INV-SW01-0812", "shop_01", "Royal Sweets Flagship", "+91 98223 45678",
            listOf(CartItem(sweetCatalog[1], 1000, 3), CartItem(sweetCatalog[2], 1000, 2)),
            3120.0, 156.0, 100.0, 3176.0, PaymentMethod.CARD, "Kishan Lal", System.currentTimeMillis() - 420000L),
        Bill("bill_103", "shop_06_b88", "shop_06", "Saffron Delight Lounge", "+91 99100 11223",
            listOf(CartItem(sweetCatalog[0], 500, 1), CartItem(sweetCatalog[7], 1000, 2)),
            1825.0, 91.25, 0.0, 1916.25, PaymentMethod.UPI_QR, "Gaurav Joshi", System.currentTimeMillis() - 850000L),
        Bill("bill_104", "INV-SW04-0331", "shop_04", "Diamond Ghee Bakes", "+91 98991 77889",
            listOf(CartItem(sweetCatalog[5], 1000, 1), CartItem(sweetCatalog[8], 1000, 2)),
            1940.0, 97.0, 0.0, 2037.0, PaymentMethod.CASH, "Neha Rajput", System.currentTimeMillis() - 1200000L)
    )
}
