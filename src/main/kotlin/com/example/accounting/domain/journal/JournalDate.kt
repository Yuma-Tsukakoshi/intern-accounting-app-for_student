package com.example.accounting.domain.journal

class JournalDate(
    val value: Int
) {
    companion object{
        fun of(value: Int): JournalDate {
            validate(value)
            return JournalDate(value)
        }

        private fun validate(value: Int) {
            if (value < 0){
                throw RuntimeException("有効な日付ではありません。")
            }
        }
    }
}