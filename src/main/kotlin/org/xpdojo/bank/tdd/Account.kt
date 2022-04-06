package org.xpdojo.bank.tdd

import org.xpdojo.bank.tdd.Account.Transaction.Companion.aDepositOf
import org.xpdojo.bank.tdd.Account.Transaction.Companion.aWithDrawlOf
import org.xpdojo.bank.tdd.Account.Transaction.DIRECTION.DEPOSIT
import org.xpdojo.bank.tdd.Account.Transaction.DIRECTION.WITHDRAWAL
import org.xpdojo.bank.tdd.Money.Companion.anAmountOf
import java.io.PrintStream
import java.time.LocalDateTime
import java.time.LocalDateTime.now

/**
 * Represents a bank account.  You can do things to this class like deposit, withdraw and transfer.
 */
class Account(private val openingBalance: Money = anAmountOf(0.0)) {

    internal var transactions = listOf(aDepositOf(openingBalance))

    companion object {
        fun accountWithOpeningBalanceOf(balance: Money): Account = Account(balance)
    }

    fun balance() = transactions.fold(anAmountOf(0.0)) { sum, element -> sum plus element.balanceEffect() }

    infix fun deposit(anAmount: Money) {
        transactions = transactions + aDepositOf(anAmount)
    }

    infix fun withdraw(anAmount: Money) {
        if (anAmount greaterThan balance()) throw IllegalStateException("Not enough funds")
        transactions = transactions + aWithDrawlOf(anAmount)
    }

    fun transfer(amount: Money) = TransferObject(amount)

    fun printBalanceSlipTo(stream: PrintStream, dateTime: LocalDateTime) = AccountPrinter(stream).printBalanceSlipFor(this, dateTime)

    fun printStatementTo(stream: PrintStream, dateTime: LocalDateTime) = AccountPrinter(stream).printStatementFor(this, dateTime)

    inner class TransferObject(private val amountToTransfer: Money) {
        fun into(receiver: Account) {
            withdraw(amountToTransfer)
            receiver deposit amountToTransfer
        }
    }

    data class Transaction(val amount: Money, val direction: DIRECTION, val dateTime: LocalDateTime) {
        enum class DIRECTION { DEPOSIT, WITHDRAWAL }
        companion object {
            fun aDepositOf(amount: Money) = Transaction(amount, DEPOSIT, now())
            fun aWithDrawlOf(amount: Money) = Transaction(amount, WITHDRAWAL, now())
        }

        fun balanceEffect() = when (direction) {
            DEPOSIT -> amount
            WITHDRAWAL -> amount times -1
        }

    }
}