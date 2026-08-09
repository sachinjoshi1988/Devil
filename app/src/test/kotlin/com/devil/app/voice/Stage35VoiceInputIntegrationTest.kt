package com.devil.app.voice

import com.devil.app.conversation.ConversationEntryId
import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.DefaultConversationRuntimeSubmissionCoordinator
import com.devil.app.conversation.DefaultConversationSubmissionFlowCoordinator
import com.devil.app.conversation.VoiceConversationRuntimeInputMetadataProvider
import com.devil.app.conversation.ConversationUiState
import com.devil.app.runtime.DefaultAndroidContextEnvelopeProvider
import com.devil.app.runtime.DefaultAndroidRuntimeGateway
import com.devil.app.runtime.DefaultAndroidRuntimeInputCoordinator
import com.devil.core.runtime.DefaultUnifiedDevilRuntime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Stage 35 production-path integration test for recognized Android voice input.
 *
 * This test proves that a genuine recognized transcript can enter the same
 * conversation architecture and the same DefaultUnifiedDevilRuntime while
 * preserving VOICE provenance.
 *
 * It does not prove speaker identity, authentication, semantic understanding,
 * capability execution, verification, outcome, or completion.
 */
class Stage35VoiceInputIntegrationTest {

    @Test
    fun `recognized voice transcript enters the one default unified runtime`() {
        val generatedIds =
            ArrayDeque(
                listOf(
                    "stage-35-real-user-entry",
                    "stage-35-real-runtime-entry",
                ),
            )

        val interactionCoordinator =
            ConversationInteractionCoordinator()

        val submissionFlowCoordinator =
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
                            DefaultAndroidRuntimeInputCoordinator(
                                contextEnvelopeProvider =
                                    DefaultAndroidContextEnvelopeProvider(),
                                runtimeGateway =
                                    DefaultAndroidRuntimeGateway(
                                        runtime =
                                            DefaultUnifiedDevilRuntime(),
                                    ),
                            ),
                    ),
            )

        val coordinator =
            VoiceConversationResultCoordinator(
                interactionCoordinator = interactionCoordinator,
                submissionFlowCoordinator = submissionFlowCoordinator,
            )

        val result =
            coordinator.handle(
                state = ConversationUiState(),
                result =
                    AndroidVoiceInputResult.recognized(
                        "  Hello Devil from voice  ",
                    ),
            )

        assertNull(result.message)
        assertEquals(false, result.state.isSubmitting)
        assertEquals("", result.state.draft)
        assertNull(result.state.submissionNotice)

        assertEquals(
            2,
            result.state.entries.size,
        )

        val userEntry = result.state.entries[0]
        val runtimeEntry = result.state.entries[1]

        assertEquals(
            ConversationEntryRole.USER,
            userEntry.role,
        )
        assertEquals(
            "Hello Devil from voice",
            userEntry.content,
        )
        assertNull(userEntry.traceId)

        assertEquals(
            ConversationEntryRole.RUNTIME,
            runtimeEntry.role,
        )
        assertEquals(
            "Deferred by the Devil runtime.",
            runtimeEntry.content,
        )
        assertNotNull(runtimeEntry.traceId)
    }

    @Test
    fun `non recognized voice result never fabricates runtime conversation entry`() {
        val interactionCoordinator =
            ConversationInteractionCoordinator()

        var submissionCalls = 0

        val coordinator =
            VoiceConversationResultCoordinator(
                interactionCoordinator = interactionCoordinator,
                submissionFlowCoordinator =
                    object :
                        com.devil.app.conversation.ConversationSubmissionFlowCoordinator {
                        override fun submit(
                            state: ConversationUiState,
                        ): ConversationUiState {
                            submissionCalls += 1
                            return state
                        }
                    },
            )

        val result =
            coordinator.handle(
                state = ConversationUiState(),
                result = AndroidVoiceInputResult.noMatch(),
            )

        assertEquals(0, submissionCalls)
        assertEquals(
            emptyList(),
            result.state.entries,
        )
        assertEquals(
            "No speech was recognized.",
            result.message,
        )
    }
}
