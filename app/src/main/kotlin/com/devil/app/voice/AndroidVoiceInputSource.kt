package com.devil.app.voice

/**
 * Android platform boundary for one bounded voice-input attempt.
 *
 * The caller must establish required Android microphone permission before
 * startListening is invoked.
 *
 * This source must not request or grant Android permission itself.
 *
 * It must not authenticate the speaker, establish identity or subject trust,
 * grant Devil authorization, create a session, invoke the Unified Devil
 * Runtime, execute capabilities, or fabricate conversational outcomes.
 */
interface AndroidVoiceInputSource {

    fun startListening(
        listener: AndroidVoiceInputListener,
    )

    fun cancel()

    fun release()
}
