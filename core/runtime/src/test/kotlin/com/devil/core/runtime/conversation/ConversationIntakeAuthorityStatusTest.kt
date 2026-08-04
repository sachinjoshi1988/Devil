package com.devil.core.runtime.conversation

import kotlin.test.Test
import kotlin.test.assertEquals

class ConversationIntakeAuthorityStatusTest {

    @Test
    fun `conversation intake authority statuses remain complete and ordered`() {
        assertEquals(
            listOf(
                ConversationIntakeAuthorityStatus.PRODUCED,
                ConversationIntakeAuthorityStatus.DEFERRED,
                ConversationIntakeAuthorityStatus.FAILED,
            ),
            ConversationIntakeAuthorityStatus.entries,
        )
    }
}
