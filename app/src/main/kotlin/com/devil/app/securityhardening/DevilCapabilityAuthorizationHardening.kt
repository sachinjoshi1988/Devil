package com.devil.app.securityhardening

/**
 * Stage 278 Capability Authorization Hardening.
 *
 * This bounded contract evaluates explicitly supplied capability-authorization
 * hardening evidence while preserving one exact Stage 277 Session Hardening result
 * as authoritative upstream security-hardening provenance.
 *
 * Existing constitutional Authorization Authority remains authoritative for
 * authorization evaluation. Existing Capability Selection Authority remains
 * authoritative for capability selection.
 *
 * CAPABILITY_SELECTED != CAPABILITY_AUTHORIZED.
 * CAPABILITY_AVAILABLE != CAPABILITY_AUTHORIZED.
 * ANDROID_PERMISSION_GRANTED != DEVIL_AUTHORIZATION.
 * AUTHORIZATION != EXECUTION_APPROVAL.
 * AUTHORIZATION != EXECUTION.
 * SESSION_HARDENED != AUTHORIZATION.
 * CAPABILITY_AUTHORIZATION_HARDENED != AUTHORIZATION_GRANTED.
 * CAPABILITY_AUTHORIZATION_HARDENED != VERIFIED_OUTCOME.
 *
 * Stage 278 does not grant or deny authorization, authorize an individual
 * capability, select a capability, establish capability availability or health,
 * grant Android permission, establish Executive readiness, approve execution,
 * create an ExecutionRequest, execute anything, or implement Stage 279 Data
 * Protection.
 */
enum class DevilCapabilityAuthorizationHardeningStatus {
    HARDENED,
    NOT_HARDENED,
}

/**
 * Explicitly supplied Stage 278 capability-authorization hardening evidence.
 *
 * These values describe already-established architectural security properties.
 * They do not themselves grant authorization or make a capability executable.
 */
data class DevilCapabilityAuthorizationHardeningEvidence(
    val sessionHardening: DevilSessionHardeningResult,
    val constitutionalAuthorizationSeparatedFromCapabilityAuthorization: Boolean,
    val capabilitySelectionCannotGrantAuthorization: Boolean,
    val androidPermissionCannotGrantDevilAuthorization: Boolean,
    val capabilityAvailabilityCannotGrantAuthorization: Boolean,
    val capabilityReadinessCannotGrantAuthorization: Boolean,
    val executionCapabilityCannotGrantAuthorization: Boolean,
    val deniedOrDeferredAuthorizationCannotBeUpgradedDownstream: Boolean,
) {
    fun isComplete(): Boolean =
        sessionHardening.status ==
            DevilSessionHardeningStatus.HARDENED &&
            sessionHardening.evidence
                .authenticationHardening
                .evidence
                .threatModel
                .coveredCategories
                .contains(
                    DevilThreatCategory.AUTHORIZATION_BYPASS_PRIVILEGE_ESCALATION,
                ) &&
            constitutionalAuthorizationSeparatedFromCapabilityAuthorization &&
            capabilitySelectionCannotGrantAuthorization &&
            androidPermissionCannotGrantDevilAuthorization &&
            capabilityAvailabilityCannotGrantAuthorization &&
            capabilityReadinessCannotGrantAuthorization &&
            executionCapabilityCannotGrantAuthorization &&
            deniedOrDeferredAuthorizationCannotBeUpgradedDownstream
}

/**
 * Bounded Stage 278 Capability Authorization Hardening result.
 *
 * HARDENED means only that every required Stage 278 architectural hardening
 * property was explicitly supplied and the preserved Stage 275 threat model
 * covers authorization bypass / privilege escalation.
 *
 * HARDENED does not grant authorization, authorize a capability, approve
 * execution, perform execution, or establish a verified outcome.
 */
@ConsistentCopyVisibility
data class DevilCapabilityAuthorizationHardeningResult private constructor(
    val status: DevilCapabilityAuthorizationHardeningStatus,
    val evidence: DevilCapabilityAuthorizationHardeningEvidence,
) {
    companion object {
        fun create(
            evidence: DevilCapabilityAuthorizationHardeningEvidence,
        ): DevilCapabilityAuthorizationHardeningResult =
            DevilCapabilityAuthorizationHardeningResult(
                status =
                    if (evidence.isComplete()) {
                        DevilCapabilityAuthorizationHardeningStatus.HARDENED
                    } else {
                        DevilCapabilityAuthorizationHardeningStatus.NOT_HARDENED
                    },
                evidence = evidence,
            )
    }
}

/**
 * Stage 278 bounded Capability Authorization Hardening coordinator.
 *
 * It evaluates explicitly supplied hardening evidence only.
 *
 * It does not:
 *
 * - grant or deny constitutional authorization;
 * - authorize an individual capability;
 * - invoke or replace Authorization Authority;
 * - invoke or replace Capability Selection Authority;
 * - establish capability availability or health;
 * - request or grant Android permission;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - approve or perform execution;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 279 Data Protection.
 */
class DevilCapabilityAuthorizationHardeningCoordinator {
    fun evaluate(
        evidence: DevilCapabilityAuthorizationHardeningEvidence,
    ): DevilCapabilityAuthorizationHardeningResult =
        DevilCapabilityAuthorizationHardeningResult.create(
            evidence = evidence,
        )
}
