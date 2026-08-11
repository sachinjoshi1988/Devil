package com.devil.app.ui.launch

/**
 * Stage 51 bounded launch-animation timing.
 *
 * Total cold-launch presentation target is approximately five seconds so the
 * Devil identity can be perceived clearly on the Owner Alpha device.
 */
object DevilLaunchTiming {

    const val VOID_DURATION_MILLIS: Long =
        500L

    const val CORE_IGNITION_DURATION_MILLIS: Long =
        1_000L

    const val CORE_PULSE_DURATION_MILLIS: Long =
        1_300L

    const val IDENTITY_REVEAL_DURATION_MILLIS: Long =
        1_200L

    const val WORDMARK_DURATION_MILLIS: Long =
        1_000L

    const val TOTAL_DURATION_MILLIS: Long =
        VOID_DURATION_MILLIS +
            CORE_IGNITION_DURATION_MILLIS +
            CORE_PULSE_DURATION_MILLIS +
            IDENTITY_REVEAL_DURATION_MILLIS +
            WORDMARK_DURATION_MILLIS
}
