package com.example.accounting.domain.account

data class Account private constructor(
    val code: AccountCode,
    val name: AccountName,
    val accountType: AccountType,
) {
    companion object {
        fun create(
            code: AccountCode,
            name: AccountName,
            accountType: AccountType,
        ): Account {
            validate()
            return Account(code, name, accountType)
        }

        fun reconstruct(
            code: AccountCode,
            name: AccountName,
            accountType: AccountType,
        ): Account {
            return Account(code, name, accountType)
        }

        private fun validate() {
            // 現状なし
        }
    }
}