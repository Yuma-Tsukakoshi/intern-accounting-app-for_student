package com.example.accounting.domain.profit_and_loss

import com.example.accounting.domain.account.Account
import com.example.accounting.domain.account.AccountName
import com.example.accounting.domain.account.AccountType
import com.example.accounting.domain.journal.Journal
import com.example.accounting.domain.journal.JournalDate
import com.example.accounting.domain.journal.JournalDetail
import com.example.accounting.domain.journal.JournalDetailAmount
import com.example.accounting.domain.journal.JournalDetailDebitCreditType
import com.example.accounting.domain.journal.JournalSummary
import java.math.BigDecimal

data class ProfitAndLoss private constructor(
    val accounts: List<Account>,
    val journalPair: JournalPair,
) {
    companion object {
        fun create(
            accounts: List<Account>,
            journalPair: JournalPair,
        ): Pl  {
            val plTypes = AccountType.getListForPL()

            val accountMap = accounts.associateBy { it.code }
            val accountTypeMap = accounts.groupBy { it.accountType }

            val hasLastJournals = journalPair.last != null

            /* 明細単位にフラット化し、再構築 */
            fun flatten(journals: List<Journal>): List<Pair<Journal, JournalDetail>> =
                journals.flatMap { j -> j.details.map { d -> j to d } }
                    .filter { (_, d) -> accountMap.containsKey(d.accountCode) }

            val detailsLast    = journalPair.last?.let { flatten(it) } ?: emptyList()
            val detailsCurrent = flatten(journalPair.current)

            /* AccountType→Account→明細 の 3 階層に組み直す */
            // ■ カテゴリごとに PL を組み立て
            val categories = plTypes.map { category ->
                val accounts = accountTypeMap[category]!!

                // 各科目ごとに前期/当期を集計
                val subjects = accounts.map { account ->
                    // 前期のエントリ
                    val lastEntries = detailsLast.filter { it.second.accountCode == account.code }
                        .map { (journal, detail) -> PlEntry(journal.date, detail.debitCreditType, detail.amount, journal.summary) }
                    val lastAmount = if (hasLastJournals)
                        judgedAmountSum(lastEntries, category)
                    else null

                    // 当期のエントリ
                    val currentEntries = detailsCurrent.filter { it.second.accountCode == account.code }
                        .map { (journal, detail) -> PlEntry(journal.date, detail.debitCreditType, detail.amount, journal.summary) }
                    val currentAmount = judgedAmountSum(currentEntries, category)

                    // 差分
                    val diff = if (lastAmount != null) currentAmount - lastAmount else null

                    PlSubject(
                        accountName = account.name,
                        last    = lastAmount?.let { amount -> PlMonthlyData(amount, lastEntries) },
                        current = PlMonthlyData(currentAmount, currentEntries),
                        diff    = diff
                    )
                }

                // カテゴリ全体の集計
                val lastCatSum    = subjects.mapNotNull { it.last?.amount }.sum()
                val currentCatSum = subjects.sumOf { it.current.amount }
                val catDiff       = if (journalPair.last != null) currentCatSum - lastCatSum else null

                PlCategory(
                    category      = category,
                    subjects      = subjects,
                    lastAmount    = if (journalPair.last != null) lastCatSum else null,
                    currentAmount = currentCatSum,
                    catDiff       = catDiff
                )
            }
            // ■ PROFIT / LOSS を抜き出し、Benefit を計算
            val profitCat = categories.first { it.category == AccountType.PROFIT }
            val lossCat   = categories.first { it.category == AccountType.LOSS }

            val lastBenefit    = if (journalPair.last != null)
                profitCat.lastAmount!! - lossCat.lastAmount!!
            else null
            val currentBenefit = profitCat.currentAmount - lossCat.currentAmount
            val diffBenefit    = if (lastBenefit != null) currentBenefit - lastBenefit else null

            return Pl(
                profit  = profitCat,
                loss    = lossCat,
                benefit = Benefit(
                    lastBenefit    = lastBenefit,
                    currentBenefit = currentBenefit,
                    diffBenefit    = diffBenefit
                )
            )
        }

        // 借貸区分を見て合計金額を算出するヘルパー
        private fun judgedAmountSum(
            entries: List<PlEntry>,
            accountType: AccountType
        ): Long = entries.sumOf { entry ->
            when (accountType to entry.debitCreditType) {
                AccountType.PROFIT to JournalDetailDebitCreditType.DEBIT  -> -entry.amount.value
                AccountType.PROFIT to JournalDetailDebitCreditType.CREDIT ->  entry.amount.value
                AccountType.LOSS   to JournalDetailDebitCreditType.DEBIT  ->  entry.amount.value
                AccountType.LOSS   to JournalDetailDebitCreditType.CREDIT -> -entry.amount.value
                else -> throw RuntimeException("誤った集計科目が挿入されました")
            }
        }
    }
}

data class JournalPair(
    val last: List<Journal>?,
    val current: List<Journal>,
)

data class Pl(
    val profit: PlCategory,
    val loss: PlCategory,
    val benefit: Benefit
)

data class Benefit(
    val lastBenefit: Long?,
    val currentBenefit: Long,
    val diffBenefit: Long?,
)

data class PlCategory(
    val category: AccountType,
    val subjects: List<PlSubject>,
    val lastAmount: Long?,
    val currentAmount: Long,
    val catDiff: Long?,
)

data class PlSubject(
    val accountName: AccountName,
    val last:PlMonthlyData?,
    val current:PlMonthlyData,
    val diff: Long?
)

data class PlMonthlyData(
    val amount: Long,
    val entries: List<PlEntry>
)

data class PlEntry(
    val date: JournalDate,
    val debitCreditType: JournalDetailDebitCreditType,
    val amount: JournalDetailAmount,
    val summary: JournalSummary
)