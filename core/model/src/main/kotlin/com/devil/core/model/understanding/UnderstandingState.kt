package com.devil.core.model.understanding

/**
 * Describes the validated state of Devil's current understanding.
 *
 * This state reports the quality of understanding only. It does not authorize
 * an action, select a decision, or claim that execution is possible.
 */
enum class UnderstandingState {
    COMPLETE,
    AMBIGUOUS,
    INCOMPLETE,
    UNSUPPORTED,
}
