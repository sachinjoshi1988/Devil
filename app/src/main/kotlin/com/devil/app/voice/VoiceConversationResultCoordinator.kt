package com.devil.app.voice

import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationSubmissionFlowCoordinator
import com.devil.app.conversation.ConversationUiState

/**
 * Stage 35 bridge from bounded Android speech-recognition results into the
 * existing conversation submission architecture.
 *
 * Only a genuine RECOGNIZED transcript may be introduced as conversation text.
 *
 * The recognized text still enters the same ConversationSubmissionFlowCoordinator
 * and therefore the same Unified Devil Runtime. No voice-specific brain,
 * understanding engine, planner, memory authority, security authority, or runtime
 * is created.
 *
 * NO_MATCH, CANCELLED, and FAILED results do not enter the runtime.
 *
 * Recognition != speaker authentication.
 * Recognition != understanding.
 * Recognition != authorization.
 * Recognition != execution.
 * Recognition != success.
 */
class VoiceConversationResultCoordinator(
    private val interactionCoordinator: ConversationInteractionCoordinator,
    private val submissionFlowCoordinator: ConversationSubmissionFlowCoordinator,
) {

    fun handle(
        state: ConversationUiState,
        result: AndroidVoiceInputResult,
    ): VoiceConversationHandlingResult {
        return when (result.status) {
            AndroidVoiceInputStatus.RECOGNIZED -> {
                val transcript =
                    requireNotNull(result.transcript)

                val preparedState =
                    interactionCoordinator.updateDraft(
                        state = state,
                        draft = transcript,
                    )

                val submittedState =
                    submissionFlowCoordinator.submit(
                        state = preparedState,
                    )

                VoiceConversationHandlingResult(
                    state = submittedState,
                    message = null,
                )
            }

            AndroidVoiceInputStatus.NO_MATCH ->
                VoiceConversationHandlingResult(
                    state = state,
                    message = "No speech was recognized.",
                )

            AndroidVoiceInputStatus.CANCELLED ->
                VoiceConversationHandlingResult(
                    state = state,
                    message = "Voice input cancelled.",
                )

            AndroidVoiceInputStatus.FAILED ->
                VoiceConversationHandlingResult(
                    state = state,
                    message = "Voice input failed.",
                )
        }
    }
}
