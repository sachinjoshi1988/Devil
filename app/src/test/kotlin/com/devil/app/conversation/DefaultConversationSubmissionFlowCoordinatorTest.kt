package com.devil.app.conversation

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultConversationSubmissionFlowCoordinatorTest {

    @Test
    fun `blank draft remains unchanged without runtime submission`() {
        var runtimeCalls = 0

        val coordinator =
            DefaultConversationSubmissionFlowCoordinator(
                entryIdProvider =
                    fixedEntryIdProvider(
                        "entry-flow-001",
                    ),
                runtimeSubmissionCoordinator =
                    object : ConversationRuntimeSubmissionCoordinator {
                        override fun submit(
                            content: String,
                        ): ConversationRuntimeSubmissionResult {
                            runtimeCalls += 1

                            error(
                                "Runtime submission must not occur for blank draft.",
                            )
                        }
                    },
            )

        val original =
            ConversationUiState(
                draft = "   ",
            )

        val result =
            coordinator.submit(
                state = original,
            )

        assertEquals(original, result)
        assertEquals(0, runtimeCalls)
    }

    @Test
    fun `active submission remains unchanged without duplicate runtime submission`() {
        var runtimeCalls = 0

        val coordinator =
            DefaultConversationSubmissionFlowCoordinator(
                entryIdProvider =
                    fixedEntryIdProvider(
                        "entry-flow-002",
                    ),
                runtimeSubmissionCoordinator =
                    object : ConversationRuntimeSubmissionCoordinator {
                        override fun submit(
                            content: String,
                        ): ConversationRuntimeSubmissionResult {
                            runtimeCalls += 1

                            error(
                                "Duplicate runtime submission must not occur.",
                            )
                        }
                    },
            )

        val original =
            ConversationUiState(
                isSubmitting = true,
            )

        val result =
            coordinator.submit(
                state = original,
            )

        assertEquals(original, result)
        assertEquals(0, runtimeCalls)
    }

    @Test
    fun `metadata unavailable preserves user entry and produces truthful local notice`() {
        val generatedIds =
            ArrayDeque(
                listOf(
                    "entry-flow-user-001",
                ),
            )

        var submittedContent: String? = null

        val coordinator =
            DefaultConversationSubmissionFlowCoordinator(
                entryIdProvider =
                    DefaultConversationEntryIdProvider(
                        rawIdProvider = {
                            generatedIds.removeFirst()
                        },
                    ),
                runtimeSubmissionCoordinator =
                    object : ConversationRuntimeSubmissionCoordinator {
                        override fun submit(
                            content: String,
                        ): ConversationRuntimeSubmissionResult {
                            submittedContent = content

                            return ConversationRuntimeSubmissionResult
                                .metadataUnavailable()
                        }
                    },
            )

        val result =
            coordinator.submit(
                state =
                    ConversationUiState(
                        draft = "  Hello Devil  ",
                    ),
            )

        assertEquals(
            "Hello Devil",
            submittedContent,
        )
        assertEquals(
            "",
            result.draft,
        )
        assertEquals(
            false,
            result.isSubmitting,
        )
        assertEquals(
            1,
            result.entries.size,
        )

        val userEntry = result.entries.single()

        assertEquals(
            ConversationEntryRole.USER,
            userEntry.role,
        )
        assertEquals(
            "Hello Devil",
            userEntry.content,
        )
        assertNull(userEntry.traceId)

        val notice =
            requireNotNull(
                result.submissionNotice,
            )

        assertEquals(
            ConversationSubmissionNoticeStatus.METADATA_UNAVAILABLE,
            notice.status,
        )
        assertEquals(
            "Runtime submission is unavailable because required constitutional metadata is not available.",
            notice.message,
        )
    }

    @Test
    fun `genuine runtime submission appends one trace backed runtime entry`() {
        val generatedIds =
            ArrayDeque(
                listOf(
                    "entry-flow-user-002",
                    "entry-flow-runtime-001",
                ),
            )

        val traceId =
            TraceId.from(
                "trace-conversation-flow-001",
            )

        var runtimeCalls = 0

        val coordinator =
            DefaultConversationSubmissionFlowCoordinator(
                entryIdProvider =
                    DefaultConversationEntryIdProvider(
                        rawIdProvider = {
                            generatedIds.removeFirst()
                        },
                    ),
                runtimeSubmissionCoordinator =
                    object : ConversationRuntimeSubmissionCoordinator {
                        override fun submit(
                            content: String,
                        ): ConversationRuntimeSubmissionResult {
                            runtimeCalls += 1

                            return ConversationRuntimeSubmissionResult.submitted(
                                presentation =
                                    ConversationRuntimePresentation(
                                        traceId = traceId,
                                        status =
                                            ConversationRuntimePresentationStatus.DEFERRED,
                                        message =
                                            "Deferred by the Devil runtime.",
                                    ),
                            )
                        }
                    },
            )

        val result =
            coordinator.submit(
                state =
                    ConversationUiState(
                        draft = "Hello Devil",
                    ),
            )

        assertEquals(1, runtimeCalls)
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
        assertEquals(
            traceId,
            runtimeEntry.traceId,
        )
    }

    @Test
    fun `runtime entry identity is not requested when metadata is unavailable`() {
        var identityCalls = 0

        val coordinator =
            DefaultConversationSubmissionFlowCoordinator(
                entryIdProvider =
                    object : ConversationEntryIdProvider {
                        override fun provide(): ConversationEntryId {
                            identityCalls += 1

                            return ConversationEntryId.from(
                                "entry-flow-$identityCalls",
                            )
                        }
                    },
                runtimeSubmissionCoordinator =
                    object : ConversationRuntimeSubmissionCoordinator {
                        override fun submit(
                            content: String,
                        ): ConversationRuntimeSubmissionResult {
                            return ConversationRuntimeSubmissionResult
                                .metadataUnavailable()
                        }
                    },
            )

        val result =
            coordinator.submit(
                state =
                    ConversationUiState(
                        draft = "Hello Devil",
                    ),
            )

        assertEquals(
            1,
            identityCalls,
        )
        assertEquals(
            1,
            result.entries.size,
        )
        assertNull(
            result.entries.single().traceId,
        )
    }

    private fun fixedEntryIdProvider(
        value: String,
    ): ConversationEntryIdProvider {
        return object : ConversationEntryIdProvider {
            override fun provide(): ConversationEntryId {
                return ConversationEntryId.from(
                    value,
                )
            }
        }
    }
}
