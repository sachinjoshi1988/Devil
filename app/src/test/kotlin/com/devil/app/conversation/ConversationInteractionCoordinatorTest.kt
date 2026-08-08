package com.devil.app.conversation

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ConversationInteractionCoordinatorTest {

    private val coordinator =
        ConversationInteractionCoordinator()

    @Test
    fun `draft update preserves local text while idle`() {
        val state =
            coordinator.updateDraft(
                state = ConversationUiState(),
                draft = "Hello Devil",
            )

        assertEquals(
            "Hello Devil",
            state.draft,
        )
        assertEquals(
            false,
            state.isSubmitting,
        )
        assertEquals(
            emptyList(),
            state.entries,
        )
    }

    @Test
    fun `draft update is ignored while submission is active`() {
        val state =
            ConversationUiState(
                draft = "",
                isSubmitting = true,
            )

        val updated =
            coordinator.updateDraft(
                state = state,
                draft = "Duplicate edit",
            )

        assertEquals(
            state,
            updated,
        )
    }

    @Test
    fun `blank draft does not begin submission`() {
        val original =
            ConversationUiState(
                draft = "   ",
            )

        val result =
            coordinator.beginSubmission(
                state = original,
                userEntryId =
                    ConversationEntryId.from(
                        "entry-interaction-001",
                    ),
            )

        assertEquals(
            ConversationSubmissionStartStatus.IGNORED_BLANK,
            result.status,
        )
        assertEquals(
            original,
            result.state,
        )
        assertNull(result.content)
    }

    @Test
    fun `non blank draft creates one user entry and begins submission`() {
        val result =
            coordinator.beginSubmission(
                state =
                    ConversationUiState(
                        draft = "  Hello Devil  ",
                    ),
                userEntryId =
                    ConversationEntryId.from(
                        "entry-interaction-002",
                    ),
            )

        assertEquals(
            ConversationSubmissionStartStatus.STARTED,
            result.status,
        )
        assertEquals(
            "Hello Devil",
            result.content,
        )
        assertEquals(
            "",
            result.state.draft,
        )
        assertEquals(
            true,
            result.state.isSubmitting,
        )
        assertEquals(
            1,
            result.state.entries.size,
        )

        val entry = result.state.entries.single()

        assertEquals(
            ConversationEntryRole.USER,
            entry.role,
        )
        assertEquals(
            "Hello Devil",
            entry.content,
        )
        assertNull(entry.traceId)
    }

    @Test
    fun `active submission prevents duplicate submission`() {
        val first =
            coordinator.beginSubmission(
                state =
                    ConversationUiState(
                        draft = "First",
                    ),
                userEntryId =
                    ConversationEntryId.from(
                        "entry-interaction-003",
                    ),
            )

        val duplicate =
            coordinator.beginSubmission(
                state = first.state,
                userEntryId =
                    ConversationEntryId.from(
                        "entry-interaction-004",
                    ),
            )

        assertEquals(
            ConversationSubmissionStartStatus.ALREADY_SUBMITTING,
            duplicate.status,
        )
        assertEquals(
            first.state,
            duplicate.state,
        )
        assertNull(duplicate.content)
        assertEquals(
            1,
            duplicate.state.entries.size,
        )
    }

    @Test
    fun `completion appends truthful runtime presentation and returns idle`() {
        val started =
            coordinator.beginSubmission(
                state =
                    ConversationUiState(
                        draft = "Hello Devil",
                    ),
                userEntryId =
                    ConversationEntryId.from(
                        "entry-interaction-005",
                    ),
            )

        val traceId =
            TraceId.from(
                "trace-conversation-interaction-001",
            )

        val completed =
            coordinator.completeSubmission(
                state = started.state,
                runtimeEntryId =
                    ConversationEntryId.from(
                        "entry-interaction-006",
                    ),
                presentation =
                    ConversationRuntimePresentation(
                        traceId = traceId,
                        status =
                            ConversationRuntimePresentationStatus.DEFERRED,
                        message =
                            "Deferred by the Devil runtime.",
                    ),
            )

        assertEquals(
            false,
            completed.isSubmitting,
        )
        assertEquals(
            2,
            completed.entries.size,
        )

        val runtimeEntry = completed.entries.last()

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
    fun `completion rejects idle state`() {
        assertFailsWith<IllegalArgumentException> {
            coordinator.completeSubmission(
                state = ConversationUiState(),
                runtimeEntryId =
                    ConversationEntryId.from(
                        "entry-interaction-007",
                    ),
                presentation =
                    ConversationRuntimePresentation(
                        traceId =
                            TraceId.from(
                                "trace-conversation-interaction-002",
                            ),
                        status =
                            ConversationRuntimePresentationStatus.ACCEPTED,
                        message =
                            "Accepted for constitutional processing.",
                    ),
            )
        }
    }
}
