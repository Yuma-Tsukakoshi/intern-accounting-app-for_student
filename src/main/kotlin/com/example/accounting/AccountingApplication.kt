package com.example.accounting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class AccountingApplication

fun main(args: Array<String>) {
	runApplication<AccountingApplication>(*args)
}
