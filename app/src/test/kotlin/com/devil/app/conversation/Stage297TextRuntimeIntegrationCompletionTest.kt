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
 * Stage 297 Integration Test Completion coverage for the established
 * typed-text Android conversation -> Unified Devil Runtime production path.
 *
 * This test surface validates existing integration behavior only.
 *
 * Stage 297 does not modify production architecture, grant authorization,
 * perform execution, establish Observation, Verification or Outcome,
 * create Learning or Memory, or implement Stage 298 End-to-End
 * Constitutional Tests.
 */
class Stage297TextRuntimeIntegrationCompletionTest {

    @Test
    fun `typed text completes existing Android to unified runtime integration path`() {
        val generatedIds =
            ArrayDeque(
                listOf(
                    "stage-297-text-user-entry",
                    "stage-297-text-runtime-entry",
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
                        draft = "  Stage 297 integration text  ",
                    ),
            )

        assertEquals(false, result.isSubmitting)
        assertEquals("", result.draft)
        assertNull(result.submissionNotice)
        assertEquals(2, result.entries.size)

        val userEntry = result.entries[0]
        val runtimeEntry = result.entries[1]

        assertEquals(
            ConversationEntryRole.USER,
            userEntry.role,
        )
        assertEquals(
            "Stage 297 integration text",
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
