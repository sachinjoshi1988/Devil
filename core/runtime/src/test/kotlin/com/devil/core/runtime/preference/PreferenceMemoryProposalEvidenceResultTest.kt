package com.devil.core.runtime.preference

import com.devil.core.model.common.TraceId
import com.devil.core.model.preference.PreferenceLearningCandidate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class PreferenceMemoryProposalEvidenceResultTest {

    @Test
    fun `established result preserves exact qualified preference candidate`() {
        val candidate =
            createCandidate()

        val result =
            PreferenceMemoryProposalEvidenceResult.create(
                status =
                    PreferenceMemoryProposalEvidenceStatus.ESTABLISHED,
                candidate = candidate,
            )

        assertEquals(
            PreferenceMemoryProposalEvidenceStatus.ESTABLISHED,
            result.status,
        )

        assertEquals(
            candidate,
            result.candidate,
        )

        assertEquals(
            "usual-map-app",
            result.candidate?.key,
        )

        assertEquals(
            "Google Maps",
            result.candidate?.value,
        )
    }

    @Test
    fun `deferred result contains no candidate`() {
        val result =
            PreferenceMemoryProposalEvidenceResult.create(
                status =
                    PreferenceMemoryProposalEvidenceStatus.DEFERRED,
            )

        assertEquals(
            PreferenceMemoryProposalEvidenceStatus.DEFERRED,
            result.status,
        )

        assertNull(result.candidate)
    }

    @Test
    fun `established result rejects missing candidate`() {
        assertFailsWith<IllegalArgumentException> {
            PreferenceMemoryProposalEvidenceResult.create(
                status =
                    PreferenceMemoryProposalEvidenceStatus.ESTABLISHED,
            )
        }
    }

    @Test
    fun `deferred result rejects candidate`() {
        assertFailsWith<IllegalArgumentException> {
            PreferenceMemoryProposalEvidenceResult.create(
                status =
                    PreferenceMemoryProposalEvidenceStatus.DEFERRED,
                candidate = createCandidate(),
            )
        }
    }

    private fun createCandidate():
        PreferenceLearningCandidate {
        val traces =
            listOf(
                TraceId.from(
                    "trace-preference-memory-proposal-evidence-001",
                ),
                TraceId.from(
                    "trace-preference-memory-proposal-evidence-002",
                ),
            )

        return PreferenceLearningCandidate.create(
            key = "usual-map-app",
            value = "Google Maps",
            confidence = 1.0,
            supportingEvidenceCount = 2,
            totalEvidenceCount = 2,
            supportingTraceIds = traces,
            evidenceTraceIds = traces,
        )
    }
}
