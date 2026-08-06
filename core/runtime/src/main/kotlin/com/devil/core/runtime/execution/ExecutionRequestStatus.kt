package com.devil.core.runtime.execution

/**
 * Describes whether one bounded constitutional execution request is available.
 *
 * This status reports request availability only. It does not establish
 * capability health, operating-system permission, execution permission,
 * execution success, observation, verification, or final outcome.
 */
enum class ExecutionRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
