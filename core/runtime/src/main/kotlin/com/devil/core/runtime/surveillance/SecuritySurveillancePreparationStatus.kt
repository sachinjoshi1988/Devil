package com.devil.core.runtime.surveillance

/**
 * Stage 90 bounded Security Surveillance preparation status.
 *
 * PREPARED means one structurally valid SecuritySurveillanceRecord was created
 * from explicitly supplied bounded surveillance-domain input.
 *
 * PREPARED does not mean:
 *
 * - a surveillance source exists physically;
 * - the source is reachable;
 * - monitoring is active;
 * - a camera is open;
 * - a frame was captured;
 * - a stream exists;
 * - the supplied description is true;
 * - constitutional Observation occurred;
 * - Verification occurred;
 * - an Outcome occurred;
 * - identity was established;
 * - authentication succeeded;
 * - trust was established;
 * - authorization exists;
 * - a threat exists;
 * - an intrusion exists;
 * - an emergency exists;
 * - a capability was registered or selected;
 * - execution was approved;
 * - a Security Response was authorized;
 * - an alarm was triggered;
 * - an external notification was sent;
 * - World Model state changed;
 * - constitutional Learning occurred;
 * - Memory was committed;
 * - or surveillance state was persisted.
 *
 * DEFERRED means no truthful bounded surveillance record was produced.
 *
 * PREPARED != OBSERVED.
 * PREPARED != VERIFIED.
 * PREPARED != THREAT.
 * PREPARED != AUTHORIZED.
 * PREPARED != RESPONSE.
 * PREPARED != EXECUTED.
 */
enum class SecuritySurveillancePreparationStatus {
    PREPARED,
    DEFERRED,
}
