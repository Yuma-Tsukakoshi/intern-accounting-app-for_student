package com.example.accounting.domain.account

enum class AccountType(val value: String) {
    // 収益
    PROFIT("収益"),
    // 費用
    LOSS("費用"),
    // 資本
    EQUITY("資本"),
    // 資産
    ASSET("資産"),
    // 負債
    LIABILITY("負債");

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