package com.devil.app.securityhardening

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 283 Prompt/Model Attack Resistance governance tests.
 *
 * Stage 283 evaluates explicitly supplied architectural separation evidence only.
 *
 * It must not inspect or rewrite real prompts/model output, create trust or
 * authorization, execute tools/capabilities, mutate World Model or Memory, or
 * begin Stage 284 Security Regression Suite.
 */
class Stage283PromptModelAttackResistanceTest {

    @Test
    fun `complete supplied prompt model attack resistance evidence becomes hardened`() {
        val financeLegalAudit = completeFinanceLegalSecurityAudit()

        val evidence =
            completeEvidence(
                financeLegalSecurityAudit = financeLegalAudit,
            )

        val result =
            DevilPromptModelAttackResistanceCoordinator()
                .evaluate(evidence)

        assertEquals(
            DevilPromptModelAttackResistanceStatus.HARDENED,
            result.status,
        )
        assertSame(evidence, result.evidence)
        assertSame(
            financeLegalAudit,
            result.evidence.financeLegalSecurityAudit,
        )
    }

    @Test
    fun `non audited Stage 282 result prevents hardened classification`() {
        val stage282Evidence =
            completeFinanceLegalEvidence(
                childGuardianSecurityAudit =
                    completeChildGuardianSecurityAudit(),
            ).copy(
                financialInformationSeparatedFromFinancialAuthorityAndAccountAccess =
                    false,
            )

        val nonAuditedStage282 =
            DevilFinanceLegalSecurityAuditResult.create(
                evidence = stage282Evidence,
            )

        assertEquals(
            DevilFinanceLegalSecurityAuditStatus.NOT_AUDITED,
            nonAuditedStage282.status,
        )

        val result =
            DevilPromptModelAttackResistanceCoordinator()
                .evaluate(
                    completeEvidence(
                        financeLegalSecurityAudit = nonAuditedStage282,
                    ),
                )

        assertEquals(
            DevilPromptModelAttackResistanceStatus.NOT_HARDENED,
            result.status,
        )
    }

    @Test
    fun `missing any required prompt model attack resistance boundary prevents hardened classification`() {
        val base =
            completeEvidence(
                financeLegalSecurityAudit =
                    completeFinanceLegalSecurityAudit(),
            )

        val incompleteEvidence =
            listOf(
                base.copy(
                    externalContentCannotBecomeDevilInstruction = false,
                ),
                base.copy(
                    modelOutputCannotBecomeTrustedInstructionOrVerifiedTruth =
                        false,
                ),
                base.copy(
                    modelCannotBecomeDevilBrainOrAuthority = false,
                ),
                base.copy(
                    modelToolIntentCannotGrantAuthorizationOrCreateExecutionRequest =
                        false,
                ),
                base.copy(
                    promptOrContextAssemblyCannotGrantAuthorization = false,
                ),
                base.copy(
                    untrustedInputCannotDirectlyMutateWorldModelOrMemory =
                        false,
                ),
                base.copy(
                    downstreamCapabilityExecutionRequiresExistingConstitutionalAuthority =
                        false,
                ),
                base.copy(
                    modelDomainAssessmentCannotBecomeConstitutionalVerification =
                        false,
                ),
            )

        incompleteEvidence.forEach { evidence ->
            val result =
                DevilPromptModelAttackResistanceCoordinator()
                    .evaluate(evidence)

            assertEquals(
                DevilPromptModelAttackResistanceStatus.NOT_HARDENED,
                result.status,
            )
        }
    }

    @Test
    fun `Stage 283 preserves exact Stage 282 provenance`() {
        val stage282 =
            completeFinanceLegalSecurityAudit()

        val result =
            DevilPromptModelAttackResistanceCoordinator()
                .evaluate(
                    completeEvidence(
                        financeLegalSecurityAudit = stage282,
                    ),
                )

        assertSame(
            stage282,
            result.evidence.financeLegalSecurityAudit,
        )
        assertSame(
            stage282.evidence,
            result.evidence.financeLegalSecurityAudit.evidence,
        )
        assertSame(
            stage282.evidence.childGuardianSecurityAudit,
            result.evidence
                .financeLegalSecurityAudit
                .evidence
                .childGuardianSecurityAudit,
        )
        assertSame(
            stage282.evidence
                .childGuardianSecurityAudit
                .evidence
                .memorySecurity,
            result.evidence
                .financeLegalSecurityAudit
                .evidence
                .childGuardianSecurityAudit
                .evidence
                .memorySecurity,
        )
    }

    @Test
    fun `Stage 283 requires untrusted input authorization bypass and capability execution threat coverage`() {
        val missingUntrustedInput =
            completeFinanceLegalSecurityAudit(
                threatModel =
                    threatModel(
                        untrustedInputThreatsCovered = false,
                    ),
            )

        val missingAuthorizationBypass =
            completeFinanceLegalSecurityAudit(
                threatModel =
                    threatModel(
                        authorizationThreatsCovered = false,
                    ),
            )

        val missingCapabilityExecution =
            completeFinanceLegalSecurityAudit(
                threatModel =
                    threatModel(
                        capabilityExecutionThreatsCovered = false,
                    ),
            )

        listOf(
            missingUntrustedInput,
            missingAuthorizationBypass,
            missingCapabilityExecution,
        ).forEach { stage282 ->
            val result =
                DevilPromptModelAttackResistanceCoordinator()
                    .evaluate(
                        completeEvidence(
                            financeLegalSecurityAudit = stage282,
                        ),
                    )

            assertEquals(
                DevilPromptModelAttackResistanceStatus.NOT_HARDENED,
                result.status,
            )
        }
    }

    @Test
    fun `Stage 283 preserves prompt model constitutional boundaries`() {
        val source = stage283Source()

        listOf(
            "EXTERNAL_CONTENT != DEVIL_INSTRUCTION.",
            "MODEL_OUTPUT != TRUSTED_INSTRUCTION.",
            "MODEL_OUTPUT != VERIFIED_TRUTH.",
            "MODEL != DEVIL.",
            "MODEL != BRAIN.",
            "MODEL != AUTHORITY.",
            "MODEL_TOOL_INTENT != AUTHORIZATION.",
            "MODEL_TOOL_INTENT != EXECUTION_REQUEST.",
            "PROMPT_OR_CONTEXT_ASSEMBLY != AUTHORIZATION.",
            "UNTRUSTED_INPUT != WORLD_MODEL_STATE.",
            "UNTRUSTED_INPUT != MEMORY.",
            "PROMPT_MODEL_ATTACK_RESISTANT != CONSTITUTIONAL_VERIFICATION.",
            "PROMPT_MODEL_ATTACK_RESISTANT != EXECUTION_AUTHORIZATION.",
            "PROMPT_MODEL_ATTACK_RESISTANT != VERIFIED_OUTCOME.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 283 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 283 stops before Stage 284 Security Regression Suite`() {
        val source = stage283Source()

        assertTrue(
            source.contains(
                "implement Stage 284 Security Regression Suite",
            ),
        )
    }

    @Test
    fun `Stage 283 contains no operational prompt model attack wiring`() {
        val source = stage283Source()

        listOf(
            "class PromptFirewall",
            "class JailbreakDetector",
            "class PromptInjectionDetector",
            "class ModelOutputSanitizer",
            "class ToolCallInterceptor",
            "fun sanitizePrompt(",
            "fun blockPrompt(",
            "fun detectJailbreak(",
            "fun detectPromptInjection(",
            "fun invokeModel(",
            "fun executeTool(",
        ).forEach { forbidden ->
            assertFalse(
                source.contains(forbidden),
                "Stage 283 must not introduce operational prompt/model attack wiring: $forbidden",
            )
        }
    }

    private fun completeEvidence(
        financeLegalSecurityAudit: DevilFinanceLegalSecurityAuditResult,
    ): DevilPromptModelAttackResistanceEvidence =
        DevilPromptModelAttackResistanceEvidence(
            financeLegalSecurityAudit = financeLegalSecurityAudit,
            externalContentCannotBecomeDevilInstruction = true,
            modelOutputCannotBecomeTrustedInstructionOrVerifiedTruth = true,
            modelCannotBecomeDevilBrainOrAuthority = true,
            modelToolIntentCannotGrantAuthorizationOrCreateExecutionRequest = true,
            promptOrContextAssemblyCannotGrantAuthorization = true,
            untrustedInputCannotDirectlyMutateWorldModelOrMemory = true,
            downstreamCapabilityExecutionRequiresExistingConstitutionalAuthority =
                true,
            modelDomainAssessmentCannotBecomeConstitutionalVerification = true,
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
        childGuardianSecurityAudit: DevilChildGuardianSecurityAuditResult,
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
            legalInformationSeparatedFromLegalAdviceAndLegalAuthority = true,
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
                    authorizationThreatsCovered = authorizationThreatsCovered,
                    deviceTrustThreatsCovered = deviceTrustThreatsCovered,
                    untrustedInputThreatsCovered = untrustedInputThreatsCovered,
                    dataMemoryThreatsCovered = dataMemoryThreatsCovered,
                    capabilityExecutionThreatsCovered =
                        capabilityExecutionThreatsCovered,
                ),
            )

    private fun stage283Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/securityhardening/DevilPromptModelAttackResistance.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/securityhardening/DevilPromptModelAttackResistance.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 283 source from: ${candidates.joinToString()}",
            )
    }
}
