package com.example.accounting.domain.account

enum class AccountType {
    PROFIT,
    LOSS,
    ASSET,
    LIABILITY;

    companion object {
        fun of(value: String): AccountType {
            return when (value) {
                "PROFIT" -> PROFIT
                "LOSS" -> LOSS
                "ASSET" -> ASSET
                "LIABILITY" -> LIABILITY
                else -> throw RuntimeException("PROFIT, LOSS, ASSET, LIABILITYで入力してください")
            }
        }
    }
}