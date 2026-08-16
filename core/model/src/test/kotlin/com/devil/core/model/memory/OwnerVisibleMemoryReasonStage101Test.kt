package com.devil.core.model.memory

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OwnerVisibleMemoryReasonStage101Test {

    @Test
    fun `owner visible memory reason trims explicit explanation`() {
        val reason =
            OwnerVisibleMemoryReason.from(
                "  User explicitly supplied this preference.  ",
            )

        assertEquals(
            "User explicitly supplied this preference.",
            reason.value,
        )
    }

    @Test
    fun `owner visible memory reason rejects blank explanation`() {
        assertFailsWith<IllegalArgumentException> {
            OwnerVisibleMemoryReason.from("   ")
        }
    }
}
