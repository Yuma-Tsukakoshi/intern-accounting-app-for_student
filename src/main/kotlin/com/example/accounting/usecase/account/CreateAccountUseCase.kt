package com.example.accounting.usecase.account

import com.example.accounting.domain.account.*
import org.springframework.stereotype.Service

@Service
class CreateAccountUseCase(
    private val accountRepository: AccountRepository,
) {
    fun execute(code: String, name: String, accountType: AccountType) {
        // 科目コードの重複をAccount自身に問い合わせるのは不自然なので、ここでチェックをしている
        // より良い方法としてDomainServiceを作成してそこで重複チェックを行う方法もある
        if (accountRepository.find(AccountCode.of(code)) != null) {
            throw RuntimeException("科目コードが重複しています")
        }
        if (accountRepository.findByName(AccountName.of(name)) != null) {
            throw RuntimeException("科目名が重複しています")
        }
        val account = Account.create(
            AccountCode.of(code),
            AccountName.of(name),
            accountType,
        )
        accountRepository.insert(account)
    }
}

data class CreateAccountRequest(
    var code: String = "",
    var name: String = "",
    var accountType: AccountType = AccountType.PROFIT
)