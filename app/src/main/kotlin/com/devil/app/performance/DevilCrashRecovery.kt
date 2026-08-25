package com.devil.app.performance

import com.devil.app.reliability.AndroidReliabilityRecoveryResult
import com.devil.app.reliability.AndroidReliabilityRecoveryStatus
import com.devil.core.model.reliability.RecoveryRequestStatus

/**
 * Stage 271 Crash Recovery.
 *
 * This bounded contract evaluates explicitly supplied crash-recovery readiness
 * while preserving one exact Stage 194 Android Reliability & Recovery result.
 *
 * Stage 45 remains authoritative for reliability assessment and recovery-request
 * governance. Stage 194 remains authoritative for Android recovery integration.
 *
 * CRASH_RECOVERY_READY != RECOVERY_EXECUTED.
 * CRASH_RECOVERY_READY != PROCESS_RESTARTED.
 * CRASH_RECOVERY_READY != COMPONENT_REINITIALIZED.
 * CRASH_RECOVERY_READY != RETRY_EXECUTED.
 * CRASH_RECOVERY_READY != RECOVERED.
 * CRASH_RECOVERY_READY != VERIFIED_OUTCOME.
 * CRASH_RECOVERY_READY != AUTHORIZATION.
 * CRASH_RECOVERY_READY != EXECUTION_APPROVAL.
 * RECOVERY_REQUEST_AVAILABLE != RECOVERY_EXECUTED.
 * RECOVERY_ATTEMPT_BUDGET != RETRY_PERMISSION.
 *
 * Stage 271 does not restart the Android process, recreate activities,
 * reinitialize components, retry operations, reconnect sources, consume recovery
 * attempts, schedule work, erase failure evidence, mutate capability health,
 * persist recovery state, or implement Stage 272 Long-Running Stability
 * or any later Phase-S reliability behavior.
 */
enum class DevilCrashRecoveryStatus {
    RECOVERY_READY,
    RECOVERY_DEFERRED,
}

/**
 * Explicitly supplied Stage 271 crash-recovery evidence.
 *
 * The exact Stage 194 result is retained unchanged as authoritative upstream
 * recovery-governance provenance.
 *
 * The additional flags describe supplied crash-recovery preparedness evidence
 * only. They do not perform or authorize recovery.
 */
data class DevilCrashRecoveryEvidence(
    val reliabilityRecovery: AndroidReliabilityRecoveryResult,
    val crashFailurePreserved: Boolean,
    val boundedRecoveryPathEstablished: Boolean,
    val lifecycleReentryPrepared: Boolean,
) {
    fun isComplete(): Boolean =
        reliabilityRecovery.status ==
            AndroidReliabilityRecoveryStatus.AVAILABLE &&
            reliabilityRecovery.recoveryRequestResult.status ==
            RecoveryRequestStatus.AVAILABLE &&
            crashFailurePreserved &&
            boundedRecoveryPathEstablished &&
            lifecycleReentryPrepared
}

/**
 * Bounded Stage 271 Crash Recovery result.
 *
 * RECOVERY_READY means only that:
 *
 * - Stage 194 supplied an AVAILABLE recovery integration result;
 * - its exact Stage 45 recovery request remains AVAILABLE;
 * - explicitly supplied crash-failure evidence is preserved;
 * - a bounded recovery path is represented as established;
 * - lifecycle re-entry preparedness is represented as established.
 *
 * It does not mean any recovery action occurred.
 */
@ConsistentCopyVisibility
data class DevilCrashRecoveryResult private constructor(
    val status: DevilCrashRecoveryStatus,
    val evidence: DevilCrashRecoveryEvidence,
) {
    companion object {
        fun create(
            evidence: DevilCrashRecoveryEvidence,
        ): DevilCrashRecoveryResult =
            DevilCrashRecoveryResult(
                status =
                    if (evidence.isComplete()) {
                        DevilCrashRecoveryStatus.RECOVERY_READY
                    } else {
                        DevilCrashRecoveryStatus.RECOVERY_DEFERRED
                    },
                evidence = evidence,
            )
    }
}

/**
 * Stage 271 bounded Crash Recovery coordinator.
 *
 * It evaluates supplied evidence only.
 *
 * It does not:
 *
 * - create RecoveryEvidence;
 * - create a ReliabilityAssessment;
 * - construct a RecoveryRequest;
 * - consume RecoveryAttemptBudget;
 * - perform retry, restart, reconnection, or reinitialization;
 * - restart the Android process;
 * - recreate an Activity;
 * - start WorkManager, JobScheduler, services, or background work;
 * - mutate capability health;
 * - erase failure evidence;
 * - grant authorization or execution approval;
 * - establish constitutional Observation, Verification, or Outcome.
 */
class DevilCrashRecoveryCoordinator {
    fun evaluate(
        evidence: DevilCrashRecoveryEvidence,
    ): DevilCrashRecoveryResult =
        DevilCrashRecoveryResult.create(
            evidence = evidence,
        )
}
