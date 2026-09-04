package com.devil.app.voice

import com.devil.app.conversation.ConversationEntryId
import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationUiState
import com.devil.app.conversation.DefaultConversationRuntimeSubmissionCoordinator
import com.devil.app.conversation.DefaultConversationSubmissionFlowCoordinator
import com.devil.app.conversation.VoiceConversationRuntimeInputMetadataProvider
import com.devil.app.modelprovider.conversation.AndroidConversationIntakeEvidenceStore
import com.devil.app.modelprovider.conversation.AndroidConversationalResponseCompositionCoordinator
import com.devil.app.modelprovider.conversation.ConversationalResponseSubmissionFlowCoordinator
import com.devil.app.runtime.AndroidRuntimeInputCoordinator
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
import com.devil.core.runtime.RuntimeResult
import com.devil.core.runtime.RuntimeStatus
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityStatus
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferencePort
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferenceResult
import com.devil.core.runtime.modelprovider.conversation.ConversationalResponseCoordinator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Stage 337D executable integration proof for one recognized voice turn.
 *
 * Recognized voice keeps genuine VOICE provenance while entering the existing
 * conversation/runtime path and the same conversational-response wrapper used
 * by typed conversation.
 *
 * No voice-specific Brain, Understanding Authority, Decision Authority,
 * Planning Authority, model authority, or runtime is introduced.
 *
 * VOICE_SOURCE != SPEAKER_AUTHENTICATED.
 * RECOGNIZED != UNDERSTOOD.
 * RECOGNIZED != AUTHORIZED.
 * GENERATED != VERIFIED.
 * SPOKEN != VERIFIED.
 */
class Stage337DUnifiedVoiceConversationIntegrationTest {

    @Test
    fun `recognized voice preserves voice provenance and reaches shared response composition`() {
        val traceId =
            TraceId.from(
                "trace-stage337d-unified-voice-001",
            )

        val generatedIds =
            ArrayDeque(
                listOf(
                    "stage337d-voice-user",
                    "stage337d-voice-runtime",
                    "stage337d-voice-assistant",
                ),
            )

        val interactionCoordinator =
            ConversationInteractionCoordinator()

        val entryIdProvider =
            object : ConversationEntryIdProvider {
                override fun provide(): ConversationEntryId {
                    return ConversationEntryId.from(
                        generatedIds.removeFirst(),
                    )
                }
            }

        val intakeEvidenceStore =
            AndroidConversationIntakeEvidenceStore()

        var receivedSource: ContextSource? = null
        var receivedContent: String? = null
        var inferenceCalls = 0
        var inferredContent: String? = null

        val runtimeInputCoordinator =
            object : AndroidRuntimeInputCoordinator {

                override fun submit(
                    schemaVersion: SchemaVersion,
                    source: ContextSource,
                    trustLevel: ContextTrustLevel,
                    securityLevel: ContextSecurityLevel,
                    content: String,
                ): RuntimeResult {
                    receivedSource = source
                    receivedContent = content

                    intakeEvidenceStore.observe(
                        acceptedIntake(
                            traceId = traceId,
                            schemaVersion = schemaVersion,
                            source = source,
                            trustLevel = trustLevel,
                            securityLevel = securityLevel,
                            content = content,
                        ),
                    )

                    return RuntimeResult.create(
                        traceId = traceId,
                        status = RuntimeStatus.DEFERRED,
                    )
                }
            }

        val baseVoiceSubmissionFlow =
            DefaultConversationSubmissionFlowCoordinator(
                interactionCoordinator =
                    interactionCoordinator,
                entryIdProvider =
                    entryIdProvider,
                runtimeSubmissionCoordinator =
                    DefaultConversationRuntimeSubmissionCoordinator(
                        metadataProvider =
                            VoiceConversationRuntimeInputMetadataProvider(),
                        runtimeInputCoordinator =
                            runtimeInputCoordinator,
                    ),
            )

        val unifiedResponseFlow =
            ConversationalResponseSubmissionFlowCoordinator(
                submissionCoordinator =
                    baseVoiceSubmissionFlow,
                responseCompositionCoordinator =
                    AndroidConversationalResponseCompositionCoordinator(
                        intakeEvidenceStore =
                            intakeEvidenceStore,
                        responseCoordinator =
                            ConversationalResponseCoordinator(
                                inferencePort =
                                    ConversationalModelInferencePort { request ->
                                        inferenceCalls += 1
                                        inferredContent =
                                            request.content

                                        ConversationalModelInferenceResult.available(
                                            traceId =
                                                request.traceId,
                                            generatedOutput =
                                                "Unified Stage337D assistant response.",
                                        )
                                    },
                            ),
                        interactionCoordinator =
                            interactionCoordinator,
                        entryIdProvider =
                            entryIdProvider,
                    ),
            )

        val coordinator =
            VoiceConversationResultCoordinator(
                interactionCoordinator =
                    interactionCoordinator,
                submissionFlowCoordinator =
                    unifiedResponseFlow,
            )

        val handled =
            coordinator.handle(
                state =
                    ConversationUiState(),
                result =
                    AndroidVoiceInputResult.recognized(
                        "  Lower the volume  ",
                    ),
            )

        assertNull(handled.message)

        assertEquals(
            ContextSource.VOICE,
            receivedSource,
        )
        assertEquals(
            "Lower the volume",
            receivedContent,
        )

        assertEquals(
            1,
            inferenceCalls,
        )
        assertEquals(
            "Lower the volume",
            inferredContent,
        )

        assertEquals(
            3,
            handled.state.entries.size,
        )

        assertEquals(
            ConversationEntryRole.USER,
            handled.state.entries[0].role,
        )
        assertEquals(
            "Lower the volume",
            handled.state.entries[0].content,
        )

        assertEquals(
            ConversationEntryRole.RUNTIME,
            handled.state.entries[1].role,
        )
        assertEquals(
            traceId,
            handled.state.entries[1].traceId,
        )

        assertEquals(
            ConversationEntryRole.ASSISTANT,
            handled.state.entries[2].role,
        )
        assertEquals(
            "Unified Stage337D assistant response.",
            handled.state.entries[2].content,
        )
        assertEquals(
            traceId,
            handled.state.entries[2].traceId,
        )
    }

    private fun acceptedIntake(
        traceId: TraceId,
        schemaVersion: SchemaVersion,
        source: ContextSource,
        trustLevel: ContextTrustLevel,
        securityLevel: ContextSecurityLevel,
        content: String,
    ): ConversationIntakeAuthorityResult {
        val input =
            ConversationInput.create(
                context =
                    ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion = schemaVersion,
                        source = source,
                        trustLevel = trustLevel,
                        securityLevel = securityLevel,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_788_000_337_004L,
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
                                "Stage337D accepted voice conversation evidence.",
                        ),
                ),
        )
    }
}
