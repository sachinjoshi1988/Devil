package com.devil.core.runtime.identity

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.identity.IdentityEvidence
import com.devil.core.model.identity.IdentityEvidenceSet
import com.devil.core.model.identity.IdentityEvidenceSource
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.identity.IdentityResolutionRequest

/**
 * Default Stage 3 identity-resolution request provider.
 *
 * When one subject identity has been explicitly configured by the composition
 * root, this provider preserves that declaration as bounded identity evidence
 * and creates one structured IdentityResolutionRequest.
 *
 * When no subject identity has been explicitly configured, it remains
 * fail-closed and reports UNAVAILABLE.
 *
 * The configured identity is evidence of an explicit subject declaration only.
 *
 * DECLARED_IDENTITY != AUTHENTICATION.
 * DECLARED_IDENTITY != OWNER_IDENTITY_PROOF.
 * DECLARED_IDENTITY != OWNER_MODE.
 * DECLARED_IDENTITY != SUBJECT_TRUST.
 * DECLARED_IDENTITY != AUTHORIZATION.
 *
 * ContextTrustLevel is not converted into subject identity evidence.
 *
 * This provider does not resolve identity, authenticate a subject, prove
 * ownership, evaluate trust, establish a session, grant authorization,
 * enter Owner Mode, plan, execute, observe, or verify.
 */
class DefaultIdentityResolutionRequestProvider(
    private val configuredSubjectIdentityId: IdentityId? = null,
) : IdentityResolutionRequestProvider {

    override fun provide(
        context: ContextEnvelope,
    ): IdentityResolutionRequestResult {
        val subjectIdentityId =
            configuredSubjectIdentityId
                ?: return IdentityResolutionRequestResult.create(
                    traceId = context.traceId,
                    status =
                        IdentityResolutionRequestStatus.UNAVAILABLE,
                )

        val evidence =
            IdentityEvidence.create(
                claimedIdentityId = subjectIdentityId,
                source = IdentityEvidenceSource.DECLARED,
                observedAt = context.observedAt,
                reference =
                    CONFIGURED_SUBJECT_IDENTITY_REFERENCE,
            )

        val request =
            IdentityResolutionRequest.create(
                context = context,
                evidenceSet =
                    IdentityEvidenceSet.create(
                        claimedIdentityId = subjectIdentityId,
                        evidence = listOf(evidence),
                    ),
            )

        return IdentityResolutionRequestResult.create(
            traceId = context.traceId,
            status = IdentityResolutionRequestStatus.AVAILABLE,
            request = request,
        )
    }

    companion object {
        private const val CONFIGURED_SUBJECT_IDENTITY_REFERENCE =
            "application-configured-subject-identity"
    }
}
