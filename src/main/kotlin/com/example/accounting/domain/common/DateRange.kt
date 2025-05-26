package com.example.accounting.domain.common

import java.time.LocalDate

data class DateRange(
    val from: LocalDate,
    val to: LocalDate,
) {
    companion object {
        fun of(from: LocalDate, to: LocalDate): DateRange {
            validate(from,to)
            return DateRange(from, to)
        }

        private fun validate(from: LocalDate, to: LocalDate) {
            if (!from.isBefore(to)) {
                throw RuntimeException("開始日は終了日より前に設定してください。")
            }
        }
    }
}
