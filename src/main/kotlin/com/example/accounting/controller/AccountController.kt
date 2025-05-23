package com.example.accounting.controller

import com.example.accounting.usecase.CreateAccountRequest
import com.example.accounting.usecase.CreateAccountUseCase
import com.example.accounting.usecase.ListAccountUseCase
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class AccountController(
    private val listUseCase: ListAccountUseCase,
    private val createUseCase: CreateAccountUseCase,
) {
    @GetMapping("/accounts")
    fun list(model: Model): String {
        val accounts = listUseCase.execute()
        model["accounts"] = accounts
        return "accounts"
    }

    @PostMapping("/accounts")
    fun create(@RequestBody params: CreateAccountRequest): ResponseEntity<String> {
        createUseCase.execute(params.code, params.name, params.accountType, params.parentCode)
        return ResponseEntity.ok("ok")
    }
}