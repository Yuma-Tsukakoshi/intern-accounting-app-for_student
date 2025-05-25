package com.example.accounting.controller

import PlCategoryDto
import PlDto
import com.example.accounting.usecase.journal.CreateJournalRequest
import com.example.accounting.usecase.journal.CreateJournalUseCase
import com.example.accounting.usecase.journal.ListJournalUseCase
import com.example.accounting.usecase.journal.ListProfitAndLossUseCase
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class ProfitAndLossController(
    private val listProfitAndLossUseCase: ListProfitAndLossUseCase
) {
    @GetMapping("/pl")
    fun list(model: Model): Any {
        val journals = listProfitAndLossUseCase.execute()
        return journals
    }
}