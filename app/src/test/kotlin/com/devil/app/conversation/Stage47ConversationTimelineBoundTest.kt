package com.devil.app.conversation

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class Stage47ConversationTimelineBoundTest {

    private val coordinator =
        ConversationInteractionCoordinator()

    @Test
    fun `completed presentation timeline remains bounded`() {
        var state =
            ConversationUiState()

        repeat(60) { index ->
            val started =
                coordinator.beginSubmission(
                    state =
                        state.copy(
                            draft = "User message $index",
                        ),
                    userEntryId =
                        ConversationEntryId.from(
                            "stage47-user-$index",
                        ),
                )

            state =
                coordinator.completeSubmission(
                    state = started.state,
                    runtimeEntryId =
                        ConversationEntryId.from(
                            "stage47-runtime-$index",
                        ),
                    presentation =
                        ConversationRuntimePresentation(
                            traceId =
                                TraceId.from(
                                    "stage47-trace-$index",
                                ),
                            status =
                                ConversationRuntimePresentationStatus.ACCEPTED,
                            message =
                                "Runtime message $index",
                        ),
                )
        }

        assertEquals(
            ConversationInteractionCoordinator
                .MAX_PRESENTATION_TIMELINE_ENTRIES,
            state.entries.size,
        )

        assertEquals(
            "User message 10",
            state.entries.first().content,
        )

        assertEquals(
            "Runtime message 59",
            state.entries.last().content,
        )

        assertEquals(
            ConversationEntryRole.USER,
            state.entries.first().role,
        )

        assertEquals(
            ConversationEntryRole.RUNTIME,
            state.entries.last().role,
        )
    }

    @Test
    fun `active submission is not trimmed before terminal completion`() {
        val existingEntries =
            buildList {
                repeat(
                    ConversationInteractionCoordinator
                        .MAX_PRESENTATION_TIMELINE_ENTRIES,
                ) { index ->
                    add(
                        ConversationTimelineEntry.user(
                            id =
                                ConversationEntryId.from(
                                    "stage47-existing-$index",
                                ),
                            content =
                                "Existing $index",
                        ),
                    )
                }
            }

        val started =
            coordinator.beginSubmission(
                state =
                    ConversationUiState(
                        entries = existingEntries,
                        draft = "Active user message",
                    ),
                userEntryId =
                    ConversationEntryId.from(
                        "stage47-active-user",
                    ),
            )

        assertEquals(
            ConversationInteractionCoordinator
                .MAX_PRESENTATION_TIMELINE_ENTRIES + 1,
            started.state.entries.size,
        )

        assertTrue(started.state.isSubmitting)

        assertEquals(
            "Active user message",
            started.state.entries.last().content,
        )
    }

    @Test
    fun `runtime completion bounds only terminal presentation state`() {
        val existingEntries =
            buildList {
                repeat(
                    ConversationInteractionCoordinator
                        .MAX_PRESENTATION_TIMELINE_ENTRIES,
                ) { index ->
                    add(
                        ConversationTimelineEntry.user(
                            id =
                                ConversationEntryId.from(
                                    "stage47-terminal-existing-$index",
                                ),
                            content =
                                "Existing $index",
                        ),
                    )
                }
            }

        val started =
            coordinator.beginSubmission(
                state =
                    ConversationUiState(
                        entries = existingEntries,
                        draft = "Newest user",
                    ),
                userEntryId =
                    ConversationEntryId.from(
                        "stage47-terminal-user",
                    ),
            )

        val completed =
            coordinator.completeSubmission(
                state = started.state,
                runtimeEntryId =
                    ConversationEntryId.from(
                        "stage47-terminal-runtime",
                    ),
                presentation =
                    ConversationRuntimePresentation(
                        traceId =
                            TraceId.from(
                                "stage47-terminal-trace",
                            ),
                        status =
                            ConversationRuntimePresentationStatus.ACCEPTED,
                        message = "Newest runtime",
                    ),
            )

        assertEquals(
            ConversationInteractionCoordinator
                .MAX_PRESENTATION_TIMELINE_ENTRIES,
            completed.entries.size,
        )

        assertEquals(
            "Newest user",
            completed.entries[
                completed.entries.lastIndex - 1
            ].content,
        )

        assertEquals(
            "Newest runtime",
            completed.entries.last().content,
        )

        assertEquals(
            ConversationEntryRole.USER,
            completed.entries[
                completed.entries.lastIndex - 1
            ].role,
        )

        assertEquals(
            ConversationEntryRole.RUNTIME,
            completed.entries.last().role,
        )
    }

    @Test
    fun `metadata unavailable completion also restores presentation bound without fabricating runtime entry`() {
        val existingEntries =
            buildList {
                repeat(
                    ConversationInteractionCoordinator
                        .MAX_PRESENTATION_TIMELINE_ENTRIES,
                ) { index ->
                    add(
                        ConversationTimelineEntry.user(
                            id =
                                ConversationEntryId.from(
                                    "stage47-metadata-existing-$index",
                                ),
                            content =
                                "Existing $index",
                        ),
                    )
                }
            }

        val started =
            coordinator.beginSubmission(
                state =
                    ConversationUiState(
                        entries = existingEntries,
                        draft = "Metadata unavailable user",
                    ),
                userEntryId =
                    ConversationEntryId.from(
                        "stage47-metadata-user",
                    ),
            )

        val completed =
            coordinator.completeMetadataUnavailable(
                state = started.state,
            )

        assertEquals(
            ConversationInteractionCoordinator
                .MAX_PRESENTATION_TIMELINE_ENTRIES,
            completed.entries.size,
        )

        assertEquals(
            "Metadata unavailable user",
            completed.entries.last().content,
        )

        assertEquals(
            ConversationEntryRole.USER,
            completed.entries.last().role,
        )

        assertNull(
            completed.entries.last().traceId,
        )

        assertEquals(
            false,
            completed.isSubmitting,
        )

        assertTrue(
            completed.submissionNotice != null,
        )
    }
}
