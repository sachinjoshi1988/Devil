package com.devil.app.voice

import com.devil.app.conversation.ConversationEntryId
import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationRuntimeInputMetadata
import com.devil.app.conversation.ConversationRuntimeInputMetadataProvider
import com.devil.app.conversation.ConversationRuntimeInputMetadataResult
import com.devil.app.conversation.DefaultConversationRuntimeSubmissionCoordinator
import com.devil.app.conversation.DefaultConversationSubmissionFlowCoordinator
import com.devil.app.conversation.VoiceConversationRuntimeInputMetadataProvider
import com.devil.app.conversation.ConversationUiState
import com.devil.app.runtime.AndroidRuntimeInputCoordinator
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.runtime.RuntimeResult
import com.devil.core.runtime.RuntimeStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class VoiceConversationResultCoordinatorTest {

    @Test
    fun `recognized transcript enters existing conversation path with voice provenance`() {
        val generatedIds =
            ArrayDeque(
                listOf(
                    "stage-35-user-entry",
                    "stage-35-runtime-entry",
                ),
            )

        var receivedSource: ContextSource? = null
        var receivedContent: String? = null
        var runtimeCalls = 0

        val interactionCoordinator =
            ConversationInteractionCoordinator()

        val submissionCoordinator =
            DefaultConversationSubmissionFlowCoordinator(
                interactionCoordinator = interactionCoordinator,
                entryIdProvider =
                    object : ConversationEntryIdProvider {
                        override fun provide(): ConversationEntryId {
                            return ConversationEntryId.from(
                                generatedIds.removeFirst(),
                            )
                        }
                    },
                runtimeSubmissionCoordinator =
                    DefaultConversationRuntimeSubmissionCoordinator(
                        metadataProvider =
                            VoiceConversationRuntimeInputMetadataProvider(),
                        runtimeInputCoordinator =
                            object : AndroidRuntimeInputCoordinator {
                                override fun submit(
                                    schemaVersion: SchemaVersion,
                                    source: ContextSource,
                                    trustLevel: ContextTrustLevel,
                                    securityLevel: ContextSecurityLevel,
                                    content: String,
                                ): RuntimeResult {
                                    runtimeCalls += 1
                                    receivedSource = source
                                    receivedContent = content

                                    return RuntimeResult.create(
                                        traceId =
                                            TraceId.from(
                                                "trace-stage-35-voice",
                                            ),
                                        status = RuntimeStatus.DEFERRED,
                                    )
                                }
                            },
                    ),
            )

        val coordinator =
            VoiceConversationResultCoordinator(
                interactionCoordinator = interactionCoordinator,
                submissionFlowCoordinator = submissionCoordinator,
            )

        val result =
            coordinator.handle(
                state = ConversationUiState(),
                result =
                    AndroidVoiceInputResult.recognized(
                        "  Hello Devil  ",
                    ),
            )

        assertEquals(1, runtimeCalls)
        assertEquals(ContextSource.VOICE, receivedSource)
        assertEquals("Hello Devil", receivedContent)
        assertNull(result.message)

        assertEquals(
            2,
            result.state.entries.size,
        )

        assertEquals(
            ConversationEntryRole.USER,
            result.state.entries[0].role,
        )

        assertEquals(
            "Hello Devil",
            result.state.entries[0].content,
        )

        assertEquals(
            ConversationEntryRole.RUNTIME,
            result.state.entries[1].role,
        )

        assertEquals(
            "Deferred by the Devil runtime.",
            result.state.entries[1].content,
        )
    }

    @Test
    fun `no match never enters runtime`() {
        var runtimeCalls = 0

        val interactionCoordinator =
            ConversationInteractionCoordinator()

        val submissionCoordinator =
            DefaultConversationSubmissionFlowCoordinator(
                interactionCoordinator = interactionCoordinator,
                entryIdProvider =
                    object : ConversationEntryIdProvider {
                        override fun provide(): ConversationEntryId {
                            return ConversationEntryId.from(
                                "unused-stage-35-entry",
                            )
                        }
                    },
                runtimeSubmissionCoordinator =
                    DefaultConversationRuntimeSubmissionCoordinator(
                        metadataProvider =
                            object : ConversationRuntimeInputMetadataProvider {
                                override fun provide():
                                    ConversationRuntimeInputMetadataResult {
                                    return ConversationRuntimeInputMetadataResult.available(
                                        ConversationRuntimeInputMetadata(
                                            schemaVersion = SchemaVersion.from(1),
                                            source = ContextSource.VOICE,
                                            trustLevel =
                                                ContextTrustLevel.UNVERIFIED,
                                            securityLevel =
                                                ContextSecurityLevel.RESTRICTED,
                                        ),
                                    )
                                }
                            },
                        runtimeInputCoordinator =
                            object : AndroidRuntimeInputCoordinator {
                                override fun submit(
                                    schemaVersion: SchemaVersion,
                                    source: ContextSource,
                                    trustLevel: ContextTrustLevel,
                                    securityLevel: ContextSecurityLevel,
                                    content: String,
                                ): RuntimeResult {
                                    runtimeCalls += 1

                                    return RuntimeResult.create(
                                        traceId =
                                            TraceId.from(
                                                "trace-stage-35-unused",
                                            ),
                                        status = RuntimeStatus.DEFERRED,
                                    )
                                }
                            },
                    ),
            )

        val coordinator =
            VoiceConversationResultCoordinator(
                interactionCoordinator = interactionCoordinator,
                submissionFlowCoordinator = submissionCoordinator,
            )

        val initialState =
            ConversationUiState(
                draft = "Existing draft",
            )

        val result =
            coordinator.handle(
                state = initialState,
                result = AndroidVoiceInputResult.noMatch(),
            )

        assertEquals(0, runtimeCalls)
        assertEquals(initialState, result.state)
        assertEquals(
            "No speech was recognized.",
            result.message,
        )
    }

    @Test
    fun `cancelled voice input never enters runtime`() {
        var submissionCalls = 0

        val coordinator =
            VoiceConversationResultCoordinator(
                interactionCoordinator =
                    ConversationInteractionCoordinator(),
                submissionFlowCoordinator =
                    object : com.devil.app.conversation.ConversationSubmissionFlowCoordinator {
                        override fun submit(
                            state: ConversationUiState,
                        ): ConversationUiState {
                            submissionCalls += 1
                            return state
                        }
                    },
            )

        val initialState =
            ConversationUiState(
                draft = "Existing draft",
            )

        val result =
            coordinator.handle(
                state = initialState,
                result = AndroidVoiceInputResult.cancelled(),
            )

        assertEquals(0, submissionCalls)
        assertEquals(initialState, result.state)
        assertEquals(
            "Voice input cancelled.",
            result.message,
        )
    }

    @Test
    fun `failed voice input never enters runtime`() {
        var submissionCalls = 0

        val coordinator =
            VoiceConversationResultCoordinator(
                interactionCoordinator =
                    ConversationInteractionCoordinator(),
                submissionFlowCoordinator =
                    object : com.devil.app.conversation.ConversationSubmissionFlowCoordinator {
                        override fun submit(
                            state: ConversationUiState,
                        ): ConversationUiState {
                            submissionCalls += 1
                            return state
                        }
                    },
            )

        val initialState = ConversationUiState()

        val result =
            coordinator.handle(
                state = initialState,
                result =
                    AndroidVoiceInputResult.failed(
                        "TEST_FAILURE",
                    ),
            )

        assertEquals(0, submissionCalls)
        assertEquals(initialState, result.state)
        assertEquals(
            "Voice input failed.",
            result.message,
        )
    }
}
