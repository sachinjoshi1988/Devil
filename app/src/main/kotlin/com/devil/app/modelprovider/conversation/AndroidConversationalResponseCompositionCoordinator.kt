package com.devil.app.modelprovider.conversation

import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationUiState
import com.devil.app.device.Stage337MDeviceKnowledgeResponseCompositionCoordinator
import com.devil.core.model.common.TraceId
import com.devil.core.model.conversation.ConversationIntakeState
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityStatus
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferenceStatus
import com.devil.core.runtime.modelprovider.conversation.ConversationalResponseCoordinator
import com.devil.core.runtime.modelprovider.conversation.GeneratedAssistantResponse

/**
 * Android composition boundary for one post-runtime conversational response.
 *
 * One genuine runtime TraceId is first correlated with exact already-observed
 * constitutional Conversation Intake evidence.
 *
 * Stage337M then gives bounded local Device Knowledge the first response
 * opportunity. A trace claimed by Stage337M never falls through to model
 * inference for competing device-state text.
 *
 * If Stage337M does not claim the trace, the existing Stage313 conversational
 * model path remains unchanged.
 *
 * RuntimeStatus is not model authority.
 *
 * TRACE_CORRELATION != AUTHORIZATION.
 * CONVERSATION_INTAKE_ACCEPTED != MODEL_OUTPUT_VERIFIED.
 * DEVICE_KNOWLEDGE_CLAIM != DEVICE_FACT.
 * LOCAL_KNOWLEDGE != RUNTIME.
 * LOCAL_KNOWLEDGE != VERIFIED_OUTCOME.
 * MODEL_OUTPUT != DEVICE_STATE.
 * ASSISTANT != RUNTIME.
 * GENERATED != VERIFIED.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
class AndroidConversationalResponseCompositionCoordinator(
    private val intakeEvidenceStore:
        AndroidConversationIntakeEvidenceStore,
    private val responseCoordinator:
        ConversationalResponseCoordinator,
    private val interactionCoordinator:
        ConversationInteractionCoordinator,
    private val entryIdProvider:
        ConversationEntryIdProvider,
    private val deviceKnowledgeResponseCompositionCoordinator:
        Stage337MDeviceKnowledgeResponseCompositionCoordinator? =
        null,
) {

    fun generateAndAppend(
        state: ConversationUiState,
        runtimeTraceId: TraceId,
    ): ConversationUiState {
        require(!state.isSubmitting) {
            "Conversational response generation requires completed runtime submission."
        }

        val conversationIntake =
            intakeEvidenceStore.consume(
                traceId = runtimeTraceId,
            ) ?: return state

        if (
            conversationIntake.status !=
            ConversationIntakeAuthorityStatus.PRODUCED
        ) {
            return state
        }

        val intake =
            conversationIntake.intake
                ?: return state

        if (
            intake.record.state !=
            ConversationIntakeState.ACCEPTED
        ) {
            return state
        }

        val deviceKnowledgeState =
            deviceKnowledgeResponseCompositionCoordinator
                ?.composeIfClaimed(
                    state = state,
                    runtimeTraceId = runtimeTraceId,
                )

        /*
         * Non-null means Stage337M claimed this genuine runtime trace.
         *
         * The returned state may contain a genuine local KNOWLEDGE entry or may
         * remain unchanged because the local path failed closed.
         *
         * Either way, model inference must not compete for the same device-state
         * request.
         */
        if (deviceKnowledgeState != null) {
            return deviceKnowledgeState
        }

        val inference =
            responseCoordinator.generate(
                conversationIntake =
                    conversationIntake,
                content =
                    intake.record.input.content,
            )

        if (
            inference.status !=
            ConversationalModelInferenceStatus.AVAILABLE
        ) {
            return state
        }

        val response =
            GeneratedAssistantResponse.from(
                inference = inference,
            )

        require(
            response.traceId ==
                runtimeTraceId,
        ) {
            "Generated assistant response must preserve runtime trace identity."
        }

        return interactionCoordinator.appendGeneratedAssistantResponse(
            state = state,
            assistantEntryId =
                entryIdProvider.provide(),
            response = response,
        )
    }
}
