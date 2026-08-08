package com.devil.app.conversation

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ConversationUiStateTest {

    @Test
    fun `conversation entry id preserves normalized UI identity`() {
        val id = ConversationEntryId.from(
            "  entry-001  ",
        )

        assertEquals(
            "entry-001",
            id.value,
        )
    }

    @Test
    fun `conversation entry id rejects blank value`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationEntryId.from("   ")
        }
    }

    @Test
    fun `user entry preserves normalized content without runtime trace`() {
        val entry =
            ConversationTimelineEntry.user(
                id = ConversationEntryId.from("entry-user-001"),
                content = "  Hello Devil  ",
            )

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
    fun `user entry rejects blank content`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationTimelineEntry.user(
                id = ConversationEntryId.from("entry-user-002"),
                content = "   ",
            )
        }
    }

    @Test
    fun `runtime entry preserves truthful presentation and trace`() {
        val traceId = TraceId.from(
            "trace-conversation-ui-state-001",
        )
        val presentation =
            ConversationRuntimePresentation(
                traceId = traceId,
                status =
                    ConversationRuntimePresentationStatus.DEFERRED,
                message = "Deferred by the Devil runtime.",
            )

        val entry =
            ConversationTimelineEntry.runtime(
                id = ConversationEntryId.from("entry-runtime-001"),
                presentation = presentation,
            )

        assertEquals(
            ConversationEntryRole.RUNTIME,
            entry.role,
        )
        assertEquals(
            "Deferred by the Devil runtime.",
            entry.content,
        )
        assertEquals(
            traceId,
            entry.traceId,
        )
    }

    @Test
    fun `default UI state begins empty and idle`() {
        val state = ConversationUiState()

        assertEquals(
            emptyList(),
            state.entries,
        )
        assertEquals(
            "",
            state.draft,
        )
        assertEquals(
            false,
            state.isSubmitting,
        )
    }

    @Test
    fun `UI state preserves draft without treating it as timeline input`() {
        val state =
            ConversationUiState(
                draft = "Not submitted yet",
            )

        assertEquals(
            "Not submitted yet",
            state.draft,
        )
        assertEquals(
            emptyList(),
            state.entries,
        )
    }

    @Test
    fun `UI state rejects duplicate presentation entry identities`() {
        val id = ConversationEntryId.from(
            "entry-duplicate",
        )

        val first =
            ConversationTimelineEntry.user(
                id = id,
                content = "First",
            )

        val second =
            ConversationTimelineEntry.runtime(
                id = id,
                presentation =
                    ConversationRuntimePresentation(
                        traceId =
                            TraceId.from(
                                "trace-conversation-ui-state-002",
                            ),
                        status =
                            ConversationRuntimePresentationStatus.ACCEPTED,
                        message =
                            "Accepted for constitutional processing.",
                    ),
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationUiState(
                entries =
                    listOf(
                        first,
                        second,
                    ),
            )
        }
    }

    @Test
    fun `submitting state does not imply execution success`() {
        val state =
            ConversationUiState(
                isSubmitting = true,
            )

        assertEquals(
            true,
            state.isSubmitting,
        )
        assertEquals(
            emptyList(),
            state.entries,
        )
    }
}
