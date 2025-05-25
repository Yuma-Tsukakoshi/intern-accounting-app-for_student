package com.example.accounting.domain.account

enum class AccountType {
    // 収益
    PROFIT,
    // 費用
    LOSS,
    // 資本
    EQUITY,
    // 資産
    ASSET,
    // 負債
    LIABILITY;

    companion object {
        fun of(value: String): AccountType {
            return when (value) {
                "PROFIT" -> PROFIT
                "LOSS" -> LOSS
                "EQUITY" -> EQUITY
                "ASSET" -> ASSET
                "LIABILITY" -> LIABILITY
                else -> throw RuntimeException("PROFIT, LOSS, EQUITY, ASSET, LIABILITYで入力してください")
            }
        }

        fun getListForPL():List<AccountType> {
            return listOf(
                PROFIT,
                LOSS,
            )
        }
    }
}