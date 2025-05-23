package com.example.accounting.domain.account

class AccountName(
    val value: String
) {
    companion object {
        private const val maxLength = 50

        fun of(value: String): AccountName {
            validate(value)
            return AccountName(value)
        }

        private fun validate(value: String) {
            if (value.isBlank() || value.isEmpty()) {
                throw RuntimeException("科目名は空欄にできません")
            }
            if (value.length > maxLength) {
                throw RuntimeException("科目名は${maxLength}文字以内で入力してください")
            }
        }
    }

}