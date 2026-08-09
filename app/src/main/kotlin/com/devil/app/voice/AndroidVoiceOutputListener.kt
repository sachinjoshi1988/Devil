package com.devil.app.voice

/**
 * Receives terminal bounded Android voice-output results.
 *
 * This listener reports platform speech-output state only.
 *
 * It does not create assistant content, invoke the Unified Devil Runtime,
 * establish authorization, or report task completion.
 */
fun interface AndroidVoiceOutputListener {

    fun onResult(
        result: AndroidVoiceOutputResult,
    )
}
