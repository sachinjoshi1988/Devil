package com.devil.app.securityhardening

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 280 Memory Security governance tests.
 *
 * Stage 280 strengthens architectural memory-security boundaries only.
 * It must not persist, encrypt, delete, recall, or expose memory or begin Stage 281.
 */
class Stage280MemorySecurityTest {

    @Test
    fun `complete supplied memory security evidence becomes hardened`() {
        val dataProtection = hardenedDataProtectionResult()
        val evidence =
            completeEvidence(
                dataProtection = dataProtection,
            )

        val result =
            DevilMemorySecurityCoordinator()
                .evaluate(evidence)

        assertEquals(
            DevilMemorySecurityStatus.HARDENED,
            result.status,
        )
        assertSame(evidence, result.evidence)
        assertSame(
            dataProtection,
            result.evidence.dataProtection,
        )
    }

    @Test
    fun `non hardened Stage 279 result prevents hardened classification`() {
        val dataProtection =
            DevilDataProtectionResult.create(
                evidence =
                    completeDataProtectionEvidence(
                        sensitiveExposureFailsClosedWhereRequired = false,
                    ),
            )

        assertEquals(
            DevilDataProtectionStatus.NOT_HARDENED,
            dataProtection.status,
        )

        val result =
            DevilMemorySecurityCoordinator()
                .evaluate(
                    completeEvidence(
                        dataProtection = dataProtection,
                    ),
                )

        assertEquals(
            DevilMemorySecurityStatus.NOT_HARDENED,
            result.status,
        )
    }

    @Test
    fun `missing any required memory security boundary prevents hardened classification`() {
        val dataProtection = hardenedDataProtectionResult()

        val incompleteEvidence =
            listOf(
                completeEvidence(
                    dataProtection = dataProtection,
                    singleMemoryAuthorityRemainsAuthoritative = false,
                ),
                completeEvidence(
                    dataProtection = dataProtection,
                    memorySensitivitySeparatedFromSecurityAndPrivacyDisclosure = false,
                ),
                completeEvidence(
                    dataProtection = dataProtection,
                    retentionClassificationSeparatedFromEnforcementAndDeletion = false,
                ),
                completeEvidence(
                    dataProtection = dataProtection,
                    memoryAuthorityApprovalSeparatedFromCommitmentAndPersistence = false,
                ),
                completeEvidence(
                    dataProtection = dataProtection,
                    persistenceEligibilitySeparatedFromStorageSuccess = false,
                ),
                completeEvidence(
                    dataProtection = dataProtection,
                    recallEligibilitySeparatedFromRecallAndDisclosurePermission = false,
                ),
                completeEvidence(
                    dataProtection = dataProtection,
                    memoryPersistenceRequiresApprovedProtectedMechanism = false,
                ),
            )

        incompleteEvidence.forEach { evidence ->
            assertFalse(evidence.isComplete())

            assertEquals(
                DevilMemorySecurityStatus.NOT_HARDENED,
                DevilMemorySecurityCoordinator()
                    .evaluate(evidence)
                    .status,
            )
        }
    }

    @Test
    fun `Stage 280 preserves exact Stage 279 provenance`() {
        val dataProtection = hardenedDataProtectionResult()
        val evidence =
            completeEvidence(
                dataProtection = dataProtection,
            )

        val result =
            DevilMemorySecurityCoordinator()
                .evaluate(evidence)

        assertSame(
            dataProtection,
            evidence.dataProtection,
        )
        assertSame(
            dataProtection,
            result.evidence.dataProtection,
        )
        assertSame(
            evidence,
            result.evidence,
        )
    }

    @Test
    fun `Stage 280 requires data memory exposure threat coverage`() {
        val dataProtection =
            hardenedDataProtectionResult(
                dataMemoryThreatsCovered = false,
            )

        assertEquals(
            DevilDataProtectionStatus.NOT_HARDENED,
            dataProtection.status,
        )

        assertFalse(
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
                ),
        )

        val result =
            DevilMemorySecurityCoordinator()
                .evaluate(
                    completeEvidence(
                        dataProtection = dataProtection,
                    ),
                )

        assertEquals(
            DevilMemorySecurityStatus.NOT_HARDENED,
            result.status,
        )
    }

    @Test
    fun `Stage 280 preserves memory security constitutional boundaries`() {
        val source = stage280Source()

        val boundaries =
            listOf(
                "MEMORY_SECURED != MEMORY_PERSISTED.",
                "MEMORY_SECURED != MEMORY_ENCRYPTED.",
                "MEMORY_SENSITIVITY != SECURITY_STAGE.",
                "MEMORY_SENSITIVITY != PRIVACY_DISCLOSURE_POLICY.",
                "RETENTION_CLASSIFICATION != RETENTION_ENFORCEMENT.",
                "RETENTION_CLASSIFICATION != DELETION_EXECUTION.",
                "MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.",
                "MEMORY_COMMITMENT != MEMORY_PERSISTENCE.",
                "RECALL_ELIGIBILITY != MEMORY_RECALL.",
                "RECALL_ELIGIBILITY != DISCLOSURE_PERMISSION.",
                "MEMORY_SECURITY_HARDENED != VERIFIED_OUTCOME.",
            )

        boundaries.forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 280 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 280 stops before Stage 281 Child Guardian Security Audit`() {
        assertTrue(
            stage280Source().contains(
                "Stage 281 Child/Guardian Security Audit",
            ),
        )
    }

    @Test
    fun `Stage 280 contains no operational memory security wiring`() {
        val source = stage280Source()

        val forbidden =
            listOf(
                "SharedPreferences",
                "DataStore",
                "SQLiteDatabase",
                "RoomDatabase",
                "Cipher.getInstance",
                "KeyStore.getInstance",
                "SecretKey",
                "FileOutputStream",
                "deleteRecursively(",
                "DefaultMemoryAuthority(",
                "DefaultMemoryPersistenceAuthority(",
                "DefaultAndroidMemoryPersistenceStore(",
            )

        forbidden.forEach { value ->
            assertFalse(
                source.contains(value),
                "Stage 280 must not introduce operational memory-security wiring: $value",
            )
        }
    }

    private fun completeEvidence(
        dataProtection: DevilDataProtectionResult,
        singleMemoryAuthorityRemainsAuthoritative: Boolean = true,
        memorySensitivitySeparatedFromSecurityAndPrivacyDisclosure: Boolean = true,
        retentionClassificationSeparatedFromEnforcementAndDeletion: Boolean = true,
        memoryAuthorityApprovalSeparatedFromCommitmentAndPersistence: Boolean = true,
        persistenceEligibilitySeparatedFromStorageSuccess: Boolean = true,
        recallEligibilitySeparatedFromRecallAndDisclosurePermission: Boolean = true,
        memoryPersistenceRequiresApprovedProtectedMechanism: Boolean = true,
    ): DevilMemorySecurityEvidence =
        DevilMemorySecurityEvidence(
            dataProtection = dataProtection,
            singleMemoryAuthorityRemainsAuthoritative =
                singleMemoryAuthorityRemainsAuthoritative,
            memorySensitivitySeparatedFromSecurityAndPrivacyDisclosure =
                memorySensitivitySeparatedFromSecurityAndPrivacyDisclosure,
            retentionClassificationSeparatedFromEnforcementAndDeletion =
                retentionClassificationSeparatedFromEnforcementAndDeletion,
            memoryAuthorityApprovalSeparatedFromCommitmentAndPersistence =
                memoryAuthorityApprovalSeparatedFromCommitmentAndPersistence,
            persistenceEligibilitySeparatedFromStorageSuccess =
                persistenceEligibilitySeparatedFromStorageSuccess,
            recallEligibilitySeparatedFromRecallAndDisclosurePermission =
                recallEligibilitySeparatedFromRecallAndDisclosurePermission,
            memoryPersistenceRequiresApprovedProtectedMechanism =
                memoryPersistenceRequiresApprovedProtectedMechanism,
        )

    private fun hardenedDataProtectionResult(
        dataMemoryThreatsCovered: Boolean = true,
    ): DevilDataProtectionResult =
        DevilDataProtectionCoordinator()
            .evaluate(
                completeDataProtectionEvidence(
                    dataMemoryThreatsCovered = dataMemoryThreatsCovered,
                ),
            )

    private fun completeDataProtectionEvidence(
        dataMemoryThreatsCovered: Boolean = true,
        sensitiveExposureFailsClosedWhereRequired: Boolean = true,
    ): DevilDataProtectionEvidence =
        DevilDataProtectionEvidence(
            capabilityAuthorizationHardening =
                hardenedCapabilityAuthorizationResult(
                    dataMemoryThreatsCovered =
                        dataMemoryThreatsCovered,
                ),
            privacyClassificationGovernsSensitiveDataHandling = true,
            sensitiveExposureFailsClosedWhereRequired =
                sensitiveExposureFailsClosedWhereRequired,
            privacyDisclosureTreatmentCannotTransmitData = true,
            representationReductionCannotPersistOrTransmitProtectedContent = true,
            durablePersistenceRequiresApprovedProtectedStore = true,
            credentialsAndSecretsSeparatedFromOrdinaryApplicationData = true,
            dataProtectionSeparatedFromMemorySecurity = true,
        )

    private fun hardenedCapabilityAuthorizationResult(
        dataMemoryThreatsCovered: Boolean,
    ): DevilCapabilityAuthorizationHardeningResult =
        DevilCapabilityAuthorizationHardeningCoordinator()
            .evaluate(
                evidence =
                    DevilCapabilityAuthorizationHardeningEvidence(
                        sessionHardening =
                            hardenedSessionResult(
                                dataMemoryThreatsCovered =
                                    dataMemoryThreatsCovered,
                            ),
                        constitutionalAuthorizationSeparatedFromCapabilityAuthorization = true,
                        capabilitySelectionCannotGrantAuthorization = true,
                        androidPermissionCannotGrantDevilAuthorization = true,
                        capabilityAvailabilityCannotGrantAuthorization = true,
                        capabilityReadinessCannotGrantAuthorization = true,
                        executionCapabilityCannotGrantAuthorization = true,
                        deniedOrDeferredAuthorizationCannotBeUpgradedDownstream = true,
                    ),
            )

    private fun hardenedSessionResult(
        dataMemoryThreatsCovered: Boolean,
    ): DevilSessionHardeningResult =
        DevilSessionHardeningCoordinator()
            .evaluate(
                evidence =
                    DevilSessionHardeningEvidence(
                        authenticationHardening =
                            hardenedAuthenticationResult(
                                dataMemoryThreatsCovered =
                                    dataMemoryThreatsCovered,
                            ),
                        nonActiveSessionsRejected = true,
                        validityWindowEnforced = true,
                        authoritativeObservationTimeRequired = true,
                        revokedSessionsInvalidated = true,
                        sessionValiditySeparatedFromAuthentication = true,
                        sessionValiditySeparatedFromAuthorization = true,
                    ),
            )

    private fun hardenedAuthenticationResult(
        dataMemoryThreatsCovered: Boolean,
    ): DevilAuthenticationHardeningResult =
        DevilAuthenticationHardeningCoordinator()
            .evaluate(
                evidence =
                    DevilAuthenticationHardeningEvidence(
                        threatModel =
                            completeThreatModel(
                                dataMemoryThreatsCovered =
                                    dataMemoryThreatsCovered,
                            ),
                        wakePhraseSeparatedFromAuthentication = true,
                        identityResolutionSeparatedFromAuthentication = true,
                        genuineAuthenticatorRequired = true,
                        unavailableAuthenticatorFailsClosed = true,
                        authenticationRequestCannotEstablishSession = true,
                    ),
            )

    private fun completeThreatModel(
        dataMemoryThreatsCovered: Boolean,
    ): DevilThreatModelResult =
        DevilFullThreatModelCoordinator()
            .evaluate(
                evidence =
                    DevilThreatModelEvidence(
                        identityAuthenticationThreatsCovered = true,
                        sessionThreatsCovered = true,
                        authorizationThreatsCovered = true,
                        deviceTrustThreatsCovered = true,
                        untrustedInputThreatsCovered = true,
                        dataMemoryThreatsCovered = dataMemoryThreatsCovered,
                        capabilityExecutionThreatsCovered = true,
                    ),
            )

    private fun stage280Source(): String {
        val candidates =
            listOf(
                File(
                    "app/src/main/kotlin/com/devil/app/securityhardening/DevilMemorySecurity.kt",
                ),
                File(
                    "src/main/kotlin/com/devil/app/securityhardening/DevilMemorySecurity.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 280 source from: ${candidates.joinToString()}",
            )
    }
}
