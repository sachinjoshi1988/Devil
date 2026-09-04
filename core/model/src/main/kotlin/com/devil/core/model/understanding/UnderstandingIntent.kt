package com.devil.core.model.understanding

/**
 * Describes one bounded semantic intent established during understanding.
 *
 * Intent describes interpreted meaning only.
 *
 * It does not select a constitutional decision, create a task, create a plan,
 * authorize a capability, execute an action, or establish an outcome.
 *
 * INTENT_RECOGNIZED != DECISION_SELECTED.
 * INTENT_RECOGNIZED != CAPABILITY_SELECTED.
 * ACTIONABLE != AUTHORIZED.
 * ACTIONABLE != EXECUTABLE.
 */
enum class UnderstandingIntent {
    GREETING,
    OPEN_TARGET,
    ACTION_REQUEST,
    INFORMATION_QUERY,
    INFORMATIONAL,
}
