package com.devil.app.voice

/**
 * Receives bounded lifecycle signals from one Android voice-input attempt.
 *
 * READY means the Android recognition mechanism is ready to receive speech.
 *
 * RESULT contains the terminal bounded recognition result.
 *
 * These callbacks do not create conversation input, invoke the Unified Devil
 * Runtime, authenticate a speaker, grant authorization, or report task success.
 */
interface AndroidVoiceInputListener {

    fun onReady()

    fun onResult(
        result: AndroidVoiceInputResult,
    )
}
