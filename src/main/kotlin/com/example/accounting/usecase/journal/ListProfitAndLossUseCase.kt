package com.example.accounting.usecase.journal

import com.example.accounting.domain.account.AccountRepository
import com.example.accounting.domain.account.AccountType
import com.example.accounting.domain.common.DateRange
import com.example.accounting.domain.journal.JournalRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.Date

@Service
class ListProfitAndLossUseCase(
    private val accountRepository: AccountRepository,
    private val journalRepository: JournalRepository,
) {
    fun execute() {
        val today = LocalDate.now()
        val targetDateRange = DateRange.of(
            today.withDayOfMonth(1),
            today.withDayOfMonth(today.lengthOfMonth())
        )

        val targetAccountTypes = AccountType.getListForPL()
        val accounts = accountRepository.filterByAccountTypes(targetAccountTypes)
        val journals = journalRepository.filterByDateRange(targetDateRange)
    }
}