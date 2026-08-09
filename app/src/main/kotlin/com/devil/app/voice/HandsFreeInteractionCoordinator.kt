package com.devil.app.voice

/**
 * Stage 37 orchestration boundary between recognized speech and Android
 * hands-free lifecycle behavior.
 *
 * This coordinator does not own SpeechRecognizer or TextToSpeech.
 *
 * It converts the constitutional HandsFreeConversationCoordinator result into
 * one explicit next action that the Android presentation layer may perform.
 *
 * The allowed control sequence is:
 *
 * IDLE
 * -> approved wake phrase
 * -> AWAITING_AUTHENTICATION_PHRASE
 * -> Code Red
 * -> AUTHENTICATION_REQUESTED
 * -> genuine authentication boundary
 * -> future ACTIVE_SESSION
 * -> ordinary conversation submission.
 *
 * This coordinator cannot create ACTIVE_SESSION.
 *
 * Wake != Authentication.
 * Code Red != Authentication.
 * Android microphone permission != Devil authorization.
 */
class HandsFreeInteractionCoordinator(
    private val conversationCoordinator:
        HandsFreeConversationCoordinator =
        HandsFreeConversationCoordinator(),
) {

    fun handleRecognizedTranscript(
        state: HandsFreeConversationState,
        transcript: String,
    ): HandsFreeInteractionResult {
        val result =
            conversationCoordinator.handleRecognizedTranscript(
                state = state,
                transcript = transcript,
            )

        return when (result.status) {
            HandsFreeConversationResultStatus.IGNORED ->
                HandsFreeInteractionResult(
                    state = result.state,
                    action = HandsFreeInteractionAction.NONE,
                    spokenMessage = null,
                    conversationTranscript = null,
                )

            HandsFreeConversationResultStatus.WAKE_ESTABLISHED ->
                HandsFreeInteractionResult(
                    state = result.state,
                    action =
                        HandsFreeInteractionAction.SPEAK_AND_LISTEN,
                    spokenMessage =
                        requireNotNull(result.message),
                    conversationTranscript = null,
                )

            HandsFreeConversationResultStatus
                .AUTHENTICATION_PHRASE_REQUIRED ->
                HandsFreeInteractionResult(
                    state = result.state,
                    action =
                        HandsFreeInteractionAction.SPEAK_AND_LISTEN,
                    spokenMessage =
                        requireNotNull(result.message),
                    conversationTranscript = null,
                )

            HandsFreeConversationResultStatus.AUTHENTICATION_REQUESTED ->
                HandsFreeInteractionResult(
                    state = result.state,
                    action =
                        HandsFreeInteractionAction.REQUEST_AUTHENTICATION,
                    spokenMessage =
                        requireNotNull(result.message),
                    conversationTranscript = null,
                )

            HandsFreeConversationResultStatus
                .CONVERSATION_INPUT_ALLOWED ->
                HandsFreeInteractionResult(
                    state = result.state,
                    action =
                        HandsFreeInteractionAction.SUBMIT_CONVERSATION,
                    spokenMessage = null,
                    conversationTranscript =
                        requireNotNull(
                            result.runtimeTranscript,
                        ),
                )
        }
    }

    fun reset(): HandsFreeInteractionResult {
        val result =
            conversationCoordinator.reset()

        return HandsFreeInteractionResult(
            state = result.state,
            action = HandsFreeInteractionAction.NONE,
            spokenMessage = null,
            conversationTranscript = null,
        )
    }
}
