package com.example.accounting.domain.journal

interface JournalRepository {
    fun find(id: Int): Journal?
    fun list(): List<Journal>
    fun insert(journal: Journal)
}