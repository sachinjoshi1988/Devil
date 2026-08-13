package com.devil.core.runtime.preference

/**
 * Operational result of attempting to produce one bounded qualified preference
 * candidate.
 *
 * AVAILABLE means one preference-learning assessment already qualified and its
 * exact evidence provenance was successfully preserved.
 *
 * UNAVAILABLE means the supplied preference-learning assessment did not qualify.
 *
 * AVAILABLE does not mean remembered, approved, committed, or persisted.
 */
enum class PreferenceLearningCandidateStatus {
    AVAILABLE,
    UNAVAILABLE,
}
