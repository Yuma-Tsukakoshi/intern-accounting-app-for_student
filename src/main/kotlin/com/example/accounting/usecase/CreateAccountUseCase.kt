package com.example.accounting.usecase

import com.example.accounting.domain.account.*
import org.springframework.stereotype.Service

@Service
class CreateAccountUseCase(
    private val accountRepository: AccountRepository,
) {
    fun execute(code: String, name: String, accountType: AccountType, parentCode: String?) {
        val parentAccount = parentCode?.let { accountRepository.find(AccountCode.of(it)) ?: throw RuntimeException("親科目が指定されていますが見つかりません") }
        // 科目コードの重複をAccount自身に問い合わせるのは不自然なので、ここでチェックをしている
        // より良い方法としてDomainServiceを作成してそこで重複チェックを行う方法もある
        if (accountRepository.find(AccountCode.of(code)) != null) {
            throw RuntimeException("科目コードが重複しています")
        }
        val account = Account.create(
            AccountCode.of(code),
            AccountName.of(name),
            accountType,
            parentAccount,
        )
        accountRepository.insert(account)
    }
}

class CreateAccountRequest(
    val code: String,
    val name: String,
    val accountType: AccountType,
    val parentCode: String?,
)