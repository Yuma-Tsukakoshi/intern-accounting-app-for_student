package com.example.accounting.domain.journal

data class JournalDetailAmount(
    val value: Long
) {
    companion object {
        private const val maxValue = 9_999_999_999

        fun of(value: Long): JournalDetailAmount {
            validate(value)
            return JournalDetailAmount(value)
        }

        private fun validate(value: Long) {
            if (value <= 0){
                throw RuntimeException("金額は正の数で入力してください")
            }
            if (value > maxValue){
                throw RuntimeException("金額は${maxValue}以下で入力してください")
            }
        }
    }
}