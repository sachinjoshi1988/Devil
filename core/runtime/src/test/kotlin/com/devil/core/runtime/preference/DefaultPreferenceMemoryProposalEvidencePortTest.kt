package com.devil.core.runtime.preference

import com.devil.core.model.common.TraceId
import com.devil.core.model.preference.PreferenceLearningCandidate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultPreferenceMemoryProposalEvidencePortTest {

    private val port =
        DefaultPreferenceMemoryProposalEvidencePort()

    @Test
    fun `available qualified preference remains deferred without authorized evidence mechanism`() {
        val candidate =
            createCandidate()

        val candidateResult =
            PreferenceLearningCandidateResult.create(
                status =
                    PreferenceLearningCandidateStatus.AVAILABLE,
                candidate = candidate,
            )

        val result =
            port.establish(
                candidateResult = candidateResult,
            )

        assertEquals(
            PreferenceMemoryProposalEvidenceStatus.DEFERRED,
            result.status,
        )

        assertNull(result.candidate)
    }

    @Test
    fun `unavailable preference remains deferred`() {
        val candidateResult =
            PreferenceLearningCandidateResult.create(
                status =
                    PreferenceLearningCandidateStatus.UNAVAILABLE,
            )

        val result =
            port.establish(
                candidateResult = candidateResult,
            )

        assertEquals(
            PreferenceMemoryProposalEvidenceStatus.DEFERRED,
            result.status,
        )

        assertNull(result.candidate)
    }

    private fun createCandidate():
        PreferenceLearningCandidate {
        val traces =
            listOf(
                TraceId.from(
                    "trace-default-preference-memory-proposal-evidence-001",
                ),
                TraceId.from(
                    "trace-default-preference-memory-proposal-evidence-002",
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
