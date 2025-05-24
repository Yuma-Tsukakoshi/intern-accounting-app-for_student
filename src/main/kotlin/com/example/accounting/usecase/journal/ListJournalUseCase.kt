package com.example.accounting.usecase.journal

import com.example.accounting.domain.account.Account
import com.example.accounting.domain.account.AccountRepository
import com.example.accounting.domain.journal.Journal
import com.example.accounting.domain.journal.JournalRepository
import org.springframework.stereotype.Service

@Service
class ListJournalUseCase(
    private val journalRepository: JournalRepository,
) {
    fun execute(): List<Journal> {
        return journalRepository.list()
    }
}