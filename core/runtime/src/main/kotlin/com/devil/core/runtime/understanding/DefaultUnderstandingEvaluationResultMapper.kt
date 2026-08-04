package com.devil.core.runtime.understanding

import com.devil.core.model.common.TraceId
import com.devil.core.model.understanding.UnderstandingRecord

/**
 * Default Stage 6 mapping from bounded understanding records into the stable
 * UnderstandingAuthorityResult contract.
 *
 * Every valid UnderstandingRecord is mapped as PRODUCED. Its COMPLETE,
 * AMBIGUOUS, INCOMPLETE, or UNSUPPORTED quality remains unchanged inside the
 * record and is not converted into operational deferral or failure.
 *
 * This mapper performs no language interpretation, understanding evaluation,
 * memory creation, decision selection, planning, capability authorization,
 * execution, observation, or verification.
 */
class DefaultUnderstandingEvaluationResultMapper :
    UnderstandingEvaluationResultMapper {

    override fun map(
        traceId: TraceId,
        understanding: UnderstandingRecord,
    ): UnderstandingAuthorityResult {
        return UnderstandingAuthorityResult.create(
            traceId = traceId,
            status = UnderstandingAuthorityStatus.PRODUCED,
            understanding = understanding,
        )
    }
}
