package com.example.moneyku.data.repository

import com.example.moneyku.data.database.AppDatabase
import com.example.moneyku.data.database.entity.GroupTransactionEntity
import com.example.moneyku.data.database.entity.SplitTransactionEntity
import com.example.moneyku.data.database.entity.SplitTransactionStatus
import com.example.moneyku.data.database.entity.TransactionEntity
import com.example.moneyku.data.database.entity.TransactionType
import java.math.BigDecimal

class MoneyRepository(private val db: AppDatabase) {


    suspend fun createGroupSplitBill(
        userId: String,
        categoryId: String,
        totalAmount: BigDecimal,
        tax: BigDecimal,
        service: BigDecimal,
        participants: List<ParticipantDraft>
    ): String {
        val groupTxId = java.util.UUID.randomUUID().toString()
        val groupTx = GroupTransactionEntity(
            groupTransactionId = groupTxId,
            userId = userId,
            categoryId = categoryId,
            totalAmount = totalAmount,
            tax = tax,
            service = service
        )

        val splitEntities = participants.map { draft ->
            SplitTransactionEntity(
                splitTransactionId = java.util.UUID.randomUUID().toString(),
                name = draft.name,
                amount = draft.amount,
                groupTransactionId = groupTxId,
                status = SplitTransactionStatus.Pending
            )
        }

        return groupTxId
    }

    data class ParticipantDraft(
        val name: String,
        val amount: BigDecimal
    )
}
