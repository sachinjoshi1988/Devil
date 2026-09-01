package com.devil.app.modelprovider.conversation

import com.devil.app.conversation.ConversationEntryId
import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationRuntimePresentation
import com.devil.app.conversation.ConversationRuntimePresentationStatus
import com.devil.app.conversation.ConversationSubmissionFlowCoordinator
import com.devil.app.conversation.ConversationTimelineEntry
import com.devil.app.conversation.ConversationUiState
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.model.conversation.ConversationIntakeRecord
import com.devil.core.model.conversation.ConversationIntakeResult
import com.devil.core.model.conversation.ConversationIntakeState
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityStatus
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferencePort
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferenceResult
import com.devil.core.runtime.modelprovider.conversation.ConversationalResponseCoordinator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ConversationalResponseSubmissionFlowCoordinatorTest {

    @Test
    fun `genuine newly created runtime entry correlates exact intake and appends assistant response`() {
        val traceId =
            TraceId.from(
                "trace-stage-313-wrapper-001",
            )

        val store =
            AndroidConversationIntakeEvidenceStore()

        store.observe(
            producedAcceptedIntake(
                traceId = traceId,
                content = "Hello Devil",
            ),
        )

        var inferenceCalls = 0
        var receivedTraceId: TraceId? = null
        var receivedContent: String? = null

        val coordinator =
            wrapper(
                store = store,
                inferencePort =
                    ConversationalModelInferencePort { request ->
                        inferenceCalls += 1
                        receivedTraceId = request.traceId
                        receivedContent = request.content

                        ConversationalModelInferenceResult.available(
                            traceId = request.traceId,
                            generatedOutput =
                                "Hello from the bounded Stage 313 response path.",
                        )
                    },
                submissionCoordinator =
                    submissionFlow { state ->
                        state.copy(
                            entries =
                                state.entries +
                                    ConversationTimelineEntry.user(
                                        id =
                                            ConversationEntryId.from(
                                                "entry-stage313-user-001",
                                            ),
                                        content =
                                            state.draft,
                                    ) +
                                    ConversationTimelineEntry.runtime(
                                        id =
                                            ConversationEntryId.from(
                                                "entry-stage313-runtime-001",
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
                    },
            )

        val result =
            coordinator.submit(
                state =
                    ConversationUiState(
                        draft = "Hello Devil",
                    ),
            )

        assertEquals(1, inferenceCalls)
        assertEquals(traceId, receivedTraceId)
        assertEquals("Hello Devil", receivedContent)
        assertEquals(3, result.entries.size)

        val assistant =
            result.entries.last()

        assertEquals(
            ConversationEntryRole.ASSISTANT,
            assistant.role,
        )
        assertEquals(
            "Hello from the bounded Stage 313 response path.",
            assistant.content,
        )
        assertEquals(traceId, assistant.traceId)
    }

    @Test
    fun `unchanged blank submission cannot invoke model inference`() {
        var inferenceCalls = 0

        val original =
            ConversationUiState(
                draft = "   ",
            )

        val coordinator =
            wrapper(
                store =
                    AndroidConversationIntakeEvidenceStore(),
                inferencePort =
                    countingInferencePort {
                        inferenceCalls += 1
                    },
                submissionCoordinator =
                    submissionFlow { state ->
                        state
                    },
            )

        val result =
            coordinator.submit(original)

        assertEquals(original, result)
        assertEquals(0, inferenceCalls)
    }

    @Test
    fun `metadata unavailable user-only completion cannot invoke model inference`() {
        var inferenceCalls = 0

        val coordinator =
            wrapper(
                store =
                    AndroidConversationIntakeEvidenceStore(),
                inferencePort =
                    countingInferencePort {
                        inferenceCalls += 1
                    },
                submissionCoordinator =
                    submissionFlow { state ->
                        state.copy(
                            entries =
                                state.entries +
                                    ConversationTimelineEntry.user(
                                        id =
                                            ConversationEntryId.from(
                                                "entry-stage313-user-metadata-unavailable",
                                            ),
                                        content =
                                            state.draft,
                                    ),
                            draft = "",
                            isSubmitting = false,
                        )
                    },
            )

        val result =
            coordinator.submit(
                ConversationUiState(
                    draft = "Hello Devil",
                ),
            )

        assertEquals(0, inferenceCalls)
        assertEquals(1, result.entries.size)
        assertEquals(
            ConversationEntryRole.USER,
            result.entries.single().role,
        )
        assertNull(
            result.entries.single().traceId,
        )
    }

    @Test
    fun `preexisting runtime entry cannot trigger model inference when delegate adds nothing`() {
        val existingTraceId =
            TraceId.from(
                "trace-stage-313-old-runtime",
            )

        var inferenceCalls = 0

        val original =
            ConversationUiState(
                entries =
                    listOf(
                        ConversationTimelineEntry.runtime(
                            id =
                                ConversationEntryId.from(
                                    "entry-stage313-old-runtime",
                                ),
                            presentation =
                                ConversationRuntimePresentation(
                                    traceId = existingTraceId,
                                    status =
                                        ConversationRuntimePresentationStatus.DEFERRED,
                                    message =
                                        "Deferred by the Devil runtime.",
                                ),
                        ),
                    ),
            )

        val coordinator =
            wrapper(
                store =
                    AndroidConversationIntakeEvidenceStore(),
                inferencePort =
                    countingInferencePort {
                        inferenceCalls += 1
                    },
                submissionCoordinator =
                    submissionFlow { state ->
                        state
                    },
            )

        val result =
            coordinator.submit(original)

        assertEquals(original, result)
        assertEquals(0, inferenceCalls)
    }

    @Test
    fun `new runtime trace without matching intake evidence fails closed without inference`() {
        val traceId =
            TraceId.from(
                "trace-stage-313-no-intake",
            )

        var inferenceCalls = 0

        val coordinator =
            wrapper(
                store =
                    AndroidConversationIntakeEvidenceStore(),
                inferencePort =
                    countingInferencePort {
                        inferenceCalls += 1
                    },
                submissionCoordinator =
                    submissionFlow { state ->
                        state.copy(
                            entries =
                                state.entries +
                                    ConversationTimelineEntry.runtime(
                                        id =
                                            ConversationEntryId.from(
                                                "entry-stage313-runtime-no-intake",
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
                        )
                    },
            )

        val original =
            ConversationUiState()

        val result =
            coordinator.submit(original)

        assertEquals(0, inferenceCalls)
        assertEquals(1, result.entries.size)
        assertEquals(
            ConversationEntryRole.RUNTIME,
            result.entries.single().role,
        )
        assertEquals(
            traceId,
            result.entries.single().traceId,
        )
    }

    @Test
    fun `new runtime remains discoverable when established outcome follows it`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-stage313-outcome-compatibility",
            )

        val store =
            AndroidConversationIntakeEvidenceStore()

        store.observe(
            producedAcceptedIntake(
                traceId = traceId,
                content = "Open Settings",
            ),
        )

        var inferenceCalls = 0
        var receivedTraceId: TraceId? = null

        val coordinator =
            wrapper(
                store = store,
                inferencePort =
                    ConversationalModelInferencePort { request ->
                        inferenceCalls += 1
                        receivedTraceId = request.traceId

                        ConversationalModelInferenceResult.available(
                            traceId = request.traceId,
                            generatedOutput =
                                "Stage 313 compatibility response.",
                        )
                    },
                submissionCoordinator =
                    submissionFlow { state ->
                        state.copy(
                            entries =
                                state.entries +
                                    ConversationTimelineEntry.runtime(
                                        id =
                                            ConversationEntryId.from(
                                                "entry-stage314-compatible-runtime",
                                            ),
                                        presentation =
                                            ConversationRuntimePresentation(
                                                traceId = traceId,
                                                status =
                                                    ConversationRuntimePresentationStatus.DEFERRED,
                                                message =
                                                    "Deferred by the Devil runtime.",
                                            ),
                                    ) +
                                    ConversationTimelineEntry.outcome(
                                        id =
                                            ConversationEntryId.from(
                                                "entry-stage314-compatible-outcome",
                                            ),
                                        traceId = traceId,
                                        content =
                                            "Android action verified.",
                                    ),
                            draft = "",
                            isSubmitting = false,
                        )
                    },
            )

        val result =
            coordinator.submit(
                ConversationUiState(
                    draft = "Open Settings",
                ),
            )

        assertEquals(
            1,
            inferenceCalls,
        )
        assertEquals(
            traceId,
            receivedTraceId,
        )

        assertEquals(
            3,
            result.entries.size,
        )

        assertEquals(
            ConversationEntryRole.RUNTIME,
            result.entries[0].role,
        )
        assertEquals(
            ConversationEntryRole.OUTCOME,
            result.entries[1].role,
        )
        assertEquals(
            ConversationEntryRole.ASSISTANT,
            result.entries[2].role,
        )

        assertEquals(
            traceId,
            result.entries[0].traceId,
        )
        assertEquals(
            traceId,
            result.entries[1].traceId,
        )
        assertEquals(
            traceId,
            result.entries[2].traceId,
        )

        assertEquals(
            "Android action verified.",
            result.entries[1].content,
        )
        assertEquals(
            "Stage 313 compatibility response.",
            result.entries[2].content,
        )
    }
    private fun submissionFlow(
        submitBlock: (ConversationUiState) -> ConversationUiState,
    ): ConversationSubmissionFlowCoordinator {
        return object : ConversationSubmissionFlowCoordinator {
            override fun submit(
                state: ConversationUiState,
            ): ConversationUiState {
                return submitBlock(state)
            }
        }
    }

    private fun wrapper(
        store: AndroidConversationIntakeEvidenceStore,
        inferencePort: ConversationalModelInferencePort,
        submissionCoordinator: ConversationSubmissionFlowCoordinator,
    ): ConversationalResponseSubmissionFlowCoordinator {
        return ConversationalResponseSubmissionFlowCoordinator(
            submissionCoordinator =
                submissionCoordinator,
            responseCompositionCoordinator =
                AndroidConversationalResponseCompositionCoordinator(
                    intakeEvidenceStore = store,
                    responseCoordinator =
                        ConversationalResponseCoordinator(
                            inferencePort = inferencePort,
                        ),
                    interactionCoordinator =
                        ConversationInteractionCoordinator(),
                    entryIdProvider =
                        object : ConversationEntryIdProvider {
                            override fun provide(): ConversationEntryId {
                                return ConversationEntryId.from(
                                    "entry-stage313-generated-assistant",
                                )
                            }
                        },
                ),
        )
    }

    private fun countingInferencePort(
        onCall: () -> Unit,
    ): ConversationalModelInferencePort {
        return ConversationalModelInferencePort { request ->
            onCall()

            ConversationalModelInferenceResult.available(
                traceId = request.traceId,
                generatedOutput =
                    "This model call must not occur.",
            )
        }
    }

    private fun producedAcceptedIntake(
        traceId: TraceId,
        content: String,
    ): ConversationIntakeAuthorityResult {
        val input =
            ConversationInput.create(
                context =
                    ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion =
                            SchemaVersion.from(1),
                        source =
                            ContextSource.TEST,
                        trustLevel =
                            ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_313_000L,
                            ),
                    ),
                content = content,
            )

        return ConversationIntakeAuthorityResult.create(
            traceId = traceId,
            status =
                ConversationIntakeAuthorityStatus.PRODUCED,
            intake =
                ConversationIntakeResult.create(
                    record =
                        ConversationIntakeRecord.create(
                            input = input,
                            state =
                                ConversationIntakeState.ACCEPTED,
                            rationale =
                                "Stage 313 bounded accepted wrapper test intake.",
                        ),
                ),
        )
    }
}
