package com.devil.app.device

import com.devil.app.capability.AndroidCapabilityStateProvider
import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationUiState
import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.model.common.TraceId

/**
 * Stage337M local Device Knowledge response-composition boundary.
 *
 * One genuine runtime TraceId may claim this boundary only through the
 * structured Stage337M correlation store populated at Capability Selection.
 *
 * A claimed trace with no supported typed query fails closed and suppresses
 * competing model inference. This is required for requests such as battery
 * level where Stage337M has no genuine Android evidence source.
 *
 * A supported typed query is answered only after the already-registered
 * AndroidDeviceKnowledgeCapability is rechecked as AVAILABLE and health READY.
 *
 * The actual fact then comes only through the existing Stage40:
 *
 * AndroidDeviceKnowledgeQuery
 * -> AndroidDeviceKnowledgeQueryCoordinator
 * -> AndroidDeviceKnowledgeSource
 * -> AndroidDeviceKnowledgeSnapshot
 * -> AndroidDeviceKnowledgeQueryPolicy
 * -> AndroidDeviceKnowledgeResult.
 *
 * This coordinator does not parse raw conversation text, infer intent,
 * authenticate, authorize, execute an Android action, establish Observation,
 * Verification, Outcome, update the World Model, or commit Memory.
 *
 * DEVICE_KNOWLEDGE_ROUTE != DEVICE_FACT.
 * CAPABILITY_AVAILABLE != DEVICE_FACT.
 * HEALTH_READY != EXECUTIVE_READY.
 * QUERY_RECORD != DEVICE_FACT.
 * MODEL_OUTPUT != DEVICE_STATE.
 * KNOWLEDGE_PRESENTATION != RUNTIME_STATUS.
 * KNOWLEDGE_PRESENTATION != VERIFIED_OUTCOME.
 * BATTERY_QUERY != BATTERY_FACT.
 */
class Stage337MDeviceKnowledgeResponseCompositionCoordinator(
    private val queryStore:
        Stage337MDeviceKnowledgeQueryStore,
    private val capabilityStateProvider:
        AndroidCapabilityStateProvider,
    private val queryCoordinator:
        AndroidDeviceKnowledgeQueryCoordinator,
    private val interactionCoordinator:
        ConversationInteractionCoordinator,
    private val entryIdProvider:
        ConversationEntryIdProvider,
) {

    /**
     * Returns:
     *
     * null  -> this trace was not claimed by Stage337M; another response path
     *          may continue.
     *
     * state -> this trace was claimed by Stage337M. The returned state may
     *          contain a genuine KNOWLEDGE entry or may be unchanged because
     *          local knowledge failed closed. A model must not answer the same
     *          claimed device-state request.
     */
    fun composeIfClaimed(
        state: ConversationUiState,
        runtimeTraceId: TraceId,
    ): ConversationUiState? {
        require(!state.isSubmitting) {
            "Device Knowledge response composition requires completed runtime submission."
        }

        val record =
            queryStore.consumeRecord(
                traceId = runtimeTraceId,
            ) ?: return null

        val queryType =
            record.queryType
                ?: return state

        val capabilityState =
            capabilityStateProvider.stateOf(
                AndroidDeviceKnowledgeCapability.contract,
            )

        if (
            capabilityState.capability.capabilityId !=
            AndroidDeviceKnowledgeCapability.capabilityId
        ) {
            return state
        }

        if (
            capabilityState.availability !=
            CapabilityAvailabilityState.AVAILABLE
        ) {
            return state
        }

        if (
            capabilityState.health !=
            CapabilityHealthState.READY
        ) {
            return state
        }

        val result =
            try {
                queryCoordinator.query(
                    request =
                        AndroidDeviceKnowledgeQuery(
                            type = queryType,
                        ),
                )
            } catch (_: Exception) {
                return state
            }

        if (result.queryType != queryType) {
            return state
        }

        return interactionCoordinator.appendKnowledge(
            state = state,
            knowledgeEntryId =
                entryIdProvider.provide(),
            traceId = runtimeTraceId,
            message = result.presentation,
        )
    }
}
