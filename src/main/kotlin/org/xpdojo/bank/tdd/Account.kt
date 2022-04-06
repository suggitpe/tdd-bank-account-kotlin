package org.xpdojo.bank.tdd

import org.xpdojo.bank.tdd.Account.Transaction.Companion.aDepositOf
import org.xpdojo.bank.tdd.Account.Transaction.Companion.aWithDrawlOf
import org.xpdojo.bank.tdd.Account.Transaction.DIRECTION.DEPOSIT
import org.xpdojo.bank.tdd.Account.Transaction.DIRECTION.WITHDRAWAL
import org.xpdojo.bank.tdd.Money.Companion.anAmountOf

/**
 * Represents a bank account.  You can do things to this class like deposit, withdraw and transfer.
 */
class Account(private val openingBalance: Money = anAmountOf(0.0)) {

    private var transactions = listOf(Transaction(openingBalance, DEPOSIT))

    companion object {
        fun accountWithOpeningBalanceOf(balance: Money): Account = Account(balance)
    }

    fun balance(): Money {
        fun calcBalanceEffect(transaction: Transaction) = if (transaction.direction == DEPOSIT) transaction.amount else transaction.amount times -1
        return transactions.fold(anAmountOf(0.0)) { sum, element -> sum plus calcBalanceEffect(element) }
    }

    infix fun deposit(anAmount: Money) {
        transactions = transactions + aDepositOf(anAmount)
    }

    infix fun withdraw(anAmount: Money) {
        if (anAmount greaterThan balance()) throw IllegalStateException("Not enough funds")
        transactions = transactions + aWithDrawlOf(anAmount)
    }

    fun transfer(amount: Money): TransferObject {
        return TransferObject(amount)
    }

    inner class TransferObject(private val amountToTransfer: Money) {
        fun into(receiver: Account) {
            withdraw(amountToTransfer)
            receiver deposit amountToTransfer
        }
    }

    data class Transaction(val amount: Money, val direction: DIRECTION) {
        enum class DIRECTION { DEPOSIT, WITHDRAWAL }
        companion object {
            fun aDepositOf(amount: Money) = Transaction(amount, DEPOSIT)
            fun aWithDrawlOf(amount: Money) = Transaction(amount, WITHDRAWAL)
        }
    }
}