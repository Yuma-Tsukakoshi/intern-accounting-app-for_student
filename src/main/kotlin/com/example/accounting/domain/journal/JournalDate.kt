package com.example.accounting.domain.journal

import java.time.LocalDate

data class JournalDate(
    val value: LocalDate
) {
    companion object{
        fun of(value: LocalDate): JournalDate {
            return JournalDate(value)
        }
    }
}