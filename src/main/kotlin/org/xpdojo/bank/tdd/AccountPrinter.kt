package org.xpdojo.bank.tdd

import org.xpdojo.bank.tdd.Account.Transaction
import java.io.PrintStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AccountPrinter(private val stream: PrintStream) {
    private val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

    fun printBalanceSlipFor(account: Account, dateTime: LocalDateTime) {
        stream.println(
            """
            **************************************
            **             Dojo Bank            **
            **           Balance Slip           **
            **************************************
               Date:    ${dateTime.format(dateFormat)} 
               Balance: ${account.balance().amount}
            **************************************
            **   Thank you for using Dojo Bank  **
            ************************************** """
        )
    }

    fun printStatementFor(account: Account, dateTime: LocalDateTime, filter: (Transaction) -> Boolean) {

        fun printStatementLineFor(transaction: Transaction) {
            stream.println("                  ${transaction.dateTime.format(dateFormat)} ${transaction.direction} ${transaction.balanceEffect().amount.toString().padStart(6)}")
        }

        stream.println(
            """
            ***************************************************
            **                    Dojo Bank                  **
            **               Account Statement               **
            ***************************************************
               Date: ${dateTime.format(dateFormat)}
               Balance: ${account.balance().amount}
            ***************************************************
               
               Transaction History:"""
        )

        account.transactions.filter(filter).forEach { printStatementLineFor(it) }

        stream.println(
            """
            ***************************************************
            **         Thank you for using Dojo Bank         **
            *************************************************** """
        )
    }
}
