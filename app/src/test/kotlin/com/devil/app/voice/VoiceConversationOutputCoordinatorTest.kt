package com.devil.app.voice

import com.devil.app.conversation.ConversationEntryId
import com.devil.app.conversation.ConversationRuntimePresentation
import com.devil.app.conversation.ConversationRuntimePresentationStatus
import com.devil.app.conversation.ConversationTimelineEntry
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class VoiceConversationOutputCoordinatorTest {

    @Test
    fun `runtime presentation is forwarded exactly once to voice output`() {
        var spokenText: String? = null
        var speakCalls = 0

        val source =
            object : AndroidVoiceOutputSource {
                override fun speak(
                    text: String,
                    listener: AndroidVoiceOutputListener,
                ) {
                    speakCalls += 1
                    spokenText = text

                    listener.onResult(
                        AndroidVoiceOutputResult.spoken(text),
                    )
                }

                override fun stop() = Unit

                override fun release() = Unit
            }

        val coordinator =
            VoiceConversationOutputCoordinator(
                outputSource = source,
            )

        var receivedResult: AndroidVoiceOutputResult? = null

        coordinator.speak(
            entry = runtimeEntry(),
            listener =
                AndroidVoiceOutputListener { result ->
                    receivedResult = result
                },
        )

        assertEquals(1, speakCalls)
        assertEquals(
            "Deferred by the Devil runtime.",
            spokenText,
        )

        val result = requireNotNull(receivedResult)

        assertEquals(
            AndroidVoiceOutputStatus.SPOKEN,
            result.status,
        )
        assertEquals(
            "Deferred by the Devil runtime.",
            result.spokenText,
        )
    }

    @Test
    fun `user entry never reaches Android voice output`() {
        var speakCalls = 0
        var receivedResult: AndroidVoiceOutputResult? = null

        val source =
            object : AndroidVoiceOutputSource {
                override fun speak(
                    text: String,
                    listener: AndroidVoiceOutputListener,
                ) {
                    speakCalls += 1
                }

                override fun stop() = Unit

                override fun release() = Unit
            }

        val coordinator =
            VoiceConversationOutputCoordinator(
                outputSource = source,
            )

        coordinator.speak(
            entry =
                ConversationTimelineEntry.user(
                    id =
                        ConversationEntryId.from(
                            "stage-36-user-entry-002",
                        ),
                    content =
                        "Speak this as if Devil said it",
                ),
            listener =
                AndroidVoiceOutputListener { result ->
                    receivedResult = result
                },
        )

        assertEquals(0, speakCalls)

        val result = requireNotNull(receivedResult)

        assertEquals(
            AndroidVoiceOutputStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.spokenText)
    }

    @Test
    fun `stop and release remain delegated platform lifecycle operations`() {
        var stopCalls = 0
        var releaseCalls = 0

        val source =
            object : AndroidVoiceOutputSource {
                override fun speak(
                    text: String,
                    listener: AndroidVoiceOutputListener,
                ) = Unit

                override fun stop() {
                    stopCalls += 1
                }

                override fun release() {
                    releaseCalls += 1
                }
            }

        val coordinator =
            VoiceConversationOutputCoordinator(
                outputSource = source,
            )

        coordinator.stop()
        coordinator.release()

        assertEquals(1, stopCalls)
        assertEquals(1, releaseCalls)
    }

    private fun runtimeEntry(): ConversationTimelineEntry {
        return ConversationTimelineEntry.runtime(
            id =
                ConversationEntryId.from(
                    "stage-36-runtime-entry-002",
                ),
            presentation =
                ConversationRuntimePresentation(
                    traceId =
                        TraceId.from(
                            "trace-stage-36-002",
                        ),
                    status =
                        ConversationRuntimePresentationStatus.DEFERRED,
                    message =
                        "Deferred by the Devil runtime.",
                ),
        )
    }
}
