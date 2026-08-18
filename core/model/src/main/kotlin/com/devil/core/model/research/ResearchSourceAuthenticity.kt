package com.devil.core.model.research

/**
 * Stage 109 bounded authenticity assessment for one research source.
 *
 * ESTABLISHED means approved evidence supplied to this boundary explicitly
 * establishes authenticity for the bounded source assessment.
 *
 * NOT_ESTABLISHED means the supplied assessment does not establish authenticity.
 *
 * UNKNOWN means no justified authenticity conclusion is available.
 *
 * This status does not establish factual truth, source trust, factual freshness,
 * constitutional Verification, consensus, synthesis, or verified Outcome.
 */
enum class ResearchSourceAuthenticity {
    ESTABLISHED,
    NOT_ESTABLISHED,
    UNKNOWN,
}
