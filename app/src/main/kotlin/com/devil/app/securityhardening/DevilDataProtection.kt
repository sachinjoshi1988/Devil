package com.devil.app.securityhardening

/**
 * Stage 279 Data Protection.
 *
 * This bounded contract evaluates explicitly supplied architectural
 * data-protection evidence while preserving one exact Stage 278 Capability
 * Authorization Hardening result as authoritative upstream provenance.
 *
 * Existing Stage 46 privacy classification, exposure, disclosure-treatment,
 * and representation-reduction contracts remain authoritative for their
 * respective privacy responsibilities.
 *
 * Existing Memory Authority and Android memory-persistence boundaries remain
 * authoritative for memory governance and persistence.
 *
 * DATA_PROTECTED != DATA_ENCRYPTED.
 * DATA_PROTECTED != DATA_PERSISTED.
 * DATA_PROTECTED != DISCLOSURE_AUTHORIZED.
 * PRIVACY_ALLOWED != DEVIL_AUTHORIZATION.
 * DISCLOSURE_TREATMENT != DISCLOSURE_PERFORMED.
 * REPRESENTATION_REDUCED != DATA_TRANSMITTED.
 * PERSISTABLE != PERSISTED.
 * DATA_PROTECTION != MEMORY_SECURITY.
 * DATA_PROTECTION_HARDENED != VERIFIED_OUTCOME.
 *
 * Stage 279 does not encrypt or decrypt data, generate or manage keys, choose
 * a storage destination, persist data, transmit data, authorize disclosure,
 * modify Memory Authority, implement credential storage, delete protected
 * content, execute anything, or implement Stage 280 Memory Security.
 */
enum class DevilDataProtectionStatus {
    HARDENED,
    NOT_HARDENED,
}

/**
 * Explicitly supplied Stage 279 architectural data-protection evidence.
 *
 * These values describe already-established protection properties. They do
 * not contain protected raw data, credentials, secrets, encryption keys, or
 * persisted memory.
 */
data class DevilDataProtectionEvidence(
    val capabilityAuthorizationHardening:
        DevilCapabilityAuthorizationHardeningResult,
    val privacyClassificationGovernsSensitiveDataHandling: Boolean,
    val sensitiveExposureFailsClosedWhereRequired: Boolean,
    val privacyDisclosureTreatmentCannotTransmitData: Boolean,
    val representationReductionCannotPersistOrTransmitProtectedContent: Boolean,
    val durablePersistenceRequiresApprovedProtectedStore: Boolean,
    val credentialsAndSecretsSeparatedFromOrdinaryApplicationData: Boolean,
    val dataProtectionSeparatedFromMemorySecurity: Boolean,
) {
    fun isComplete(): Boolean =
        capabilityAuthorizationHardening.status ==
            DevilCapabilityAuthorizationHardeningStatus.HARDENED &&
            capabilityAuthorizationHardening.evidence
                .sessionHardening
                .evidence
                .authenticationHardening
                .evidence
                .threatModel
                .coveredCategories
                .contains(
                    DevilThreatCategory.DATA_MEMORY_EXPOSURE,
                ) &&
            privacyClassificationGovernsSensitiveDataHandling &&
            sensitiveExposureFailsClosedWhereRequired &&
            privacyDisclosureTreatmentCannotTransmitData &&
            representationReductionCannotPersistOrTransmitProtectedContent &&
            durablePersistenceRequiresApprovedProtectedStore &&
            credentialsAndSecretsSeparatedFromOrdinaryApplicationData &&
            dataProtectionSeparatedFromMemorySecurity
}

/**
 * Bounded Stage 279 Data Protection result.
 *
 * HARDENED means only that every required Stage 279 architectural protection
 * property was explicitly supplied and the preserved Stage 275 threat model
 * covers data / memory exposure.
 *
 * HARDENED does not prove encryption, persistence, disclosure authorization,
 * transmission, memory security, execution, or a verified outcome.
 */
@ConsistentCopyVisibility
data class DevilDataProtectionResult private constructor(
    val status: DevilDataProtectionStatus,
    val evidence: DevilDataProtectionEvidence,
) {
    companion object {
        fun create(
            evidence: DevilDataProtectionEvidence,
        ): DevilDataProtectionResult =
            DevilDataProtectionResult(
                status =
                    if (evidence.isComplete()) {
                        DevilDataProtectionStatus.HARDENED
                    } else {
                        DevilDataProtectionStatus.NOT_HARDENED
                    },
                evidence = evidence,
            )
    }
}

/**
 * Stage 279 bounded Data Protection coordinator.
 *
 * It evaluates explicitly supplied architectural protection evidence only.
 *
 * It does not:
 *
 * - classify raw data;
 * - inspect protected content;
 * - perform privacy disclosure;
 * - grant constitutional authorization;
 * - encrypt or decrypt data;
 * - generate, store, rotate, or revoke cryptographic keys;
 * - select or create a durable storage mechanism;
 * - persist, transmit, synchronize, replicate, or delete data;
 * - modify Memory Authority or memory-persistence authority;
 * - create an ExecutionRequest or execute anything;
 * - establish Observation, Verification, Outcome, or storage success;
 * - implement Stage 280 Memory Security.
 */
class DevilDataProtectionCoordinator {
    fun evaluate(
        evidence: DevilDataProtectionEvidence,
    ): DevilDataProtectionResult =
        DevilDataProtectionResult.create(
            evidence = evidence,
        )
}
