package com.devil.app.device

import com.devil.core.model.task.TaskRecord

/**
 * Stage 220 bounded Cross-Device Task Continuity result.
 *
 * AVAILABLE preserves one exact available Stage 219 Cross-Device Session Governance
 * result together with one exact existing TaskRecord.
 *
 * DEFERRED preserves both exact upstream objects without claiming task continuation.
 *
 * CROSS_DEVICE_TASK_CONTINUITY != TASK_CREATION.
 * CROSS_DEVICE_TASK_CONTINUITY != TASK_TRANSFER.
 * CROSS_DEVICE_TASK_CONTINUITY != TASK_MUTATION.
 * CROSS_DEVICE_TASK_CONTINUITY != TASK_REENTRY.
 * TASK_CONTINUITY != AUTOMATIC_CONTINUATION.
 * TASK_CONTINUITY != CONTROLLED_AUTONOMY.
 * SESSION_GOVERNANCE != TASK_AUTHORIZATION.
 * TASK_CONTEXT != EXECUTION_REQUEST.
 * TASK_CONTEXT != REMOTE_EXECUTION.
 * TASK_CONTINUITY != MEMORY_SYNC.
 */
@ConsistentCopyVisibility
data class AndroidCrossDeviceTaskContinuityResult private constructor(
    val status: AndroidCrossDeviceTaskContinuityStatus,
    val sessionGovernance: AndroidCrossDeviceSessionGovernanceResult,
    val task: TaskRecord,
) {
    companion object {
        fun create(
            status: AndroidCrossDeviceTaskContinuityStatus,
            sessionGovernance: AndroidCrossDeviceSessionGovernanceResult,
            task: TaskRecord,
        ): AndroidCrossDeviceTaskContinuityResult {
            when (status) {
                AndroidCrossDeviceTaskContinuityStatus.AVAILABLE -> {
                    require(
                        sessionGovernance.status ==
                            AndroidCrossDeviceSessionGovernanceStatus.AVAILABLE,
                    ) {
                        "Available Stage 220 Cross-Device Task Continuity requires available Stage 219 Cross-Device Session Governance."
                    }
                }

                AndroidCrossDeviceTaskContinuityStatus.DEFERRED -> Unit
            }

            return AndroidCrossDeviceTaskContinuityResult(
                status = status,
                sessionGovernance = sessionGovernance,
                task = task,
            )
        }
    }
}
