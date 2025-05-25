package com.example.accounting.domain.journal

import com.example.accounting.domain.common.DateRange

interface JournalRepository {
    fun list(): List<Journal>
    fun insert(journal: Journal)
    fun filterByDateRange(dateRange: DateRange): List<Journal>
}