package com.devil.core.runtime.verification

import com.devil.core.runtime.observation.ObservationResult

/**
 * Supplies one structured constitutional verification request when genuine
 * observation evidence has been established.
 *
 * This provider does not create verification evidence, infer whether an intended
 * outcome was achieved, update world state, report success, or produce a final
 * outcome.
 */
interface VerificationRequestProvider {

    fun provide(
        observation: ObservationResult,
    ): VerificationRequestResult
}
