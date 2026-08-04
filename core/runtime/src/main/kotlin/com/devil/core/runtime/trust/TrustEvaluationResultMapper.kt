package com.devil.core.runtime.trust

import com.devil.core.model.common.TraceId
import com.devil.core.model.trust.TrustAssessment

/**
 * Translates a bounded subject trust assessment into the stable runtime trust
 * result contract.
 *
 * This mapper does not evaluate trust, alter identity, grant authorization,
 * enter Owner Mode, or permit execution.
 */
interface TrustEvaluationResultMapper {

    fun map(
        traceId: TraceId,
        assessment: TrustAssessment,
    ): TrustResult
}
