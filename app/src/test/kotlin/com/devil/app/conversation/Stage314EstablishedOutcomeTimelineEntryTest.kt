package com.devil.app.conversation

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage314EstablishedOutcomeTimelineEntryTest {

    @Test
    fun `established outcome entry preserves distinct role and trace`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-outcome-timeline",
            )

        val entry =
            ConversationTimelineEntry.outcome(
                id =
                    ConversationEntryId.from(
                        "entry-stage-314-outcome",
                    ),
                traceId = traceId,
                content = "Android action verified.",
            )

        assertEquals(
            ConversationEntryRole.OUTCOME,
            entry.role,
        )
        assertEquals(
            traceId,
            entry.traceId,
        )
        assertEquals(
            "Android action verified.",
            entry.content,
        )
    }
}
