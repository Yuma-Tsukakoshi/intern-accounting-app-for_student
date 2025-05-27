package com.example.accounting.controller

import com.example.accounting.usecase.journal.CreateJournalRequest
import com.example.accounting.usecase.journal.CreateJournalUseCase
import com.example.accounting.usecase.journal.ListJournalUseCase
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@Controller
class JournalController(
    private val listUseCase: ListJournalUseCase,
    private val createUseCase: CreateJournalUseCase,
) {
    @GetMapping("/journals")
    fun list(
        model: Model,
        @RequestParam fromDate: LocalDate = LocalDate.now().withDayOfMonth(1),
        @RequestParam toDate: LocalDate = LocalDate.now()
    ): String {
        val journals = listUseCase.execute(
            fromDate = fromDate,
            toDate = toDate
        )
        model["fromDate"] = fromDate
        model["toDate"] = toDate

        model["journals"] = journals
        return "journals"
    }

    @PostMapping("/journals")
    fun create(@RequestBody params: CreateJournalRequest): ResponseEntity<String> {
        createUseCase.execute(params.date, params.summary, params.journalDetails)
        return ResponseEntity.ok("ok")
    }
}