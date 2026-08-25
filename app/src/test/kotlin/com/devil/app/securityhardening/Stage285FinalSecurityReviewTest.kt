package com.devil.app.securityhardening

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 285 Final Security Review governance tests.
 *
 * Stage 285 closes Phase T structurally only.
 *
 * It must not become constitutional security review, constitutional Verification,
 * security or execution authorization, production security acceptance, or begin
 * Stage 286 Constitutional Chain Validation.
 */
class Stage285FinalSecurityReviewTest {

    @Test
    fun `complete supplied final security review evidence becomes reviewed`() {
        val regressionSuite = completeSecurityRegressionSuite()
        val evidence = completeEvidence(regressionSuite)

        val result =
            DevilFinalSecurityReviewCoordinator()
                .evaluate(evidence)

        assertEquals(
            DevilFinalSecurityReviewStatus.REVIEWED,
            result.status,
        )
        assertSame(evidence, result.evidence)
        assertSame(regressionSuite, result.evidence.securityRegressionSuite)
    }

    @Test
    fun `non passed Stage 284 regression suite prevents reviewed classification`() {
        val failedRegressionSuite =
            DevilSecurityRegressionSuiteResult.create(
                evidence =
                    completeRegressionEvidence(
                        fullThreatModelRegressionCovered = false,
                    ),
            )

        assertEquals(
            DevilSecurityRegressionSuiteStatus.FAILED,
            failedRegressionSuite.status,
        )

        val result =
            DevilFinalSecurityReviewCoordinator()
                .evaluate(
                    completeEvidence(failedRegressionSuite),
                )

        assertEquals(
            DevilFinalSecurityReviewStatus.NOT_REVIEWED,
            result.status,
        )
        assertSame(
            failedRegressionSuite,
            result.evidence.securityRegressionSuite,
        )
    }

    @Test
    fun `missing any required final security review property prevents reviewed classification`() {
        val regressionSuite = completeSecurityRegressionSuite()

        val incompleteEvidence =
            listOf(
                completeEvidence(regressionSuite).copy(
                    threatModelReviewed = false,
                ),
                completeEvidence(regressionSuite).copy(
                    authenticationAndSessionHardeningReviewed = false,
                ),
                completeEvidence(regressionSuite).copy(
                    capabilityAuthorizationHardeningReviewed = false,
                ),
                completeEvidence(regressionSuite).copy(
                    dataAndMemoryProtectionReviewed = false,
                ),
                completeEvidence(regressionSuite).copy(
                    childGuardianSecurityReviewed = false,
                ),
                completeEvidence(regressionSuite).copy(
                    financeLegalSecurityReviewed = false,
                ),
                completeEvidence(regressionSuite).copy(
                    promptModelAttackResistanceReviewed = false,
                ),
                completeEvidence(regressionSuite).copy(
                    securityRegressionCoverageReviewed = false,
                ),
                completeEvidence(regressionSuite).copy(
                    constitutionalAuthorityBoundariesPreserved = false,
                ),
            )

        incompleteEvidence.forEach { evidence ->
            val result =
                DevilFinalSecurityReviewCoordinator()
                    .evaluate(evidence)

            assertEquals(
                DevilFinalSecurityReviewStatus.NOT_REVIEWED,
                result.status,
            )
        }
    }

    @Test
    fun `Stage 285 preserves exact Stage 284 and upstream Phase T provenance`() {
        val stage283 = completePromptModelAttackResistance()
        val stage284 =
            DevilSecurityRegressionSuiteResult.create(
                evidence =
                    completeRegressionEvidence(
                        promptModelAttackResistance = stage283,
                    ),
            )

        val result =
            DevilFinalSecurityReviewCoordinator()
                .evaluate(
                    completeEvidence(stage284),
                )

        assertSame(stage284, result.evidence.securityRegressionSuite)
        assertSame(
            stage283,
            result.evidence
                .securityRegressionSuite
                .evidence
                .promptModelAttackResistance,
        )

        assertSame(
            stage283.evidence.financeLegalSecurityAudit,
            result.evidence
                .securityRegressionSuite
                .evidence
                .promptModelAttackResistance
                .evidence
                .financeLegalSecurityAudit,
        )

        assertSame(
            stage283.evidence
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
            result.evidence
                .securityRegressionSuite
                .evidence
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
    fun `Stage 285 preserves final review constitutional boundaries and stops before Stage 286`() {
        val source = stage285Source()

        listOf(
            "FINAL_SECURITY_REVIEW != CONSTITUTIONAL_SECURITY_REVIEW.",
            "FINAL_SECURITY_REVIEW != CONSTITUTIONAL_VERIFICATION.",
            "FINAL_SECURITY_REVIEW != SECURITY_AUTHORIZATION.",
            "FINAL_SECURITY_REVIEW != EXECUTION_AUTHORIZATION.",
            "FINAL_SECURITY_REVIEW != ATTACK_PREVENTION.",
            "FINAL_SECURITY_REVIEW != SECURITY_INCIDENT_ABSENT.",
            "FINAL_SECURITY_REVIEW != VERIFIED_OUTCOME.",
            "FINAL_SECURITY_REVIEW != PRODUCTION_SECURITY_ACCEPTANCE.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 285 boundary: $boundary",
            )
        }

        assertTrue(
            source.contains(
                "implement Stage 286 Constitutional Chain Validation",
            ),
        )

        listOf(
            "Runtime.getRuntime(",
            "ProcessBuilder(",
            "HttpURLConnection",
            "OkHttpClient",
            "ExecutionRequest(",
            "UnifiedDevilRuntime(",
        ).forEach { forbidden ->
            assertTrue(
                !source.contains(forbidden),
                "Stage 285 must not introduce operational security wiring: $forbidden",
            )
        }
    }

    private fun completeEvidence(
        securityRegressionSuite: DevilSecurityRegressionSuiteResult,
    ): DevilFinalSecurityReviewEvidence =
        DevilFinalSecurityReviewEvidence(
            securityRegressionSuite = securityRegressionSuite,
            threatModelReviewed = true,
            authenticationAndSessionHardeningReviewed = true,
            capabilityAuthorizationHardeningReviewed = true,
            dataAndMemoryProtectionReviewed = true,
            childGuardianSecurityReviewed = true,
            financeLegalSecurityReviewed = true,
            promptModelAttackResistanceReviewed = true,
            securityRegressionCoverageReviewed = true,
            constitutionalAuthorityBoundariesPreserved = true,
        )

    private fun completeSecurityRegressionSuite():
        DevilSecurityRegressionSuiteResult =
        DevilSecurityRegressionSuiteResult.create(
            evidence = completeRegressionEvidence(),
        )

    private fun completeRegressionEvidence(
        promptModelAttackResistance: DevilPromptModelAttackResistanceResult =
            completePromptModelAttackResistance(),
        fullThreatModelRegressionCovered: Boolean = true,
    ): DevilSecurityRegressionSuiteEvidence =
        DevilSecurityRegressionSuiteEvidence(
            promptModelAttackResistance = promptModelAttackResistance,
            fullThreatModelRegressionCovered = fullThreatModelRegressionCovered,
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
        threatModel: DevilThreatModelResult = completeThreatModel(),
    ): DevilPromptModelAttackResistanceResult =
        DevilPromptModelAttackResistanceResult.create(
            evidence =
                DevilPromptModelAttackResistanceEvidence(
                    financeLegalSecurityAudit =
                        completeFinanceLegalSecurityAudit(
                            threatModel,
                        ),
                    externalContentCannotBecomeDevilInstruction = true,
                    modelOutputCannotBecomeTrustedInstructionOrVerifiedTruth = true,
                    modelCannotBecomeDevilBrainOrAuthority = true,
                    modelToolIntentCannotGrantAuthorizationOrCreateExecutionRequest = true,
                    promptOrContextAssemblyCannotGrantAuthorization = true,
                    untrustedInputCannotDirectlyMutateWorldModelOrMemory = true,
                    downstreamCapabilityExecutionRequiresExistingConstitutionalAuthority = true,
                    modelDomainAssessmentCannotBecomeConstitutionalVerification = true,
                ),
        )

    private fun completeFinanceLegalSecurityAudit(
        threatModel: DevilThreatModelResult,
    ): DevilFinanceLegalSecurityAuditResult =
        DevilFinanceLegalSecurityAuditResult.create(
            evidence =
                DevilFinanceLegalSecurityAuditEvidence(
                    childGuardianSecurityAudit =
                        completeChildGuardianSecurityAudit(threatModel),
                    financialInformationSeparatedFromFinancialAuthorityAndAccountAccess = true,
                    financialAnalysisSeparatedFromTransactionAndExecution = true,
                    financialSafetySeparatedFromConstitutionalVerificationAndExecutionAuthorization =
                        true,
                    suppliedFinancialFactsSeparatedFromVerifiedExternalFinancialState = true,
                    legalInformationSeparatedFromLegalAdviceAndLegalAuthority = true,
                    legalGuidanceSeparatedFromRightsObligationsAndLegalDeterminations = true,
                    suppliedLegalEvidenceAndCitationSeparatedFromVerificationAndAuthority = true,
                    highStakesLegalSafetySeparatedFromExecutionAndEmergencyAuthority = true,
                ),
        )

    private fun completeChildGuardianSecurityAudit(
        threatModel: DevilThreatModelResult,
    ): DevilChildGuardianSecurityAuditResult =
        DevilChildGuardianSecurityAuditResult.create(
            evidence =
                DevilChildGuardianSecurityAuditEvidence(
                    memorySecurity = completeMemorySecurity(threatModel),
                    childClassificationSeparatedFromAuthentication = true,
                    childClassificationSeparatedFromGuardianAuthority = true,
                    guardianAuthoritySeparatedFromGuardianApproval = true,
                    guardianApprovalSeparatedFromDevilAuthorization = true,
                    childPolicySatisfactionSeparatedFromAuthorizationAndExecution = true,
                    guardianContextSeparatedFromOwnerModeAndProtectedPrivacyContext = true,
                    childPrivacyBoundarySeparatedFromPrivacyAuthorizationAndDisclosure = true,
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
                            completeCapabilityAuthorizationHardening(
                                completeSessionHardening(
                                    completeAuthenticationHardening(
                                        threatModel,
                                    ),
                                ),
                            ),
                        ),
                    singleMemoryAuthorityRemainsAuthoritative = true,
                    memorySensitivitySeparatedFromSecurityAndPrivacyDisclosure = true,
                    retentionClassificationSeparatedFromEnforcementAndDeletion = true,
                    memoryAuthorityApprovalSeparatedFromCommitmentAndPersistence = true,
                    persistenceEligibilitySeparatedFromStorageSuccess = true,
                    recallEligibilitySeparatedFromRecallAndDisclosurePermission = true,
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
                    representationReductionCannotPersistOrTransmitProtectedContent = true,
                    durablePersistenceRequiresApprovedProtectedStore = true,
                    credentialsAndSecretsSeparatedFromOrdinaryApplicationData = true,
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
                    constitutionalAuthorizationSeparatedFromCapabilityAuthorization = true,
                    capabilitySelectionCannotGrantAuthorization = true,
                    androidPermissionCannotGrantDevilAuthorization = true,
                    capabilityAvailabilityCannotGrantAuthorization = true,
                    capabilityReadinessCannotGrantAuthorization = true,
                    executionCapabilityCannotGrantAuthorization = true,
                    deniedOrDeferredAuthorizationCannotBeUpgradedDownstream = true,
                ),
        )

    private fun completeSessionHardening(
        authenticationHardening: DevilAuthenticationHardeningResult,
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

    private fun completeThreatModel(): DevilThreatModelResult =
        DevilFullThreatModelCoordinator()
            .evaluate(
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

    private fun stage285Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/securityhardening/DevilFinalSecurityReview.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/securityhardening/DevilFinalSecurityReview.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 285 source from: ${candidates.joinToString()}",
            )
    }
}
