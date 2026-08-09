package com.devil.app.outcome

import com.devil.app.verification.AndroidVerificationEvidence
import com.devil.core.model.common.TraceId

/**
 * Default Stage 33 Android outcome source.
 *
 * No approved production Android outcome-determination mechanism currently
 * exists.
 *
 * Therefore this source truthfully returns DEFERRED rather than treating
 * verification evidence as proof of a final constitutional outcome.
 *
 * It establishes no task or plan completion, mutates no world state, creates
 * no learning or logical memory, and reports no fabricated success.
 */
class DefaultAndroidOutcomeSource : AndroidOutcomeSource {

    override fun establish(
        traceId: TraceId,
        verificationEvidence: AndroidVerificationEvidence,
    ): AndroidOutcomeResult {
        return AndroidOutcomeResult.create(
            traceId = traceId,
            status = AndroidOutcomeStatus.DEFERRED,
        )
    }
}
