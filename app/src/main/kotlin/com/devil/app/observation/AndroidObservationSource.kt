package com.devil.app.observation

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId

/**
 * Obtains genuine Android execution-observation evidence after Stage 30 has
 * established that one platform action was genuinely attempted.
 *
 * This source must never treat the execution attempt itself as observation
 * evidence.
 *
 * It must not fabricate device state, infer intended success, perform
 * verification, establish final outcome, or report task completion.
 */
fun interface AndroidObservationSource {

    fun observe(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ): AndroidObservationResult
}
