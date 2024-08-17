package tech.johnnydev.cashflow.entity

data class Transaction(
    val type: TransactionType,
    val detail: String,
    val value: Double,
    val date: String,
    val id: Int = 0
)
