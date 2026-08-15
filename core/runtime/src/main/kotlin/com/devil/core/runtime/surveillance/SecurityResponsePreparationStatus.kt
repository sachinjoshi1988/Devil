package com.devil.core.runtime.surveillance

/**
 * Stage 91 bounded Security Response preparation status.
 *
 * PREPARED means one structurally valid SecurityResponseRecord was constructed
 * from:
 *
 * - one existing Stage 90 SecuritySurveillanceRecord;
 * - one explicitly supplied response action;
 * - and one explicitly supplied rationale.
 *
 * PREPARED does not mean:
 *
 * - the surveillance signal is verified reality;
 * - a watchlist candidate claim establishes verified identity;
 * - a person was established to be a criminal;
 * - a threat exists;
 * - an intrusion exists;
 * - an emergency exists;
 * - identity was established;
 * - authentication succeeded;
 * - trust was established;
 * - authorization exists;
 * - a constitutional Decision exists;
 * - a Task or Plan exists;
 * - a capability was registered or selected;
 * - capability availability or readiness exists;
 * - an ExecutionRequest exists;
 * - execution is approved;
 * - a notification was sent;
 * - an alarm was triggered;
 * - a lock was operated;
 * - emergency services were contacted;
 * - constitutional Observation occurred;
 * - Verification occurred;
 * - an Outcome occurred;
 * - World Model state changed;
 * - constitutional Learning occurred;
 * - Memory was committed;
 * - or Security Response state was persisted.
 *
 * DEFERRED means no truthful bounded Security Response record was produced.
 *
 * PREPARED != THREAT.
 * PREPARED != AUTHORIZED.
 * PREPARED != EXECUTION_REQUESTED.
 * PREPARED != EXECUTED.
 * ALERT_PREPARED != ALERT_SENT.
 * LOCK_RESPONSE_PREPARED != DEVICE_LOCKED.
 * EMERGENCY_ESCALATION_PREPARED != EMERGENCY_SERVICE_CONTACTED.
 */
enum class SecurityResponsePreparationStatus {
    PREPARED,
    DEFERRED,
}
