package com.devil.core.runtime.worldmodel

import com.devil.core.runtime.outcome.OutcomeResult

/**
 * Neutral World Model update-evidence port between constitutional Outcome and
 * constitutional World Model update evaluation.
 *
 * The single Unified Devil Runtime may approach this port only with the genuine
 * OutcomeResult produced by the constitutional Outcome Authority.
 *
 * Implementations may obtain bounded World Model update evidence only through
 * authorized embodiment-specific mechanisms.
 *
 * This port grants no authority of its own and does not mutate World Model
 * state.
 *
 * OutcomeStatus.ESTABLISHED is necessary for World Model update evidence but
 * does not itself establish such evidence or prove that world state changed.
 *
 * This contract contains no Android dependency and creates no alternate Brain,
 * Executive, Planner, Security Authority, Outcome Authority, World Model
 * Authority, or runtime.
 *
 * OUTCOME != WORLD_MODEL_UPDATE_EVIDENCE.
 * WORLD_MODEL_UPDATE_EVIDENCE != WORLD_MODEL_UPDATE.
 * WORLD_MODEL_UPDATE != WORLD_STATE_CHANGED.
 */
fun interface WorldModelUpdateEvidencePort {

    fun establish(
        outcome: OutcomeResult,
    ): WorldModelUpdateEvidenceResult
}
