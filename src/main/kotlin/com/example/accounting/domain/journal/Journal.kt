package com.example.accounting.domain.journal
data class Journal(
    val id: Int?,
    val date: JournalDate,
    val summary: JournalSummary,
    val details: List<JournalDetail>
) {
    companion object {
        private const val minJournalDetailSize = 2
        fun create(
            date: JournalDate,
            summary: JournalSummary,
            details: List<JournalDetail>
        ): Journal {
            validate(details)
            return Journal(null, date, summary, details)
        }

        fun reconstruct(
            id: Int,
            date: JournalDate,
            summary: JournalSummary,
            details: List<JournalDetail>
        ): Journal {
            return Journal(id, date, summary, details)
        }

        private fun validate(
            details: List<JournalDetail>
        ) {
            // detailを2つ以上持つ
            if (details.size < minJournalDetailSize){
                throw RuntimeException("仕訳明細は${minJournalDetailSize}つ以上入力してください")
            }
            // detailsのDebitとCreditの合計は一致する
            val debitSum = details.filter { it.debitCreditType === JournalDetailDebitCreditType.DEBIT}.sumOf{it.amount.value}
            val creditSum = details.filter { it.debitCreditType === JournalDetailDebitCreditType.CREDIT}.sumOf{it.amount.value}

            if (debitSum != creditSum) {
                throw RuntimeException("借方、貸方の合計が一致しません")
            }
        }
    }
}