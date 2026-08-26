package com.devil.app.voice

import com.devil.app.conversation.ConversationEntryId
import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationUiState
import com.devil.app.conversation.DefaultConversationRuntimeSubmissionCoordinator
import com.devil.app.conversation.DefaultConversationSubmissionFlowCoordinator
import com.devil.app.conversation.VoiceConversationRuntimeInputMetadataProvider
import com.devil.app.runtime.DefaultAndroidContextEnvelopeProvider
import com.devil.app.runtime.DefaultAndroidRuntimeGateway
import com.devil.app.runtime.DefaultAndroidRuntimeInputCoordinator
import com.devil.core.runtime.DefaultUnifiedDevilRuntime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Stage 297 Integration Test Completion coverage for the established
 * recognized-voice -> conversation -> Unified Devil Runtime production path.
 *
 * This test surface validates existing integration behavior only.
 *
 * Voice input does not establish speaker identity, authentication,
 * authorization, execution, Verification, Outcome, Learning or Memory.
 *
 * Stage 297 does not implement Stage 298 End-to-End Constitutional Tests.
 */
class Stage297VoiceRuntimeIntegrationCompletionTest {

    @Test
    fun `recognized voice completes existing voice to unified runtime integration path`() {
        val generatedIds =
            ArrayDeque(
                listOf(
                    "stage-297-voice-user-entry",
                    "stage-297-voice-runtime-entry",
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
                        "  Stage 297 voice integration  ",
                    ),
            )

        assertNull(result.message)
        assertEquals(false, result.state.isSubmitting)
        assertEquals("", result.state.draft)
        assertNull(result.state.submissionNotice)
        assertEquals(2, result.state.entries.size)

        val userEntry = result.state.entries[0]
        val runtimeEntry = result.state.entries[1]

        assertEquals(
            ConversationEntryRole.USER,
            userEntry.role,
        )
        assertEquals(
            "Stage 297 voice integration",
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
}
