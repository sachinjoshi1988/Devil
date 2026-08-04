package com.devil.core.runtime.trust

import com.devil.core.model.common.TraceId
import com.devil.core.model.trust.TrustAssessment

/**
 * Default Stage 4 mapping from subject trust assessments to TrustResult.
 *
 * TrustResult currently exposes ContextTrustLevel, which describes context
 * provenance rather than subject trust. This mapper therefore returns DEFERRED
 * for every subject trust assessment instead of fabricating a context trust
 * level or weakening the trust boundary.
 *
 * It performs no trust evaluation, authorization, planning, execution,
 * observation, or verification.
 */
class DefaultTrustEvaluationResultMapper :
    TrustEvaluationResultMapper {

    override fun map(
        traceId: TraceId,
        assessment: TrustAssessment,
    ): TrustResult {
        return TrustResult.create(
            traceId = traceId,
            status = TrustStatus.DEFERRED,
        )
    }
}
