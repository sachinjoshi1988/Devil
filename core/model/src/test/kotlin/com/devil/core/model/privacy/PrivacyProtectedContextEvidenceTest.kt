package com.devil.core.model.privacy

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PrivacyProtectedContextEvidenceTest {

    @Test
    fun `protected context evidence preserves bounded status`() {
        val evidence =
            PrivacyProtectedContextEvidence.create(
                status =
                    PrivacyProtectedContextStatus.UNAVAILABLE,
                rationale =
                    "Owner protection cannot currently be established.",
            )

        assertEquals(
            PrivacyProtectedContextStatus.UNAVAILABLE,
            evidence.status,
        )

        assertEquals(
            "Owner protection cannot currently be established.",
            evidence.rationale,
        )
    }

    @Test
    fun `protected context evidence rejects blank rationale`() {
        assertFailsWith<IllegalArgumentException> {
            PrivacyProtectedContextEvidence.create(
                status =
                    PrivacyProtectedContextStatus.NOT_ESTABLISHED,
                rationale = "   ",
            )
        }
    }
}
