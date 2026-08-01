package com.devil.core.model.task

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TaskIdTest {

    @Test
    fun `from trims surrounding whitespace`() {
        val taskId = TaskId.from("  task-001  ")

        assertEquals("task-001", taskId.value)
    }

    @Test
    fun `from preserves a valid task identity`() {
        val taskId = TaskId.from("task-002")

        assertEquals("task-002", taskId.value)
    }

    @Test
    fun `from rejects a blank task identity`() {
        assertFailsWith<IllegalArgumentException> {
            TaskId.from("   ")
        }
    }
}
