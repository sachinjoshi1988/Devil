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
 * Stage 287 Brain Authority Validation governance tests.
 *
 * Stage 287 validates the existing constitutional Brain / Decision boundary only.
 *
 * It must not become Brain, Decision Authority, Planner, Executive, execution,
 * constitutional Verification, or Stage 288 Planner Boundary Validation.
 */
class Stage287BrainAuthorityValidationTest {

    @Test
    fun `complete Brain Authority evidence becomes validated`() {
        val chainValidation = completeConstitutionalChainValidation()
        val evidence = completeEvidence(chainValidation)

        val result =
            DevilBrainAuthorityValidationCoordinator()
                .evaluate(evidence)

        assertEquals(
            DevilBrainAuthorityValidationStatus.VALIDATED,
            result.status,
        )
        assertSame(evidence, result.evidence)
        assertSame(
            chainValidation,
            result.evidence.constitutionalChainValidation,
        )
    }

    @Test
    fun `non validated Stage 286 result prevents Brain Authority validation`() {
        val chainValidation =
            DevilConstitutionalChainValidationCoordinator()
                .evaluate(
                    completeConstitutionalChainEvidence(
                        unifiedRuntimeChainPreserved = false,
                    ),
                )

        assertEquals(
            DevilConstitutionalChainValidationStatus.NOT_VALIDATED,
            chainValidation.status,
        )

        val result =
            DevilBrainAuthorityValidationCoordinator()
                .evaluate(
                    completeEvidence(chainValidation),
                )

        assertEquals(
            DevilBrainAuthorityValidationStatus.NOT_VALIDATED,
            result.status,
        )
        assertSame(
            chainValidation,
            result.evidence.constitutionalChainValidation,
        )
    }

    @Test
    fun `missing any required Stage 287 property prevents validation`() {
        val chainValidation = completeConstitutionalChainValidation()

        val incompleteEvidence =
            listOf(
                completeEvidence(chainValidation).copy(
                    decisionAuthorityRemainsBoundedDecisionSelectionAuthority = false,
                ),
                completeEvidence(chainValidation).copy(
                    authorizationAndUnderstandingRemainUpstreamOfDecision = false,
                ),
                completeEvidence(chainValidation).copy(
                    decisionRemainsUpstreamOfTask = false,
                ),
                completeEvidence(chainValidation).copy(
                    decisionTraceAndResultInvariantsPreserved = false,
                ),
                completeEvidence(chainValidation).copy(
                    modelAndStructuredReasoningCannotBecomeBrainOrSelectDecision = false,
                ),
                completeEvidence(chainValidation).copy(
                    laterCapabilitiesCannotCreateOrReplaceBrainDecision = false,
                ),
                completeEvidence(chainValidation).copy(
                    downstreamAuthoritySeparationPreserved = false,
                ),
            )

        incompleteEvidence.forEach { evidence ->
            assertEquals(
                DevilBrainAuthorityValidationStatus.NOT_VALIDATED,
                DevilBrainAuthorityValidationCoordinator()
                    .evaluate(evidence)
                    .status,
            )
        }
    }

    @Test
    fun `Stage 287 preserves exact Stage 286 and Stage 285 provenance`() {
        val chainValidation = completeConstitutionalChainValidation()

        val result =
            DevilBrainAuthorityValidationCoordinator()
                .evaluate(
                    completeEvidence(chainValidation),
                )

        assertSame(
            chainValidation,
            result.evidence.constitutionalChainValidation,
        )
        assertSame(
            chainValidation.evidence.finalSecurityReview,
            result.evidence
                .constitutionalChainValidation
                .evidence
                .finalSecurityReview,
        )
        assertSame(
            chainValidation.evidence
                .finalSecurityReview
                .evidence
                .securityRegressionSuite,
            result.evidence
                .constitutionalChainValidation
                .evidence
                .finalSecurityReview
                .evidence
                .securityRegressionSuite,
        )
    }

    @Test
    fun `Stage 287 preserves Brain boundaries and stops before Stage 288`() {
        val source = stage287Source()

        listOf(
            "BRAIN_AUTHORITY_VALIDATION != BRAIN_DECISION.",
            "BRAIN_AUTHORITY_VALIDATION != DECISION_AUTHORITY.",
            "BRAIN_AUTHORITY_VALIDATION != AUTHORIZATION.",
            "BRAIN_AUTHORITY_VALIDATION != PLANNING.",
            "BRAIN_AUTHORITY_VALIDATION != EXECUTION.",
            "BRAIN_AUTHORITY_VALIDATION != CONSTITUTIONAL_VERIFICATION.",
            "BRAIN_AUTHORITY_VALIDATION != VERIFIED_OUTCOME.",
            "MODEL != BRAIN.",
            "MODEL != AUTHORITY.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 287 boundary: $boundary",
            )
        }

        assertTrue(
            source.contains(
                "implement Stage 288 Planner Boundary Validation",
            ),
        )

        listOf(
            "DefaultDecisionAuthority(",
            "UnifiedDevilRuntime(",
            "DecisionRecord(",
            "TaskRecord(",
            "PlanRecord(",
            "ExecutionRequest(",
            "Runtime.getRuntime(",
            "ProcessBuilder(",
            "HttpURLConnection",
            "OkHttpClient",
        ).forEach { forbidden ->
            assertTrue(
                !source.contains(forbidden),
                "Stage 287 must not introduce operational or future-stage wiring: $forbidden",
            )
        }
    }

    private fun completeEvidence(
        chainValidation: DevilConstitutionalChainValidationResult,
    ): DevilBrainAuthorityValidationEvidence =
        DevilBrainAuthorityValidationEvidence(
            constitutionalChainValidation = chainValidation,
            decisionAuthorityRemainsBoundedDecisionSelectionAuthority = true,
            authorizationAndUnderstandingRemainUpstreamOfDecision = true,
            decisionRemainsUpstreamOfTask = true,
            decisionTraceAndResultInvariantsPreserved = true,
            modelAndStructuredReasoningCannotBecomeBrainOrSelectDecision = true,
            laterCapabilitiesCannotCreateOrReplaceBrainDecision = true,
            downstreamAuthoritySeparationPreserved = true,
        )

    private fun completeConstitutionalChainValidation():
        DevilConstitutionalChainValidationResult =
        DevilConstitutionalChainValidationCoordinator()
            .evaluate(
                completeConstitutionalChainEvidence(),
            )

    private fun completeConstitutionalChainEvidence(
        unifiedRuntimeChainPreserved: Boolean = true,
    ): DevilConstitutionalChainValidationEvidence =
        DevilConstitutionalChainValidationEvidence(
            finalSecurityReview = completeFinalSecurityReview(),
            unifiedRuntimeChainPreserved = unifiedRuntimeChainPreserved,
            constitutionalAuthoritySeparationPreserved = true,
            traceAndProvenanceContinuityPreserved = true,
            executionObservationVerificationOutcomeSeparationPreserved = true,
            worldModelLearningMemoryChainSeparationPreserved = true,
        )

    private fun completeFinalSecurityReview():
        DevilFinalSecurityReviewResult =
        DevilFinalSecurityReviewCoordinator()
            .evaluate(
                DevilFinalSecurityReviewEvidence(
                    securityRegressionSuite =
                        completeSecurityRegressionSuite(),
                    threatModelReviewed = true,
                    authenticationAndSessionHardeningReviewed = true,
                    capabilityAuthorizationHardeningReviewed = true,
                    dataAndMemoryProtectionReviewed = true,
                    childGuardianSecurityReviewed = true,
                    financeLegalSecurityReviewed = true,
                    promptModelAttackResistanceReviewed = true,
                    securityRegressionCoverageReviewed = true,
                    constitutionalAuthorityBoundariesPreserved = true,
                ),
            )

    private fun completeSecurityRegressionSuite():
        DevilSecurityRegressionSuiteResult =
        DevilSecurityRegressionSuiteResult.create(
            evidence =
                DevilSecurityRegressionSuiteEvidence(
                    promptModelAttackResistance =
                        completePromptModelAttackResistance(),
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
                ),
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

    private fun stage287Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/constitutionalvalidation/DevilBrainAuthorityValidation.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/constitutionalvalidation/DevilBrainAuthorityValidation.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 287 Brain Authority Validation source.",
            )
    }
}
