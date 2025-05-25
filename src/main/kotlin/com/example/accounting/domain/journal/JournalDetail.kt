package com.example.accounting.domain.journal

import com.example.accounting.domain.account.AccountCode

data class JournalDetail private constructor(
    val id: Int?,
    val debitCreditType: JournalDetailDebitCreditType,
    val amount: JournalDetailAmount,
    val accountCode: AccountCode
) {
    companion object {
        fun create(
            debitCreditType: JournalDetailDebitCreditType,
            amount: JournalDetailAmount,
            accountCode: AccountCode
        ) : JournalDetail{
            validate()
            return JournalDetail(null, debitCreditType, amount, accountCode)
        }

        fun reconstruct(
            id: Int,
            debitCreditType: JournalDetailDebitCreditType,
            amount: JournalDetailAmount,
            accountCode: AccountCode
        ): JournalDetail {
            return JournalDetail(id, debitCreditType, amount, accountCode)
        }

        private fun validate() {
            // 現状なし
        }
    }
}