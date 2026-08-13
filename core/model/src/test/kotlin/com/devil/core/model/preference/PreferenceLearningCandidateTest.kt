package com.devil.core.model.preference

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PreferenceLearningCandidateTest {

    @Test
    fun `candidate preserves qualified preference and evidence provenance`() {
        val traces =
            listOf(
                trace("trace-preference-candidate-001"),
                trace("trace-preference-candidate-002"),
                trace("trace-preference-candidate-003"),
            )

        val candidate =
            PreferenceLearningCandidate.create(
                key = "  usual-map-app  ",
                value = "  Google Maps  ",
                confidence = 2.0 / 3.0,
                supportingEvidenceCount = 2,
                totalEvidenceCount = 3,
                supportingTraceIds =
                    traces.take(2),
                evidenceTraceIds = traces,
            )

        assertEquals(
            "usual-map-app",
            candidate.key,
        )
        assertEquals(
            "Google Maps",
            candidate.value,
        )
        assertEquals(
            2,
            candidate.supportingTraceIds.size,
        )
        assertEquals(
            3,
            candidate.evidenceTraceIds.size,
        )
    }

    @Test
    fun `candidate rejects single supporting occurrence`() {
        val one =
            trace(
                "trace-preference-candidate-004",
            )

        assertFailsWith<IllegalArgumentException> {
            PreferenceLearningCandidate.create(
                key = "usual-map-app",
                value = "Google Maps",
                confidence = 1.0,
                supportingEvidenceCount = 1,
                totalEvidenceCount = 1,
                supportingTraceIds =
                    listOf(one),
                evidenceTraceIds =
                    listOf(one),
            )
        }
    }

    @Test
    fun `candidate rejects supporting trace outside evidence set`() {
        assertFailsWith<IllegalArgumentException> {
            PreferenceLearningCandidate.create(
                key = "usual-map-app",
                value = "Google Maps",
                confidence = 1.0,
                supportingEvidenceCount = 2,
                totalEvidenceCount = 2,
                supportingTraceIds =
                    listOf(
                        trace(
                            "trace-preference-candidate-005",
                        ),
                        trace(
                            "trace-preference-candidate-foreign",
                        ),
                    ),
                evidenceTraceIds =
                    listOf(
                        trace(
                            "trace-preference-candidate-005",
                        ),
                        trace(
                            "trace-preference-candidate-006",
                        ),
                    ),
            )
        }
    }

    private fun trace(
        value: String,
    ): TraceId {
        return TraceId.from(value)
    }
}
