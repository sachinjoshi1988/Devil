package com.devil.app.reliability

import com.devil.core.model.reliability.RecoveryRequestResult
import com.devil.core.model.reliability.RecoveryRequestStatus
import com.devil.core.model.reliability.ReliabilityAssessment

/**
 * Stage 194 bounded Android Reliability & Recovery integration result.
 *
 * The exact Stage 45 reliability assessment and recovery-request result are
 * preserved unchanged.
 *
 * RECOVERY_REQUEST != RECOVERY_EXECUTED.
 * RECOVERY_RECORDED != RECOVERED.
 */
@ConsistentCopyVisibility
data class AndroidReliabilityRecoveryResult private constructor(
    val status: AndroidReliabilityRecoveryStatus,
    val assessment: ReliabilityAssessment,
    val recoveryRequestResult: RecoveryRequestResult,
) {
    companion object {
        fun create(
            status: AndroidReliabilityRecoveryStatus,
            assessment: ReliabilityAssessment,
            recoveryRequestResult: RecoveryRequestResult,
        ): AndroidReliabilityRecoveryResult {
            when (status) {
                AndroidReliabilityRecoveryStatus.AVAILABLE ->
                    require(
                        recoveryRequestResult.status ==
                            RecoveryRequestStatus.AVAILABLE,
                    ) {
                        "Available Android reliability recovery requires an available Stage 45 recovery request."
                    }

                AndroidReliabilityRecoveryStatus.DEFERRED ->
                    require(
                        recoveryRequestResult.status !=
                            RecoveryRequestStatus.AVAILABLE,
                    ) {
                        "Deferred Android reliability recovery must not contain an available Stage 45 recovery request."
                    }
            }

            recoveryRequestResult.request?.let { request ->
                require(request.assessment == assessment) {
                    "Android reliability recovery must preserve the exact Stage 45 reliability assessment provenance."
                }
            }

            return AndroidReliabilityRecoveryResult(
                status = status,
                assessment = assessment,
                recoveryRequestResult = recoveryRequestResult,
            )
        }
    }
}
