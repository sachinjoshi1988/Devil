package com.devil.core.runtime.trust

import com.devil.core.model.common.TraceId
import com.devil.core.model.trust.TrustAssessment

/**
 * Default Stage 4 mapping from subject trust assessments to TrustResult.
 *
 * The exact bounded TrustAssessment is preserved so downstream constitutional
 * authorization can consume genuine subject-trust evidence without reconstructing
 * it from ContextTrustLevel.
 *
 * ContextTrustLevel remains context provenance and is not fabricated here.
 *
 * SUBJECT_TRUST != CONTEXT_TRUST.
 * TRUST_ASSESSMENT != AUTHENTICATION.
 * TRUST_ASSESSMENT != AUTHORIZATION.
 * TRUST_ASSESSMENT != EXECUTION.
 */
class DefaultTrustEvaluationResultMapper :
    TrustEvaluationResultMapper {

    override fun map(
        traceId: TraceId,
        assessment: TrustAssessment,
    ): TrustResult {
        return TrustResult.create(
            traceId = traceId,
            status = TrustStatus.EVALUATED,
            assessment = assessment,
        )
    }
}
