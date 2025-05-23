package com.example.accounting.domain.account

class Account(
    val code: AccountCode,
    val name: AccountName,
    val accountType: AccountType,
    val parentCode: AccountCode?,
) {
    companion object {
        fun create(
            code: AccountCode,
            name: AccountName,
            accountType: AccountType,
            parentAccount: Account?,
        ): Account {
            validate(parentAccount, accountType)
            return Account(code, name, accountType, parentAccount?.code)
        }

        fun reconstruct(
            code: AccountCode,
            name: AccountName,
            accountType: AccountType,
            parentCode: AccountCode?,
        ): Account {
            return Account(code, name, accountType, parentCode)
        }

        private fun validate(parentAccount: Account?, accountType: AccountType) {
            if (parentAccount?.parentCode != null) {
                throw RuntimeException("科目は第二階層までしか設定できません")
            }
            if (parentAccount != null && parentAccount.accountType != accountType) {
                throw RuntimeException("親科目と同じ種類の科目しか設定できません")
            }
        }
    }
}