package com.example.accounting.domain.account

class AccountCode(
    val value: String
) {
    companion object {
        private const val length = 4

        fun of(value: String): AccountCode {
            validate(value)
            return AccountCode(value)
        }

        private fun validate(value: String) {
            if (value.isBlank() || value.isEmpty()) {
                throw RuntimeException("科目コードは空欄にできません")
            }
            if (value.length != length) {
                throw RuntimeException("科目コードは${length}文字で入力してください")
            }
            if (!value.matches(Regex("^[0-9]{4}$"))) {
                throw RuntimeException("科目コードは4桁の数字で入力してください")
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as AccountCode

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value
    }
}