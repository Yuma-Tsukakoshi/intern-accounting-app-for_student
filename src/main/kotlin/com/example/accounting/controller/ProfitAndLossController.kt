package com.example.accounting.controller

import com.example.accounting.usecase.journal.ListProfitAndLossUseCase
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
        @RequestParam month: YearMonth = YearMonth.now()
    ): String {
        val pl = listProfitAndLossUseCase.execute(month)
        model["pl"] = pl
        model["year"] = month.year
        model["month"] = month.monthValue
        model["PL_ENTRIES_BY_SUBJECT"] = objectMapper.writeValueAsString(
            pl.profit.subjects.plus(pl.loss.subjects).associate {
                it.accountName.value to it.entries.map { e ->
                    mapOf(
                        "date" to e.date.value.toString(),
                        "type" to e.debitCreditType.value,
                        "amount" to e.amount.value,
                        "summary" to e.summary.value
                    )
                }
            }
        )
        return "pl"
    }
}