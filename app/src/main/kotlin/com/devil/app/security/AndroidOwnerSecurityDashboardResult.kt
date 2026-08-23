package com.devil.app.security

import com.devil.core.runtime.owner.OwnerMultiUserContextResult
import com.devil.core.runtime.owner.OwnerMultiUserContextStatus

/**
 * Stage 230 bounded Owner Security Dashboard result.
 *
 * AVAILABLE preserves:
 *
 * - one exact READY Stage 229 Emergency Escalation result;
 * - one exact ESTABLISHED Stage 100 Owner / Multi-User Context result;
 * - the exact Stage 100 owner/current-subject context record;
 * - one normalized explicitly supplied bounded dashboard summary.
 *
 * DEFERRED preserves the exact upstream Stage 229 and Stage 100 results without
 * claiming owner-security dashboard availability and contains no dashboard
 * summary metadata.
 *
 * Stage 230 does not:
 *
 * - authenticate the owner or current subject;
 * - prove ownership;
 * - infer that the current subject is the owner;
 * - enter Owner Mode;
 * - establish trust;
 * - grant constitutional authorization;
 * - determine threat, intrusion, or emergency state;
 * - reinterpret Stage 226 understanding as verified reality;
 * - create, choose, modify, authorize, or execute a Security Response;
 * - send an Android notification;
 * - notify or contact the owner;
 * - place calls or send messages;
 * - contact emergency services;
 * - trigger alarms;
 * - operate locks;
 * - create a Decision, Task, Plan, capability, or ExecutionRequest;
 * - execute local or remote capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - create, commit, or persist Memory;
 * - retain or persist security evidence;
 * - build the final Stage 261 Security Interface;
 * - implement Stage 231 Security Evidence Retention.
 *
 * OWNER_CONTEXT != AUTHENTICATION.
 * OWNER_CONTEXT != OWNERSHIP_PROOF.
 * OWNER_CONTEXT != OWNER_MODE.
 * OWNER_CONTEXT != AUTHORIZATION.
 * OWNER_SECURITY_DASHBOARD_AVAILABLE != OWNER_AUTHENTICATED.
 * OWNER_SECURITY_DASHBOARD_AVAILABLE != OWNER_MODE.
 * OWNER_SECURITY_DASHBOARD_AVAILABLE != AUTHORIZATION.
 * OWNER_SECURITY_DASHBOARD_AVAILABLE != SECURITY_RESPONSE_EXECUTED.
 * OWNER_SECURITY_DASHBOARD_AVAILABLE != EMERGENCY_CONFIRMED.
 * DASHBOARD_SUMMARY != VERIFIED_REALITY.
 * DASHBOARD_VISIBILITY != SECURITY_EVIDENCE_RETENTION.
 */
@ConsistentCopyVisibility
data class AndroidOwnerSecurityDashboardResult private constructor(
    val status: AndroidOwnerSecurityDashboardStatus,
    val emergencyEscalation: AndroidEmergencyEscalationResult,
    val ownerContext: OwnerMultiUserContextResult,
    val dashboardSummary: String?,
) {
    companion object {

        fun create(
            status: AndroidOwnerSecurityDashboardStatus,
            emergencyEscalation: AndroidEmergencyEscalationResult,
            ownerContext: OwnerMultiUserContextResult,
            dashboardSummary: String? = null,
        ): AndroidOwnerSecurityDashboardResult {
            return when (status) {
                AndroidOwnerSecurityDashboardStatus.AVAILABLE -> {
                    require(
                        emergencyEscalation.status ==
                            AndroidEmergencyEscalationStatus.READY,
                    ) {
                        "Available Stage 230 Owner Security Dashboard requires ready Stage 229 Emergency Escalation context."
                    }

                    require(
                        ownerContext.status ==
                            OwnerMultiUserContextStatus.ESTABLISHED,
                    ) {
                        "Available Stage 230 Owner Security Dashboard requires established Stage 100 Owner / Multi-User Context."
                    }

                    requireNotNull(ownerContext.record) {
                        "Available Stage 230 Owner Security Dashboard requires one preserved Stage 100 owner context record."
                    }

                    val normalizedSummary =
                        requireNotNull(dashboardSummary)
                            .trim()

                    require(normalizedSummary.isNotEmpty()) {
                        "Stage 230 owner security dashboard summary must not be blank."
                    }

                    AndroidOwnerSecurityDashboardResult(
                        status = status,
                        emergencyEscalation = emergencyEscalation,
                        ownerContext = ownerContext,
                        dashboardSummary = normalizedSummary,
                    )
                }

                AndroidOwnerSecurityDashboardStatus.DEFERRED -> {
                    require(dashboardSummary == null) {
                        "Deferred Stage 230 Owner Security Dashboard must not contain dashboard summary metadata."
                    }

                    AndroidOwnerSecurityDashboardResult(
                        status = status,
                        emergencyEscalation = emergencyEscalation,
                        ownerContext = ownerContext,
                        dashboardSummary = null,
                    )
                }
            }
        }
    }
}
