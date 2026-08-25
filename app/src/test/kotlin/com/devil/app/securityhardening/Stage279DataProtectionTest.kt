package com.devil.app.securityhardening

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 279 Data Protection governance tests.
 *
 * Stage 279 strengthens architectural data-protection boundaries only.
 * It must not implement encryption, persistence, disclosure, or Stage 280.
 */
class Stage279DataProtectionTest {

    @Test
    fun `complete supplied data protection evidence becomes hardened`() {
        val upstream = hardenedCapabilityAuthorizationResult()

        val evidence =
            completeEvidence(
                capabilityAuthorizationHardening = upstream,
            )

        val result =
            DevilDataProtectionCoordinator()
                .evaluate(evidence)

        assertEquals(
            DevilDataProtectionStatus.HARDENED,
            result.status,
        )
        assertSame(evidence, result.evidence)
        assertSame(
            upstream,
            result.evidence.capabilityAuthorizationHardening,
        )
    }

    @Test
    fun `non hardened Stage 278 result prevents hardened classification`() {
        val upstream =
            DevilCapabilityAuthorizationHardeningResult.create(
                evidence =
                    DevilCapabilityAuthorizationHardeningEvidence(
                        sessionHardening = hardenedSessionResult(),
                        constitutionalAuthorizationSeparatedFromCapabilityAuthorization = false,
                        capabilitySelectionCannotGrantAuthorization = true,
                        androidPermissionCannotGrantDevilAuthorization = true,
                        capabilityAvailabilityCannotGrantAuthorization = true,
                        capabilityReadinessCannotGrantAuthorization = true,
                        executionCapabilityCannotGrantAuthorization = true,
                        deniedOrDeferredAuthorizationCannotBeUpgradedDownstream = true,
                    ),
            )

        assertEquals(
            DevilCapabilityAuthorizationHardeningStatus.NOT_HARDENED,
            upstream.status,
        )

        val result =
            DevilDataProtectionCoordinator()
                .evaluate(
                    completeEvidence(
                        capabilityAuthorizationHardening = upstream,
                    ),
                )

        assertEquals(
            DevilDataProtectionStatus.NOT_HARDENED,
            result.status,
        )
    }

    @Test
    fun `missing any required data protection boundary prevents hardened classification`() {
        val upstream = hardenedCapabilityAuthorizationResult()

        val variants =
            listOf(
                completeEvidence(
                    capabilityAuthorizationHardening = upstream,
                    privacyClassificationGovernsSensitiveDataHandling = false,
                ),
                completeEvidence(
                    capabilityAuthorizationHardening = upstream,
                    sensitiveExposureFailsClosedWhereRequired = false,
                ),
                completeEvidence(
                    capabilityAuthorizationHardening = upstream,
                    privacyDisclosureTreatmentCannotTransmitData = false,
                ),
                completeEvidence(
                    capabilityAuthorizationHardening = upstream,
                    representationReductionCannotPersistOrTransmitProtectedContent = false,
                ),
                completeEvidence(
                    capabilityAuthorizationHardening = upstream,
                    durablePersistenceRequiresApprovedProtectedStore = false,
                ),
                completeEvidence(
                    capabilityAuthorizationHardening = upstream,
                    credentialsAndSecretsSeparatedFromOrdinaryApplicationData = false,
                ),
                completeEvidence(
                    capabilityAuthorizationHardening = upstream,
                    dataProtectionSeparatedFromMemorySecurity = false,
                ),
            )

        variants.forEach { evidence ->
            val result =
                DevilDataProtectionCoordinator()
                    .evaluate(evidence)

            assertEquals(
                DevilDataProtectionStatus.NOT_HARDENED,
                result.status,
            )
        }
    }

    @Test
    fun `Stage 279 preserves exact Stage 278 provenance`() {
        val upstream = hardenedCapabilityAuthorizationResult()

        val evidence =
            completeEvidence(
                capabilityAuthorizationHardening = upstream,
            )

        val result =
            DevilDataProtectionCoordinator()
                .evaluate(evidence)

        assertSame(
            upstream,
            evidence.capabilityAuthorizationHardening,
        )
        assertSame(
            upstream,
            result.evidence.capabilityAuthorizationHardening,
        )
        assertSame(evidence, result.evidence)
    }

    @Test
    fun `Stage 279 requires data memory exposure threat coverage`() {
        val threatModel =
            DevilFullThreatModelCoordinator()
                .evaluate(
                    evidence =
                        DevilThreatModelEvidence(
                            identityAuthenticationThreatsCovered = true,
                            sessionThreatsCovered = true,
                            authorizationThreatsCovered = true,
                            deviceTrustThreatsCovered = true,
                            untrustedInputThreatsCovered = true,
                            dataMemoryThreatsCovered = false,
                            capabilityExecutionThreatsCovered = true,
                        ),
                )

        val authenticationHardening =
            DevilAuthenticationHardeningCoordinator()
                .evaluate(
                    evidence =
                        DevilAuthenticationHardeningEvidence(
                            threatModel = threatModel,
                            wakePhraseSeparatedFromAuthentication = true,
                            identityResolutionSeparatedFromAuthentication = true,
                            genuineAuthenticatorRequired = true,
                            unavailableAuthenticatorFailsClosed = true,
                            authenticationRequestCannotEstablishSession = true,
                        ),
                )

        val sessionHardening =
            DevilSessionHardeningResult.create(
                evidence =
                    DevilSessionHardeningEvidence(
                        authenticationHardening = authenticationHardening,
                        nonActiveSessionsRejected = true,
                        validityWindowEnforced = true,
                        authoritativeObservationTimeRequired = true,
                        revokedSessionsInvalidated = true,
                        sessionValiditySeparatedFromAuthentication = true,
                        sessionValiditySeparatedFromAuthorization = true,
                    ),
            )

        val capabilityHardening =
            DevilCapabilityAuthorizationHardeningResult.create(
                evidence =
                    DevilCapabilityAuthorizationHardeningEvidence(
                        sessionHardening = sessionHardening,
                        constitutionalAuthorizationSeparatedFromCapabilityAuthorization = true,
                        capabilitySelectionCannotGrantAuthorization = true,
                        androidPermissionCannotGrantDevilAuthorization = true,
                        capabilityAvailabilityCannotGrantAuthorization = true,
                        capabilityReadinessCannotGrantAuthorization = true,
                        executionCapabilityCannotGrantAuthorization = true,
                        deniedOrDeferredAuthorizationCannotBeUpgradedDownstream = true,
                    ),
            )

        val result =
            DevilDataProtectionCoordinator()
                .evaluate(
                    completeEvidence(
                        capabilityAuthorizationHardening = capabilityHardening,
                    ),
                )

        assertFalse(
            threatModel.coveredCategories.contains(
                DevilThreatCategory.DATA_MEMORY_EXPOSURE,
            ),
        )
        assertEquals(
            DevilDataProtectionStatus.NOT_HARDENED,
            result.status,
        )
    }

    @Test
    fun `Stage 279 preserves data protection constitutional boundaries`() {
        val source = stage279Source()

        val boundaries =
            listOf(
                "DATA_PROTECTED != DATA_ENCRYPTED.",
                "DATA_PROTECTED != DATA_PERSISTED.",
                "DATA_PROTECTED != DISCLOSURE_AUTHORIZED.",
                "PRIVACY_ALLOWED != DEVIL_AUTHORIZATION.",
                "DISCLOSURE_TREATMENT != DISCLOSURE_PERFORMED.",
                "REPRESENTATION_REDUCED != DATA_TRANSMITTED.",
                "PERSISTABLE != PERSISTED.",
                "DATA_PROTECTION != MEMORY_SECURITY.",
                "DATA_PROTECTION_HARDENED != VERIFIED_OUTCOME.",
            )

        boundaries.forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 279 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 279 stops before Stage 280 Memory Security`() {
        val source = stage279Source()

        assertTrue(
            source.contains(
                "Stage 280 Memory Security",
            ),
        )
    }

    @Test
    fun `Stage 279 contains no operational data protection wiring`() {
        val source = stage279Source()

        val forbidden =
            listOf(
                "Cipher.getInstance",
                "KeyStore.getInstance",
                "SecretKeySpec",
                "EncryptedSharedPreferences",
                "SharedPreferences.Editor",
                "DataStoreFactory",
                "Room.databaseBuilder",
                "SQLiteDatabase",
                "FileOutputStream",
                "Files.write",
                "HttpURLConnection",
                "Socket(",
                "ExecutionRequest.create",
            )

        forbidden.forEach { value ->
            assertFalse(
                source.contains(value),
                "Stage 279 must not introduce operational data-protection wiring: $value",
            )
        }
    }

    private fun completeEvidence(
        capabilityAuthorizationHardening:
            DevilCapabilityAuthorizationHardeningResult,
        privacyClassificationGovernsSensitiveDataHandling: Boolean = true,
        sensitiveExposureFailsClosedWhereRequired: Boolean = true,
        privacyDisclosureTreatmentCannotTransmitData: Boolean = true,
        representationReductionCannotPersistOrTransmitProtectedContent: Boolean = true,
        durablePersistenceRequiresApprovedProtectedStore: Boolean = true,
        credentialsAndSecretsSeparatedFromOrdinaryApplicationData: Boolean = true,
        dataProtectionSeparatedFromMemorySecurity: Boolean = true,
    ): DevilDataProtectionEvidence =
        DevilDataProtectionEvidence(
            capabilityAuthorizationHardening =
                capabilityAuthorizationHardening,
            privacyClassificationGovernsSensitiveDataHandling =
                privacyClassificationGovernsSensitiveDataHandling,
            sensitiveExposureFailsClosedWhereRequired =
                sensitiveExposureFailsClosedWhereRequired,
            privacyDisclosureTreatmentCannotTransmitData =
                privacyDisclosureTreatmentCannotTransmitData,
            representationReductionCannotPersistOrTransmitProtectedContent =
                representationReductionCannotPersistOrTransmitProtectedContent,
            durablePersistenceRequiresApprovedProtectedStore =
                durablePersistenceRequiresApprovedProtectedStore,
            credentialsAndSecretsSeparatedFromOrdinaryApplicationData =
                credentialsAndSecretsSeparatedFromOrdinaryApplicationData,
            dataProtectionSeparatedFromMemorySecurity =
                dataProtectionSeparatedFromMemorySecurity,
        )

    private fun hardenedCapabilityAuthorizationResult():
        DevilCapabilityAuthorizationHardeningResult =
        DevilCapabilityAuthorizationHardeningCoordinator()
            .evaluate(
                evidence =
                    DevilCapabilityAuthorizationHardeningEvidence(
                        sessionHardening = hardenedSessionResult(),
                        constitutionalAuthorizationSeparatedFromCapabilityAuthorization = true,
                        capabilitySelectionCannotGrantAuthorization = true,
                        androidPermissionCannotGrantDevilAuthorization = true,
                        capabilityAvailabilityCannotGrantAuthorization = true,
                        capabilityReadinessCannotGrantAuthorization = true,
                        executionCapabilityCannotGrantAuthorization = true,
                        deniedOrDeferredAuthorizationCannotBeUpgradedDownstream = true,
                    ),
            )

    private fun hardenedSessionResult(): DevilSessionHardeningResult =
        DevilSessionHardeningCoordinator()
            .evaluate(
                evidence =
                    DevilSessionHardeningEvidence(
                        authenticationHardening =
                            hardenedAuthenticationResult(),
                        nonActiveSessionsRejected = true,
                        validityWindowEnforced = true,
                        authoritativeObservationTimeRequired = true,
                        revokedSessionsInvalidated = true,
                        sessionValiditySeparatedFromAuthentication = true,
                        sessionValiditySeparatedFromAuthorization = true,
                    ),
            )

    private fun hardenedAuthenticationResult():
        DevilAuthenticationHardeningResult =
        DevilAuthenticationHardeningCoordinator()
            .evaluate(
                evidence =
                    DevilAuthenticationHardeningEvidence(
                        threatModel = completeThreatModel(),
                        wakePhraseSeparatedFromAuthentication = true,
                        identityResolutionSeparatedFromAuthentication = true,
                        genuineAuthenticatorRequired = true,
                        unavailableAuthenticatorFailsClosed = true,
                        authenticationRequestCannotEstablishSession = true,
                    ),
            )

    private fun completeThreatModel(): DevilThreatModelResult =
        DevilFullThreatModelCoordinator()
            .evaluate(
                evidence =
                    DevilThreatModelEvidence(
                        identityAuthenticationThreatsCovered = true,
                        sessionThreatsCovered = true,
                        authorizationThreatsCovered = true,
                        deviceTrustThreatsCovered = true,
                        untrustedInputThreatsCovered = true,
                        dataMemoryThreatsCovered = true,
                        capabilityExecutionThreatsCovered = true,
                    ),
            )

    private fun stage279Source(): String {
        val candidates =
            listOf(
                File(
                    "app/src/main/kotlin/com/devil/app/securityhardening/DevilDataProtection.kt",
                ),
                File(
                    "src/main/kotlin/com/devil/app/securityhardening/DevilDataProtection.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 279 source from: ${candidates.joinToString()}",
            )
    }
}
