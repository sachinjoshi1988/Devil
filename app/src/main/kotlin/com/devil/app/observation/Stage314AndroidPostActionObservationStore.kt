package com.devil.app.observation

import com.devil.app.accessibility.AndroidScreenElementRecord
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId

/**
 * Stage 314 process-local store for one genuine accessibility-derived
 * post-action screen observation.
 *
 * The stored elements preserve only metadata actually returned by the existing
 * Stage 179 Screen Understanding source.
 *
 * OBSERVED_SCREEN_METADATA != EXPECTED_EFFECT_MATCHED.
 * OBSERVED != VERIFIED.
 * VERIFIED != OUTCOME.
 */
class Stage314AndroidPostActionObservationStore {

    private val lock = Any()

    private var observation:
        Stage314AndroidPostActionObservation? = null

    fun bind(
        traceId: TraceId,
        capabilityId: CapabilityId,
        elements: List<AndroidScreenElementRecord>,
    ) {
        synchronized(lock) {
            observation =
                Stage314AndroidPostActionObservation(
                    traceId = traceId,
                    capabilityId = capabilityId,
                    elements = elements.toList(),
                )
        }
    }

    fun current(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ): Stage314AndroidPostActionObservation? {
        synchronized(lock) {
            val currentObservation =
                observation
                    ?: return null

            if (
                currentObservation.traceId != traceId ||
                currentObservation.capabilityId != capabilityId
            ) {
                return null
            }

            return currentObservation
        }
    }

    fun clear() {
        synchronized(lock) {
            observation = null
        }
    }
}

data class Stage314AndroidPostActionObservation(
    val traceId: TraceId,
    val capabilityId: CapabilityId,
    val elements: List<AndroidScreenElementRecord>,
)
