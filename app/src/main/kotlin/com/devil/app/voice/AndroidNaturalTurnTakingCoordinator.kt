package com.devil.app.voice

/**
 * Stage 200 bounded Natural Turn-Taking coordinator.
 *
 * It evaluates only established voice activity state and whether listening
 * should resume after output.
 *
 * It does not:
 *
 * - start Android speech recognition;
 * - invoke TextToSpeech;
 * - permit simultaneous listening and speaking;
 * - implement barge-in;
 * - detect voice activity or noise;
 * - authenticate or authorize;
 * - implement Stage 201 Voice Activity & Noise Handling.
 *
 * SPEAKING != LISTENING.
 * NATURAL_TURN_TAKING != BARGE_IN.
 * TURN_STATE != AUTHORIZATION.
 */
class AndroidNaturalTurnTakingCoordinator {

    fun evaluate(
        isListening: Boolean,
        isSpeaking: Boolean,
        resumeListeningAfterOutput: Boolean,
    ): AndroidNaturalTurnTakingResult {
        require(!(isListening && isSpeaking)) {
            "Stage 200 must not accept simultaneous listening and speaking."
        }

        if (isSpeaking) {
            return AndroidNaturalTurnTakingResult.create(
                status =
                    if (resumeListeningAfterOutput) {
                        AndroidNaturalTurnTakingStatus.WAITING_TO_LISTEN
                    } else {
                        AndroidNaturalTurnTakingStatus.SPEAKING
                    },
                shouldListen = false,
            )
        }

        if (isListening) {
            return AndroidNaturalTurnTakingResult.create(
                status = AndroidNaturalTurnTakingStatus.DEFERRED,
                shouldListen = false,
            )
        }

        if (resumeListeningAfterOutput) {
            return AndroidNaturalTurnTakingResult.create(
                status = AndroidNaturalTurnTakingStatus.LISTEN,
                shouldListen = true,
            )
        }

        return AndroidNaturalTurnTakingResult.create(
            status = AndroidNaturalTurnTakingStatus.DEFERRED,
            shouldListen = false,
        )
    }
}
