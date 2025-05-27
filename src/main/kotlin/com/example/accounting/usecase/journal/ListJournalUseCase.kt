package com.example.accounting.usecase.journal

import com.example.accounting.domain.account.AccountName
import com.example.accounting.domain.account.AccountRepository
import com.example.accounting.domain.journal.JournalDate
import com.example.accounting.domain.journal.JournalDetailAmount
import com.example.accounting.domain.journal.JournalDetailDebitCreditType
import com.example.accounting.domain.journal.JournalRepository
import com.example.accounting.domain.journal.JournalSummary
import org.springframework.stereotype.Service

@Service
class ListJournalUseCase(
    private val accountRepository: AccountRepository,
    private val journalRepository: JournalRepository,
) {
    fun execute(): List<JournalDetailDisplay> {
        val accountNameMap = accountRepository.list()
            .associate { it.code to it.name }

        return journalRepository.list().flatMap { journal ->
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