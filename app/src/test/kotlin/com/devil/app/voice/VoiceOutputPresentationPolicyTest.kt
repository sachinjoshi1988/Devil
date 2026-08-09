package com.devil.app.voice

import com.devil.app.conversation.ConversationEntryId
import com.devil.app.conversation.ConversationRuntimePresentation
import com.devil.app.conversation.ConversationRuntimePresentationStatus
import com.devil.app.conversation.ConversationTimelineEntry
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class VoiceOutputPresentationPolicyTest {

    private val policy =
        VoiceOutputPresentationPolicy()

    @Test
    fun `runtime presentation is eligible for speech without reinterpretation`() {
        val entry =
            ConversationTimelineEntry.runtime(
                id =
                    ConversationEntryId.from(
                        "stage-36-runtime-entry-001",
                    ),
                presentation =
                    ConversationRuntimePresentation(
                        traceId =
                            TraceId.from(
                                "trace-stage-36-001",
                            ),
                        status =
                            ConversationRuntimePresentationStatus.DEFERRED,
                        message =
                            "Deferred by the Devil runtime.",
                    ),
            )

        assertEquals(
            "Deferred by the Devil runtime.",
            policy.speakableText(entry),
        )
    }

    @Test
    fun `user text is not eligible to become Devil speech`() {
        val entry =
            ConversationTimelineEntry.user(
                id =
                    ConversationEntryId.from(
                        "stage-36-user-entry-001",
                    ),
                content =
                    "Hello Devil",
            )

        assertNull(
            policy.speakableText(entry),
        )
    }
}
