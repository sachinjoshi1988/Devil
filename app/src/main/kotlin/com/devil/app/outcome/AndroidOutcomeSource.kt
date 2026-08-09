package com.devil.app.outcome

import com.devil.app.verification.AndroidVerificationEvidence
import com.devil.core.model.common.TraceId

/**
 * Obtains genuine bounded Android outcome evidence after Stage 32 produced
 * genuine Android verification evidence.
 *
 * Verification must never be reinterpreted automatically as an established
 * outcome. The source must independently establish only what an approved
 * Android outcome mechanism can genuinely determine.
 *
 * It must not claim task or plan completion, mutate world state, create
 * learning or logical memory, persist memory, or fabricate success.
 */
fun interface AndroidOutcomeSource {

    fun establish(
        traceId: TraceId,
        verificationEvidence: AndroidVerificationEvidence,
    ): AndroidOutcomeResult
}
