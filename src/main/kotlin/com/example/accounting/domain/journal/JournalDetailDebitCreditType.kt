package com.example.accounting.domain.journal

enum class JournalDetailDebitCreditType(
    val value: String,
) {
    DEBIT("借方"),
    CREDIT("貸方");

    companion object {
        fun of(value: String): JournalDetailDebitCreditType {
            return when (value) {
                "DEBIT" -> DEBIT
                "CREDIT" -> CREDIT
                else -> throw RuntimeException("DEBIT, CREDITで入力してください")
            }
        }
    }
}