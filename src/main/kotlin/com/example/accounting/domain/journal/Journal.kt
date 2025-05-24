package com.example.accounting.domain.journal

class Journal(
    val id: Int?,
    val date: JournalDate,
    val summary: JournalSummary,
    val journalDetails: List<JournalDetail>
) {
    companion object {
        fun create(
            id: Int?,
            date: JournalDate,
            summary: JournalSummary,
            journalDetails: List<JournalDetail>
        ): Journal {
            validate()
            return Journal(id, date, summary, journalDetails)
        }

        fun reconstruct(
            id: Int,
            date: JournalDate,
            summary: JournalSummary,
            journalDetails: List<JournalDetail>
        ): Journal {
            return Journal(id, date, summary, journalDetails)
        }

        private fun validate() {
            // 現状なし
        }
    }
}