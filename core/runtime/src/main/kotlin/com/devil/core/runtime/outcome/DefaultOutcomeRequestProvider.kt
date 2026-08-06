package com.devil.core.runtime.outcome

import com.devil.core.model.outcome.OutcomeRequest
import com.devil.core.runtime.verification.VerificationResult
import com.devil.core.runtime.verification.VerificationStatus

/**
 * Default Stage 15 constitutional outcome-request provider.
 *
 * A request is available only when constitutional verification produced a
 * VERIFIED VerificationResult containing one bounded VerificationRequest.
 *
 * Deferred verification remains unavailable. Verification failure preserves
 * its matching error.
 *
 * This implementation does not determine final task success or failure, update
 * world state, change task or plan state, create memory or learning,
 * communicate an outcome, or produce the final runtime result.
 */
class DefaultOutcomeRequestProvider :
    OutcomeRequestProvider {

    override fun provide(
        verification: VerificationResult,
    ): OutcomeRequestResult {
        return when (verification.status) {
            VerificationStatus.VERIFIED ->
                OutcomeRequestResult.create(
                    traceId = verification.traceId,
                    status = OutcomeRequestStatus.AVAILABLE,
                    request = OutcomeRequest.create(
                        verification =
                            requireNotNull(verification.request),
                    ),
                )

            VerificationStatus.DEFERRED ->
                OutcomeRequestResult.create(
                    traceId = verification.traceId,
                    status = OutcomeRequestStatus.UNAVAILABLE,
                )

            VerificationStatus.FAILED ->
                OutcomeRequestResult.create(
                    traceId = verification.traceId,
                    status = OutcomeRequestStatus.FAILED,
                    error = requireNotNull(verification.error),
                )
        }
    }
}
