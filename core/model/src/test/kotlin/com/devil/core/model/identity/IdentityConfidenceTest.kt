package com.devil.core.model.identity

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IdentityConfidenceTest {

    @Test
    fun `from preserves zero confidence`() {
        val confidence = IdentityConfidence.from(0)

        assertEquals(0, confidence.value)
    }

    @Test
    fun `from preserves maximum confidence`() {
        val confidence = IdentityConfidence.from(100)

        assertEquals(100, confidence.value)
    }

    @Test
    fun `from preserves confidence within the valid range`() {
        val confidence = IdentityConfidence.from(72)

        assertEquals(72, confidence.value)
    }

    @Test
    fun `from rejects confidence below zero`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityConfidence.from(-1)
        }
    }

    @Test
    fun `from rejects confidence above one hundred`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityConfidence.from(101)
        }
    }
}
