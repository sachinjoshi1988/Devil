package com.devil.core.model.research

/**
 * Stage 110 bounded corroboration status for one existing Research source-
 * assessment set.
 *
 * CORROBORATED means an approved upstream mechanism explicitly established
 * bounded corroboration among the supplied research material.
 *
 * NOT_CORROBORATED means the supplied material does not establish bounded
 * corroboration.
 *
 * INDETERMINATE means no justified corroboration conclusion is available.
 *
 * Corroboration does not establish factual truth, constitutional Verification,
 * consensus, confidence, World Model state, Learning, Memory, or verified
 * Outcome.
 *
 * CORROBORATED != TRUE.
 * CORROBORATED != VERIFIED.
 * CORROBORATED != CONSENSUS.
 */
enum class ResearchCorroborationStatus {
    CORROBORATED,
    NOT_CORROBORATED,
    INDETERMINATE,
}
