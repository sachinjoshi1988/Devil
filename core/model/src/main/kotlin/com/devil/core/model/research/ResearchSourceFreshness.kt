package com.devil.core.model.research

/**
 * Stage 109 bounded freshness assessment for one research source.
 *
 * CURRENT means approved assessment input explicitly establishes bounded source
 * freshness relative to the assessment context.
 *
 * STALE means approved assessment input explicitly establishes staleness.
 *
 * UNKNOWN means no justified freshness conclusion is available.
 *
 * SOURCE_FRESHNESS != FACTUAL_TRUTH.
 * SOURCE_FRESHNESS != FACTUAL_VERIFICATION.
 */
enum class ResearchSourceFreshness {
    CURRENT,
    STALE,
    UNKNOWN,
}
