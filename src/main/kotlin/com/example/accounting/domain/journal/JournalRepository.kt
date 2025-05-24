package com.example.accounting.domain.journal

interface JournalRepository {
    fun list(): List<Journal>
    fun insert(journal: Journal)
}