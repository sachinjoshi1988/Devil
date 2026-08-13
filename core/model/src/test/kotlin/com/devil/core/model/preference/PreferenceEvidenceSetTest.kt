package com.devil.core.model.preference

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PreferenceEvidenceSetTest {

    @Test
    fun `set preserves conflicting evidence for one key`() {
        val set =
            PreferenceEvidenceSet.create(
                listOf(
                    evidence(
                        trace = "trace-preference-set-001",
                        value = "Google Maps",
                    ),
                    evidence(
                        trace = "trace-preference-set-002",
                        value = "Waze",
                    ),
                ),
            )

        assertEquals(
            "usual-map-app",
            set.key,
        )
        assertEquals(
            2,
            set.evidence.size,
        )
    }

    @Test
    fun `set rejects mixed preference keys`() {
        assertFailsWith<IllegalArgumentException> {
            PreferenceEvidenceSet.create(
                listOf(
                    evidence(
                        trace = "trace-preference-set-003",
                        value = "Google Maps",
                    ),
                    PreferenceEvidence.create(
                        traceId =
                            TraceId.from(
                                "trace-preference-set-004",
                            ),
                        key = "usual-music-app",
                        value = "Spotify",
                    ),
                ),
            )
        }
    }

    @Test
    fun `set rejects duplicate trace evidence`() {
        val trace =
            TraceId.from(
                "trace-preference-set-005",
            )

        assertFailsWith<IllegalArgumentException> {
            PreferenceEvidenceSet.create(
                listOf(
                    PreferenceEvidence.create(
                        traceId = trace,
                        key = "usual-map-app",
                        value = "Google Maps",
                    ),
                    PreferenceEvidence.create(
                        traceId = trace,
                        key = "usual-map-app",
                        value = "Google Maps",
                    ),
                ),
            )
        }
    }

    private fun evidence(
        trace: String,
        value: String,
    ): PreferenceEvidence {
        return PreferenceEvidence.create(
            traceId = TraceId.from(trace),
            key = "usual-map-app",
            value = value,
        )
    }
}
