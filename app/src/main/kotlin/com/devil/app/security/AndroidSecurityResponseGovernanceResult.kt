package com.devil.app.security

import com.devil.core.runtime.surveillance.SecurityResponsePreparationResult
import com.devil.core.runtime.surveillance.SecurityResponsePreparationStatus

/**
 * Stage 228 bounded Security Response Governance result.
 *
 * GOVERNED preserves:
 *
 * - one exact AVAILABLE Stage 227 Security Alerting result;
 * - one exact PREPARED Stage 91 Security Response preparation result;
 * - exact identity of the Stage 90 surveillance record already preserved
 *   through the Stage 227 -> 226 -> 225 -> 224 chain.
 *
 * DEFERRED preserves the exact upstream Stage 227 and Stage 91 objects
 * without claiming governed response availability.
 *
 * Stage 228 does not determine threat, intrusion, or emergency state,
 * authorize a response, choose a response, execute a response, send alerts,
 * notify the owner, trigger alarms, operate locks, contact emergency services,
 * create Decision/Task/Plan/ExecutionRequest, establish Observation,
 * Verification or Outcome, mutate World Model state, or create/persist Memory.
 *
 * SECURITY_RESPONSE_GOVERNED != AUTHORIZED.
 * SECURITY_RESPONSE_GOVERNED != EXECUTION_APPROVED.
 * SECURITY_RESPONSE_PREPARED != EXECUTED.
 * SECURITY_ALERT_AVAILABLE != RESPONSE_AUTHORITY.
 * SECURITY_EVENT_UNDERSTOOD != THREAT.
 */
@ConsistentCopyVisibility
data class AndroidSecurityResponseGovernanceResult private constructor(
    val status: AndroidSecurityResponseGovernanceStatus,
    val alerting: AndroidSecurityAlertingResult,
    val responsePreparation: SecurityResponsePreparationResult,
) {
    companion object {

        fun create(
            status: AndroidSecurityResponseGovernanceStatus,
            alerting: AndroidSecurityAlertingResult,
            responsePreparation: SecurityResponsePreparationResult,
        ): AndroidSecurityResponseGovernanceResult {
            when (status) {
                AndroidSecurityResponseGovernanceStatus.GOVERNED -> {
                    require(
                        alerting.status ==
                            AndroidSecurityAlertingStatus.AVAILABLE,
                    ) {
                        "Governed Stage 228 Security Response requires available Stage 227 Security Alerting."
                    }

                    require(
                        responsePreparation.status ==
                            SecurityResponsePreparationStatus.PREPARED,
                    ) {
                        "Governed Stage 228 Security Response requires prepared Stage 91 Security Response context."
                    }

                    val responseRecord =
                        requireNotNull(responsePreparation.record) {
                            "Governed Stage 228 Security Response requires one prepared Stage 91 response record."
                        }

                    val surveillanceRecord =
                        requireNotNull(
                            alerting
                                .eventUnderstanding
                                .cameraAdapter
                                .surveillanceIntegration
                                .surveillancePreparation
                                .record,
                        ) {
                            "Governed Stage 228 Security Response requires preserved Stage 90 surveillance provenance."
                        }

                    require(responseRecord.surveillance === surveillanceRecord) {
                        "Stage 228 Security Response Governance must preserve the exact Stage 90 surveillance-record provenance."
                    }
                }

                AndroidSecurityResponseGovernanceStatus.DEFERRED -> Unit
            }

            return AndroidSecurityResponseGovernanceResult(
                status = status,
                alerting = alerting,
                responsePreparation = responsePreparation,
            )
        }
    }
}
