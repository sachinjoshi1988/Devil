package com.devil.app.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferenceResult
import com.devil.core.runtime.modelprovider.conversation.GeneratedAssistantResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage313GeneratedAssistantTimelineEntryTest {

    @Test
    fun `generated assistant response becomes distinct trace preserving assistant entry`() {
        val traceId =
            TraceId.from(
                "trace-stage-313-generated-assistant-entry",
            )

        val inference =
            ConversationalModelInferenceResult.available(
                traceId = traceId,
                generatedOutput =
                    "Bounded generated assistant response.",
            )

        val response =
            GeneratedAssistantResponse.from(
                inference = inference,
            )

        val entry =
            ConversationTimelineEntry.assistant(
                id =
                    ConversationEntryId.from(
                        "stage-313-assistant-entry",
                    ),
                response = response,
            )

        assertEquals(
            ConversationEntryRole.ASSISTANT,
            entry.role,
        )

        assertEquals(
            "Bounded generated assistant response.",
            entry.content,
        )

        assertEquals(
            traceId,
            entry.traceId,
        )
    }

    @Test
    fun `existing user role remains distinct from generated assistant role`() {
        val entry =
            ConversationTimelineEntry.user(
                id =
                    ConversationEntryId.from(
                        "stage-313-user-entry",
                    ),
                content =
                    "User supplied conversation.",
            )

        assertEquals(
            ConversationEntryRole.USER,
            entry.role,
        )

        assertEquals(
            null,
            entry.traceId,
        )
    }
}
