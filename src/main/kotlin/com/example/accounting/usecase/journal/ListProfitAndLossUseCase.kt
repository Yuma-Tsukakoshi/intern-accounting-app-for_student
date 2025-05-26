package com.example.accounting.usecase.journal

import com.example.accounting.domain.account.AccountRepository
import com.example.accounting.domain.account.AccountType
import com.example.accounting.domain.common.DateRange
import com.example.accounting.domain.journal.JournalRepository
import com.example.accounting.domain.profit_and_loss.Pl
import com.example.accounting.domain.profit_and_loss.ProfitAndLoss
import org.springframework.cglib.core.Local
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.YearMonth

@Service
class ListProfitAndLossUseCase(
    private val accountRepository: AccountRepository,
    private val journalRepository: JournalRepository,
) {
    fun execute(
        month: YearMonth,
    ): Pl {
        val range = DateRange.of(
            month.atDay(1),
            month.atEndOfMonth(),
        )

        /* P/L に載せる科目(収益・費用)を取得 */
        val plTypes = AccountType.getListForPL()
        val accounts = accountRepository.filterByAccountTypes(plTypes)
        /* 今月の仕訳を取得 */
        val journals = journalRepository.filterByDateRange(range)

        val result =  ProfitAndLoss.create(
            accounts,
            journals
        )
        return result
    }
}