package com.example.accounting.domain.journal

data class JournalSummary(
    val value: String?
) {
    companion object {
        private const val maxLength = 200

        fun of(value: String?): JournalSummary {
            validate(value)
            return JournalSummary(value)
        }

        private fun validate(value: String?) {
            if (value != null && value.length > maxLength) {
                throw RuntimeException("摘要は${maxLength}文字内で入力してください")
            }
        }
    }
}