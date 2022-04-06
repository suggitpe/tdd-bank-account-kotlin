package org.xpdojo.bank.tdd

/**
 * Class to represent a monetary amount.  Should treat this an immutable class.
 * Hint: should this be a data class.
 */
data class Money(val amount: Double) {
    companion object {
        fun anAmountOf(amount: Double) = Money(amount)
    }

    infix fun plus(anAmount: Money) = anAmountOf(amount + anAmount.amount)
    infix fun less(anAmount: Money) = anAmountOf(amount - anAmount.amount)
    infix fun times(factor: Int) = anAmountOf(amount * factor)

    infix fun greaterThan(otherAmount: Money) = amount > otherAmount.amount
    infix fun lessThan(otherAmount: Money) = amount < otherAmount.amount
    infix fun sameAs(otherAmount: Money) = amount == otherAmount.amount

}