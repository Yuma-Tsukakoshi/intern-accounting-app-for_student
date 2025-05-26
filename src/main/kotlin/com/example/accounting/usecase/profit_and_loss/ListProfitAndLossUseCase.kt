package com.example.accounting.usecase.profit_and_loss

import com.example.accounting.domain.account.AccountRepository
import com.example.accounting.domain.account.AccountType
import com.example.accounting.domain.common.DateRange
import com.example.accounting.domain.journal.JournalRepository
import com.example.accounting.domain.profit_and_loss.JournalPair
import com.example.accounting.domain.profit_and_loss.Pl
import com.example.accounting.domain.profit_and_loss.ProfitAndLoss
import org.springframework.stereotype.Service
import java.time.YearMonth

@Service
class ListProfitAndLossUseCase(
    private val accountRepository: AccountRepository,
    private val journalRepository: JournalRepository,
) {
    fun execute(
        lastMonth: YearMonth?,
        currentMonth: YearMonth,
    ): Pl {
        val lastMonthRange = lastMonth?.let { lastMonth ->
            DateRange.Companion.of(
                lastMonth.atDay(1),
                lastMonth.atEndOfMonth(),
            )
        }

        val currentMonthRange = DateRange.Companion.of(
            currentMonth.atDay(1),
            currentMonth.atEndOfMonth(),
        )

        /* P/L に載せる科目(収益・費用)を取得 */
        val plTypes = AccountType.Companion.getListForPL()
        val accounts = accountRepository.filterByAccountTypes(plTypes)
        /* 今月の仕訳を取得 */
        val lastJournals = lastMonthRange?.let { lastMonthRange ->
            journalRepository.filterByDateRange(lastMonthRange)
        }
        val currentJournals = journalRepository.filterByDateRange(currentMonthRange)

        val journalPair = JournalPair(
            last = lastJournals,
            current = currentJournals,
        )

        val result =  ProfitAndLoss.Companion.create(
            accounts,
            journalPair
        )
        return result
    }
}