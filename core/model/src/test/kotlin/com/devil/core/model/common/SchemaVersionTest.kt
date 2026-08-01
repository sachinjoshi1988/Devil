package com.devil.core.model.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SchemaVersionTest {

    @Test
    fun `from preserves a positive schema version`() {
        val schemaVersion = SchemaVersion.from(1)

        assertEquals(1, schemaVersion.value)
    }

    @Test
    fun `from rejects zero`() {
        assertFailsWith<IllegalArgumentException> {
            SchemaVersion.from(0)
        }
    }

    @Test
    fun `from rejects a negative schema version`() {
        assertFailsWith<IllegalArgumentException> {
            SchemaVersion.from(-1)
        }
    }
}
