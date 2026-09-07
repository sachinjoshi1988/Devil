package com.devil.app.device

import com.devil.app.conversation.ConversationEntryId
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationTimelineEntry
import com.devil.app.conversation.ConversationUiState
import com.devil.app.voice.VoiceOutputPresentationPolicy
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Stage337MDeviceKnowledgeFoundationTest {

    @Test
    fun `query store is trace bound and one shot`() {
        val store =
            Stage337MDeviceKnowledgeQueryStore()

        val firstTrace =
            TraceId.from(
                "trace-stage337m-device-model",
            )

        val secondTrace =
            TraceId.from(
                "trace-stage337m-android-version",
            )

        store.record(
            traceId = firstTrace,
            queryType =
                AndroidDeviceKnowledgeQueryType.DEVICE_MODEL,
        )

        store.record(
            traceId = secondTrace,
            queryType =
                AndroidDeviceKnowledgeQueryType.ANDROID_VERSION,
        )

        assertEquals(
            AndroidDeviceKnowledgeQueryType.DEVICE_MODEL,
            store.consume(firstTrace),
        )

        assertNull(
            store.consume(firstTrace),
        )

        assertEquals(
            AndroidDeviceKnowledgeQueryType.ANDROID_VERSION,
            store.consume(secondTrace),
        )

        assertEquals(
            0,
            store.size(),
        )
    }

    @Test
    fun `knowledge entry preserves trace and remains distinct from runtime assistant and outcome`() {
        val traceId =
            TraceId.from(
                "trace-stage337m-knowledge-entry",
            )

        val entry =
            ConversationTimelineEntry.knowledge(
                id =
                    ConversationEntryId.from(
                        "entry-stage337m-knowledge",
                    ),
                traceId = traceId,
                content =
                    "Example Phone.",
            )

        assertEquals(
            ConversationEntryRole.KNOWLEDGE,
            entry.role,
        )

        assertEquals(
            traceId,
            entry.traceId,
        )

        assertEquals(
            "Example Phone.",
            entry.content,
        )
    }

    @Test
    fun `interaction coordinator appends knowledge only as presentation`() {
        val traceId =
            TraceId.from(
                "trace-stage337m-append-knowledge",
            )

        val state =
            ConversationInteractionCoordinator()
                .appendKnowledge(
                    state = ConversationUiState(),
                    knowledgeEntryId =
                        ConversationEntryId.from(
                            "entry-stage337m-appended",
                        ),
                    traceId = traceId,
                    message =
                        "Android 14 (SDK 34).",
                )

        assertEquals(
            1,
            state.entries.size,
        )

        assertEquals(
            ConversationEntryRole.KNOWLEDGE,
            state.entries.single().role,
        )

        assertEquals(
            traceId,
            state.entries.single().traceId,
        )
    }

    @Test
    fun `voice policy may speak runtime and knowledge but not user assistant or outcome`() {
        val traceId =
            TraceId.from(
                "trace-stage337m-voice-policy",
            )

        val policy =
            VoiceOutputPresentationPolicy()

        val knowledge =
            ConversationTimelineEntry.knowledge(
                id =
                    ConversationEntryId.from(
                        "entry-stage337m-spoken-knowledge",
                    ),
                traceId = traceId,
                content =
                    "Example Phone.",
            )

        assertEquals(
            "Example Phone.",
            policy.speakableText(knowledge),
        )

        val user =
            ConversationTimelineEntry.user(
                id =
                    ConversationEntryId.from(
                        "entry-stage337m-user",
                    ),
                content =
                    "What phone is this?",
            )

        assertNull(
            policy.speakableText(user),
        )

        val outcome =
            ConversationTimelineEntry.outcome(
                id =
                    ConversationEntryId.from(
                        "entry-stage337m-outcome",
                    ),
                traceId = traceId,
                content =
                    "Outcome text.",
            )

        assertNull(
            policy.speakableText(outcome),
        )
    }
}
