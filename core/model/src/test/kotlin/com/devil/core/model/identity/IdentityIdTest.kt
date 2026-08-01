package com.devil.core.model.identity

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IdentityIdTest {

    @Test
    fun `from trims surrounding whitespace`() {
        val identityId = IdentityId.from("  subject-001  ")

        assertEquals("subject-001", identityId.value)
    }

    @Test
    fun `from preserves a valid identity`() {
        val identityId = IdentityId.from("subject-002")

        assertEquals("subject-002", identityId.value)
    }

    @Test
    fun `from rejects a blank identity`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityId.from("   ")
        }
    }
}
