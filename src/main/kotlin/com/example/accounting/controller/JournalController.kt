package com.example.accounting.controller

import com.example.accounting.usecase.journal.CreateJournalRequest
import com.example.accounting.usecase.journal.CreateJournalUseCase
import com.example.accounting.usecase.journal.ListJournalUseCase
import com.example.accounting.usecase.profit_and_loss.ListProfitAndLossUseCase
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class JournalController(
    private val listUseCase: ListJournalUseCase,
    private val createUseCase: CreateJournalUseCase,
) {
    @GetMapping("/journals")
    fun list(model: Model): String {
        val journals = listUseCase.execute()
        model["journals"] = journals
        return "journals"
    }

    @PostMapping("/journals")
    fun create(@RequestBody params: CreateJournalRequest): ResponseEntity<String> {
        createUseCase.execute(params.date, params.summary, params.journalDetails)
        return ResponseEntity.ok("ok")
    }
}