package com.example.data

import com.example.model.*

object SweetRepository {

    // All 10 shops start with ZERO sales and 0 bills
    val allShops = listOf(
        SweetShop("shop_01", "Royal Sweets Flagship", "SW-01", "Connaught Place, Central", "+91 11 2341 5501", "Vikram Rathore", 150000.0, 0.0, 0),
        SweetShop("shop_02", "Motichoor Heritage Hub", "SW-02", "Chandni Chowk, Old Town", "+91 11 2386 5502", "Harish Agrawal", 120000.0, 0.0, 0),
        SweetShop("shop_03", "Kesar Confectionery", "SW-03", "Karol Bagh Market", "+91 11 2572 5503", "Rameshwar Dayal", 110000.0, 0.0, 0),
        SweetShop("shop_04", "Diamond Ghee Bakes", "SW-04", "Lajpat Nagar Central", "+91 11 2984 5504", "Sunil Sharma", 130000.0, 0.0, 0),
        SweetShop("shop_05", "Golden Gulab Corner", "SW-05", "South Extension Part 2", "+91 11 2462 5505", "Amit Verma", 100000.0, 0.0, 0),
        SweetShop("shop_06", "Saffron Delight Lounge", "SW-06", "Greater Kailash M-Block", "+91 11 2923 5506", "Rajesh Singhania", 140000.0, 0.0, 0),
        SweetShop("shop_07", "Silver City Mithai", "SW-07", "Rajouri Garden Main", "+91 11 2541 5507", "Maninder Singh", 115000.0, 0.0, 0),
        SweetShop("shop_08", "Peda Palace", "SW-08", "Vasant Kunj Promenade", "+91 11 4175 5508", "Pradeep Gupta", 125000.0, 0.0, 0),
        SweetShop("shop_09", "Crown Rasgulla Bar", "SW-09", "Noida Sector 18 Plaza", "+91 120 456 5509", "Dinesh Chandra", 105000.0, 0.0, 0),
        SweetShop("shop_10", "Galaxy Sweet Treats", "SW-10", "Gurgaon Cyber Hub Phase 2", "+91 124 489 5510", "Kunal Mehra", 160000.0, 0.0, 0)
    )

    val sweetCatalog = listOf(
        SweetItem("sw_01", "Kaju Katli Diamond", "Kaju / Dry Fruit", 1050.0, 50.0, "per kg", "Premium cashew paste infused with organic silver varak"),
        SweetItem("sw_02", "Motichoor Ladoo", "Pure Desi Ghee", 680.0, 75.0, "per kg", "Traditional melt-in-mouth tiny boondi pearls in desi ghee"),
        SweetItem("sw_03", "Royal Gulab Jamun", "Syrup & Ras", 540.0, 60.0, "per kg", "Soft mawa dumplings stewed in rose-cardamom syrup"),
        SweetItem("sw_04", "Kesar Mathura Peda", "Mawa Sweets", 720.0, 40.0, "per kg", "Caramelized khoya with authentic saffron strands"),
        SweetItem("sw_05", "Bengali Sponge Rasgulla", "Syrup & Ras", 480.0, 80.0, "per kg", "Chilled airy chenna balls in light saffron sugar syrup"),
        SweetItem("sw_06", "Desi Ghee Milk Cake", "Mawa Sweets", 780.0, 45.0, "per kg", "Granular caramelized milk fudge with roasted aroma"),
        SweetItem("sw_07", "Pista Royal Barfi", "Kaju / Dry Fruit", 950.0, 30.0, "per kg", "Rich Iranian pistachios crushed with pure condensed milk"),
        SweetItem("sw_08", "Saffron Rasmalai", "Syrup & Ras", 650.0, 35.0, "per kg", "Flat cottage cheese dumplings in thickened saffron rabri"),
        SweetItem("sw_09", "Besan Ladoo Supreme", "Pure Desi Ghee", 580.0, 60.0, "per kg", "Slow roasted gram flour with crunchy almonds & ghee"),
        SweetItem("sw_10", "Salted Cashew Namkeen", "Namkeen & Snacks", 1200.0, 40.0, "per kg", "Crisp roasted jumbo cashews with Himalayan pink salt")
    )

    // Employees start with unpaid salary (0 disbursed initially)
    fun initialEmployees(): List<Employee> = listOf(
        Employee("emp_01", "Rameshwar Sen", 46, "shop_01", "Royal Sweets Flagship", "Master Halwai", 48000.0, "November 2026", false, null, "+91 98112 01001"),
        Employee("emp_02", "Kishan Lal", 32, "shop_01", "Royal Sweets Flagship", "Cashier & Billing", 28000.0, "November 2026", false, null, "+91 98112 01002"),
        Employee("emp_03", "Mahesh Yadav", 28, "shop_01", "Royal Sweets Flagship", "Packaging Associate", 22000.0, "November 2026", false, null, "+91 98112 01003"),

        Employee("emp_04", "Bhanu Pratap", 50, "shop_02", "Motichoor Heritage Hub", "Master Halwai", 45000.0, "November 2026", false, null, "+91 98112 02001"),
        Employee("emp_05", "Pooja Sharma", 26, "shop_02", "Motichoor Heritage Hub", "Cashier & Billing", 27000.0, "November 2026", false, null, "+91 98112 02002"),

        Employee("emp_06", "Surendra Rawat", 44, "shop_03", "Kesar Confectionery", "Master Halwai", 44000.0, "November 2026", false, null, "+91 98112 03001"),
        Employee("emp_07", "Deepak Verma", 31, "shop_03", "Kesar Confectionery", "Store Manager", 38000.0, "November 2026", false, null, "+91 98112 03002"),

        Employee("emp_08", "Govind Ram", 48, "shop_04", "Diamond Ghee Bakes", "Master Halwai", 47000.0, "November 2026", false, null, "+91 98112 04001"),
        Employee("emp_09", "Neha Rajput", 25, "shop_04", "Diamond Ghee Bakes", "Cashier & Billing", 26000.0, "November 2026", false, null, "+91 98112 04002"),

        Employee("emp_10", "Murli Manohar", 39, "shop_05", "Golden Gulab Corner", "Master Halwai", 43000.0, "November 2026", false, null, "+91 98112 05001"),
        Employee("emp_11", "Gaurav Joshi", 29, "shop_06", "Saffron Delight Lounge", "Store Manager", 40000.0, "November 2026", false, null, "+91 98112 06001"),
        Employee("emp_12", "Lalit Mohan", 34, "shop_07", "Silver City Mithai", "Cashier & Billing", 28000.0, "November 2026", false, null, "+91 98112 07001"),
        Employee("emp_13", "Omkar Mishra", 47, "shop_08", "Peda Palace", "Master Halwai", 46000.0, "November 2026", false, null, "+91 98112 08001"),
        Employee("emp_14", "Ankit Tiwari", 24, "shop_09", "Crown Rasgulla Bar", "Packaging Associate", 21000.0, "November 2026", false, null, "+91 98112 09001"),
        Employee("emp_15", "Vipin Chawla", 35, "shop_10", "Galaxy Sweet Treats", "Store Manager", 45000.0, "November 2026", false, null, "+91 98112 10001")
    )

    // Starts with 0 expenses
    fun initialExpenses(): List<Expense> = emptyList()

    // Starts with 0 bills
    fun initialBills(): List<Bill> = emptyList()
}
