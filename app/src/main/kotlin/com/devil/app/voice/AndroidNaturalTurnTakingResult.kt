package com.devil.app.voice

/**
 * Stage 200 bounded Natural Turn-Taking result.
 *
 * LISTEN requires shouldListen = true.
 *
 * SPEAKING, WAITING_TO_LISTEN, and DEFERRED require
 * shouldListen = false.
 *
 * SPEAKING != LISTENING.
 * NATURAL_TURN_TAKING != BARGE_IN.
 * TURN_STATE != AUTHORIZATION.
 */
@ConsistentCopyVisibility
data class AndroidNaturalTurnTakingResult private constructor(
    val status: AndroidNaturalTurnTakingStatus,
    val shouldListen: Boolean,
) {
    companion object {
        fun create(
            status: AndroidNaturalTurnTakingStatus,
            shouldListen: Boolean,
        ): AndroidNaturalTurnTakingResult {
            when (status) {
                AndroidNaturalTurnTakingStatus.LISTEN ->
                    require(shouldListen) {
                        "Stage 200 LISTEN requires listening to be requested."
                    }

                AndroidNaturalTurnTakingStatus.SPEAKING,
                AndroidNaturalTurnTakingStatus.WAITING_TO_LISTEN,
                AndroidNaturalTurnTakingStatus.DEFERRED,
                ->
                    require(!shouldListen) {
                        "Stage 200 non-listening states must not request listening."
                    }
            }

            return AndroidNaturalTurnTakingResult(
                status = status,
                shouldListen = shouldListen,
            )
        }
    }
}
