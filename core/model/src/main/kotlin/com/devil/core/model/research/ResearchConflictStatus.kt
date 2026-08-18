package com.devil.core.model.research

/**
 * Stage 110 bounded conflict status for one existing Research source-assessment
 * set.
 *
 * CONSISTENT means an approved upstream mechanism explicitly established that
 * the bounded assessed research material contains no represented conflict for
 * the question being evaluated.
 *
 * CONFLICTING means an approved upstream mechanism explicitly established that
 * relevant supplied research material remains in conflict.
 *
 * INDETERMINATE means no justified conflict conclusion is available.
 *
 * This status does not resolve disagreement, select a winning source, rank
 * sources, establish factual truth, perform Verification, create consensus, or
 * synthesize a conclusion.
 *
 * CONFLICT_STATUS != CONFLICT_RESOLUTION.
 * CONSISTENT != TRUE.
 * CONFLICTING != FALSE.
 */
enum class ResearchConflictStatus {
    CONSISTENT,
    CONFLICTING,
    INDETERMINATE,
}
