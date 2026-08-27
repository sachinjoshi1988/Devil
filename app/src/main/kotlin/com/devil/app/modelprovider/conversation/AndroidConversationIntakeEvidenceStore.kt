package com.devil.app.modelprovider.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult
import com.devil.core.runtime.modelprovider.conversation.ConversationIntakeEvidencePort

/**
 * Stage 313 process-local correlation boundary for already-established
 * constitutional conversation-intake evidence.
 *
 * The Unified Devil Runtime may expose the exact
 * ConversationIntakeAuthorityResult through ConversationIntakeEvidencePort.
 *
 * This store preserves that exact object temporarily by TraceId so a later
 * bounded Android conversational-response composition may consume it.
 *
 * Evidence is:
 *
 * - process-local only;
 * - transient only;
 * - trace-bound;
 * - bounded;
 * - consumed at most once.
 *
 * This is not Devil Memory, conversation persistence, logical Memory,
 * authorization state, model state, or verified Outcome state.
 *
 * This component does not:
 *
 * - perform Conversation Intake Authority;
 * - reinterpret conversation intake;
 * - authenticate anyone;
 * - establish trust;
 * - grant authorization;
 * - invoke a model;
 * - perform networking;
 * - execute capabilities;
 * - establish Observation, Verification, or Outcome;
 * - perform Learning;
 * - persist or recall Devil Memory.
 *
 * OBSERVED_INTAKE != AUTHORITY.
 * CORRELATED_INTAKE != MODEL_AUTHORIZATION.
 * GENERATED != VERIFIED.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
class AndroidConversationIntakeEvidenceStore(
    private val maximumEntries: Int =
        DEFAULT_MAXIMUM_ENTRIES,
) : ConversationIntakeEvidencePort {

    private val lock = Any()

    private val evidenceByTraceId =
        LinkedHashMap<
            TraceId,
            ConversationIntakeAuthorityResult,
        >()

    init {
        require(maximumEntries > 0) {
            "Conversation-intake evidence capacity must be positive."
        }
    }

    override fun observe(
        conversationIntake: ConversationIntakeAuthorityResult,
    ) {
        synchronized(lock) {
            evidenceByTraceId[
                conversationIntake.traceId
            ] = conversationIntake

            while (
                evidenceByTraceId.size >
                maximumEntries
            ) {
                val eldestTraceId =
                    evidenceByTraceId
                        .entries
                        .first()
                        .key

                evidenceByTraceId.remove(
                    eldestTraceId,
                )
            }
        }
    }

    /**
     * Consumes the exact intake evidence associated with [traceId].
     *
     * Missing or previously consumed evidence returns null.
     *
     * No synthetic or substitute intake result is created.
     */
    fun consume(
        traceId: TraceId,
    ): ConversationIntakeAuthorityResult? {
        return synchronized(lock) {
            evidenceByTraceId.remove(traceId)
        }
    }

    internal fun size(): Int {
        return synchronized(lock) {
            evidenceByTraceId.size
        }
    }

    companion object {
        internal const val DEFAULT_MAXIMUM_ENTRIES:
            Int = 32
    }
}
