package com.devil.app.voice

import com.devil.app.conversation.ConversationTimelineEntry

/**
 * Stage 36 bounded coordinator from existing conversation presentation truth to
 * Android voice output.
 *
 * This coordinator speaks only text already present in one eligible RUNTIME
 * timeline entry.
 *
 * It does not invoke UnifiedDevilRuntime.
 * It does not create assistant answers.
 * It does not reinterpret DEFERRED, ACCEPTED, or REJECTED.
 * It does not establish success.
 *
 * Runtime presentation != generated assistant answer.
 * Spoken presentation != verified outcome.
 */
class VoiceConversationOutputCoordinator(
    private val outputSource: AndroidVoiceOutputSource,
    private val presentationPolicy: VoiceOutputPresentationPolicy =
        VoiceOutputPresentationPolicy(),
) {

    fun speak(
        entry: ConversationTimelineEntry,
        listener: AndroidVoiceOutputListener,
    ) {
        val text =
            presentationPolicy.speakableText(entry)
                ?: run {
                    listener.onResult(
                        AndroidVoiceOutputResult.unavailable(),
                    )
                    return
                }

        outputSource.speak(
            text = text,
            listener = listener,
        )
    }

    fun stop() {
        outputSource.stop()
    }

    fun release() {
        outputSource.release()
    }
}
