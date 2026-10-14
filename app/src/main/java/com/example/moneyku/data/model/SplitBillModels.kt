package com.example.moneyku.data.model

import java.math.BigDecimal

data class SplitMember(
    val id: String,
    val name: String,
    val avatarUrl: String? = null,
    val amountOwed: BigDecimal,
    val isPaid: Boolean = false,
    val paymentMethod: String? = null,
    val paidAt: String? = null
)

data class ItemizedSplitRow(
    val id: String,
    val itemName: String,
    val quantity: Int = 1,
    val unitPrice: BigDecimal,
    val assignedMembers: List<String> = emptyList() // member IDs
)

data class SplitBillData(
    val id: String,
    val title: String,
    val category: String,
    val dateText: String,
    val totalBill: BigDecimal,
    val taxAndService: BigDecimal = BigDecimal.ZERO,
    val Members: List<SplitMember> = emptyList(),
    val itemizedRows: List<ItemizedSplitRow> = emptyList(),
    val isItemized: Boolean = false
) {
    val paidBackAmount: BigDecimal
        get() = Members.filter { it.isPaid }.fold(BigDecimal.ZERO) { acc, m -> acc.add(m.amountOwed) }

    val stillWaitingAmount: BigDecimal
        get() = Members.filter { !it.isPaid }.fold(BigDecimal.ZERO) { acc, m -> acc.add(m.amountOwed) }

    val paidMembersCount: Int
        get() = Members.count { it.isPaid }

    val progressPercent: Float
        get() = if (totalBill > BigDecimal.ZERO) {
            paidBackAmount.toFloat() / totalBill.toFloat()
        } else 0f
}
