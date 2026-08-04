package com.devil.core.runtime.understanding

import com.devil.core.model.common.TraceId
import com.devil.core.model.understanding.UnderstandingRecord

/**
 * Translates one bounded understanding record into the stable operational
 * Understanding Authority result contract.
 *
 * Understanding quality remains represented by UnderstandingState inside the
 * record. This mapper does not reinterpret language, alter understanding,
 * create memory, select decisions, plan work, execute capabilities, or verify
 * outcomes.
 */
interface UnderstandingEvaluationResultMapper {

    fun map(
        traceId: TraceId,
        understanding: UnderstandingRecord,
    ): UnderstandingAuthorityResult
}
