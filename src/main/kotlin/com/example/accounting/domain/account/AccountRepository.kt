package com.example.accounting.domain.account

interface AccountRepository {
    fun find(code: AccountCode): Account?
    fun findByName(name: AccountName): Account?
    fun list(): List<Account>
    fun insert(account: Account)
    fun filterByAccountTypes(types: List<AccountType>): List<Account>
}