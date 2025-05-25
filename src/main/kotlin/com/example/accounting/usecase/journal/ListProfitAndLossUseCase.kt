package com.example.accounting.usecase.journal

import PlCategoryDto
import PlDto
import PlEntryDto
import PlSubjectDto
import com.example.accounting.domain.account.AccountRepository
import com.example.accounting.domain.account.AccountType
import com.example.accounting.domain.common.DateRange
import com.example.accounting.domain.journal.JournalRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ListProfitAndLossUseCase(
    private val accountRepository: AccountRepository,
    private val journalRepository: JournalRepository,
) {
    fun execute(): PlDto {
        // 2024年の1月

        val today = LocalDate.of(2024,1,10)
        val range = DateRange.of(
            today.withDayOfMonth(1),
            today.withDayOfMonth(today.lengthOfMonth())
        )

        /* ② P/L に載せる科目(収益・費用)を取得 */
        val plTypes = AccountType.getListForPL()          // [PROFIT, LOSS]
        val plAccounts = accountRepository.filterByAccountTypes(plTypes)
        val accountMap = plAccounts.associateBy { it.code }  // AccountCode → Account
        val accountTypeMap = plAccounts.groupBy { it.accountType }

        /* ③ 今月の仕訳を取得 */
        val journals = journalRepository.filterByDateRange(range)

        /* ④ 明細単位にフラット化し、PL 用 DTO に再構築 */
        val detailDtos = journals.flatMap { j ->
            j.details.map { d -> j to d }     // Pair<Journal, JournalDetail>
        }.filter { (_, d) ->                  // P/L 科目だけ残す
            accountMap.containsKey(d.accountCode)
        }

        /* ⑤ AccountType→Account→明細 の 3 階層に組み直す */
        val plDtos =  plTypes.map { cat ->
            val accountsInCat = accountTypeMap[cat]!!

            /* 科目ごとに明細をまとめる */
            val subjectDtos = accountsInCat.map { acc ->

                val entries = detailDtos
                    .filter { (_, d) -> d.accountCode == acc.code }
                    .map { (j, d) ->
                        PlEntryDto(
                            date = j.date.value,
                            debitCreditType = d.debitCreditType.name,
                            amount = d.amount.value,
                            summary = j.summary.value
                        )
                    }

                PlSubjectDto(
                    accountName = acc.name.value,
                    totalAmount = entries.sumOf { it.amount },
                    entries = entries
                )
            }

            PlCategoryDto(
                category = cat,
                subjects = subjectDtos,
                totalAmount = subjectDtos.sumOf { it.totalAmount }
            )
        }

        val profitDto = plDtos.find{it.category == AccountType.PROFIT}!!
        val lossDto = plDtos.find{it.category == AccountType.LOSS}!!
        val benefitSum =  profitDto.totalAmount - lossDto.totalAmount

        val result =  PlDto(
            profit = profitDto,
            loss = lossDto,
            benefit = benefitSum
        )
        return result
    }
}