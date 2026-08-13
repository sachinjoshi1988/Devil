package com.devil.core.runtime.preference

/**
 * Result state of bounded preference-evidence assessment.
 *
 * QUALIFIED means repeated independent evidence satisfies the explicitly
 * supplied criteria for one candidate value.
 *
 * INSUFFICIENT_EVIDENCE means a leading candidate exists but the supplied
 * evidence does not yet satisfy the required repetition and confidence.
 *
 * AMBIGUOUS means competing candidate values have equal strongest support.
 *
 * QUALIFIED does not mean remembered, approved, committed, or persisted.
 */
enum class PreferenceLearningStatus {
    QUALIFIED,
    INSUFFICIENT_EVIDENCE,
    AMBIGUOUS,
}
