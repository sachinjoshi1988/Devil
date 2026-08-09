package com.devil.app.observation

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId

/**
 * Default Stage 31 Android observation source.
 *
 * No production capability-specific Android observation mechanism has yet been
 * approved.
 *
 * Therefore this source truthfully returns DEFERRED rather than treating an
 * execution attempt as proof that an observable effect occurred.
 *
 * It invokes no Android platform API, performs no verification, establishes no
 * outcome, mutates no world state, and reports no success.
 */
class DefaultAndroidObservationSource : AndroidObservationSource {

    override fun observe(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ): AndroidObservationResult {
        return AndroidObservationResult.create(
            traceId = traceId,
            status = AndroidObservationStatus.DEFERRED,
        )
    }
}
