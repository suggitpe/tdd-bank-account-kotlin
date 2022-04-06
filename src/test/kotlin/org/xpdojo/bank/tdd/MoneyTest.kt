package org.xpdojo.bank.tdd

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.xpdojo.bank.tdd.Money.Companion.anAmountOf

@DisplayName("With Money we can ...")
internal class MoneyTest {

    @Test
    fun `add two amounts together`() =
        anAmountOf(10.0) plus anAmountOf(20.0) shouldBe anAmountOf(30.0)

    @Test
    fun `subtract one amount from another`() =
        anAmountOf(50.0) less anAmountOf(20.0) shouldBe anAmountOf(30.0)

    @Test
    fun `times the amount by a factor`() =
        anAmountOf(50.0) times -1 shouldBe anAmountOf(-50.0)

    @Test
    fun `compare two amounts`() {
        anAmountOf(50.0) lessThan anAmountOf(45.0) shouldBe false
        anAmountOf(50.0) lessThan anAmountOf(50.0) shouldBe false
        anAmountOf(50.0) lessThan anAmountOf(55.0) shouldBe true

        anAmountOf(50.0) greaterThan anAmountOf(45.0) shouldBe true
        anAmountOf(50.0) greaterThan anAmountOf(50.0) shouldBe false
        anAmountOf(50.0) greaterThan anAmountOf(55.0) shouldBe false

        anAmountOf(50.0) sameAs anAmountOf(45.0) shouldBe false
        anAmountOf(50.0) sameAs anAmountOf(50.0) shouldBe true
        anAmountOf(50.0) sameAs anAmountOf(55.0) shouldBe false
    }

}

