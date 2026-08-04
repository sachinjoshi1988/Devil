package com.devil.core.model.conversation

import kotlin.test.Test
import kotlin.test.assertEquals

class ConversationIntakeStateTest {

    @Test
    fun `conversation intake states remain complete and ordered`() {
        assertEquals(
            listOf(
                ConversationIntakeState.ACCEPTED,
                ConversationIntakeState.DEFERRED,
                ConversationIntakeState.REJECTED,
            ),
            ConversationIntakeState.entries,
        )
    }
}
