package com.devil.core.model.understanding

/**
 * Describes whether established semantic meaning expresses a request for action.
 *
 * ACTIONABLE does not mean authorized, executable, available, ready, or
 * successfully executed.
 */
enum class UnderstandingActionability {
    ACTIONABLE,
    NON_ACTIONABLE,
}
