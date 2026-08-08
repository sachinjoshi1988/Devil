package com.devil.app.conversation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ConversationMetadataUnavailableCompletionTest {

    private val coordinator =
        ConversationInteractionCoordinator()

    @Test
    fun `metadata unavailable returns submitting UI to idle`() {
        val started =
            coordinator.beginSubmission(
                state =
                    ConversationUiState(
                        draft = "Hello Devil",
                    ),
                userEntryId =
                    ConversationEntryId.from(
                        "entry-metadata-unavailable-001",
                    ),
            )

        val completed =
            coordinator.completeMetadataUnavailable(
                state = started.state,
            )

        assertEquals(
            false,
            completed.isSubmitting,
        )
    }

    @Test
    fun `metadata unavailable preserves user entry without creating runtime entry`() {
        val started =
            coordinator.beginSubmission(
                state =
                    ConversationUiState(
                        draft = "Hello Devil",
                    ),
                userEntryId =
                    ConversationEntryId.from(
                        "entry-metadata-unavailable-002",
                    ),
            )

        val completed =
            coordinator.completeMetadataUnavailable(
                state = started.state,
            )

        assertEquals(
            1,
            completed.entries.size,
        )

        val entry = completed.entries.single()

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
    fun `metadata unavailable exposes truthful UI local notice`() {
        val started =
            coordinator.beginSubmission(
                state =
                    ConversationUiState(
                        draft = "Hello Devil",
                    ),
                userEntryId =
                    ConversationEntryId.from(
                        "entry-metadata-unavailable-003",
                    ),
            )

        val completed =
            coordinator.completeMetadataUnavailable(
                state = started.state,
            )

        val notice = requireNotNull(
            completed.submissionNotice,
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
    fun `metadata unavailable notice has no fabricated runtime trace`() {
        val started =
            coordinator.beginSubmission(
                state =
                    ConversationUiState(
                        draft = "Hello Devil",
                    ),
                userEntryId =
                    ConversationEntryId.from(
                        "entry-metadata-unavailable-004",
                    ),
            )

        val completed =
            coordinator.completeMetadataUnavailable(
                state = started.state,
            )

        assertEquals(
            1,
            completed.entries.size,
        )
        assertNull(
            completed.entries.single().traceId,
        )
    }

    @Test
    fun `metadata unavailable completion rejects idle state`() {
        assertFailsWith<IllegalArgumentException> {
            coordinator.completeMetadataUnavailable(
                state = ConversationUiState(),
            )
        }
    }

    @Test
    fun `new submission clears previous local submission notice`() {
        val previousState =
            ConversationUiState(
                draft = "Try again",
                submissionNotice =
                    ConversationSubmissionNotice.metadataUnavailable(),
            )

        val started =
            coordinator.beginSubmission(
                state = previousState,
                userEntryId =
                    ConversationEntryId.from(
                        "entry-metadata-unavailable-005",
                    ),
            )

        assertEquals(
            ConversationSubmissionStartStatus.STARTED,
            started.status,
        )
        assertNull(
            started.state.submissionNotice,
        )
    }

    @Test
    fun `draft edit clears previous local submission notice`() {
        val state =
            ConversationUiState(
                submissionNotice =
                    ConversationSubmissionNotice.metadataUnavailable(),
            )

        val updated =
            coordinator.updateDraft(
                state = state,
                draft = "New input",
            )

        assertEquals(
            "New input",
            updated.draft,
        )
        assertNull(
            updated.submissionNotice,
        )
    }
}
