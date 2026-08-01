package com.devil.core.runtime.decision

/**
 * Describes the operational result of the Decision Authority.
 *
 * This status reports whether a DecisionRecord was produced. The constitutional
 * decision state belongs to DecisionState inside that record.
 */
enum class DecisionAuthorityStatus {
    PRODUCED,
    DEFERRED,
    FAILED,
}
