package com.example.accounting.usecase.journal

import com.example.accounting.domain.account.AccountName
import com.example.accounting.domain.account.AccountRepository
import com.example.accounting.domain.common.DateRange
import com.example.accounting.domain.journal.JournalDate
import com.example.accounting.domain.journal.JournalDetailAmount
import com.example.accounting.domain.journal.JournalDetailDebitCreditType
import com.example.accounting.domain.journal.JournalRepository
import com.example.accounting.domain.journal.JournalSummary
import com.example.accounting.domain.profit_and_loss.JournalPair
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.YearMonth

@Service
class ListJournalUseCase(
    private val accountRepository: AccountRepository,
    private val journalRepository: JournalRepository,
) {
    fun execute(
        fromDate: LocalDate = LocalDate.of(1970, 1,1 ),
        toDate: LocalDate = LocalDate.now(),
    ): List<JournalDetailDisplay> {
        val dateRange = DateRange.Companion.of(
            fromDate,
            toDate
        )

        /* 今月の仕訳を取得 */
        val targetJournals = journalRepository.filterByDateRange(dateRange)

        val accountNameMap = accountRepository.list()
            .associate { it.code to it.name }

        return targetJournals.flatMap { journal ->
            // 借方・貸方リスト
            val debits  = journal.details.filter { it.debitCreditType == JournalDetailDebitCreditType.DEBIT }
            val credits = journal.details.filter { it.debitCreditType == JournalDetailDebitCreditType.CREDIT }

            // 行数は多い方に合わせる
            val rows = maxOf(debits.size, credits.size)

            // 0 until rows を map して DTO を生成
            (0 until rows).map { idx ->
                val date    = journal.date.takeIf { idx == 0 }
                val summary = journal.summary.takeIf { idx == 0 }

                val debit = debits.getOrNull(idx)?.let { d ->
                    DebitAndCredit(
                        accountName = accountNameMap[d.accountCode]
                            ?: error("Unknown account: ${d.accountCode}"),
                        amount      = d.amount
                    )
                }

                val credit = credits.getOrNull(idx)?.let { c ->
                    DebitAndCredit(
                        accountName = accountNameMap[c.accountCode]
                            ?: error("Unknown account: ${c.accountCode}"),
                        amount      = c.amount
                    )
                }

                JournalDetailDisplay(
                    date    = date,
                    summary = summary,
                    debit   = debit,
                    credit  = credit
                )
            }
        }
    }
}

data class JournalDetailDisplay(
    val date: JournalDate?,
    val summary: JournalSummary?,
    val debit: DebitAndCredit?,
    val credit: DebitAndCredit?
)

data class DebitAndCredit(
    val accountName: AccountName,
    val amount: JournalDetailAmount
)