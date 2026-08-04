package com.devil.core.runtime.identity

import com.devil.core.model.identity.IdentityResolutionCandidate
import com.devil.core.model.identity.IdentityResolutionCandidateSet
import com.devil.core.model.identity.IdentityResolutionRecord
import com.devil.core.model.identity.IdentityResolutionRequest
import com.devil.core.model.identity.IdentityResolutionState

/**
 * Default Stage 3 identity-resolution implementation.
 *
 * The current implementation preserves the supplied claimed identity and its
 * coherent evidence as one candidate, but it has no evidence-analysis policy
 * capable of selecting that candidate. It therefore returns an honest
 * unresolved record without inventing confidence or resolution.
 *
 * It performs no authentication, ownership determination, trust evaluation,
 * authorization, planning, execution, observation, or verification.
 */
class DefaultIdentityResolutionResolver :
    IdentityResolutionResolver {

    override fun resolve(
        request: IdentityResolutionRequest,
    ): IdentityResolutionRecord {
        val candidate = IdentityResolutionCandidate.create(
            identityId = request.evidenceSet.claimedIdentityId,
            evidenceSet = request.evidenceSet,
        )

        return IdentityResolutionRecord.create(
            candidateSet = IdentityResolutionCandidateSet.create(
                candidates = listOf(candidate),
            ),
            state = IdentityResolutionState.UNRESOLVED,
            rationale = "No identity resolution policy is available.",
        )
    }
}
