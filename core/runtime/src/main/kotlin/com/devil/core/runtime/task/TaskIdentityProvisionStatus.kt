package com.devil.core.runtime.task

/**
 * Describes whether one genuine task identity is available for task creation.
 *
 * This status does not generate identities, create tasks, change task lifecycle
 * state, create plans, authorize capabilities, execute actions, observe
 * results, or verify outcomes.
 */
enum class TaskIdentityProvisionStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
