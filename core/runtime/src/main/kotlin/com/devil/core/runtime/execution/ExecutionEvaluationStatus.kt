package com.devil.core.runtime.execution

/**
 * Describes the bounded result of constitutional execution evaluation.
 *
 * APPROVED means that constitutional execution policy produced affirmative
 * permission to approach an execution implementation. UNAVAILABLE means no
 * justified execution determination was produced. FAILED represents an
 * operational evaluation failure.
 *
 * This status does not activate capabilities, perform platform actions, observe
 * execution, verify outcomes, or report final success.
 */
enum class ExecutionEvaluationStatus {
    APPROVED,
    UNAVAILABLE,
    FAILED,
}
