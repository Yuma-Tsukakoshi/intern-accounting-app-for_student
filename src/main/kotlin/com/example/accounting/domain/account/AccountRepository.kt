package com.example.accounting.domain.account

interface AccountRepository {
    fun find(code: AccountCode): Account?
    fun list(): List<Account>
    fun insert(account: Account)
}