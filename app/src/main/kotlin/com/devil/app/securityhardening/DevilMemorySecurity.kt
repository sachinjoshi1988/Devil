package com.devil.app.securityhardening

/**
 * Stage 280 Memory Security.
 *
 * This bounded contract evaluates explicitly supplied architectural memory-security
 * evidence while preserving one exact Stage 279 Data Protection result as
 * authoritative upstream provenance.
 *
 * Existing constitutional Memory Authority, commitment, persistence, recall,
 * sensitivity, retention, and privacy contracts remain authoritative for their
 * respective responsibilities.
 *
 * MEMORY_SECURED != MEMORY_PERSISTED.
 * MEMORY_SECURED != MEMORY_ENCRYPTED.
 * MEMORY_SENSITIVITY != SECURITY_STAGE.
 * MEMORY_SENSITIVITY != PRIVACY_DISCLOSURE_POLICY.
 * RETENTION_CLASSIFICATION != RETENTION_ENFORCEMENT.
 * RETENTION_CLASSIFICATION != DELETION_EXECUTION.
 * MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.
 * MEMORY_COMMITMENT != MEMORY_PERSISTENCE.
 * RECALL_ELIGIBILITY != MEMORY_RECALL.
 * RECALL_ELIGIBILITY != DISCLOSURE_PERMISSION.
 * MEMORY_SECURITY_HARDENED != VERIFIED_OUTCOME.
 *
 * Stage 280 does not create another Memory Authority, classify or inspect raw
 * memory content, encrypt or decrypt memory, generate or manage keys, choose a
 * storage destination, persist or delete memory, enforce retention, perform
 * recall, authorize disclosure, execute anything, or implement Stage 281
 * Child/Guardian Security Audit.
 */
enum class DevilMemorySecurityStatus {
    HARDENED,
    NOT_HARDENED,
}

/**
 * Explicitly supplied Stage 280 architectural memory-security evidence.
 *
 * These values describe already-established architectural properties. They do
 * not contain logical-memory content, credentials, secrets, encryption keys,
 * persisted memory, or executable authority.
 */
data class DevilMemorySecurityEvidence(
    val dataProtection:
        DevilDataProtectionResult,
    val singleMemoryAuthorityRemainsAuthoritative: Boolean,
    val memorySensitivitySeparatedFromSecurityAndPrivacyDisclosure: Boolean,
    val retentionClassificationSeparatedFromEnforcementAndDeletion: Boolean,
    val memoryAuthorityApprovalSeparatedFromCommitmentAndPersistence: Boolean,
    val persistenceEligibilitySeparatedFromStorageSuccess: Boolean,
    val recallEligibilitySeparatedFromRecallAndDisclosurePermission: Boolean,
    val memoryPersistenceRequiresApprovedProtectedMechanism: Boolean,
) {
    fun isComplete(): Boolean =
        dataProtection.status ==
            DevilDataProtectionStatus.HARDENED &&
            dataProtection.evidence
                .capabilityAuthorizationHardening
                .evidence
                .sessionHardening
                .evidence
                .authenticationHardening
                .evidence
                .threatModel
                .coveredCategories
                .contains(
                    DevilThreatCategory.DATA_MEMORY_EXPOSURE,
                ) &&
            singleMemoryAuthorityRemainsAuthoritative &&
            memorySensitivitySeparatedFromSecurityAndPrivacyDisclosure &&
            retentionClassificationSeparatedFromEnforcementAndDeletion &&
            memoryAuthorityApprovalSeparatedFromCommitmentAndPersistence &&
            persistenceEligibilitySeparatedFromStorageSuccess &&
            recallEligibilitySeparatedFromRecallAndDisclosurePermission &&
            memoryPersistenceRequiresApprovedProtectedMechanism
}

/**
 * Bounded Stage 280 Memory Security result.
 *
 * HARDENED means only that every required Stage 280 architectural memory-security
 * property was explicitly supplied and the preserved Stage 275 threat model
 * covers data / memory exposure.
 *
 * HARDENED does not prove memory persistence, encryption, retention enforcement,
 * deletion, recall, disclosure permission, execution, or a verified outcome.
 */
@ConsistentCopyVisibility
data class DevilMemorySecurityResult private constructor(
    val status: DevilMemorySecurityStatus,
    val evidence: DevilMemorySecurityEvidence,
) {
    companion object {
        fun create(
            evidence: DevilMemorySecurityEvidence,
        ): DevilMemorySecurityResult =
            DevilMemorySecurityResult(
                status =
                    if (evidence.isComplete()) {
                        DevilMemorySecurityStatus.HARDENED
                    } else {
                        DevilMemorySecurityStatus.NOT_HARDENED
                    },
                evidence = evidence,
            )
    }
}

/**
 * Stage 280 bounded Memory Security coordinator.
 *
 * It evaluates explicitly supplied architectural memory-security evidence only.
 *
 * It does not:
 *
 * - create or replace Memory Authority;
 * - classify or inspect raw logical-memory content;
 * - change memory sensitivity or retention classifications;
 * - establish Memory Authority approval or commitment;
 * - encrypt or decrypt memory;
 * - generate, store, rotate, or revoke cryptographic keys;
 * - select or create a durable memory store;
 * - persist, delete, synchronize, replicate, or expose memory;
 * - enforce retention;
 * - perform memory recall;
 * - establish privacy-disclosure permission;
 * - create an ExecutionRequest or execute anything;
 * - establish Observation, Verification, Outcome, or storage success;
 * - implement Stage 281 Child/Guardian Security Audit.
 */
class DevilMemorySecurityCoordinator {
    fun evaluate(
        evidence: DevilMemorySecurityEvidence,
    ): DevilMemorySecurityResult =
        DevilMemorySecurityResult.create(
            evidence = evidence,
        )
}
