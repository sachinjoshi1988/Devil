package com.devil.app.voice

/**
 * Stage 37 constitutional coordinator for bounded wake and hands-free voice
 * interaction.
 *
 * The coordinator distinguishes:
 *
 * recognition,
 * wake,
 * authentication request,
 * authenticated session,
 * and ordinary conversation input.
 *
 * These concepts must never be collapsed.
 *
 * Current Stage 37 behavior:
 *
 * IDLE
 *     accepts only an approved wake phrase.
 *
 * AWAITING_AUTHENTICATION_PHRASE
 *     accepts only "Code Red" as an authentication-request signal.
 *
 * AUTHENTICATION_REQUESTED
 *     blocks ordinary hands-free conversation because no genuine Android
 *     authentication/session bridge exists yet.
 *
 * ACTIVE_SESSION
 *     permits recognized conversation text, but Stage 37 never creates this
 *     state itself.
 *
 * Wake != authentication.
 * Code Red != authentication.
 */
class HandsFreeConversationCoordinator(
    private val wakePhrasePolicy: WakePhrasePolicy =
        WakePhrasePolicy(),
    private val authenticationPhrasePolicy:
        VoiceAuthenticationPhrasePolicy =
        VoiceAuthenticationPhrasePolicy(),
) {

    fun handleRecognizedTranscript(
        state: HandsFreeConversationState,
        transcript: String,
    ): HandsFreeConversationResult {
        val normalizedTranscript =
            transcript.trim()

        require(normalizedTranscript.isNotEmpty()) {
            "Hands-free recognized transcript must not be blank."
        }

        return when (state) {
            HandsFreeConversationState.IDLE ->
                handleIdle(
                    transcript = normalizedTranscript,
                )

            HandsFreeConversationState.AWAITING_AUTHENTICATION_PHRASE ->
                handleAuthenticationPhrase(
                    transcript = normalizedTranscript,
                )

            HandsFreeConversationState.AUTHENTICATION_REQUESTED ->
                HandsFreeConversationResult(
                    state =
                        HandsFreeConversationState.AUTHENTICATION_REQUESTED,
                    status =
                        HandsFreeConversationResultStatus
                            .AUTHENTICATION_REQUESTED,
                    message =
                        "Authentication is required before hands-free conversation can continue.",
                    runtimeTranscript = null,
                )

            HandsFreeConversationState.ACTIVE_SESSION ->
                HandsFreeConversationResult(
                    state =
                        HandsFreeConversationState.ACTIVE_SESSION,
                    status =
                        HandsFreeConversationResultStatus
                            .CONVERSATION_INPUT_ALLOWED,
                    message = null,
                    runtimeTranscript = normalizedTranscript,
                )
        }
    }

    fun reset(): HandsFreeConversationResult {
        return HandsFreeConversationResult(
            state = HandsFreeConversationState.IDLE,
            status =
                HandsFreeConversationResultStatus.IGNORED,
            message = null,
            runtimeTranscript = null,
        )
    }

    private fun handleIdle(
        transcript: String,
    ): HandsFreeConversationResult {
        val wakeResult =
            wakePhrasePolicy.evaluate(
                transcript = transcript,
            )

        return when (wakeResult.status) {
            WakePhraseMatchStatus.NOT_MATCHED ->
                HandsFreeConversationResult(
                    state = HandsFreeConversationState.IDLE,
                    status =
                        HandsFreeConversationResultStatus.IGNORED,
                    message = null,
                    runtimeTranscript = null,
                )

            WakePhraseMatchStatus.MATCHED ->
                HandsFreeConversationResult(
                    state =
                        HandsFreeConversationState
                            .AWAITING_AUTHENTICATION_PHRASE,
                    status =
                        HandsFreeConversationResultStatus
                            .WAKE_ESTABLISHED,
                    message =
                        "I'm here, sir. Authentication please.",
                    runtimeTranscript = null,
                )
        }
    }

    private fun handleAuthenticationPhrase(
        transcript: String,
    ): HandsFreeConversationResult {
        val authenticationPhraseResult =
            authenticationPhrasePolicy.evaluate(
                transcript = transcript,
            )

        return when (authenticationPhraseResult.status) {
            VoiceAuthenticationPhraseStatus.NOT_RECOGNIZED ->
                HandsFreeConversationResult(
                    state =
                        HandsFreeConversationState
                            .AWAITING_AUTHENTICATION_PHRASE,
                    status =
                        HandsFreeConversationResultStatus
                            .AUTHENTICATION_PHRASE_REQUIRED,
                    message =
                        "Authentication please.",
                    runtimeTranscript = null,
                )

            VoiceAuthenticationPhraseStatus.CODE_RED_RECOGNIZED ->
                HandsFreeConversationResult(
                    state =
                        HandsFreeConversationState
                            .AUTHENTICATION_REQUESTED,
                    status =
                        HandsFreeConversationResultStatus
                            .AUTHENTICATION_REQUESTED,
                    message =
                        "Authentication requested.",
                    runtimeTranscript = null,
                )
        }
    }
}
