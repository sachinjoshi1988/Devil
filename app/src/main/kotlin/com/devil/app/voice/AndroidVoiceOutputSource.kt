package com.devil.app.voice

/**
 * Android platform boundary for bounded text-to-speech output.
 *
 * Implementations may speak only the exact normalized text supplied by the
 * caller.
 *
 * They must not generate, rewrite, summarize, infer, or reinterpret content.
 *
 * Voice output is presentation only.
 */
interface AndroidVoiceOutputSource {

    fun speak(
        text: String,
        listener: AndroidVoiceOutputListener,
    )

    fun stop()

    fun release()
}
