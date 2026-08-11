package com.devil.app.accessibility

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AndroidAccessibilityTraversalBudgetTest {

    @Test
    fun `default traversal budget permits exactly configured node count`() {
        val budget =
            AndroidAccessibilityTraversalBudget()

        repeat(
            AndroidAccessibilityTraversalBudget
                .MAX_VISITED_NODES,
        ) {
            assertTrue(
                budget.tryAcquireNodeInspection(),
            )
        }

        assertFalse(
            budget.tryAcquireNodeInspection(),
        )
    }

    @Test
    fun `custom traversal budget stops before inspecting beyond bound`() {
        val budget =
            AndroidAccessibilityTraversalBudget(
                maxVisitedNodes = 2,
            )

        assertTrue(
            budget.tryAcquireNodeInspection(),
        )
        assertTrue(
            budget.tryAcquireNodeInspection(),
        )
        assertFalse(
            budget.tryAcquireNodeInspection(),
        )
        assertFalse(
            budget.tryAcquireNodeInspection(),
        )
    }

    @Test
    fun `single node traversal budget permits one inspection only`() {
        val budget =
            AndroidAccessibilityTraversalBudget(
                maxVisitedNodes = 1,
            )

        assertTrue(
            budget.tryAcquireNodeInspection(),
        )
        assertFalse(
            budget.tryAcquireNodeInspection(),
        )
    }
}
