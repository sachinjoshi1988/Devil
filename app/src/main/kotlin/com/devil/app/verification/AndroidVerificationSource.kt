package com.devil.app.verification

import com.devil.app.observation.AndroidObservationEvidence
import com.devil.core.model.common.TraceId

/**
 * Obtains genuine Android verification evidence after Stage 31 has established
 * one genuinely observed Android effect.
 *
 * This source must never treat observation itself as verification evidence.
 *
 * It must independently establish only what an approved verification mechanism
 * can genuinely verify.
 *
 * It must not establish a final constitutional Outcome, claim task or plan
 * completion, mutate world state, create logical memory, or fabricate success.
 */
fun interface AndroidVerificationSource {

    fun verify(
        traceId: TraceId,
        observationEvidence: AndroidObservationEvidence,
    ): AndroidVerificationResult
}
