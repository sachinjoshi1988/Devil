package com.devil.app.execution

import com.devil.app.capability.AndroidCapabilityState
import com.devil.app.permission.AndroidPermissionAssessment
import com.devil.core.runtime.execution.ExecutionResult

/**
 * Android embodiment boundary for one bounded execution attempt.
 *
 * The adapter may approach a platform execution performer only when:
 *
 * - constitutional Execution Authority returned APPROVED,
 * - the same capability is AVAILABLE,
 * - that capability's health is READY,
 * - and Android permission is either explicitly NOT_REQUIRED or GRANTED.
 *
 * These conditions remain independent:
 *
 * Devil authorization != Android permission.
 * Capability health READY != Executive readiness.
 * Execution approval != execution attempt.
 * Execution attempt != observed effect.
 * Observed effect != verified outcome.
 */
fun interface AndroidExecutionAdapter {

    fun execute(
        execution: ExecutionResult,
        capabilityState: AndroidCapabilityState,
        permissionAssessment: AndroidPermissionAssessment,
    ): AndroidExecutionAttemptResult
}
