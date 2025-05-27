package com.example.accounting.controller

import com.example.accounting.domain.common.DateRange
import com.example.accounting.usecase.profit_and_loss.ListProfitAndLossUseCase
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.YearMonth

@Controller
class ProfitAndLossController(
    private val listProfitAndLossUseCase: ListProfitAndLossUseCase,
    private val objectMapper: ObjectMapper
) {
    @GetMapping("/pl")
    fun list(
        model: Model,
        @RequestParam lastMonthFrom: YearMonth?,
        @RequestParam lastMonthTo: YearMonth?,
        @RequestParam currentMonthFrom: YearMonth = YearMonth.now(),
        @RequestParam currentMonthTo: YearMonth = YearMonth.now()
    ): String {

        val lastRange: DateRange? = if (lastMonthFrom != null && lastMonthTo != null) {
            DateRange(from=lastMonthFrom.atDay(1), to=  lastMonthTo.atEndOfMonth())
        } else {
            null
        }

        val currentRange: DateRange = DateRange(from = currentMonthFrom.atDay(1),to = currentMonthTo.atEndOfMonth())

        val pl = listProfitAndLossUseCase.execute(lastRange, currentRange)
        model["pl"]             = pl
        val fromMonth = lastMonthFrom?.toString()?.let { "${it}" } ?: ""
        val toMonth = lastMonthTo?.toString()?.let { "${it}" } ?: ""

        model["lastMonthLabel"]    = fromMonth + "-" + toMonth
        model["currentMonthLabel"] = currentMonthFrom.toString() + "〜" + currentMonthTo.toString()

        // フォームのデフォルト入力値として渡す
        model["lastYear"]       = lastMonthFrom?.year ?: ""
        model["lastMonthVal"]   = lastMonthFrom?.monthValue ?: ""
        model["currentYear"]    = currentMonthFrom.year
        model["currentMonthVal"]= currentMonthFrom.monthValue

        // JS 用：明細の内訳を Map<科目名, List<明細>> として JSON 変換
        model["PL_ENTRIES_BY_SUBJECT"] = objectMapper.writeValueAsString(
            (pl.profit.subjects + pl.loss.subjects).associate { subj ->
                subj.accountName.value to mapOf(
                    "last"    to (subj.last?.entries?.map { e ->
                        mapOf("date" to e.date.value.toString(), "type" to e.debitCreditType.value,
                            "amount" to e.amount.value, "summary" to e.summary.value)
                        } ?: emptyList()),
                    "current" to subj.current.entries.map { e ->
                        mapOf("date" to e.date.value.toString(), "type" to e.debitCreditType.value,
                            "amount" to e.amount.value, "summary" to e.summary.value)
                    }
                )
            }
        )

        return "pl"
    }
}