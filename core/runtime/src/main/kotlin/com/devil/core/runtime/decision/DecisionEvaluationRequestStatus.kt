package com.devil.core.runtime.decision

/**
 * Describes whether a structured constitutional decision-evaluation request is
 * available.
 *
 * This status does not evaluate or select a decision, create memory, create
 * tasks, plan work, authorize capabilities, execute actions, observe results,
 * or verify outcomes.
 */
enum class DecisionEvaluationRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
