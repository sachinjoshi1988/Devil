package com.devil.core.model.verification

import com.devil.core.model.observation.ObservationRequest

/**
 * Represents one structured request for bounded constitutional verification.
 *
 * The request preserves one existing ObservationRequest after genuine
 * observation evidence has been established by the Observation Authority. It
 * does not reinterpret execution, observation, planning, or capability
 * selection.
 *
 * This request does not establish verification evidence, determine whether an
 * intended outcome was achieved, update world state, report success or failure,
 * change task or plan state, or produce a final outcome.
 */
@ConsistentCopyVisibility
data class VerificationRequest private constructor(
    val observation: ObservationRequest,
) {
    companion object {
        fun create(
            observation: ObservationRequest,
        ): VerificationRequest {
            return VerificationRequest(
                observation = observation,
            )
        }
    }
}
