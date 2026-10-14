package com.example.moneyku.data.model

data class ScannedReceiptItem(
    val id: String,
    val name: String,
    val quantity: Int = 1,
    val unitPrice: Long,
    val totalPrice: Long = quantity * unitPrice,
    val categoryIcon: String = "cart", // cart, bakery, fruit, coffee
    val matchConfidencePercent: Int = 98 // e.g. 100%, 98%, 96%
)

data class MerchantInfo(
    val name: String = "Supermarket Ranch Market",
    val transactionDate: String = "24 Mei 2024 - 11:42 WIB",
    val paymentMethod: String = "BCA Debit",
    val accuracyPercent: Int = 98
)

data class ReceiptScanUiState(
    val merchant: MerchantInfo = MerchantInfo(),
    val isAiVisionActive: Boolean = true,
    val isFlashlightOn: Boolean = false,
    val detectedItemsCount: Int = 4,
    val items: List<ScannedReceiptItem> = listOf(
        ScannedReceiptItem("1", "Susu UHT Oat Milk 1L", 1, 38000L, 38000L, "milk", 100),
        ScannedReceiptItem("2", "Roti Gandum Whole Wheat", 1, 26000L, 26000L, "bakery", 98),
        ScannedReceiptItem("3", "Buah Apel Fuji 1kg", 1, 45000L, 45000L, "fruit", 96),
        ScannedReceiptItem("4", "Kopi Cold Brew Latte", 2, 25000L, 50000L, "coffee", 99)
    ),
    val taxPercentage: Int = 11, // PPN 11%
    val isEditingItem: Boolean = false
) {
    val subtotal: Long
        get() = items.sumOf { it.totalPrice }

    val taxAmount: Long
        get() = (subtotal * taxPercentage) / 100

    val grandTotal: Long
        get() = subtotal + taxAmount
}
