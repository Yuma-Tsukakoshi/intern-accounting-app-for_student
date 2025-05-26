package com.example.accounting.controller

import com.example.accounting.usecase.journal.ListProfitAndLossUseCase
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ProfitAndLossController(
    private val listProfitAndLossUseCase: ListProfitAndLossUseCase
) {
    @GetMapping("/pl")
    fun list(model: Model): String {
        val pl = listProfitAndLossUseCase.execute()
        model["pl"] = pl
        return "pl"
    }
}