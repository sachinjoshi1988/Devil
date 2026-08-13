package com.devil.core.runtime.verification

import com.devil.core.runtime.observation.ObservationResult

/**
 * Neutral verification-embodiment port between constitutional Observation and
 * constitutional Verification.
 *
 * The core runtime may approach this port only with the genuine
 * ObservationResult produced by its configured Observation Authority.
 *
 * Implementations may obtain bounded verification evidence only through their
 * authorized embodiment-specific mechanisms.
 *
 * This port grants no authority of its own.
 *
 * ObservationStatus.OBSERVED is necessary for verification evidence but does
 * not itself establish VERIFIED.
 *
 * This contract contains no Android dependency and creates no alternate Brain,
 * Executive, Planner, Security Authority, Verification Authority, or runtime.
 */
fun interface VerificationEvidencePort {

    fun verify(
        observation: ObservationResult,
    ): VerificationEvidenceResult
}
