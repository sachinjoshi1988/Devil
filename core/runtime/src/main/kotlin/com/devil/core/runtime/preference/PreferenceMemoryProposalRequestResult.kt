package com.devil.core.runtime.preference

import com.devil.core.model.memory.MemoryProposalRequest

/**
 * Stable result of preference-specific typed Memory Proposal request
 * preparation.
 *
 * AVAILABLE contains exactly one MemoryProposalRequest whose
 * preferenceCandidate remains typed and whose LearningRequest is preserved
 * unchanged.
 *
 * UNAVAILABLE contains no request.
 *
 * This result does not create a Memory Proposal, invoke Memory Authority,
 * approve Memory, commit Memory, persist Memory, assign memory class,
 * sensitivity, retention policy, owner-visible reason, storage destination,
 * or authorization.
 */
@ConsistentCopyVisibility
data class PreferenceMemoryProposalRequestResult private constructor(
    val status: PreferenceMemoryProposalRequestStatus,
    val request: MemoryProposalRequest?,
) {
    companion object {
        fun create(
            status: PreferenceMemoryProposalRequestStatus,
            request: MemoryProposalRequest? = null,
        ): PreferenceMemoryProposalRequestResult {
            when (status) {
                PreferenceMemoryProposalRequestStatus.AVAILABLE -> {
                    require(request != null) {
                        "Available preference Memory Proposal request result requires one request."
                    }

                    require(request.preferenceCandidate != null) {
                        "Available preference Memory Proposal request result requires one typed preference candidate."
                    }
                }

                PreferenceMemoryProposalRequestStatus.UNAVAILABLE -> {
                    require(request == null) {
                        "Unavailable preference Memory Proposal request result must not contain a request."
                    }
                }
            }

            return PreferenceMemoryProposalRequestResult(
                status = status,
                request = request,
            )
        }
    }
}
