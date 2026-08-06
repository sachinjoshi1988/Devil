package com.devil.core.model.outcome

import com.devil.core.model.verification.VerificationRequest

/**
 * Represents one structured request for bounded constitutional outcome
 * evaluation.
 *
 * The request preserves one existing VerificationRequest after genuine
 * verification evidence has been established by the Verification Authority. It
 * does not reinterpret verification, observation, execution, planning, or
 * capability selection.
 *
 * This request does not determine final task success or failure, update world
 * state, change task or plan state, create memory or learning, communicate an
 * outcome, or produce the final runtime result.
 */
@ConsistentCopyVisibility
data class OutcomeRequest private constructor(
    val verification: VerificationRequest,
) {
    companion object {
        fun create(
            verification: VerificationRequest,
        ): OutcomeRequest {
            return OutcomeRequest(
                verification = verification,
            )
        }
    }
}
