package com.devil.core.model.decision

/**
 * Describes the constitutional result of one Devil reasoning cycle.
 *
 * This state does not represent execution, task progress, or capability state.
 */
enum class DecisionState {
    SELECTED,
    DEFERRED,
    REQUIRES_CLARIFICATION,
    REJECTED,
}
