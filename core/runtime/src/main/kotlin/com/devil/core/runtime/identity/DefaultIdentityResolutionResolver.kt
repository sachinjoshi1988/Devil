package com.devil.core.runtime.identity

import com.devil.core.model.identity.IdentityConfidence
import com.devil.core.model.identity.IdentityResolutionCandidate
import com.devil.core.model.identity.IdentityResolutionCandidateSet
import com.devil.core.model.identity.IdentityResolutionRecord
import com.devil.core.model.identity.IdentityResolutionRequest
import com.devil.core.model.identity.IdentityResolutionSelection
import com.devil.core.model.identity.IdentityResolutionState

/**
 * Default Stage 3 identity-resolution implementation.
 *
 * IdentityResolutionRequest already guarantees one coherent IdentityEvidenceSet
 * for one claimed subject identity.
 *
 * This resolver therefore preserves that exact logical subject as one candidate
 * and selects that sole coherent candidate.
 *
 * The resulting resolution means only that the logical subject identity carried
 * by the request has been resolved from the request's own bounded evidence.
 *
 * IDENTITY_RESOLVED != AUTHENTICATED.
 * IDENTITY_RESOLVED != OWNER_IDENTITY_PROOF.
 * IDENTITY_RESOLVED != OWNER_MODE.
 * IDENTITY_RESOLVED != SUBJECT_TRUST.
 * IDENTITY_RESOLVED != AUTHORIZATION.
 * IDENTITY_RESOLVED != SESSION_ESTABLISHED.
 * IDENTITY_RESOLVED != EXECUTION_APPROVAL.
 *
 * IdentityConfidence here represents deterministic confidence in the structural
 * selection of the sole coherent candidate already represented by the request.
 * It is not biometric, credential, authentication, ownership, or trust confidence.
 *
 * This resolver performs no authentication, ownership determination, trust
 * evaluation, authorization, session creation, Owner Mode entry, planning,
 * execution, observation, or verification.
 */
class DefaultIdentityResolutionResolver :
    IdentityResolutionResolver {

    override fun resolve(
        request: IdentityResolutionRequest,
    ): IdentityResolutionRecord {
        val candidate =
            IdentityResolutionCandidate.create(
                identityId =
                    request.evidenceSet.claimedIdentityId,
                evidenceSet = request.evidenceSet,
            )

        val candidateSet =
            IdentityResolutionCandidateSet.create(
                candidates = listOf(candidate),
            )

        val selection =
            IdentityResolutionSelection.create(
                candidate = candidate,
                confidence = IdentityConfidence.from(100),
                rationale =
                    "The request contains one coherent claimed subject identity and therefore one exact logical identity candidate.",
            )

        return IdentityResolutionRecord.create(
            candidateSet = candidateSet,
            state = IdentityResolutionState.RESOLVED,
            selection = selection,
            rationale =
                "The sole coherent logical subject identity represented by the request was selected.",
        )
    }
}
