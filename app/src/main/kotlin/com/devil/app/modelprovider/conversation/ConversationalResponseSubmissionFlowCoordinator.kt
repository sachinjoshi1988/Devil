package com.devil.app.modelprovider.conversation

import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationSubmissionFlowCoordinator
import com.devil.app.conversation.ConversationUiState

/**
 * Stage 313 production-facing wrapper around the existing conversation-submission
 * flow.
 *
 * The existing ConversationSubmissionFlowCoordinator remains authoritative for
 * Stage 24 UI submission and runtime entry creation.
 *
 * This wrapper performs no runtime submission itself. After delegation, it may
 * request one bounded conversational-model response only when delegation created
 * one genuinely new RUNTIME presentation entry.
 *
 * The genuine TraceId preserved by that new RUNTIME timeline entry is used only
 * for correlation with already-observed constitutional conversation-intake
 * evidence.
 *
 * No runtime status is reconstructed from presentation text.
 * No UI message is parsed into constitutional state.
 * No old RUNTIME entry can trigger a new model invocation.
 *
 * Blank, already-submitting, metadata-unavailable, or otherwise non-runtime
 * completions produce no model inference.
 *
 * This wrapper does not:
 *
 * - create ContextEnvelope;
 * - create TraceId;
 * - invoke UnifiedDevilRuntime directly;
 * - perform Conversation Intake Authority;
 * - reconstruct RuntimeResult;
 * - infer runtime status from UI text;
 * - grant authorization;
 * - execute capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform Learning;
 * - create or persist Memory;
 * - or treat generated output as verified truth.
 *
 * NEW_RUNTIME_ENTRY != MODEL_AUTHORIZATION.
 * TRACE_CORRELATION != AUTHORIZATION.
 * GENERATED != VERIFIED.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
class ConversationalResponseSubmissionFlowCoordinator(
    private val submissionCoordinator:
        ConversationSubmissionFlowCoordinator,
    private val responseCompositionCoordinator:
        AndroidConversationalResponseCompositionCoordinator,
) : ConversationSubmissionFlowCoordinator {

    override fun submit(
        state: ConversationUiState,
    ): ConversationUiState {
        val previousEntryIds =
            state.entries
                .map { entry -> entry.id }
                .toSet()

        val submittedState =
            submissionCoordinator.submit(
                state = state,
            )

        val newRuntimeEntry =
            submittedState.entries
                .lastOrNull()
                ?.takeIf { entry ->
                    entry.role ==
                        ConversationEntryRole.RUNTIME &&
                        entry.id !in previousEntryIds
                }
                ?: return submittedState

        val runtimeTraceId =
            requireNotNull(
                newRuntimeEntry.traceId,
            ) {
                "A genuine runtime timeline entry must preserve its runtime TraceId."
            }

        return responseCompositionCoordinator.generateAndAppend(
            state = submittedState,
            runtimeTraceId = runtimeTraceId,
        )
    }
}
