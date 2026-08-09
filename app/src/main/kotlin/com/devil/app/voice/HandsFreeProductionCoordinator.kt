package com.devil.app.voice

/**
 * Stage 37 production coordinator for the bounded:
 *
 * wake
 * -> authentication-request phrase
 * -> genuine authentication handoff
 * -> future authenticated hands-free conversation
 *
 * This coordinator joins the already-established Stage 37 interaction state
 * machine with the bounded authentication-handoff contract.
 *
 * It does not perform SpeechRecognizer or TextToSpeech work.
 *
 * It does not authenticate a speaker.
 *
 * It does not create ACTIVE_SESSION.
 *
 * It does not create a session, enter Owner Mode, grant Devil authorization,
 * invoke UnifiedDevilRuntime directly, execute capabilities, or establish
 * successful task completion.
 *
 * Wake != authentication.
 *
 * Code Red != authentication.
 *
 * Authentication handoff != authenticated session.
 */
class HandsFreeProductionCoordinator(
    private val interactionCoordinator:
        HandsFreeInteractionCoordinator =
        HandsFreeInteractionCoordinator(),
    private val authenticationCoordinator:
        HandsFreeAuthenticationCoordinator =
        HandsFreeAuthenticationCoordinator(),
) {

    fun handleRecognizedTranscript(
        state: HandsFreeConversationState,
        transcript: String,
    ): HandsFreeProductionResult {
        val interaction =
            interactionCoordinator.handleRecognizedTranscript(
                state = state,
                transcript = transcript,
            )

        return when (interaction.action) {
            HandsFreeInteractionAction.NONE ->
                HandsFreeProductionResult(
                    state = interaction.state,
                    action =
                        HandsFreeProductionAction.LISTEN,
                    message =
                        interaction.spokenMessage,
                    runtimeTranscript = null,
                    authenticationResult = null,
                )

            HandsFreeInteractionAction.SPEAK_AND_LISTEN ->
                HandsFreeProductionResult(
                    state = interaction.state,
                    action =
                        HandsFreeProductionAction.SPEAK_AND_LISTEN,
                    message =
                        interaction.spokenMessage,
                    runtimeTranscript = null,
                    authenticationResult = null,
                )

            HandsFreeInteractionAction.REQUEST_AUTHENTICATION -> {
                val authenticationResult =
                    authenticationCoordinator.requestAuthentication(
                        state = interaction.state,
                    )

                HandsFreeProductionResult(
                    state = interaction.state,
                    action =
                        HandsFreeProductionAction.AUTHENTICATION_HANDOFF,
                    message =
                        when (authenticationResult.status) {
                            HandsFreeAuthenticationHandoffStatus.REQUIRED ->
                                authenticationResult.message

                            HandsFreeAuthenticationHandoffStatus.UNAVAILABLE ->
                                authenticationResult.message
                        },
                    runtimeTranscript = null,
                    authenticationResult =
                        authenticationResult,
                )
            }

            HandsFreeInteractionAction.SUBMIT_CONVERSATION ->
                HandsFreeProductionResult(
                    state = interaction.state,
                    action =
                        HandsFreeProductionAction.SUBMIT_CONVERSATION,
                    message =
                        interaction.spokenMessage,
                    runtimeTranscript =
                        requireNotNull(
                            interaction.conversationTranscript,
                        ),
                    authenticationResult = null,
                )
        }
    }

    fun reset(): HandsFreeProductionResult {
        val interaction =
            interactionCoordinator.reset()

        return HandsFreeProductionResult(
            state = interaction.state,
            action = HandsFreeProductionAction.NONE,
            message = null,
            runtimeTranscript = null,
            authenticationResult = null,
        )
    }
}
