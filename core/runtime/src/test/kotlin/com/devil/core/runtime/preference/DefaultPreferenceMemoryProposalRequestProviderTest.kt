package com.devil.core.runtime.preference

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultPreferenceMemoryProposalRequestProviderTest {

    @Test
    fun `established preference evidence creates typed Memory Proposal request`() {
        val learning =
            PreferenceTestFixtures.learningRequest()

        val candidate =
            PreferenceTestFixtures.candidate()

        val evidence =
            PreferenceMemoryProposalEvidenceResult.create(
                status =
                    PreferenceMemoryProposalEvidenceStatus.ESTABLISHED,
                candidate = candidate,
            )

        val result =
            DefaultPreferenceMemoryProposalRequestProvider()
                .provide(
                    learning = learning,
                    evidence = evidence,
                )

        assertEquals(
            PreferenceMemoryProposalRequestStatus.AVAILABLE,
            result.status,
        )

        val request =
            requireNotNull(result.request)

        assertEquals(
            learning,
            request.learning,
        )

        assertEquals(
            candidate,
            request.preferenceCandidate,
        )

        assertEquals(
            "usual-map-app",
            request.preferenceCandidate?.key,
        )

        assertEquals(
            "Google Maps",
            request.preferenceCandidate?.value,
        )

        assertEquals(
            2.0 / 3.0,
            request.preferenceCandidate?.confidence,
        )

        assertEquals(
            2,
            request.preferenceCandidate
                ?.supportingEvidenceCount,
        )

        assertEquals(
            3,
            request.preferenceCandidate
                ?.totalEvidenceCount,
        )

        assertEquals(
            candidate.supportingTraceIds,
            request.preferenceCandidate
                ?.supportingTraceIds,
        )

        assertEquals(
            candidate.evidenceTraceIds,
            request.preferenceCandidate
                ?.evidenceTraceIds,
        )
    }

    @Test
    fun `deferred preference evidence creates no Memory Proposal request`() {
        val result =
            DefaultPreferenceMemoryProposalRequestProvider()
                .provide(
                    learning =
                        PreferenceTestFixtures.learningRequest(),
                    evidence =
                        PreferenceMemoryProposalEvidenceResult.create(
                            status =
                                PreferenceMemoryProposalEvidenceStatus.DEFERRED,
                        ),
                )

        assertEquals(
            PreferenceMemoryProposalRequestStatus.UNAVAILABLE,
            result.status,
        )

        assertNull(result.request)
    }
}
