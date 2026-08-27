package com.devil.app.modelprovider.conversation

import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationUiState
import com.devil.core.model.common.TraceId
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferenceStatus
import com.devil.core.runtime.modelprovider.conversation.ConversationalResponseCoordinator
import com.devil.core.runtime.modelprovider.conversation.GeneratedAssistantResponse

/**
 * Stage 313 Android composition boundary for one generated conversational response.
 *
 * This coordinator operates only from one genuine runtime TraceId supplied by the
 * bounded Stage 313 submission wrapper after an actual runtime timeline entry was
 * created.
 *
 * It uses that exact trace identity to consume the already-established
 * ConversationIntakeAuthorityResult observed from the single Unified Devil Runtime.
 *
 * The exact original content preserved by that intake evidence is the only content
 * supplied to conversational model inference. No caller may substitute different
 * conversational text after constitutional intake.
 *
 * ConversationalResponseCoordinator remains responsible for requiring that the exact
 * intake result was PRODUCED and constitutionally ACCEPTED before model inference.
 *
 * RuntimeStatus is deliberately not treated as model-inference authority.
 *
 * Missing intake evidence, unavailable inference, or failed inference produces no
 * assistant entry and does not fabricate output.
 *
 * This coordinator does not:
 *
 * - perform Conversation Intake Authority;
 * - reinterpret runtime status as authorization;
 * - reconstruct RuntimeResult or ConversationRuntimePresentation;
 * - substitute content after constitutional intake;
 * - resolve identity or trust;
 * - authenticate a subject;
 * - grant authorization;
 * - execute capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform Learning;
 * - create, commit, persist, or recall Memory;
 * - or treat generated model text as verified truth.
 *
 * TRACE_CORRELATION != AUTHORIZATION.
 * INTAKE_CONTENT != CALLER_SUBSTITUTABLE_CONTENT.
 * CONVERSATION_INTAKE_ACCEPTED != MODEL_OUTPUT_VERIFIED.
 * ASSISTANT != RUNTIME.
 * GENERATED != VERIFIED.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
class AndroidConversationalResponseCompositionCoordinator(
    private val intakeEvidenceStore: AndroidConversationIntakeEvidenceStore,
    private val responseCoordinator: ConversationalResponseCoordinator,
    private val interactionCoordinator: ConversationInteractionCoordinator,
    private val entryIdProvider: ConversationEntryIdProvider,
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

        val intake =
            requireNotNull(
                conversationIntake.intake,
            ) {
                "Observed conversation-intake evidence must preserve its intake result."
            }

        val inference =
            responseCoordinator.generate(
                conversationIntake = conversationIntake,
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
            assistantEntryId = entryIdProvider.provide(),
            response = response,
        )
    }
}
