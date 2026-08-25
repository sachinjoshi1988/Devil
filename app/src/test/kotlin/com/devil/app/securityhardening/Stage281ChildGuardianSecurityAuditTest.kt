package com.devil.app.securityhardening

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 281 Child/Guardian Security Audit governance tests.
 *
 * Stage 281 audits explicitly supplied architectural child/guardian security
 * boundaries only.
 *
 * It must not infer child status, authenticate anyone, manufacture guardian
 * authority or approval, authorize execution, persist child/guardian state,
 * create another authority/intelligence, or begin Stage 282.
 */
class Stage281ChildGuardianSecurityAuditTest {

    @Test
    fun `complete supplied child guardian security audit evidence becomes audited`() {
        val memorySecurity = hardenedMemorySecurityResult()
        val evidence =
            completeEvidence(
                memorySecurity = memorySecurity,
            )

        val result =
            DevilChildGuardianSecurityAuditCoordinator()
                .evaluate(
                    evidence = evidence,
                )

        assertEquals(
            DevilChildGuardianSecurityAuditStatus.AUDITED,
            result.status,
        )
        assertSame(
            evidence,
            result.evidence,
        )
        assertSame(
            memorySecurity,
            result.evidence.memorySecurity,
        )
    }

    @Test
    fun `non hardened Stage 280 result prevents audited classification`() {
        val hardenedMemorySecurity = hardenedMemorySecurityResult()

        val nonHardenedMemorySecurity =
            DevilMemorySecurityResult.create(
                evidence =
                    hardenedMemorySecurity.evidence.copy(
                        singleMemoryAuthorityRemainsAuthoritative = false,
                    ),
            )

        assertEquals(
            DevilMemorySecurityStatus.NOT_HARDENED,
            nonHardenedMemorySecurity.status,
        )

        val result =
            DevilChildGuardianSecurityAuditCoordinator()
                .evaluate(
                    evidence =
                        completeEvidence(
                            memorySecurity = nonHardenedMemorySecurity,
                        ),
                )

        assertEquals(
            DevilChildGuardianSecurityAuditStatus.NOT_AUDITED,
            result.status,
        )
    }

    @Test
    fun `missing any required child guardian security boundary prevents audited classification`() {
        val memorySecurity = hardenedMemorySecurityResult()

        val variants =
            listOf(
                completeEvidence(
                    memorySecurity = memorySecurity,
                    childClassificationSeparatedFromAuthentication = false,
                ),
                completeEvidence(
                    memorySecurity = memorySecurity,
                    childClassificationSeparatedFromGuardianAuthority = false,
                ),
                completeEvidence(
                    memorySecurity = memorySecurity,
                    guardianAuthoritySeparatedFromGuardianApproval = false,
                ),
                completeEvidence(
                    memorySecurity = memorySecurity,
                    guardianApprovalSeparatedFromDevilAuthorization = false,
                ),
                completeEvidence(
                    memorySecurity = memorySecurity,
                    childPolicySatisfactionSeparatedFromAuthorizationAndExecution = false,
                ),
                completeEvidence(
                    memorySecurity = memorySecurity,
                    guardianContextSeparatedFromOwnerModeAndProtectedPrivacyContext = false,
                ),
                completeEvidence(
                    memorySecurity = memorySecurity,
                    childPrivacyBoundarySeparatedFromPrivacyAuthorizationAndDisclosure = false,
                ),
                completeEvidence(
                    memorySecurity = memorySecurity,
                    noChildSpecificBrainSecurityOrMemoryAuthority = false,
                ),
            )

        variants.forEach { evidence ->
            val result =
                DevilChildGuardianSecurityAuditCoordinator()
                    .evaluate(
                        evidence = evidence,
                    )

            assertEquals(
                DevilChildGuardianSecurityAuditStatus.NOT_AUDITED,
                result.status,
            )
        }
    }

    @Test
    fun `Stage 281 preserves exact Stage 280 provenance`() {
        val memorySecurity = hardenedMemorySecurityResult()
        val evidence =
            completeEvidence(
                memorySecurity = memorySecurity,
            )

        val result =
            DevilChildGuardianSecurityAuditCoordinator()
                .evaluate(
                    evidence = evidence,
                )

        assertSame(
            memorySecurity,
            evidence.memorySecurity,
        )
        assertSame(
            memorySecurity,
            result.evidence.memorySecurity,
        )
        assertSame(
            evidence,
            result.evidence,
        )
    }

    @Test
    fun `Stage 281 requires identity authentication authorization and data memory threat coverage`() {
        val threatVariants =
            listOf(
                completeThreatModelEvidence().copy(
                    identityAuthenticationThreatsCovered = false,
                ),
                completeThreatModelEvidence().copy(
                    authorizationThreatsCovered = false,
                ),
                completeThreatModelEvidence().copy(
                    dataMemoryThreatsCovered = false,
                ),
            )

        threatVariants.forEach { threatEvidence ->
            val threatModel =
                DevilFullThreatModelCoordinator()
                    .evaluate(
                        evidence = threatEvidence,
                    )

            assertEquals(
                DevilThreatModelStatus.INCOMPLETE,
                threatModel.status,
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

            val capabilityAuthorization =
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

            val dataProtection =
                DevilDataProtectionResult.create(
                    evidence =
                        DevilDataProtectionEvidence(
                            capabilityAuthorizationHardening = capabilityAuthorization,
                            privacyClassificationGovernsSensitiveDataHandling = true,
                            sensitiveExposureFailsClosedWhereRequired = true,
                            privacyDisclosureTreatmentCannotTransmitData = true,
                            representationReductionCannotPersistOrTransmitProtectedContent = true,
                            durablePersistenceRequiresApprovedProtectedStore = true,
                            credentialsAndSecretsSeparatedFromOrdinaryApplicationData = true,
                            dataProtectionSeparatedFromMemorySecurity = true,
                        ),
                )

            val memorySecurity =
                DevilMemorySecurityResult.create(
                    evidence =
                        DevilMemorySecurityEvidence(
                            dataProtection = dataProtection,
                            singleMemoryAuthorityRemainsAuthoritative = true,
                            memorySensitivitySeparatedFromSecurityAndPrivacyDisclosure = true,
                            retentionClassificationSeparatedFromEnforcementAndDeletion = true,
                            memoryAuthorityApprovalSeparatedFromCommitmentAndPersistence = true,
                            persistenceEligibilitySeparatedFromStorageSuccess = true,
                            recallEligibilitySeparatedFromRecallAndDisclosurePermission = true,
                            memoryPersistenceRequiresApprovedProtectedMechanism = true,
                        ),
                )

            val result =
                DevilChildGuardianSecurityAuditCoordinator()
                    .evaluate(
                        evidence =
                            completeEvidence(
                                memorySecurity = memorySecurity,
                            ),
                    )

            assertEquals(
                DevilChildGuardianSecurityAuditStatus.NOT_AUDITED,
                result.status,
            )
        }
    }

    @Test
    fun `Stage 281 preserves child guardian constitutional boundaries`() {
        val source = stage281Source()

        val boundaries =
            listOf(
                "CHILD_CLASSIFICATION != AUTHENTICATION.",
                "CHILD_CLASSIFICATION != GUARDIAN_AUTHORITY.",
                "GUARDIAN_AUTHORITY != GUARDIAN_APPROVAL.",
                "GUARDIAN_APPROVAL != DEVIL_AUTHORIZATION.",
                "CHILD_POLICY_SATISFIED != DEVIL_AUTHORIZATION.",
                "CHILD_POLICY_SATISFIED != EXECUTION_APPROVAL.",
                "GUARDIAN_CONTEXT != OWNER_MODE.",
                "GUARDIAN_CONTEXT != PROTECTED_PRIVACY_CONTEXT.",
                "CHILD_PRIVACY_BOUNDARY != PRIVACY_AUTHORIZATION.",
                "PRIVACY_ALLOWED != DEVIL_AUTHORIZATION.",
                "CHILD_GUARDIAN_AUDITED != VERIFIED_OUTCOME.",
            )

        boundaries.forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 281 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 281 stops before Stage 282`() {
        val source = stage281Source()

        assertTrue(
            source.contains(
                "implement Stage 282",
            ),
        )
    }

    @Test
    fun `Stage 281 contains no operational child guardian security wiring`() {
        val source = stage281Source()

        val forbidden =
            listOf(
                "ChildGuardianContextCoordinator(",
                "ChildPolicyCoordinator(",
                "GuardianApprovalCoordinator(",
                "ChildPolicySatisfactionCoordinator(",
                "ChildGuardianRuntimeCoordinator(",
                "ChildPrivacyBoundaryCoordinator(",
                "PrivacyProtectedContextResolver(",
                "DefaultAuthorizationAuthority(",
                "DefaultMemoryAuthority(",
                "ExecutionRequest.create(",
                "SharedPreferences",
                "DataStore",
                "Room.databaseBuilder",
                "SQLiteDatabase",
            )

        forbidden.forEach { value ->
            assertFalse(
                source.contains(value),
                "Stage 281 must not introduce operational child/guardian security wiring: $value",
            )
        }
    }

    private fun completeEvidence(
        memorySecurity: DevilMemorySecurityResult,
        childClassificationSeparatedFromAuthentication: Boolean = true,
        childClassificationSeparatedFromGuardianAuthority: Boolean = true,
        guardianAuthoritySeparatedFromGuardianApproval: Boolean = true,
        guardianApprovalSeparatedFromDevilAuthorization: Boolean = true,
        childPolicySatisfactionSeparatedFromAuthorizationAndExecution: Boolean = true,
        guardianContextSeparatedFromOwnerModeAndProtectedPrivacyContext: Boolean = true,
        childPrivacyBoundarySeparatedFromPrivacyAuthorizationAndDisclosure: Boolean = true,
        noChildSpecificBrainSecurityOrMemoryAuthority: Boolean = true,
    ): DevilChildGuardianSecurityAuditEvidence =
        DevilChildGuardianSecurityAuditEvidence(
            memorySecurity = memorySecurity,
            childClassificationSeparatedFromAuthentication =
                childClassificationSeparatedFromAuthentication,
            childClassificationSeparatedFromGuardianAuthority =
                childClassificationSeparatedFromGuardianAuthority,
            guardianAuthoritySeparatedFromGuardianApproval =
                guardianAuthoritySeparatedFromGuardianApproval,
            guardianApprovalSeparatedFromDevilAuthorization =
                guardianApprovalSeparatedFromDevilAuthorization,
            childPolicySatisfactionSeparatedFromAuthorizationAndExecution =
                childPolicySatisfactionSeparatedFromAuthorizationAndExecution,
            guardianContextSeparatedFromOwnerModeAndProtectedPrivacyContext =
                guardianContextSeparatedFromOwnerModeAndProtectedPrivacyContext,
            childPrivacyBoundarySeparatedFromPrivacyAuthorizationAndDisclosure =
                childPrivacyBoundarySeparatedFromPrivacyAuthorizationAndDisclosure,
            noChildSpecificBrainSecurityOrMemoryAuthority =
                noChildSpecificBrainSecurityOrMemoryAuthority,
        )

    private fun hardenedMemorySecurityResult(): DevilMemorySecurityResult {
        val threatModel =
            DevilFullThreatModelCoordinator()
                .evaluate(
                    evidence = completeThreatModelEvidence(),
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

        val capabilityAuthorization =
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

        val dataProtection =
            DevilDataProtectionResult.create(
                evidence =
                    DevilDataProtectionEvidence(
                        capabilityAuthorizationHardening = capabilityAuthorization,
                        privacyClassificationGovernsSensitiveDataHandling = true,
                        sensitiveExposureFailsClosedWhereRequired = true,
                        privacyDisclosureTreatmentCannotTransmitData = true,
                        representationReductionCannotPersistOrTransmitProtectedContent = true,
                        durablePersistenceRequiresApprovedProtectedStore = true,
                        credentialsAndSecretsSeparatedFromOrdinaryApplicationData = true,
                        dataProtectionSeparatedFromMemorySecurity = true,
                    ),
            )

        return DevilMemorySecurityResult.create(
            evidence =
                DevilMemorySecurityEvidence(
                    dataProtection = dataProtection,
                    singleMemoryAuthorityRemainsAuthoritative = true,
                    memorySensitivitySeparatedFromSecurityAndPrivacyDisclosure = true,
                    retentionClassificationSeparatedFromEnforcementAndDeletion = true,
                    memoryAuthorityApprovalSeparatedFromCommitmentAndPersistence = true,
                    persistenceEligibilitySeparatedFromStorageSuccess = true,
                    recallEligibilitySeparatedFromRecallAndDisclosurePermission = true,
                    memoryPersistenceRequiresApprovedProtectedMechanism = true,
                ),
        )
    }

    private fun completeThreatModelEvidence(): DevilThreatModelEvidence =
        DevilThreatModelEvidence(
            identityAuthenticationThreatsCovered = true,
            sessionThreatsCovered = true,
            authorizationThreatsCovered = true,
            deviceTrustThreatsCovered = true,
            untrustedInputThreatsCovered = true,
            dataMemoryThreatsCovered = true,
            capabilityExecutionThreatsCovered = true,
        )

    private fun stage281Source(): String {
        val candidates =
            listOf(
                File(
                    "app/src/main/kotlin/com/devil/app/securityhardening/DevilChildGuardianSecurityAudit.kt",
                ),
                File(
                    "src/main/kotlin/com/devil/app/securityhardening/DevilChildGuardianSecurityAudit.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 281 source from: ${candidates.joinToString()}",
            )
    }
}
