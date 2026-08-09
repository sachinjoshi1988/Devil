package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertNotEquals

class AndroidVoiceInteractionModeTest {

    @Test
    fun `manual and hands free voice interaction modes remain distinct`() {
        assertNotEquals(
            AndroidVoiceInteractionMode.MANUAL,
            AndroidVoiceInteractionMode.HANDS_FREE,
        )
    }
}
