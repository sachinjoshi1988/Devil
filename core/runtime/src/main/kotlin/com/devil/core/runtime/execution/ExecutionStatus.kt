package com.devil.core.runtime.execution

/**
 * Describes the stable operational result of constitutional execution
 * evaluation.
 *
 * APPROVED means that constitutional execution evaluation produced permission
 * to approach a future execution implementation. It does not mean that a
 * capability was activated, an action was attempted, or execution succeeded.
 *
 * DEFERRED means no justified execution approval is currently available.
 * FAILED represents an operational failure with a matching error.
 */
enum class ExecutionStatus {
    APPROVED,
    DEFERRED,
    FAILED,
}
