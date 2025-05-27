package com.example.accounting.controller

import com.example.accounting.domain.account.AccountType
import com.example.accounting.usecase.account.CreateAccountRequest
import com.example.accounting.usecase.account.CreateAccountUseCase
import com.example.accounting.usecase.account.ListAccountUseCase
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class AccountController(
    private val listUseCase: ListAccountUseCase,
    private val createUseCase: CreateAccountUseCase,
) {
    @GetMapping("/accounts")
    fun list(
        @RequestParam(required = false) flash: String?,
        model: Model
    ): String {
        model["accounts"] = listUseCase.execute()
        if (!flash.isNullOrBlank()) model["flash"] = flash
        return "accounts/list"
    }

    @GetMapping("/accounts/new")
    fun newForm(
        model: Model,
        @ModelAttribute("form") form: CreateAccountRequest = CreateAccountRequest()
    ): String {
        model["form"] = form
        model["accountTypes"] = AccountType.entries
        return "accounts/new"
    }

    @PostMapping("/accounts")
    fun create(
        @ModelAttribute form: CreateAccountRequest,
        redirect: RedirectAttributes
    ): String {
        return try {
            createUseCase.execute(form.code, form.name, form.accountType)
            redirect.addAttribute("flash", "科目「${form.name}」を登録しました")
            "redirect:/accounts"
        } catch (e: Exception) {
            redirect.addFlashAttribute("error", e.message ?: "不明なエラーが発生しました")
            redirect.addFlashAttribute("form", form)
            "redirect:/accounts/new"
        }
    }
}