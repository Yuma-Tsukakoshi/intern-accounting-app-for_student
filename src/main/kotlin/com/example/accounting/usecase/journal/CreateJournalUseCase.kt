package com.example.accounting.usecase.journal

import com.example.accounting.domain.account.*
import com.example.accounting.domain.journal.Journal
import com.example.accounting.domain.journal.JournalDate
import com.example.accounting.domain.journal.JournalDetail
import com.example.accounting.domain.journal.JournalDetailAmount
import com.example.accounting.domain.journal.JournalDetailDebitCreditType
import com.example.accounting.domain.journal.JournalRepository
import com.example.accounting.domain.journal.JournalSummary
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CreateJournalUseCase(
    private val accountRepository: AccountRepository,
    private val journalRepository: JournalRepository,
) {
    fun execute(date: LocalDate, summary: String, journalDetails: List<CreateJournalDetailRequest>) {
        // 科目コードが実際に存在するかのチェック
        val accountCodes = accountRepository.list().map{ it.code.value }.toSet()
        if (journalDetails.any{accountCodes.contains(it.accountCode)}) {
            throw RuntimeException("科目コードが科目に存在していません")
        }
        val details = journalDetails.map{
            JournalDetail.create(
                it.debitCreditType,
                JournalDetailAmount.of(it.amount),
                AccountCode.of(it.accountCode),
            )
        }
        val journal = Journal.create(
            JournalDate.of(date),
            JournalSummary.of(summary),
            details,
        )
        journalRepository.insert(journal)
    }
}

class CreateJournalRequest(
    val date: String,
    val summary: String,
    val journalDetails: List<CreateJournalDetailRequest>
)

class CreateJournalDetailRequest(
    val debitCreditType: JournalDetailDebitCreditType,
    val amount: Long,
    val accountCode: String,
)