package com.devil.app.security

/**
 * Stage 229 bounded Emergency Escalation status.
 *
 * READY means one exact GOVERNED Stage 228 Security Response Governance result
 * has been associated with one explicitly supplied bounded escalation description.
 *
 * DEFERRED means Stage 229 cannot truthfully claim bounded emergency escalation
 * readiness.
 *
 * EMERGENCY_ESCALATION_READY != EMERGENCY_CONFIRMED.
 * EMERGENCY_ESCALATION_READY != AUTHORIZED.
 * EMERGENCY_ESCALATION_READY != EMERGENCY_SERVICE_CONTACTED.
 * EMERGENCY_ESCALATION_READY != EXECUTION_REQUESTED.
 * EMERGENCY_ESCALATION_READY != EXECUTED.
 * SECURITY_RESPONSE_GOVERNED != EMERGENCY_DETERMINATION.
 */
enum class AndroidEmergencyEscalationStatus {
    READY,
    DEFERRED,
}
