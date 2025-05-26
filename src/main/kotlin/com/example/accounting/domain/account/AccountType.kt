package com.example.accounting.domain.account

enum class AccountType(val value: String) {
    PROFIT("収益"),
    LOSS("費用"),
    EQUITY("資本"),
    ASSET("資産"),
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