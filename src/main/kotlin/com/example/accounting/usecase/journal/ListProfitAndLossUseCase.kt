package com.example.accounting.usecase.journal

import com.example.accounting.domain.account.AccountRepository
import com.example.accounting.domain.account.AccountType
import com.example.accounting.domain.common.DateRange
import com.example.accounting.domain.journal.JournalRepository
import com.example.accounting.domain.profit_and_loss.ProfitAndLoss
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ListProfitAndLossUseCase(
    private val accountRepository: AccountRepository,
    private val journalRepository: JournalRepository,
) {
    fun execute(): Any {
        // 2024年の1月
        val today = LocalDate.of(2024,1,10)
        val range = DateRange.of(
            today.withDayOfMonth(1),
            today.withDayOfMonth(today.lengthOfMonth())
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