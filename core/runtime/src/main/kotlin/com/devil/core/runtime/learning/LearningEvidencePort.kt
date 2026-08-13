package com.devil.core.runtime.learning

import com.devil.core.runtime.worldmodel.WorldModelUpdateResult

/**
 * Neutral Learning-evidence port between constitutional World Model update and
 * constitutional Learning evaluation.
 *
 * The single Unified Devil Runtime may approach this port only with the genuine
 * WorldModelUpdateResult produced by the constitutional World Model Update
 * Authority.
 *
 * Implementations may obtain bounded Learning evidence only through authorized
 * evidence mechanisms.
 *
 * This port grants no authority of its own and does not create Learning,
 * propose Memory, invoke Memory Authority, commit Memory, persist Memory,
 * mutate world state, or report completion.
 *
 * WorldModelUpdateStatus.APPLICABLE is necessary for Learning evidence but does
 * not itself establish Learning evidence or prove that Learning should occur.
 *
 * This contract contains no Android dependency and creates no alternate Brain,
 * Executive, Planner, Security Authority, Learning Authority, Memory Authority,
 * memory domain, or runtime.
 *
 * WORLD_MODEL_UPDATE != LEARNING_EVIDENCE.
 * LEARNING_EVIDENCE != LEARNING.
 * LEARNING != MEMORY_PROPOSAL.
 */
fun interface LearningEvidencePort {

    fun establish(
        worldModelUpdate: WorldModelUpdateResult,
    ): LearningEvidenceResult
}
