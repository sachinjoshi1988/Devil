package com.devil.app.security

import com.devil.core.runtime.surveillance.SecurityResponsePreparationResult
import com.devil.core.runtime.surveillance.SecurityResponsePreparationStatus

/**
 * Stage 228 bounded Security Response Governance coordinator.
 *
 * It integrates:
 *
 * - one exact Stage 227 Security Alerting result;
 * - one exact existing Stage 91 Security Response preparation result;
 *
 * only when both preserve the exact same Stage 90 surveillance record.
 *
 * It does not:
 *
 * - create a Security Response action;
 * - choose a Security Response;
 * - determine threat, intrusion, or emergency state;
 * - authenticate a subject or device;
 * - grant constitutional authorization;
 * - approve execution;
 * - create a Decision, Task, Plan, capability, or ExecutionRequest;
 * - send Android notifications;
 * - notify or contact the owner;
 * - trigger alarms;
 * - operate locks;
 * - contact emergency services;
 * - execute local or remote capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - create, commit, or persist Memory;
 * - implement Stage 229 Emergency Escalation.
 *
 * SECURITY_RESPONSE_GOVERNED != AUTHORIZED.
 * SECURITY_RESPONSE_GOVERNED != EXECUTION_APPROVED.
 * SECURITY_RESPONSE_PREPARED != EXECUTED.
 * SECURITY_ALERT_AVAILABLE != RESPONSE_AUTHORITY.
 * SECURITY_EVENT_UNDERSTOOD != THREAT.
 */
class AndroidSecurityResponseGovernanceCoordinator {

    fun govern(
        alerting: AndroidSecurityAlertingResult,
        responsePreparation: SecurityResponsePreparationResult,
    ): AndroidSecurityResponseGovernanceResult {
        val surveillanceRecord =
            alerting
                .eventUnderstanding
                .cameraAdapter
                .surveillanceIntegration
                .surveillancePreparation
                .record

        val responseRecord =
            responsePreparation.record

        val status =
            if (
                alerting.status ==
                    AndroidSecurityAlertingStatus.AVAILABLE &&
                responsePreparation.status ==
                    SecurityResponsePreparationStatus.PREPARED &&
                surveillanceRecord != null &&
                responseRecord != null &&
                responseRecord.surveillance === surveillanceRecord
            ) {
                AndroidSecurityResponseGovernanceStatus.GOVERNED
            } else {
                AndroidSecurityResponseGovernanceStatus.DEFERRED
            }

        return AndroidSecurityResponseGovernanceResult.create(
            status = status,
            alerting = alerting,
            responsePreparation = responsePreparation,
        )
    }
}
