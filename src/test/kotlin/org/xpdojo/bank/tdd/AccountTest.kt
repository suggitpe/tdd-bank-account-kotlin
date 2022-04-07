package org.xpdojo.bank.tdd

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.openMocks
import org.xpdojo.bank.tdd.Account.Companion.accountWithOpeningBalanceOf
import org.xpdojo.bank.tdd.Account.Transaction.DIRECTION.DEPOSIT
import org.xpdojo.bank.tdd.Money.Companion.anAmountOf
import java.io.PrintStream
import java.time.LocalDateTime.now

/**
 * Requirements:
 *  I can Deposit money to accounts
 *  I can Withdraw money from accounts
 *  I can Transfer amounts between accounts (if I have the funds)
 *  I can print out an Account balance slip (date, time, balance)
 *  I can print a statement of account activity (statement)
 *  I can apply Statement filters (include just deposits, withdrawal, date)
 */
@DisplayName("With an account we can ...")
internal class AccountTest {

    @Mock
    private lateinit var mockStream: PrintStream

    @BeforeEach
    fun setup() {
        openMocks(this)
    }

    @Test
    fun `deposit an amount to increase the balance`() {
        val account = accountWithOpeningBalanceOf(anAmountOf(10.0))
        account deposit anAmountOf(15.0)
        account.balance() shouldBe anAmountOf(25.0)
    }

    @Test
    fun `withdraw an amount to reduce the balance`() {
        val account = accountWithOpeningBalanceOf(anAmountOf(55.0))
        account withdraw anAmountOf(20.0)
        account.balance() shouldBe anAmountOf(35.0)
    }

    @Test
    fun `attempting to withdraw without funds causes exception`() {
        val account = accountWithOpeningBalanceOf(anAmountOf(10.0))
        shouldThrowAny {
            account withdraw anAmountOf(25.0)
        }
    }

    @Test
    fun `transfer between accounts`() {
        val sender = accountWithOpeningBalanceOf(anAmountOf(50.0))
        val receiver = accountWithOpeningBalanceOf(anAmountOf(10.0))
        sender.transfer(anAmountOf(30.0)).into(receiver)
        sender.balance() shouldBe anAmountOf(20.0)
        receiver.balance() shouldBe anAmountOf(40.0)
    }

    @Test
    fun `attempting to transfer without funds causes exception`() {
        val sender = accountWithOpeningBalanceOf(anAmountOf(50.0))
        val receiver = accountWithOpeningBalanceOf(anAmountOf(10.0))
        shouldThrowAny {
            sender.transfer(anAmountOf(130.0)).into(receiver)
        }
    }

    @Test
    fun `process a number of transactions on an account`() {
        val account = accountWithOpeningBalanceOf(anAmountOf(100.0))

        account.deposit(anAmountOf(220.0))
        account.deposit(anAmountOf(50.0))
        account.deposit(anAmountOf(75.0))
        account.withdraw(anAmountOf(225.0))
        account.withdraw(anAmountOf(25.0))
        account.deposit(anAmountOf(5.0))

        account.balance() shouldBe anAmountOf(200.0)

        shouldThrowAny {
            account.withdraw(anAmountOf(220.0))
        }
    }

    @Test
    fun `print a balance slip`() {
        val account = accountWithOpeningBalanceOf(anAmountOf(100.0))
        account.printBalanceSlipTo(mockStream, now())
        verify(mockStream, times(1)).println(anyString())
        account.printBalanceSlipTo(PrintStream(System.out), now())
    }

    @Test
    fun `print a statement`() {
        val account = accountWithOpeningBalanceOf(anAmountOf(100.0))
        account.deposit(anAmountOf(200.0))
        account.deposit(anAmountOf(300.00))
        account.withdraw(anAmountOf(290.0))
        account.printStatementTo(mockStream, now())
        verify(mockStream, times(6)).println(anyString())
        account.printStatementTo(PrintStream(System.out), now())
    }

    @Test
    fun `print a filtered statement of deposits only`() {
        val account = accountWithOpeningBalanceOf(anAmountOf(100.0))
        account.deposit(anAmountOf(150.0))
        account.withdraw(anAmountOf(30.0))
        account.withdraw(anAmountOf(35.0))
        account.printStatementTo(mockStream, now()) { it.direction == DEPOSIT }
        verify(mockStream, times(4)).println(anyString())
        account.printStatementTo(PrintStream(System.out), now()) { it.direction == DEPOSIT }
    }
}