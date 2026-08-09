package com.devil.app.conversation

import com.devil.app.runtime.DefaultAndroidContextEnvelopeProvider
import com.devil.app.runtime.DefaultAndroidRuntimeGateway
import com.devil.app.runtime.DefaultAndroidRuntimeInputCoordinator
import com.devil.core.runtime.DefaultUnifiedDevilRuntime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Stage 34 production-path integration tests for bounded typed text conversation.
 *
 * These tests use the real default Android conversation metadata provider and the
 * real default Unified Devil Runtime.
 *
 * They prove only that typed Android text can now enter the single constitutional
 * runtime through the approved Android boundaries and that the genuine immediate
 * runtime result is represented without reinterpretation.
 *
 * They do not establish language understanding, a Devil answer, capability
 * execution, observation, verification, final outcome, task completion, learning,
 * or memory persistence.
 */
class Stage34TextConversationIntegrationTest {

    @Test
    fun `typed text enters one default unified runtime and returns genuine deferred presentation`() {
        val generatedIds =
            ArrayDeque(
                listOf(
                    "stage-34-user-entry",
                    "stage-34-runtime-entry",
                ),
            )

        val coordinator =
            DefaultConversationSubmissionFlowCoordinator(
                entryIdProvider =
                    DefaultConversationEntryIdProvider(
                        rawIdProvider = {
                            generatedIds.removeFirst()
                        },
                    ),
                runtimeSubmissionCoordinator =
                    DefaultConversationRuntimeSubmissionCoordinator(
                        metadataProvider =
                            DefaultConversationRuntimeInputMetadataProvider(),
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

        val result =
            coordinator.submit(
                state =
                    ConversationUiState(
                        draft = "  Hello Devil  ",
                    ),
            )

        assertEquals(
            false,
            result.isSubmitting,
        )
        assertEquals(
            "",
            result.draft,
        )
        assertNull(result.submissionNotice)
        assertEquals(
            2,
            result.entries.size,
        )

        val userEntry = result.entries[0]
        val runtimeEntry = result.entries[1]

        assertEquals(
            ConversationEntryRole.USER,
            userEntry.role,
        )
        assertEquals(
            "Hello Devil",
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
    fun `default typed text production path does not fabricate assistant success`() {
        val generatedIds =
            ArrayDeque(
                listOf(
                    "stage-34-user-entry-002",
                    "stage-34-runtime-entry-002",
                ),
            )

        val coordinator =
            DefaultConversationSubmissionFlowCoordinator(
                entryIdProvider =
                    DefaultConversationEntryIdProvider(
                        rawIdProvider = {
                            generatedIds.removeFirst()
                        },
                    ),
                runtimeSubmissionCoordinator =
                    DefaultConversationRuntimeSubmissionCoordinator(
                        metadataProvider =
                            DefaultConversationRuntimeInputMetadataProvider(),
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

        val result =
            coordinator.submit(
                state =
                    ConversationUiState(
                        draft = "Tell me something",
                    ),
            )

        val runtimeEntry = result.entries.last()

        assertEquals(
            ConversationEntryRole.RUNTIME,
            runtimeEntry.role,
        )
        assertEquals(
            "Deferred by the Devil runtime.",
            runtimeEntry.content,
        )
        assertNotNull(runtimeEntry.traceId)
        assertNull(result.submissionNotice)
    }
}
