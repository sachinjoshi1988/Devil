package com.devil.core.runtime.verification

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded constitutional verification-evaluation result into the
 * stable operational VerificationResult contract.
 *
 * This mapper does not create verification evidence, update world state, report
 * final task success, change task or plan state, or produce a final Outcome.
 */
interface VerificationResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: VerificationEvaluationResult,
    ): VerificationResult
}
