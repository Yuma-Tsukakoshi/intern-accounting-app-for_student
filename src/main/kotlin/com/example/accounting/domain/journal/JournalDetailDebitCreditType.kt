package com.example.accounting.domain.journal

enum class JournalDetailDebitCreditType {
    // 借方
    DEBIT,
    // 貸方
    CREDIT;

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