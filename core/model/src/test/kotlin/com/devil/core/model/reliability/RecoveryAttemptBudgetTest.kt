package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RecoveryAttemptBudgetTest {

    @Test
    fun `budget exposes finite remaining attempts`() {
        val budget =
            RecoveryAttemptBudget.create(
                maximumAttempts = 3,
                attemptsAlreadyUsed = 1,
            )

        assertEquals(
            2,
            budget.remainingAttempts,
        )
        assertFalse(
            budget.exhausted,
        )
    }

    @Test
    fun `fully used budget is exhausted`() {
        val budget =
            RecoveryAttemptBudget.create(
                maximumAttempts = 2,
                attemptsAlreadyUsed = 2,
            )

        assertEquals(
            0,
            budget.remainingAttempts,
        )
        assertTrue(
            budget.exhausted,
        )
    }

    @Test
    fun `zero maximum attempts is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            RecoveryAttemptBudget.create(
                maximumAttempts = 0,
            )
        }
    }

    @Test
    fun `attempt count cannot exceed maximum`() {
        assertFailsWith<IllegalArgumentException> {
            RecoveryAttemptBudget.create(
                maximumAttempts = 2,
                attemptsAlreadyUsed = 3,
            )
        }
    }
}
