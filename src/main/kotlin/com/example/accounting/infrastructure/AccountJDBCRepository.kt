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
                    AccountType.of(accountRecord[ACCOUNTS.ACCOUNT_TYPE])
                )
            }
    }

    override fun findByName(name: AccountName): Account? {
        return jooq.selectFrom(ACCOUNTS)
            .where(ACCOUNTS.NAME.eq(name.value))
            .fetchOne()
            ?.let { accountRecord ->
                Account.reconstruct(
                    AccountCode.of(accountRecord[ACCOUNTS.ACCOUNT_CODE]),
                    AccountName.of(accountRecord[ACCOUNTS.NAME]),
                    AccountType.of(accountRecord[ACCOUNTS.ACCOUNT_TYPE])
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
                )
            }
    }

    override fun insert(account: Account) {
        jooq.insertInto(ACCOUNTS)
            .set(ACCOUNTS.ACCOUNT_CODE, account.code.value)
            .set(ACCOUNTS.NAME, account.name.value)
            .set(ACCOUNTS.ACCOUNT_TYPE, account.accountType.toString())
            .execute()
    }
}