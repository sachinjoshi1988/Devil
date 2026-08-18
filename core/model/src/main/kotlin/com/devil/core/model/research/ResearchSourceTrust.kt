package com.devil.core.model.research

/**
 * Stage 109 bounded trust assessment for one research source.
 *
 * TRUSTED means approved assessment input explicitly establishes bounded source
 * trust for this research assessment only.
 *
 * UNTRUSTED means approved assessment input explicitly establishes that the
 * source must not be treated as trusted.
 *
 * UNESTABLISHED means source trust has not been constitutionally established.
 *
 * SOURCE_TRUST != FACTUAL_TRUTH.
 * SOURCE_TRUST != VERIFICATION.
 */
enum class ResearchSourceTrust {
    TRUSTED,
    UNTRUSTED,
    UNESTABLISHED,
}
