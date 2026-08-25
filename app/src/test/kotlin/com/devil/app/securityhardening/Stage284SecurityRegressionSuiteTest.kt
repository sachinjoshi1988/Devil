package com.devil.app.securityhardening

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 284 Security Regression Suite governance tests.
 *
 * Stage 284 evaluates explicitly supplied architectural regression coverage
 * while preserving the exact Stage 283 Prompt/Model Attack Resistance result.
 *
 * It must not perform runtime security operations, fabricate successful
 * constitutional Verification, execute anything, or begin Stage 285.
 */
class Stage284SecurityRegressionSuiteTest {

    @Test
    fun `complete supplied security regression evidence passes`() {
        val promptModelAttackResistance =
            completePromptModelAttackResistance()

        val evidence =
            completeEvidence(
                promptModelAttackResistance =
                    promptModelAttackResistance,
            )

        val result =
            DevilSecurityRegressionSuiteCoordinator()
                .evaluate(evidence)

        assertEquals(
            DevilSecurityRegressionSuiteStatus.PASSED,
            result.status,
        )
        assertSame(evidence, result.evidence)
        assertSame(
            promptModelAttackResistance,
            result.evidence.promptModelAttackResistance,
        )
    }

    @Test
    fun `non hardened Stage 283 result prevents passed classification`() {
        val stage283 =
            DevilPromptModelAttackResistanceResult.create(
                evidence =
                    completePromptModelEvidence(
                        financeLegalSecurityAudit =
                            completeFinanceLegalSecurityAudit(),
                    ).copy(
                        externalContentCannotBecomeDevilInstruction =
                            false,
                    ),
            )

        assertEquals(
            DevilPromptModelAttackResistanceStatus.NOT_HARDENED,
            stage283.status,
        )

        val result =
            DevilSecurityRegressionSuiteCoordinator()
                .evaluate(
                    completeEvidence(
                        promptModelAttackResistance = stage283,
                    ),
                )

        assertEquals(
            DevilSecurityRegressionSuiteStatus.FAILED,
            result.status,
        )
        assertSame(stage283, result.evidence.promptModelAttackResistance)
    }

    @Test
    fun `missing any required security regression coverage prevents passed classification`() {
        val stage283 =
            completePromptModelAttackResistance()

        val complete =
            completeEvidence(
                promptModelAttackResistance = stage283,
            )

        val incompleteEvidence =
            listOf(
                complete.copy(
                    fullThreatModelRegressionCovered = false,
                ),
                complete.copy(
                    authenticationHardeningRegressionCovered = false,
                ),
                complete.copy(
                    sessionHardeningRegressionCovered = false,
                ),
                complete.copy(
                    capabilityAuthorizationHardeningRegressionCovered =
                        false,
                ),
                complete.copy(
                    dataProtectionRegressionCovered = false,
                ),
                complete.copy(
                    memorySecurityRegressionCovered = false,
                ),
                complete.copy(
                    childGuardianSecurityAuditRegressionCovered = false,
                ),
                complete.copy(
                    financeLegalSecurityAuditRegressionCovered = false,
                ),
                complete.copy(
                    promptModelAttackResistanceRegressionCovered = false,
                ),
                complete.copy(
                    constitutionalSecurityBoundariesRegressionCovered =
                        false,
                ),
            )

        incompleteEvidence.forEach { evidence ->
            val result =
                DevilSecurityRegressionSuiteCoordinator()
                    .evaluate(evidence)

            assertEquals(
                DevilSecurityRegressionSuiteStatus.FAILED,
                result.status,
                "Missing Stage 284 regression coverage must fail.",
            )
        }
    }

    @Test
    fun `Stage 284 preserves exact Stage 283 and upstream Phase T provenance`() {
        val threatModel = threatModel()
        val stage283 =
            completePromptModelAttackResistance(
                threatModel = threatModel,
            )

        val result =
            DevilSecurityRegressionSuiteCoordinator()
                .evaluate(
                    completeEvidence(
                        promptModelAttackResistance = stage283,
                    ),
                )

        assertSame(
            stage283,
            result.evidence.promptModelAttackResistance,
        )

        assertSame(
            stage283.evidence.financeLegalSecurityAudit,
            result.evidence
                .promptModelAttackResistance
                .evidence
                .financeLegalSecurityAudit,
        )

        assertSame(
            stage283.evidence
                .financeLegalSecurityAudit
                .evidence
                .childGuardianSecurityAudit,
            result.evidence
                .promptModelAttackResistance
                .evidence
                .financeLegalSecurityAudit
                .evidence
                .childGuardianSecurityAudit,
        )

        assertSame(
            threatModel,
            result.evidence
                .promptModelAttackResistance
                .evidence
                .financeLegalSecurityAudit
                .evidence
                .childGuardianSecurityAudit
                .evidence
                .memorySecurity
                .evidence
                .dataProtection
                .evidence
                .capabilityAuthorizationHardening
                .evidence
                .sessionHardening
                .evidence
                .authenticationHardening
                .evidence
                .threatModel,
        )
    }

    @Test
    fun `Stage 284 requires complete preserved Stage 275 threat model`() {
        val incompleteThreatModel =
            threatModel(
                deviceTrustThreatsCovered = false,
            )

        val stage283 =
            completePromptModelAttackResistance(
                threatModel = incompleteThreatModel,
            )

        assertEquals(
            DevilPromptModelAttackResistanceStatus.NOT_HARDENED,
            stage283.status,
        )

        val result =
            DevilSecurityRegressionSuiteCoordinator()
                .evaluate(
                    completeEvidence(
                        promptModelAttackResistance = stage283,
                    ),
                )

        assertEquals(
            DevilSecurityRegressionSuiteStatus.FAILED,
            result.status,
        )
    }

    @Test
    fun `Stage 284 preserves regression and constitutional boundaries`() {
        val source = stage284Source()

        val requiredBoundaries =
            listOf(
                "SECURITY_REGRESSION_COVERED != ATTACK_PREVENTED.",
                "SECURITY_REGRESSION_COVERED != SECURITY_INCIDENT_ABSENT.",
                "SECURITY_REGRESSION_COVERED != CONSTITUTIONAL_VERIFICATION.",
                "SECURITY_REGRESSION_COVERED != EXECUTION_AUTHORIZATION.",
                "SECURITY_REGRESSION_COVERED != VERIFIED_OUTCOME.",
                "TEST_COVERAGE != RUNTIME_SECURITY_ENFORCEMENT.",
                "REGRESSION_SUITE != FINAL_SECURITY_REVIEW.",
            )

        requiredBoundaries.forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 284 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 284 stops before Stage 285 Final Security Review`() {
        val source = stage284Source()

        assertTrue(
            source.contains(
                "implement Stage 285 Final Security Review",
            ) ||
                source.contains(
                    "perform Stage 285 Final Security Review",
                ),
        )
    }

    @Test
    fun `Stage 284 contains no operational security regression wiring`() {
        val source = stage284Source()

        val forbiddenOperationalWiring =
            listOf(
                "Runtime.getRuntime().exec",
                "ProcessBuilder(",
                "HttpURLConnection",
                "OkHttpClient",
                "Socket(",
                "Cipher.getInstance",
                "SecretKeySpec(",
                "SharedPreferences",
                "DataStore<",
                "ExecutionRequest.create(",
                "UnifiedDevilRuntime(",
            )

        forbiddenOperationalWiring.forEach { forbidden ->
            assertTrue(
                !source.contains(forbidden),
                "Stage 284 must not introduce operational security regression wiring: $forbidden",
            )
        }
    }

    private fun completeEvidence(
        promptModelAttackResistance:
            DevilPromptModelAttackResistanceResult,
    ): DevilSecurityRegressionSuiteEvidence =
        DevilSecurityRegressionSuiteEvidence(
            promptModelAttackResistance =
                promptModelAttackResistance,
            fullThreatModelRegressionCovered = true,
            authenticationHardeningRegressionCovered = true,
            sessionHardeningRegressionCovered = true,
            capabilityAuthorizationHardeningRegressionCovered = true,
            dataProtectionRegressionCovered = true,
            memorySecurityRegressionCovered = true,
            childGuardianSecurityAuditRegressionCovered = true,
            financeLegalSecurityAuditRegressionCovered = true,
            promptModelAttackResistanceRegressionCovered = true,
            constitutionalSecurityBoundariesRegressionCovered = true,
        )

    private fun completePromptModelAttackResistance(
        threatModel: DevilThreatModelResult = threatModel(),
    ): DevilPromptModelAttackResistanceResult =
        DevilPromptModelAttackResistanceResult.create(
            evidence =
                completePromptModelEvidence(
                    financeLegalSecurityAudit =
                        completeFinanceLegalSecurityAudit(
                            threatModel = threatModel,
                        ),
                ),
        )

    private fun completePromptModelEvidence(
        financeLegalSecurityAudit:
            DevilFinanceLegalSecurityAuditResult,
    ): DevilPromptModelAttackResistanceEvidence =
        DevilPromptModelAttackResistanceEvidence(
            financeLegalSecurityAudit = financeLegalSecurityAudit,
            externalContentCannotBecomeDevilInstruction = true,
            modelOutputCannotBecomeTrustedInstructionOrVerifiedTruth =
                true,
            modelCannotBecomeDevilBrainOrAuthority = true,
            modelToolIntentCannotGrantAuthorizationOrCreateExecutionRequest =
                true,
            promptOrContextAssemblyCannotGrantAuthorization = true,
            untrustedInputCannotDirectlyMutateWorldModelOrMemory = true,
            downstreamCapabilityExecutionRequiresExistingConstitutionalAuthority =
                true,
            modelDomainAssessmentCannotBecomeConstitutionalVerification =
                true,
        )

    private fun completeFinanceLegalSecurityAudit(
        threatModel: DevilThreatModelResult = threatModel(),
    ): DevilFinanceLegalSecurityAuditResult =
        DevilFinanceLegalSecurityAuditResult.create(
            evidence =
                completeFinanceLegalEvidence(
                    childGuardianSecurityAudit =
                        completeChildGuardianSecurityAudit(
                            threatModel = threatModel,
                        ),
                ),
        )

    private fun completeFinanceLegalEvidence(
        childGuardianSecurityAudit:
            DevilChildGuardianSecurityAuditResult,
    ): DevilFinanceLegalSecurityAuditEvidence =
        DevilFinanceLegalSecurityAuditEvidence(
            childGuardianSecurityAudit = childGuardianSecurityAudit,
            financialInformationSeparatedFromFinancialAuthorityAndAccountAccess =
                true,
            financialAnalysisSeparatedFromTransactionAndExecution = true,
            financialSafetySeparatedFromConstitutionalVerificationAndExecutionAuthorization =
                true,
            suppliedFinancialFactsSeparatedFromVerifiedExternalFinancialState =
                true,
            legalInformationSeparatedFromLegalAdviceAndLegalAuthority =
                true,
            legalGuidanceSeparatedFromRightsObligationsAndLegalDeterminations =
                true,
            suppliedLegalEvidenceAndCitationSeparatedFromVerificationAndAuthority =
                true,
            highStakesLegalSafetySeparatedFromExecutionAndEmergencyAuthority =
                true,
        )

    private fun completeChildGuardianSecurityAudit(
        threatModel: DevilThreatModelResult = threatModel(),
    ): DevilChildGuardianSecurityAuditResult =
        DevilChildGuardianSecurityAuditResult.create(
            evidence =
                DevilChildGuardianSecurityAuditEvidence(
                    memorySecurity =
                        completeMemorySecurity(
                            threatModel = threatModel,
                        ),
                    childClassificationSeparatedFromAuthentication = true,
                    childClassificationSeparatedFromGuardianAuthority = true,
                    guardianAuthoritySeparatedFromGuardianApproval = true,
                    guardianApprovalSeparatedFromDevilAuthorization = true,
                    childPolicySatisfactionSeparatedFromAuthorizationAndExecution =
                        true,
                    guardianContextSeparatedFromOwnerModeAndProtectedPrivacyContext =
                        true,
                    childPrivacyBoundarySeparatedFromPrivacyAuthorizationAndDisclosure =
                        true,
                    noChildSpecificBrainSecurityOrMemoryAuthority = true,
                ),
        )

    private fun completeMemorySecurity(
        threatModel: DevilThreatModelResult,
    ): DevilMemorySecurityResult =
        DevilMemorySecurityResult.create(
            evidence =
                DevilMemorySecurityEvidence(
                    dataProtection =
                        completeDataProtection(
                            capabilityAuthorizationHardening =
                                completeCapabilityAuthorizationHardening(
                                    sessionHardening =
                                        completeSessionHardening(
                                            authenticationHardening =
                                                completeAuthenticationHardening(
                                                    threatModel = threatModel,
                                                ),
                                        ),
                                ),
                        ),
                    singleMemoryAuthorityRemainsAuthoritative = true,
                    memorySensitivitySeparatedFromSecurityAndPrivacyDisclosure =
                        true,
                    retentionClassificationSeparatedFromEnforcementAndDeletion =
                        true,
                    memoryAuthorityApprovalSeparatedFromCommitmentAndPersistence =
                        true,
                    persistenceEligibilitySeparatedFromStorageSuccess = true,
                    recallEligibilitySeparatedFromRecallAndDisclosurePermission =
                        true,
                    memoryPersistenceRequiresApprovedProtectedMechanism = true,
                ),
        )

    private fun completeDataProtection(
        capabilityAuthorizationHardening:
            DevilCapabilityAuthorizationHardeningResult,
    ): DevilDataProtectionResult =
        DevilDataProtectionResult.create(
            evidence =
                DevilDataProtectionEvidence(
                    capabilityAuthorizationHardening =
                        capabilityAuthorizationHardening,
                    privacyClassificationGovernsSensitiveDataHandling = true,
                    sensitiveExposureFailsClosedWhereRequired = true,
                    privacyDisclosureTreatmentCannotTransmitData = true,
                    representationReductionCannotPersistOrTransmitProtectedContent =
                        true,
                    durablePersistenceRequiresApprovedProtectedStore = true,
                    credentialsAndSecretsSeparatedFromOrdinaryApplicationData =
                        true,
                    dataProtectionSeparatedFromMemorySecurity = true,
                ),
        )

    private fun completeCapabilityAuthorizationHardening(
        sessionHardening: DevilSessionHardeningResult,
    ): DevilCapabilityAuthorizationHardeningResult =
        DevilCapabilityAuthorizationHardeningResult.create(
            evidence =
                DevilCapabilityAuthorizationHardeningEvidence(
                    sessionHardening = sessionHardening,
                    constitutionalAuthorizationSeparatedFromCapabilityAuthorization =
                        true,
                    capabilitySelectionCannotGrantAuthorization = true,
                    androidPermissionCannotGrantDevilAuthorization = true,
                    capabilityAvailabilityCannotGrantAuthorization = true,
                    capabilityReadinessCannotGrantAuthorization = true,
                    executionCapabilityCannotGrantAuthorization = true,
                    deniedOrDeferredAuthorizationCannotBeUpgradedDownstream =
                        true,
                ),
        )

    private fun completeSessionHardening(
        authenticationHardening:
            DevilAuthenticationHardeningResult,
    ): DevilSessionHardeningResult =
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

    private fun completeAuthenticationHardening(
        threatModel: DevilThreatModelResult,
    ): DevilAuthenticationHardeningResult =
        DevilAuthenticationHardeningResult.create(
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

    private fun threatModel(
        identityAuthenticationThreatsCovered: Boolean = true,
        sessionThreatsCovered: Boolean = true,
        authorizationThreatsCovered: Boolean = true,
        deviceTrustThreatsCovered: Boolean = true,
        untrustedInputThreatsCovered: Boolean = true,
        dataMemoryThreatsCovered: Boolean = true,
        capabilityExecutionThreatsCovered: Boolean = true,
    ): DevilThreatModelResult =
        DevilFullThreatModelCoordinator()
            .evaluate(
                DevilThreatModelEvidence(
                    identityAuthenticationThreatsCovered =
                        identityAuthenticationThreatsCovered,
                    sessionThreatsCovered = sessionThreatsCovered,
                    authorizationThreatsCovered =
                        authorizationThreatsCovered,
                    deviceTrustThreatsCovered =
                        deviceTrustThreatsCovered,
                    untrustedInputThreatsCovered =
                        untrustedInputThreatsCovered,
                    dataMemoryThreatsCovered =
                        dataMemoryThreatsCovered,
                    capabilityExecutionThreatsCovered =
                        capabilityExecutionThreatsCovered,
                ),
            )

    private fun stage284Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/securityhardening/DevilSecurityRegressionSuite.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/securityhardening/DevilSecurityRegressionSuite.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 284 source from: ${candidates.joinToString()}",
            )
    }
}
