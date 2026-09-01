package com.devil.app.outcome

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId

/**
 * Holds one process-local Stage 314 Android Outcome fact that has already been
 * constitutionally established.
 *
 * This store does not observe Android state, perform Verification, establish an
 * Outcome, reinterpret RuntimeStatus, or create constitutional evidence.
 *
 * It is only a bounded handoff from the genuine Android Outcome source to the
 * conversation presentation wrapper.
 *
 * STORED_OUTCOME != RUNTIME_ACCEPTED.
 * STORED_OUTCOME != MEMORY_PERSISTED.
 */
class Stage314VerifiedAndroidOutcomePresentationStore {

    private data class EstablishedOutcome(
        val traceId: TraceId,
        val capabilityId: CapabilityId,
        val message: String,
    )

    private val lock = Any()

    private var establishedOutcome: EstablishedOutcome? = null

    fun bindEstablished(
        traceId: TraceId,
        capabilityId: CapabilityId,
        message: String,
    ) {
        val normalizedMessage = message.trim()

        require(normalizedMessage.isNotEmpty()) {
            "Verified Android outcome presentation message must not be blank."
        }

        synchronized(lock) {
            establishedOutcome =
                EstablishedOutcome(
                    traceId = traceId,
                    capabilityId = capabilityId,
                    message = normalizedMessage,
                )
        }
    }

    fun consume(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ): String? {
        synchronized(lock) {
            val current =
                establishedOutcome
                    ?: return null

            if (
                current.traceId != traceId ||
                current.capabilityId != capabilityId
            ) {
                return null
            }

            establishedOutcome = null

            return current.message
        }
    }

    fun clear() {
        synchronized(lock) {
            establishedOutcome = null
        }
    }
}
