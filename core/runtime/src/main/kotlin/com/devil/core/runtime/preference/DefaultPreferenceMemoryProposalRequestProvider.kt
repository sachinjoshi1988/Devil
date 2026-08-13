package com.devil.core.runtime.preference

import com.devil.core.model.learning.LearningRequest
import com.devil.core.model.memory.MemoryProposalRequest

/**
 * Default preference-specific typed Memory Proposal request provider.
 *
 * ESTABLISHED preference Memory-Proposal evidence permits preparation of one
 * MemoryProposalRequest containing:
 *
 * - the exact supplied LearningRequest;
 * - the exact qualified PreferenceLearningCandidate.
 *
 * DEFERRED evidence remains unavailable.
 *
 * The provider performs no inference, string encoding, memory classification,
 * sensitivity classification, retention selection, Memory Authority review,
 * commitment, persistence, or storage.
 */
class DefaultPreferenceMemoryProposalRequestProvider :
    PreferenceMemoryProposalRequestProvider {

    override fun provide(
        learning: LearningRequest,
        evidence: PreferenceMemoryProposalEvidenceResult,
    ): PreferenceMemoryProposalRequestResult {
        return when (evidence.status) {
            PreferenceMemoryProposalEvidenceStatus.ESTABLISHED -> {
                val candidate =
                    requireNotNull(evidence.candidate)

                PreferenceMemoryProposalRequestResult.create(
                    status =
                        PreferenceMemoryProposalRequestStatus.AVAILABLE,
                    request =
                        MemoryProposalRequest.create(
                            learning = learning,
                            preferenceCandidate = candidate,
                        ),
                )
            }

            PreferenceMemoryProposalEvidenceStatus.DEFERRED ->
                PreferenceMemoryProposalRequestResult.create(
                    status =
                        PreferenceMemoryProposalRequestStatus.UNAVAILABLE,
                )
        }
    }
}
