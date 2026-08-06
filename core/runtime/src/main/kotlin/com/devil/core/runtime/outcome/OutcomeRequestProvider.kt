package com.devil.core.runtime.outcome

import com.devil.core.runtime.verification.VerificationResult

/**
 * Supplies one structured constitutional outcome request when genuine
 * verification evidence has been established.
 *
 * This provider does not determine final task success or failure, update world
 * state, change task or plan state, create memory or learning, communicate an
 * outcome, or produce the final runtime result.
 */
interface OutcomeRequestProvider {

    fun provide(
        verification: VerificationResult,
    ): OutcomeRequestResult
}
