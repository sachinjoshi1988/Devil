package com.devil.core.model.preference

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PreferenceEvidenceTest {

    @Test
    fun `evidence normalizes bounded key and value`() {
        val evidence =
            PreferenceEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-preference-evidence-001",
                    ),
                key = "  usual-map-app  ",
                value = "  Google Maps  ",
            )

        assertEquals(
            "usual-map-app",
            evidence.key,
        )
        assertEquals(
            "Google Maps",
            evidence.value,
        )
    }

    @Test
    fun `evidence rejects blank key`() {
        assertFailsWith<IllegalArgumentException> {
            PreferenceEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-preference-evidence-002",
                    ),
                key = "   ",
                value = "Google Maps",
            )
        }
    }

    @Test
    fun `evidence rejects blank value`() {
        assertFailsWith<IllegalArgumentException> {
            PreferenceEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-preference-evidence-003",
                    ),
                key = "usual-map-app",
                value = "   ",
            )
        }
    }
}
