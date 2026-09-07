package com.devil.app.voice

import com.devil.app.conversation.ConversationEntryId
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationRuntimePresentation
import com.devil.app.conversation.ConversationRuntimePresentationStatus
import com.devil.app.conversation.ConversationTimelineEntry
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Stage337M spoken Device Knowledge selection proof.
 *
 * The selection policy operates only on already-established conversation
 * presentation entries.
 *
 * SPEAKABLE_ENTRY_SELECTED != AUTHORIZATION.
 * SPOKEN_DEVICE_KNOWLEDGE != RUNTIME_STATUS.
 * SPOKEN_DEVICE_KNOWLEDGE != VERIFIED_OUTCOME.
 */
class Stage337MVoiceOutputSelectionTest {

    private val policy =
        VoiceOutputPresentationPolicy()

    @Test
    fun `newest knowledge presentation takes precedence over earlier runtime presentation`() {
        val runtime =
            runtimeEntry(
                traceValue =
                    "trace-stage337m-speech-runtime",
            )

        val knowledge =
            ConversationTimelineEntry.knowledge(
                id =
                    ConversationEntryId.from(
                        "stage337m-speech-knowledge",
                    ),
                traceId =
                    TraceId.from(
                        "trace-stage337m-speech-runtime",
                    ),
                content =
                    "Xiaomi Redmi Note 12.",
            )

        val selected =
            policy.newestSpeakableEntry(
                listOf(
                    runtime,
                    knowledge,
                ),
            )

        assertEquals(
            ConversationEntryRole.KNOWLEDGE,
            selected?.role,
        )
        assertEquals(
            "Xiaomi Redmi Note 12.",
            selected?.content,
        )
    }

    @Test
    fun `later non speakable presentation does not hide earlier runtime presentation`() {
        val runtime =
            runtimeEntry(
                traceValue =
                    "trace-stage337m-speech-runtime-fallback",
            )

        val user =
            ConversationTimelineEntry.user(
                id =
                    ConversationEntryId.from(
                        "stage337m-speech-user",
                    ),
                content =
                    "Owner text.",
            )

        val selected =
            policy.newestSpeakableEntry(
                listOf(
                    runtime,
                    user,
                ),
            )

        assertEquals(
            ConversationEntryRole.RUNTIME,
            selected?.role,
        )
    }

    @Test
    fun `timeline without runtime or knowledge has no Devil speech candidate`() {
        val user =
            ConversationTimelineEntry.user(
                id =
                    ConversationEntryId.from(
                        "stage337m-speech-user-only",
                    ),
                content =
                    "Owner text.",
            )

        val outcome =
            ConversationTimelineEntry.outcome(
                id =
                    ConversationEntryId.from(
                        "stage337m-speech-outcome",
                    ),
                traceId =
                    TraceId.from(
                        "trace-stage337m-speech-outcome",
                    ),
                content =
                    "Established outcome.",
            )

        assertNull(
            policy.newestSpeakableEntry(
                listOf(
                    user,
                    outcome,
                ),
            ),
        )
    }

    private fun runtimeEntry(
        traceValue: String,
    ): ConversationTimelineEntry {
        return ConversationTimelineEntry.runtime(
            id =
                ConversationEntryId.from(
                    "$traceValue-entry",
                ),
            presentation =
                ConversationRuntimePresentation(
                    traceId =
                        TraceId.from(
                            traceValue,
                        ),
                    status =
                        ConversationRuntimePresentationStatus.DEFERRED,
                    message =
                        "Deferred by the Devil runtime.",
                ),
        )
    }
}
