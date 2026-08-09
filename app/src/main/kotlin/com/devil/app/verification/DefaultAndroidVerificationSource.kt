package com.devil.app.verification

import com.devil.app.observation.AndroidObservationEvidence
import com.devil.core.model.common.TraceId

/**
 * Default Stage 32 Android verification source.
 *
 * No production capability-specific Android verification mechanism has yet
 * been approved.
 *
 * Therefore this source truthfully returns DEFERRED rather than treating
 * observation evidence as proof that the intended outcome was verified.
 *
 * It invokes no Android platform API, establishes no final Outcome, mutates no
 * world state, creates no logical memory, and reports no completion.
 */
class DefaultAndroidVerificationSource : AndroidVerificationSource {

    override fun verify(
        traceId: TraceId,
        observationEvidence: AndroidObservationEvidence,
    ): AndroidVerificationResult {
        return AndroidVerificationResult.create(
            traceId = traceId,
            status = AndroidVerificationStatus.DEFERRED,
        )
    }
}
