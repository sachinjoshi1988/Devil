package com.devil.app.modelprovider.conversation

import com.devil.app.conversation.ConversationEntryId
import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationRuntimePresentation
import com.devil.app.conversation.ConversationRuntimePresentationStatus
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

class AndroidConversationalResponseCompositionCoordinatorTest {

    @Test
    fun `exact accepted intake generates one trace preserving assistant entry`() {
        val traceId =
            TraceId.from(
                "trace-stage-313-response-composition-001",
            )

        val store =
            AndroidConversationIntakeEvidenceStore()

        val acceptedIntake =
            producedIntake(
                traceId = traceId,
                content = "Hello Devil",
                state = ConversationIntakeState.ACCEPTED,
            )

        store.observe(acceptedIntake)

        var receivedRequestTrace: TraceId? = null
        var receivedContent: String? = null

        val inferencePort =
            ConversationalModelInferencePort { request ->
                receivedRequestTrace = request.traceId
                receivedContent = request.content

                ConversationalModelInferenceResult.available(
                    traceId = request.traceId,
                    generatedOutput =
                        "Hello. I am responding through the bounded Stage 313 path.",
                )
            }

        val coordinator =
            coordinator(
                store = store,
                inferencePort = inferencePort,
            )

        val runtimePresentation =
            ConversationRuntimePresentation(
                traceId = traceId,
                status =
                    ConversationRuntimePresentationStatus.DEFERRED,
                message = "Deferred by the Devil runtime.",
            )

        val result =
            coordinator.generateAndAppend(
                state =
                    ConversationUiState(
                        entries = emptyList(),
                    ),
                runtimeTraceId = runtimePresentation.traceId,
            )

        assertEquals(traceId, receivedRequestTrace)
        assertEquals("Hello Devil", receivedContent)

        val assistant = result.entries.single()

        assertEquals(
            ConversationEntryRole.ASSISTANT,
            assistant.role,
        )
        assertEquals(
            "Hello. I am responding through the bounded Stage 313 path.",
            assistant.content,
        )
        assertEquals(traceId, assistant.traceId)
    }

    @Test
    fun `missing intake evidence fails closed without model invocation`() {
        var inferenceCalls = 0

        val coordinator =
            coordinator(
                store =
                    AndroidConversationIntakeEvidenceStore(),
                inferencePort =
                    ConversationalModelInferencePort { request ->
                        inferenceCalls += 1

                        ConversationalModelInferenceResult.available(
                            traceId = request.traceId,
                            generatedOutput =
                                "Must not be generated.",
                        )
                    },
            )

        val original =
            ConversationUiState()

        val result =
            coordinator.generateAndAppend(
                state = original,
                runtimeTraceId =
                    presentation(
                        "trace-stage-313-response-composition-missing",
                    ).traceId,
            )

        assertEquals(original, result)
        assertEquals(0, inferenceCalls)
    }

    @Test
    fun `rejected or deferred intake evidence fails closed without model invocation`() {
        listOf(
            ConversationIntakeState.REJECTED,
            ConversationIntakeState.DEFERRED,
        ).forEachIndexed { index, intakeState ->
            val traceId =
                TraceId.from(
                    "trace-stage-314-response-composition-blocked-$index",
                )

            val store =
                AndroidConversationIntakeEvidenceStore()

            store.observe(
                producedIntake(
                    traceId = traceId,
                    content = "Open Settings",
                    state = intakeState,
                ),
            )

            var inferenceCalls = 0

            val coordinator =
                coordinator(
                    store = store,
                    inferencePort =
                        ConversationalModelInferencePort { request ->
                            inferenceCalls += 1

                            ConversationalModelInferenceResult.available(
                                traceId = request.traceId,
                                generatedOutput =
                                    "Must not be generated.",
                            )
                        },
                )

            val original =
                ConversationUiState()

            val result =
                coordinator.generateAndAppend(
                    state = original,
                    runtimeTraceId = traceId,
                )

            assertEquals(original, result)
            assertEquals(
                0,
                inferenceCalls,
                "Model inference must not run for intake state $intakeState.",
            )
        }
    }

    @Test
    fun `non produced intake evidence fails closed without model invocation`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-response-composition-non-produced",
            )

        val store =
            AndroidConversationIntakeEvidenceStore()

        store.observe(
            ConversationIntakeAuthorityResult.create(
                traceId = traceId,
                status =
                    ConversationIntakeAuthorityStatus.DEFERRED,
            ),
        )

        var inferenceCalls = 0

        val coordinator =
            coordinator(
                store = store,
                inferencePort =
                    ConversationalModelInferencePort { request ->
                        inferenceCalls += 1

                        ConversationalModelInferenceResult.available(
                            traceId = request.traceId,
                            generatedOutput =
                                "Must not be generated.",
                        )
                    },
            )

        val original =
            ConversationUiState()

        val result =
            coordinator.generateAndAppend(
                state = original,
                runtimeTraceId = traceId,
            )

        assertEquals(original, result)
        assertEquals(0, inferenceCalls)
    }

    @Test
    fun `unavailable inference appends no fabricated assistant entry`() {
        val traceId =
            TraceId.from(
                "trace-stage-313-response-composition-unavailable",
            )

        val store =
            populatedStore(
                traceId = traceId,
                content = "Hello Devil",
            )

        val coordinator =
            coordinator(
                store = store,
                inferencePort =
                    ConversationalModelInferencePort { request ->
                        ConversationalModelInferenceResult.unavailable(
                            traceId = request.traceId,
                        )
                    },
            )

        val original =
            ConversationUiState()

        val result =
            coordinator.generateAndAppend(
                state = original,
                runtimeTraceId =
                    ConversationRuntimePresentation(
                        traceId = traceId,
                        status =
                            ConversationRuntimePresentationStatus.DEFERRED,
                        message =
                            "Deferred by the Devil runtime.",
                    ).traceId,
            )

        assertEquals(original, result)
    }

    @Test
    fun `deferred runtime presentation does not replace accepted intake authority`() {
        val traceId =
            TraceId.from(
                "trace-stage-313-response-composition-deferred",
            )

        val store =
            populatedStore(
                traceId = traceId,
                content = "Explain this",
            )

        var inferenceCalls = 0

        val coordinator =
            coordinator(
                store = store,
                inferencePort =
                    ConversationalModelInferencePort { request ->
                        inferenceCalls += 1

                        ConversationalModelInferenceResult.available(
                            traceId = request.traceId,
                            generatedOutput =
                                "Generated after exact accepted intake evidence.",
                        )
                    },
            )

        val result =
            coordinator.generateAndAppend(
                state = ConversationUiState(),
                runtimeTraceId =
                    ConversationRuntimePresentation(
                        traceId = traceId,
                        status =
                            ConversationRuntimePresentationStatus.DEFERRED,
                        message =
                            "Deferred by the Devil runtime.",
                    ).traceId,
            )

        assertEquals(1, inferenceCalls)
        assertEquals(
            ConversationEntryRole.ASSISTANT,
            result.entries.single().role,
        )
    }

    private fun coordinator(
        store: AndroidConversationIntakeEvidenceStore,
        inferencePort: ConversationalModelInferencePort,
    ): AndroidConversationalResponseCompositionCoordinator {
        return AndroidConversationalResponseCompositionCoordinator(
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
                            "stage-313-generated-assistant-entry",
                        )
                    }
                },
        )
    }

    private fun populatedStore(
        traceId: TraceId,
        content: String,
    ): AndroidConversationIntakeEvidenceStore {
        val store =
            AndroidConversationIntakeEvidenceStore()

        store.observe(
            producedIntake(
                traceId = traceId,
                content = content,
                state = ConversationIntakeState.ACCEPTED,
            ),
        )

        return store
    }

    private fun producedIntake(
        traceId: TraceId,
        content: String,
        state: ConversationIntakeState,
    ): ConversationIntakeAuthorityResult {
        val input =
            input(
                traceId = traceId,
                content = content,
            )

        val intake =
            ConversationIntakeResult.create(
                record =
                    ConversationIntakeRecord.create(
                        input = input,
                        state = state,
                        rationale =
                            "Stage 314 bounded conversation intake test.",
                    ),
            )

        return ConversationIntakeAuthorityResult.create(
            traceId = traceId,
            status =
                ConversationIntakeAuthorityStatus.PRODUCED,
            intake = intake,
        )
    }

    private fun presentation(
        traceValue: String,
    ): ConversationRuntimePresentation {
        return ConversationRuntimePresentation(
            traceId = TraceId.from(traceValue),
            status =
                ConversationRuntimePresentationStatus.DEFERRED,
            message =
                "Deferred by the Devil runtime.",
        )
    }

    private fun input(
        traceId: TraceId,
        content: String,
    ): ConversationInput {
        return ConversationInput.create(
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
                            1_754_000_314_000L,
                        ),
                ),
            content = content,
        )
    }
}
