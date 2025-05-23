package com.example.accounting.infrastructure

import com.example.accounting.domain.account.*
import jooq.tables.Accounts.ACCOUNTS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class AccountJDBCRepository(
    private val jooq: DSLContext,
) : AccountRepository {
    override fun find(code: AccountCode): Account? {
        return jooq.selectFrom(ACCOUNTS)
            .where(ACCOUNTS.ACCOUNT_CODE.eq(code.value))
            .fetchOne()
            ?.let { accountRecord ->
                Account.reconstruct(
                    AccountCode.of(accountRecord[ACCOUNTS.ACCOUNT_CODE]),
                    AccountName.of(accountRecord[ACCOUNTS.NAME]),
                    AccountType.of(accountRecord[ACCOUNTS.ACCOUNT_TYPE]),
                    accountRecord[ACCOUNTS.PARENT_ACCOUNT_CODE]?.let { AccountCode.of(it) }
                )
            }
    }

    override fun list(): List<Account> {
        return jooq.selectFrom(ACCOUNTS)
            .orderBy(ACCOUNTS.ACCOUNT_CODE, ACCOUNTS.PARENT_ACCOUNT_CODE)
            .fetch()
            .map { accountRecord ->
                Account.reconstruct(
                    AccountCode.of(accountRecord[ACCOUNTS.ACCOUNT_CODE]),
                    AccountName.of(accountRecord[ACCOUNTS.NAME]),
                    AccountType.of(accountRecord[ACCOUNTS.ACCOUNT_TYPE]),
                    accountRecord[ACCOUNTS.PARENT_ACCOUNT_CODE]?.let { AccountCode.of(it) }
                )
            }
    }

    override fun insert(account: Account) {
        jooq.insertInto(ACCOUNTS)
            .set(ACCOUNTS.ACCOUNT_CODE, account.code.value)
            .set(ACCOUNTS.NAME, account.name.value)
            .set(ACCOUNTS.ACCOUNT_TYPE, account.accountType.toString())
            .set(ACCOUNTS.PARENT_ACCOUNT_CODE, account.parentCode?.value)
            .execute()
    }
}