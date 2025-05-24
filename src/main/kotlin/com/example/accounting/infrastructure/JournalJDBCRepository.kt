package com.example.accounting.infrastructure

import com.example.accounting.domain.account.*
import com.example.accounting.domain.journal.Journal
import com.example.accounting.domain.journal.JournalDate
import com.example.accounting.domain.journal.JournalDetail
import com.example.accounting.domain.journal.JournalDetailAmount
import com.example.accounting.domain.journal.JournalDetailDebitCreditType
import com.example.accounting.domain.journal.JournalRepository
import com.example.accounting.domain.journal.JournalSummary
import jooq.tables.JournalDetails.JOURNAL_DETAILS
import jooq.tables.Journals.JOURNALS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class JournalJDBCRepository(
    private val jooq: DSLContext,
) : JournalRepository {
    override fun list(): List<Journal> {
        return jooq
            .select(
                JOURNALS.ID,
                JOURNALS.DATE,
                JOURNALS.SUMMARY,
                JOURNAL_DETAILS.ID,
                JOURNAL_DETAILS.JOURNAL_ID,
                JOURNAL_DETAILS.ACCOUNT_CODE,
                JOURNAL_DETAILS.DEBIT_CREDIT_TYPE,
                JOURNAL_DETAILS.AMOUNT
            )
            .from(JOURNALS)
            .leftJoin(JOURNAL_DETAILS).on(JOURNAL_DETAILS.JOURNAL_ID.eq(JOURNALS.ID))
            .orderBy(JOURNALS.ID, JOURNAL_DETAILS.ID)
            .fetch()
            .groupBy { it.get(JOURNALS.ID) }
            .map { (_, group) ->
                val first = group.first()
                val details = group.mapNotNull { rec ->
                    rec.get(JOURNAL_DETAILS.ID)?.let { detailId ->
                        JournalDetail.reconstruct(
                            detailId,
                            JournalDetailDebitCreditType.of(rec.get(JOURNAL_DETAILS.DEBIT_CREDIT_TYPE)),
                            JournalDetailAmount.of(rec.get(JOURNAL_DETAILS.AMOUNT)),
                            AccountCode.of(rec.get(JOURNAL_DETAILS.DEBIT_CREDIT_TYPE)),
                        )
                    }
                }
                Journal.reconstruct(
                    first.get(JOURNALS.ID),
                    JournalDate.of(first.get(JOURNALS.DATE)),
                    JournalSummary.of(first.get(JOURNALS.SUMMARY)),
                    details
                )
            }
    }

    override fun insert(journal: Journal) {
        val journalId = jooq.insertInto(JOURNALS)
            .set(JOURNALS.DATE, journal.date.value)
            .set(JOURNALS.SUMMARY, journal.summary.value)
            .returning(JOURNALS.ID)
            .fetchOne()!!
            .get(JOURNALS.ID)

        val batchInserts = journal.details.map { detail ->
            jooq.insertInto(JOURNAL_DETAILS)
                .set(JOURNAL_DETAILS.JOURNAL_ID, journalId)
                .set(JOURNAL_DETAILS.ACCOUNT_CODE, detail.accountCode.value)
                .set(JOURNAL_DETAILS.DEBIT_CREDIT_TYPE, detail.debitCreditType.name)
                .set(JOURNAL_DETAILS.AMOUNT, detail.amount.value)
        }.toTypedArray()

        if (batchInserts.isNotEmpty()) {
            jooq.batch(*batchInserts).execute()
        }
    }
}