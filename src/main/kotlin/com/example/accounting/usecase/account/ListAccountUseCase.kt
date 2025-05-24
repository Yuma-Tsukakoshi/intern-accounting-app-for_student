package com.example.accounting.usecase.account

import com.example.accounting.domain.account.Account
import com.example.accounting.domain.account.AccountRepository
import org.springframework.stereotype.Service

@Service
class ListAccountUseCase(
    private val accountRepository: AccountRepository,
) {
    fun execute(): List<Account> {
        return accountRepository.list()
    }
}