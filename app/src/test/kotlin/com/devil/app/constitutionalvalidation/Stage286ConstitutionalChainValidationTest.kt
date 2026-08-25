package com.devil.app.constitutionalvalidation

import com.devil.app.securityhardening.DevilAuthenticationHardeningEvidence
import com.devil.app.securityhardening.DevilAuthenticationHardeningResult
import com.devil.app.securityhardening.DevilCapabilityAuthorizationHardeningEvidence
import com.devil.app.securityhardening.DevilCapabilityAuthorizationHardeningResult
import com.devil.app.securityhardening.DevilChildGuardianSecurityAuditEvidence
import com.devil.app.securityhardening.DevilChildGuardianSecurityAuditResult
import com.devil.app.securityhardening.DevilDataProtectionEvidence
import com.devil.app.securityhardening.DevilDataProtectionResult
import com.devil.app.securityhardening.DevilFinalSecurityReviewCoordinator
import com.devil.app.securityhardening.DevilFinalSecurityReviewEvidence
import com.devil.app.securityhardening.DevilFinalSecurityReviewResult
import com.devil.app.securityhardening.DevilFinalSecurityReviewStatus
import com.devil.app.securityhardening.DevilFinanceLegalSecurityAuditEvidence
import com.devil.app.securityhardening.DevilFinanceLegalSecurityAuditResult
import com.devil.app.securityhardening.DevilFullThreatModelCoordinator
import com.devil.app.securityhardening.DevilMemorySecurityEvidence
import com.devil.app.securityhardening.DevilMemorySecurityResult
import com.devil.app.securityhardening.DevilPromptModelAttackResistanceEvidence
import com.devil.app.securityhardening.DevilPromptModelAttackResistanceResult
import com.devil.app.securityhardening.DevilSecurityRegressionSuiteEvidence
import com.devil.app.securityhardening.DevilSecurityRegressionSuiteResult
import com.devil.app.securityhardening.DevilSessionHardeningEvidence
import com.devil.app.securityhardening.DevilSessionHardeningResult
import com.devil.app.securityhardening.DevilThreatModelEvidence
import com.devil.app.securityhardening.DevilThreatModelResult
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 286 Constitutional Chain Validation governance tests.
 *
 * Stage 286 validates architectural chain integrity only.
 *
 * It must not become constitutional Verification, runtime execution,
 * a constitutional authority, or Stage 287 Brain Authority Validation.
 */
class Stage286ConstitutionalChainValidationTest {

    @Test
    fun `complete constitutional chain evidence becomes validated`() {
        val finalSecurityReview = completeFinalSecurityReview()
        val evidence = completeEvidence(finalSecurityReview)

        val result =
            DevilConstitutionalChainValidationCoordinator()
                .evaluate(evidence)

        assertEquals(
            DevilConstitutionalChainValidationStatus.VALIDATED,
            result.status,
        )
        assertSame(evidence, result.evidence)
        assertSame(
            finalSecurityReview,
            result.evidence.finalSecurityReview,
        )
    }

    @Test
    fun `non reviewed Stage 285 result prevents constitutional chain validation`() {
        val finalSecurityReview =
            DevilFinalSecurityReviewCoordinator()
                .evaluate(
                    completeFinalSecurityReviewEvidence(
                        constitutionalAuthorityBoundariesPreserved = false,
                    ),
                )

        assertEquals(
            DevilFinalSecurityReviewStatus.NOT_REVIEWED,
            finalSecurityReview.status,
        )

        val result =
            DevilConstitutionalChainValidationCoordinator()
                .evaluate(
                    completeEvidence(finalSecurityReview),
                )

        assertEquals(
            DevilConstitutionalChainValidationStatus.NOT_VALIDATED,
            result.status,
        )
        assertSame(
            finalSecurityReview,
            result.evidence.finalSecurityReview,
        )
    }

    @Test
    fun `missing any required Stage 286 chain property prevents validation`() {
        val finalSecurityReview = completeFinalSecurityReview()

        val incompleteEvidence =
            listOf(
                completeEvidence(finalSecurityReview).copy(
                    unifiedRuntimeChainPreserved = false,
                ),
                completeEvidence(finalSecurityReview).copy(
                    constitutionalAuthoritySeparationPreserved = false,
                ),
                completeEvidence(finalSecurityReview).copy(
                    traceAndProvenanceContinuityPreserved = false,
                ),
                completeEvidence(finalSecurityReview).copy(
                    executionObservationVerificationOutcomeSeparationPreserved = false,
                ),
                completeEvidence(finalSecurityReview).copy(
                    worldModelLearningMemoryChainSeparationPreserved = false,
                ),
            )

        incompleteEvidence.forEach { evidence ->
            val result =
                DevilConstitutionalChainValidationCoordinator()
                    .evaluate(evidence)

            assertEquals(
                DevilConstitutionalChainValidationStatus.NOT_VALIDATED,
                result.status,
            )
        }
    }

    @Test
    fun `Stage 286 preserves exact Stage 285 and transitive Phase T provenance`() {
        val finalSecurityReview = completeFinalSecurityReview()

        val result =
            DevilConstitutionalChainValidationCoordinator()
                .evaluate(
                    completeEvidence(finalSecurityReview),
                )

        assertSame(
            finalSecurityReview,
            result.evidence.finalSecurityReview,
        )
        assertSame(
            finalSecurityReview.evidence.securityRegressionSuite,
            result.evidence
                .finalSecurityReview
                .evidence
                .securityRegressionSuite,
        )
    }

    @Test
    fun `Stage 286 preserves constitutional boundaries and stops before Stage 287`() {
        val source = stage286Source()

        listOf(
            "CONSTITUTIONAL_CHAIN_VALIDATION != CONSTITUTIONAL_VERIFICATION.",
            "CONSTITUTIONAL_CHAIN_VALIDATION != SECURITY_AUTHORIZATION.",
            "CONSTITUTIONAL_CHAIN_VALIDATION != EXECUTION_AUTHORIZATION.",
            "CONSTITUTIONAL_CHAIN_VALIDATION != VERIFIED_OUTCOME.",
            "CONSTITUTIONAL_CHAIN_VALIDATION != PRODUCTION_ACCEPTANCE.",
            "CONSTITUTIONAL_CHAIN_VALIDATION != BRAIN_AUTHORITY_VALIDATION.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 286 boundary: $boundary",
            )
        }

        assertTrue(
            source.contains(
                "implement Stage 287 Brain Authority Validation",
            ),
        )

        listOf(
            "UnifiedDevilRuntime(",
            "ExecutionRequest(",
            "Runtime.getRuntime(",
            "ProcessBuilder(",
            "HttpURLConnection",
            "OkHttpClient",
        ).forEach { forbidden ->
            assertTrue(
                !source.contains(forbidden),
                "Stage 286 must not introduce operational or future-stage wiring: $forbidden",
            )
        }
    }

    private fun completeEvidence(
        finalSecurityReview: DevilFinalSecurityReviewResult,
    ): DevilConstitutionalChainValidationEvidence =
        DevilConstitutionalChainValidationEvidence(
            finalSecurityReview = finalSecurityReview,
            unifiedRuntimeChainPreserved = true,
            constitutionalAuthoritySeparationPreserved = true,
            traceAndProvenanceContinuityPreserved = true,
            executionObservationVerificationOutcomeSeparationPreserved = true,
            worldModelLearningMemoryChainSeparationPreserved = true,
        )

    private fun completeFinalSecurityReview():
        DevilFinalSecurityReviewResult =
        DevilFinalSecurityReviewCoordinator()
            .evaluate(
                completeFinalSecurityReviewEvidence(),
            )

    private fun completeFinalSecurityReviewEvidence(
        constitutionalAuthorityBoundariesPreserved: Boolean = true,
    ): DevilFinalSecurityReviewEvidence =
        DevilFinalSecurityReviewEvidence(
            securityRegressionSuite = completeSecurityRegressionSuite(),
            threatModelReviewed = true,
            authenticationAndSessionHardeningReviewed = true,
            capabilityAuthorizationHardeningReviewed = true,
            dataAndMemoryProtectionReviewed = true,
            childGuardianSecurityReviewed = true,
            financeLegalSecurityReviewed = true,
            promptModelAttackResistanceReviewed = true,
            securityRegressionCoverageReviewed = true,
            constitutionalAuthorityBoundariesPreserved =
                constitutionalAuthorityBoundariesPreserved,
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
                        completeChildGuardianSecurityAudit(
                            threatModel,
                        ),
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
                    memorySecurity =
                        completeMemorySecurity(
                            threatModel,
                        ),
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

    private fun stage286Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/constitutionalvalidation/DevilConstitutionalChainValidation.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/constitutionalvalidation/DevilConstitutionalChainValidation.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 286 Constitutional Chain Validation source.",
            )
    }
}
