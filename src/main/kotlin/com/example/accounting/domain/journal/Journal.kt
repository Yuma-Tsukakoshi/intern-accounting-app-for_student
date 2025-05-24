package com.example.accounting.domain.journal
data class Journal(
    val id: Int?,
    val date: JournalDate,
    val summary: JournalSummary,
    val journalDetails: List<JournalDetail>
) {
    companion object {
        private const val minJournalDetailSize = 2
        fun create(
            date: JournalDate,
            summary: JournalSummary,
            journalDetails: List<JournalDetail>
        ): Journal {
            validate(journalDetails)
            return Journal(null, date, summary, journalDetails)
        }

        fun reconstruct(
            id: Int,
            date: JournalDate,
            summary: JournalSummary,
            journalDetails: List<JournalDetail>
        ): Journal {
            return Journal(id, date, summary, journalDetails)
        }

        private fun validate(
            journalDetails: List<JournalDetail>
        ) {
            // journalDetailを2つ以上持つ
            if (journalDetails.size < minJournalDetailSize){
                throw RuntimeException("仕訳明細は${minJournalDetailSize}つ以上入力してください")
            }
            // journalDetailsのDebitとCreditの合計は一致する
            val debitSum = journalDetails.filter { it.debitCreditType === JournalDetailDebitCreditType.DEBIT}.sumOf{it.amount.value}
            val creditSum = journalDetails.filter { it.debitCreditType === JournalDetailDebitCreditType.CREDIT}.sumOf{it.amount.value}

            if (debitSum != creditSum) {
                throw RuntimeException("借方、貸方の合計が一致しません")
            }
        }
    }
}