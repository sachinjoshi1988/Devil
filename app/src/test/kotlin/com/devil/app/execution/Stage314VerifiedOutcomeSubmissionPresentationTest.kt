package com.devil.app.execution

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.conversation.ConversationEntryId
import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationRuntimePresentation
import com.devil.app.conversation.ConversationRuntimePresentationStatus
import com.devil.app.conversation.ConversationSubmissionFlowCoordinator
import com.devil.app.conversation.ConversationTimelineEntry
import com.devil.app.conversation.ConversationUiState
import com.devil.app.outcome.Stage314VerifiedAndroidOutcomePresentationStore
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage314VerifiedOutcomeSubmissionPresentationTest {

    @Test
    fun `matching established outcome is appended separately from deferred runtime`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-verified-presentation",
            )

        val presentationStore =
            Stage314VerifiedAndroidOutcomePresentationStore()

        val coordinator =
            Stage314RealAndroidSubmissionFlowCoordinator(
                submissionCoordinator =
                    object : ConversationSubmissionFlowCoordinator {
                        override fun submit(
                            state: ConversationUiState,
                        ): ConversationUiState {
                            presentationStore.bindEstablished(
                                traceId = traceId,
                                capabilityId =
                                    AndroidAccessibilityCapability.capabilityId,
                                message =
                                    "Android action verified.",
                            )

                            return state.copy(
                                entries =
                                    state.entries +
                                        ConversationTimelineEntry.runtime(
                                            id =
                                                ConversationEntryId.from(
                                                    "entry-stage-314-runtime",
                                                ),
                                            presentation =
                                                ConversationRuntimePresentation(
                                                    traceId = traceId,
                                                    status =
                                                        ConversationRuntimePresentationStatus.DEFERRED,
                                                    message =
                                                        "Deferred by the Devil runtime.",
                                                ),
                                        ),
                                draft = "",
                                isSubmitting = false,
                            )
                        }
                    },
                directiveStore =
                    AndroidRealExecutionDirectiveStore(),
                presentationStore =
                    presentationStore,
                entryIdProvider =
                    fixedEntryIdProvider(
                        "entry-stage-314-outcome",
                    ),
            )

        val result =
            coordinator.submit(
                state =
                    ConversationUiState(
                        draft = "Open Settings",
                    ),
            )

        assertEquals(
            2,
            result.entries.size,
        )

        assertEquals(
            ConversationEntryRole.RUNTIME,
            result.entries[0].role,
        )

        assertEquals(
            "Deferred by the Devil runtime.",
            result.entries[0].content,
        )

        assertEquals(
            traceId,
            result.entries[0].traceId,
        )

        assertEquals(
            ConversationEntryRole.OUTCOME,
            result.entries[1].role,
        )

        assertEquals(
            "Android action verified.",
            result.entries[1].content,
        )

        assertEquals(
            traceId,
            result.entries[1].traceId,
        )
    }

    @Test
    fun `foreign runtime trace cannot consume established outcome`() {
        val establishedTraceId =
            TraceId.from(
                "trace-stage-314-established",
            )

        val runtimeTraceId =
            TraceId.from(
                "trace-stage-314-runtime-foreign",
            )

        val presentationStore =
            Stage314VerifiedAndroidOutcomePresentationStore()

        val coordinator =
            Stage314RealAndroidSubmissionFlowCoordinator(
                submissionCoordinator =
                    object : ConversationSubmissionFlowCoordinator {
                        override fun submit(
                            state: ConversationUiState,
                        ): ConversationUiState {
                            presentationStore.bindEstablished(
                                traceId = establishedTraceId,
                                capabilityId =
                                    AndroidAccessibilityCapability.capabilityId,
                                message =
                                    "Android action verified.",
                            )

                            return state.copy(
                                entries =
                                    state.entries +
                                        ConversationTimelineEntry.runtime(
                                            id =
                                                ConversationEntryId.from(
                                                    "entry-stage-314-runtime-foreign",
                                                ),
                                            presentation =
                                                ConversationRuntimePresentation(
                                                    traceId = runtimeTraceId,
                                                    status =
                                                        ConversationRuntimePresentationStatus.DEFERRED,
                                                    message =
                                                        "Deferred by the Devil runtime.",
                                                ),
                                        ),
                                draft = "",
                                isSubmitting = false,
                            )
                        }
                    },
                directiveStore =
                    AndroidRealExecutionDirectiveStore(),
                presentationStore =
                    presentationStore,
                entryIdProvider =
                    fixedEntryIdProvider(
                        "entry-stage-314-unused-outcome",
                    ),
            )

        val result =
            coordinator.submit(
                state =
                    ConversationUiState(
                        draft = "Open Settings",
                    ),
            )

        assertEquals(
            1,
            result.entries.size,
        )

        assertEquals(
            ConversationEntryRole.RUNTIME,
            result.entries.single().role,
        )
    }

    private fun fixedEntryIdProvider(
        value: String,
    ): ConversationEntryIdProvider {
        return object : ConversationEntryIdProvider {
            override fun provide(): ConversationEntryId {
                return ConversationEntryId.from(value)
            }
        }
    }
}
