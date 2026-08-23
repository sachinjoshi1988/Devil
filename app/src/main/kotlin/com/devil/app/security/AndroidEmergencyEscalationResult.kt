package com.devil.app.security

import com.devil.core.runtime.surveillance.SecurityResponsePreparationStatus

/**
 * Stage 229 bounded Emergency Escalation result.
 *
 * READY preserves:
 *
 * - one exact GOVERNED Stage 228 Security Response Governance result;
 * - the exact PREPARED Stage 91 Security Response already preserved there;
 * - one normalized explicitly supplied bounded escalation description.
 *
 * DEFERRED preserves only the exact Stage 228 upstream result and contains
 * no escalation metadata.
 *
 * Stage 229 does not:
 *
 * - determine that an emergency exists;
 * - determine threat or intrusion status;
 * - reinterpret Stage 226 understanding as verified reality;
 * - create, choose, or modify a Security Response action;
 * - grant constitutional authorization;
 * - approve execution;
 * - create Decision, Task, Plan, capability, or ExecutionRequest;
 * - send Android notifications;
 * - notify or contact the owner;
 * - place calls or send messages;
 * - contact emergency services;
 * - trigger alarms;
 * - operate locks;
 * - execute local or remote capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - create, commit, or persist Memory;
 * - implement Stage 230 Owner Security Dashboard.
 *
 * EMERGENCY_ESCALATION_READY != EMERGENCY_CONFIRMED.
 * EMERGENCY_ESCALATION_READY != AUTHORIZED.
 * EMERGENCY_ESCALATION_READY != EMERGENCY_SERVICE_CONTACTED.
 * EMERGENCY_ESCALATION_READY != EXECUTION_REQUESTED.
 * EMERGENCY_ESCALATION_READY != EXECUTED.
 * SECURITY_RESPONSE_GOVERNED != EMERGENCY_DETERMINATION.
 */
@ConsistentCopyVisibility
data class AndroidEmergencyEscalationResult private constructor(
    val status: AndroidEmergencyEscalationStatus,
    val responseGovernance: AndroidSecurityResponseGovernanceResult,
    val escalationDescription: String?,
) {
    companion object {

        fun create(
            status: AndroidEmergencyEscalationStatus,
            responseGovernance: AndroidSecurityResponseGovernanceResult,
            escalationDescription: String? = null,
        ): AndroidEmergencyEscalationResult {
            return when (status) {
                AndroidEmergencyEscalationStatus.READY -> {
                    require(
                        responseGovernance.status ==
                            AndroidSecurityResponseGovernanceStatus.GOVERNED,
                    ) {
                        "Ready Stage 229 Emergency Escalation requires governed Stage 228 Security Response context."
                    }

                    require(
                        responseGovernance.responsePreparation.status ==
                            SecurityResponsePreparationStatus.PREPARED,
                    ) {
                        "Ready Stage 229 Emergency Escalation requires preserved prepared Stage 91 Security Response context."
                    }

                    requireNotNull(
                        responseGovernance.responsePreparation.record,
                    ) {
                        "Ready Stage 229 Emergency Escalation requires one preserved Stage 91 response record."
                    }

                    val normalizedDescription =
                        requireNotNull(escalationDescription)
                            .trim()

                    require(normalizedDescription.isNotEmpty()) {
                        "Stage 229 emergency escalation description must not be blank."
                    }

                    AndroidEmergencyEscalationResult(
                        status = status,
                        responseGovernance = responseGovernance,
                        escalationDescription = normalizedDescription,
                    )
                }

                AndroidEmergencyEscalationStatus.DEFERRED -> {
                    require(escalationDescription == null) {
                        "Deferred Stage 229 Emergency Escalation must not contain escalation metadata."
                    }

                    AndroidEmergencyEscalationResult(
                        status = status,
                        responseGovernance = responseGovernance,
                        escalationDescription = null,
                    )
                }
            }
        }
    }
}
