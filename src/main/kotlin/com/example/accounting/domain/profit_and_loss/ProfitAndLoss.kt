package com.example.accounting.domain.profit_and_loss

import com.example.accounting.domain.account.Account
import com.example.accounting.domain.account.AccountName
import com.example.accounting.domain.account.AccountType
import com.example.accounting.domain.journal.Journal
import com.example.accounting.domain.journal.JournalDate
import com.example.accounting.domain.journal.JournalDetailAmount
import com.example.accounting.domain.journal.JournalDetailDebitCreditType
import com.example.accounting.domain.journal.JournalSummary

data class ProfitAndLoss private constructor(
    val accounts: List<Account>,
    val journals: List<Journal>,
) {
    companion object {
        fun create(
            accounts: List<Account>,
            journals: List<Journal>,
        ): Pl  {
            val plTypes = AccountType.getListForPL()

            val accountMap = accounts.associateBy { it.code }
            val accountTypeMap = accounts.groupBy { it.accountType }

            /* 明細単位にフラット化し、PL 用 DTO に再構築 */
            val details = journals.flatMap { j ->
                j.details.map { d -> j to d }
            }.filter { (_, d) ->
                accountMap.containsKey(d.accountCode)
            }
            /* AccountType→Account→明細 の 3 階層に組み直す */
            val pl =  plTypes.map { cat ->
                val accountsInCat = accountTypeMap[cat]!!

                /* 科目ごとに明細をまとめる */
                val subjects = accountsInCat.map { acc ->

                    val entries: List<PlEntry> = details
                        .filter { (_, d) -> d.accountCode == acc.code }
                        .map { (j, d) ->
                            PlEntry(
                                j.date,
                                d.debitCreditType,
                                d.amount,
                                j.summary
                            )
                        }

                    PlSubject(
                        acc.name,
                        entries.sumOf{judgedAmount(it, cat)},
                        entries
                    )
                }

                PlCategory(
                    cat,
                    subjects,
                    subjects.sumOf { it.totalAmount }
                )
            }

            val profit = pl.find{it.category == AccountType.PROFIT}!!
            val loss = pl.find{it.category == AccountType.LOSS}!!
            val benefitSum =  profit.totalAmount - loss.totalAmount

            return Pl(
                profit,
                loss,
                benefitSum
            )
        }

        private fun judgedAmount(
            entry: PlEntry,
            accountType: AccountType
        ) : Long {
            return when (accountType to entry.debitCreditType) {
                AccountType.PROFIT to JournalDetailDebitCreditType.DEBIT  -> -entry.amount.value
                AccountType.PROFIT to JournalDetailDebitCreditType.CREDIT ->  entry.amount.value
                AccountType.LOSS   to JournalDetailDebitCreditType.DEBIT  ->  entry.amount.value
                AccountType.LOSS   to JournalDetailDebitCreditType.CREDIT -> -entry.amount.value
                else -> throw RuntimeException("誤った集計科目が挿入されました")
            }
        }
    }
}

data class Pl(
    val profit: PlCategory,
    val loss: PlCategory,
    val benefit: Long
)

data class PlCategory(
    val category: AccountType,
    val subjects: List<PlSubject>,
    val totalAmount: Long
)

data class PlSubject(
    val accountName: AccountName,
    val totalAmount: Long,
    val entries: List<PlEntry>
)

data class PlEntry(
    val date: JournalDate,
    val debitCreditType: JournalDetailDebitCreditType,
    val amount: JournalDetailAmount,
    val summary: JournalSummary
)