package com.example.accounting.controller

import com.example.accounting.usecase.journal.ListProfitAndLossUseCase
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.YearMonth

@Controller
class ProfitAndLossController(
    private val listProfitAndLossUseCase: ListProfitAndLossUseCase
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
        return "pl"
    }
}