package com.devil.app.execution

import com.devil.app.accessibility.AndroidAccessibilityActionRequest
import com.devil.app.accessibility.AndroidAccessibilityActionType
import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.AndroidAccessibilityTarget
import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationSubmissionFlowCoordinator
import com.devil.app.conversation.ConversationUiState
import com.devil.app.outcome.Stage314VerifiedAndroidOutcomePresentationStore
import java.util.Locale

/**
 * Stage 314 bounded typed real-Android submission wrapper.
 *
 * This coordinator may arm one explicit Android embodiment action only from the
 * current pre-runtime draft before delegating to the already-established
 * conversation-submission flow.
 *
 * Stage 314 deliberately recognizes one alpha command:
 *
 * "Open Settings"
 *
 * That command arms only:
 *
 * CLICK_VISIBLE_TEXT("Settings")
 *
 * The wrapper does not:
 *
 * - create ContextEnvelope;
 * - create TraceId;
 * - create ExecutionRequest;
 * - perform Understanding;
 * - select a capability;
 * - authenticate a subject;
 * - grant authorization;
 * - establish Executive readiness;
 * - approve execution;
 * - grant Android permission;
 * - perform an Android action;
 * - establish Observation, Verification, Outcome, Learning, or Memory;
 * - inspect runtime presentation text;
 * - inspect generated model output;
 * - or create another runtime/execution path.
 *
 * The armed request remains unbound until the genuine constitutional execution
 * path supplies its own ExecutionRequest and trace identity.
 *
 * Any directive not consumed synchronously during delegated runtime submission
 * is removed in finally so it cannot leak into a later submission.
 *
 * After delegated submission completes, this wrapper may append one separate
 * presentation-only OUTCOME entry only when the genuine Stage 314 Outcome source
 * already established a record matching the new runtime TraceId and exact
 * Android accessibility capability.
 *
 * It does not reinterpret RuntimeStatus or manufacture Outcome evidence.
 *
 * ALPHA_COMMAND_RECOGNIZED != UNDERSTANDING_AUTHORITY.
 * ARMED != AUTHORIZED.
 * ARMED != TRACE_BOUND.
 * CAPABILITY_SELECTED != EXECUTION_APPROVED.
 * ANDROID_PERMISSION != DEVIL_AUTHORIZATION.
 * ATTEMPTED != VERIFIED.
 * OUTCOME != RUNTIME.
 * OUTCOME_ESTABLISHED != TASK_COMPLETED.
 */
class Stage314RealAndroidSubmissionFlowCoordinator(
    private val submissionCoordinator:
        ConversationSubmissionFlowCoordinator,
    private val directiveStore:
        AndroidRealExecutionDirectiveStore,
    private val presentationStore:
        Stage314VerifiedAndroidOutcomePresentationStore? = null,
    private val interactionCoordinator:
        ConversationInteractionCoordinator =
        ConversationInteractionCoordinator(),
    private val entryIdProvider:
        ConversationEntryIdProvider? = null,
) : ConversationSubmissionFlowCoordinator {

    override fun submit(
        state: ConversationUiState,
    ): ConversationUiState {
        if (
            state.isSubmitting ||
            !isStage314OpenSettingsCommand(
                state.draft,
            )
        ) {
            return submissionCoordinator.submit(
                state = state,
            )
        }

        val previousEntryIds =
            state.entries
                .map { entry ->
                    entry.id
                }
                .toSet()

        presentationStore?.clear()

        directiveStore.arm(
            accessibilityRequest =
                AndroidAccessibilityActionRequest(
                    actionType =
                        AndroidAccessibilityActionType
                            .CLICK_VISIBLE_TEXT,
                    target =
                        AndroidAccessibilityTarget.fromText(
                            "Settings",
                        ),
                ),
        )

        return try {
            val submittedState =
                submissionCoordinator.submit(
                    state = state,
                )

            val newRuntimeEntry =
                submittedState.entries
                    .lastOrNull { entry ->
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

            val outcomeMessage =
                presentationStore?.consume(
                    traceId = runtimeTraceId,
                    capabilityId =
                        AndroidAccessibilityCapability.capabilityId,
                )
                    ?: return submittedState

            val provider =
                entryIdProvider
                    ?: return submittedState

            interactionCoordinator.appendEstablishedOutcome(
                state = submittedState,
                outcomeEntryId = provider.provide(),
                traceId = runtimeTraceId,
                message = outcomeMessage,
            )
        } finally {
            directiveStore.clear()
            presentationStore?.clear()
        }
    }

    private fun isStage314OpenSettingsCommand(
        draft: String,
    ): Boolean {
        return draft
            .trim()
            .lowercase(Locale.ROOT) ==
            STAGE_314_OPEN_SETTINGS_COMMAND
    }

    private companion object {
        const val STAGE_314_OPEN_SETTINGS_COMMAND =
            "open settings"
    }
}
