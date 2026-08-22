package com.devil.app.device

import com.devil.core.model.task.TaskRecord

/**
 * Stage 220 bounded Cross-Device Task Continuity coordinator.
 *
 * It associates one exact Stage 219 Cross-Device Session Governance result with
 * one exact existing TaskRecord.
 *
 * It does not:
 *
 * - create or replace a TaskRecord;
 * - generate a TaskId;
 * - invoke TaskAuthority;
 * - transfer a task between devices;
 * - mutate task lifecycle state;
 * - prepare task reentry;
 * - automatically resume or continue work;
 * - establish controlled autonomy;
 * - create a PlanRecord or ExecutionRequest;
 * - grant task authorization;
 * - execute local or remote capabilities;
 * - synchronize Conversation, World Model, or Memory state;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 221 Cross-Device Memory Continuity.
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
class AndroidCrossDeviceTaskContinuityCoordinator {

    fun integrate(
        sessionGovernance: AndroidCrossDeviceSessionGovernanceResult,
        task: TaskRecord,
    ): AndroidCrossDeviceTaskContinuityResult {
        val status =
            if (
                sessionGovernance.status ==
                    AndroidCrossDeviceSessionGovernanceStatus.AVAILABLE
            ) {
                AndroidCrossDeviceTaskContinuityStatus.AVAILABLE
            } else {
                AndroidCrossDeviceTaskContinuityStatus.DEFERRED
            }

        return AndroidCrossDeviceTaskContinuityResult.create(
            status = status,
            sessionGovernance = sessionGovernance,
            task = task,
        )
    }
}
