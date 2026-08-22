package com.devil.app.device

import com.devil.core.runtime.memory.MemoryContinuityResult
import com.devil.core.runtime.memory.MemoryContinuityStatus

/**
 * Stage 221 bounded Cross-Device Memory Continuity result.
 *
 * AVAILABLE preserves one exact available Stage 220 Cross-Device Task Continuity
 * result together with one exact ESTABLISHED Stage 103 Memory Continuity result.
 *
 * DEFERRED preserves both exact upstream objects without claiming cross-device
 * memory continuity availability.
 *
 * This result does not copy, synchronize, replicate, commit, persist, store,
 * expose, recall, restore, or otherwise mutate logical memory.
 *
 * CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_SYNC.
 * CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_REPLICATION.
 * CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_TRANSFER.
 * CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_COMMITMENT.
 * CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_PERSISTENCE.
 * CROSS_DEVICE_MEMORY_CONTINUITY != STORAGE_SUCCESS.
 * CROSS_DEVICE_MEMORY_CONTINUITY != RECALL_AVAILABILITY.
 * CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_RECALL.
 * MEMORY_CONTINUITY != AUTHORIZATION.
 * MEMORY_CONTINUITY != REMOTE_EXECUTION.
 */
@ConsistentCopyVisibility
data class AndroidCrossDeviceMemoryContinuityResult private constructor(
    val status: AndroidCrossDeviceMemoryContinuityStatus,
    val taskContinuity: AndroidCrossDeviceTaskContinuityResult,
    val memoryContinuity: MemoryContinuityResult,
) {
    companion object {
        fun create(
            status: AndroidCrossDeviceMemoryContinuityStatus,
            taskContinuity: AndroidCrossDeviceTaskContinuityResult,
            memoryContinuity: MemoryContinuityResult,
        ): AndroidCrossDeviceMemoryContinuityResult {
            when (status) {
                AndroidCrossDeviceMemoryContinuityStatus.AVAILABLE -> {
                    require(
                        taskContinuity.status ==
                            AndroidCrossDeviceTaskContinuityStatus.AVAILABLE,
                    ) {
                        "Available Stage 221 Cross-Device Memory Continuity requires available Stage 220 Cross-Device Task Continuity."
                    }

                    require(
                        memoryContinuity.status ==
                            MemoryContinuityStatus.ESTABLISHED,
                    ) {
                        "Available Stage 221 Cross-Device Memory Continuity requires ESTABLISHED Stage 103 Memory Continuity."
                    }

                    require(memoryContinuity.record != null) {
                        "Established Stage 221 memory continuity requires one preserved Stage 103 continuity record."
                    }
                }

                AndroidCrossDeviceMemoryContinuityStatus.DEFERRED -> Unit
            }

            return AndroidCrossDeviceMemoryContinuityResult(
                status = status,
                taskContinuity = taskContinuity,
                memoryContinuity = memoryContinuity,
            )
        }
    }
}
