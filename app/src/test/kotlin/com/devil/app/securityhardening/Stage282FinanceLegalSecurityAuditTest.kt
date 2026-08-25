package com.devil.app.securityhardening

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 282 Finance/Legal Security Audit governance tests.
 *
 * Stage 282 audits explicitly supplied architectural finance/legal security
 * boundaries while preserving the exact Stage 281 audit result.
 *
 * It must not access accounts or legal systems, perform transactions,
 * provide financial/legal advice, authorize execution, or begin Stage 283.
 */
class Stage282FinanceLegalSecurityAuditTest {

    @Test
    fun `complete supplied finance legal security audit evidence becomes audited`() {
        val upstream =
            completeChildGuardianSecurityAudit()

        val evidence =
            completeEvidence(
                childGuardianSecurityAudit = upstream,
            )

        val result =
            DevilFinanceLegalSecurityAuditCoordinator()
                .evaluate(evidence)

        assertEquals(
            DevilFinanceLegalSecurityAuditStatus.AUDITED,
            result.status,
        )
        assertSame(evidence, result.evidence)
        assertSame(
            upstream,
            result.evidence.childGuardianSecurityAudit,
        )
    }

    @Test
    fun `non audited Stage 281 result prevents audited classification`() {
        val upstream =
            DevilChildGuardianSecurityAuditResult.create(
                evidence =
                    completeChildGuardianSecurityAuditEvidence(
                        noChildSpecificBrainSecurityOrMemoryAuthority = false,
                    ),
            )

        assertEquals(
            DevilChildGuardianSecurityAuditStatus.NOT_AUDITED,
            upstream.status,
        )

        val result =
            DevilFinanceLegalSecurityAuditCoordinator()
                .evaluate(
                    completeEvidence(
                        childGuardianSecurityAudit = upstream,
                    ),
                )

        assertEquals(
            DevilFinanceLegalSecurityAuditStatus.NOT_AUDITED,
            result.status,
        )
    }

    @Test
    fun `missing any required finance legal security boundary prevents audited classification`() {
        val upstream =
            completeChildGuardianSecurityAudit()

        val incompleteEvidence =
            listOf(
                completeEvidence(
                    childGuardianSecurityAudit = upstream,
                    financialInformationSeparatedFromFinancialAuthorityAndAccountAccess = false,
                ),
                completeEvidence(
                    childGuardianSecurityAudit = upstream,
                    financialAnalysisSeparatedFromTransactionAndExecution = false,
                ),
                completeEvidence(
                    childGuardianSecurityAudit = upstream,
                    financialSafetySeparatedFromConstitutionalVerificationAndExecutionAuthorization = false,
                ),
                completeEvidence(
                    childGuardianSecurityAudit = upstream,
                    suppliedFinancialFactsSeparatedFromVerifiedExternalFinancialState = false,
                ),
                completeEvidence(
                    childGuardianSecurityAudit = upstream,
                    legalInformationSeparatedFromLegalAdviceAndLegalAuthority = false,
                ),
                completeEvidence(
                    childGuardianSecurityAudit = upstream,
                    legalGuidanceSeparatedFromRightsObligationsAndLegalDeterminations = false,
                ),
                completeEvidence(
                    childGuardianSecurityAudit = upstream,
                    suppliedLegalEvidenceAndCitationSeparatedFromVerificationAndAuthority = false,
                ),
                completeEvidence(
                    childGuardianSecurityAudit = upstream,
                    highStakesLegalSafetySeparatedFromExecutionAndEmergencyAuthority = false,
                ),
            )

        incompleteEvidence.forEach { evidence ->
            val result =
                DevilFinanceLegalSecurityAuditCoordinator()
                    .evaluate(evidence)

            assertEquals(
                DevilFinanceLegalSecurityAuditStatus.NOT_AUDITED,
                result.status,
            )
        }
    }

    @Test
    fun `Stage 282 preserves exact Stage 281 provenance`() {
        val threatModel =
            completeThreatModel()

        val authenticationHardening =
            completeAuthenticationHardening(
                threatModel = threatModel,
            )

        val sessionHardening =
            completeSessionHardening(
                authenticationHardening = authenticationHardening,
            )

        val capabilityAuthorizationHardening =
            completeCapabilityAuthorizationHardening(
                sessionHardening = sessionHardening,
            )

        val dataProtection =
            completeDataProtection(
                capabilityAuthorizationHardening =
                    capabilityAuthorizationHardening,
            )

        val memorySecurity =
            completeMemorySecurity(
                dataProtection = dataProtection,
            )

        val childGuardianSecurityAudit =
            DevilChildGuardianSecurityAuditResult.create(
                evidence =
                    completeChildGuardianSecurityAuditEvidence(
                        memorySecurity = memorySecurity,
                    ),
            )

        val evidence =
            completeEvidence(
                childGuardianSecurityAudit = childGuardianSecurityAudit,
            )

        val result =
            DevilFinanceLegalSecurityAuditCoordinator()
                .evaluate(evidence)

        assertSame(
            childGuardianSecurityAudit,
            result.evidence.childGuardianSecurityAudit,
        )
        assertSame(
            memorySecurity,
            result.evidence.childGuardianSecurityAudit
                .evidence
                .memorySecurity,
        )
        assertSame(
            dataProtection,
            result.evidence.childGuardianSecurityAudit
                .evidence
                .memorySecurity
                .evidence
                .dataProtection,
        )
        assertSame(
            evidence,
            result.evidence,
        )
    }

    @Test
    fun `Stage 282 requires relevant high stakes finance legal threat coverage`() {
        val requiredCategories =
            setOf(
                DevilThreatCategory.IDENTITY_AUTHENTICATION_SPOOFING,
                DevilThreatCategory.AUTHORIZATION_BYPASS_PRIVILEGE_ESCALATION,
                DevilThreatCategory.DATA_MEMORY_EXPOSURE,
                DevilThreatCategory.CAPABILITY_EXECUTION_MISUSE,
            )

        requiredCategories.forEach { missingCategory ->
            val threatModel =
                threatModelMissing(
                    category = missingCategory,
                )

            val upstream =
                completeChildGuardianSecurityAudit(
                    threatModel = threatModel,
                )

            val result =
                DevilFinanceLegalSecurityAuditCoordinator()
                    .evaluate(
                        completeEvidence(
                            childGuardianSecurityAudit = upstream,
                        ),
                    )

            assertEquals(
                DevilFinanceLegalSecurityAuditStatus.NOT_AUDITED,
                result.status,
            )
        }
    }

    @Test
    fun `Stage 282 preserves finance legal constitutional boundaries`() {
        val source =
            stage282Source()

        listOf(
            "FINANCIAL_INFORMATION != FINANCIAL_AUTHORITY.",
            "FINANCIAL_ANALYSIS != TRANSACTION.",
            "FINANCIAL_SAFETY_VERIFICATION != CONSTITUTIONAL_VERIFICATION.",
            "FINANCIAL_SAFETY_VERIFICATION != EXECUTION_AUTHORIZATION.",
            "SUPPLIED_FINANCIAL_FACT != VERIFIED_EXTERNAL_FINANCIAL_STATE.",
            "LEGAL_INFORMATION != LEGAL_ADVICE.",
            "GUIDANCE != LEGAL_DETERMINATION.",
            "SUPPLIED_LEGAL_EVIDENCE != VERIFIED_EVIDENCE.",
            "CITATION != CONSTITUTIONAL_VERIFICATION.",
            "HIGH_STAKES_LEGAL_SAFETY != EXECUTION_AUTHORIZATION.",
            "FINANCE_LEGAL_AUDITED != VERIFIED_OUTCOME.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 282 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 282 stops before Stage 283 Prompt Model Attack Resistance`() {
        val source =
            stage282Source()

        assertTrue(
            source.contains(
                "implement Stage 283 Prompt/Model Attack Resistance",
            ),
        )
    }

    @Test
    fun `Stage 282 contains no operational finance legal security wiring`() {
        val source =
            stage282Source()

        listOf(
            "ExecutionRequest(",
            "UnifiedDevilRuntime(",
            "SharedPreferences",
            "DataStore",
            "Room.databaseBuilder",
            "SQLiteDatabase",
            "HttpClient",
            "OkHttpClient",
            "Retrofit.Builder",
            "startActivity(",
            "sendBroadcast(",
            "contentResolver.",
            "java.net.",
            "javax.crypto.",
            "android.security.keystore",
        ).forEach { value ->
            assertFalse(
                source.contains(value),
                "Stage 282 must not introduce operational finance/legal security wiring: $value",
            )
        }
    }

    private fun completeEvidence(
        childGuardianSecurityAudit: DevilChildGuardianSecurityAuditResult,
        financialInformationSeparatedFromFinancialAuthorityAndAccountAccess: Boolean = true,
        financialAnalysisSeparatedFromTransactionAndExecution: Boolean = true,
        financialSafetySeparatedFromConstitutionalVerificationAndExecutionAuthorization: Boolean = true,
        suppliedFinancialFactsSeparatedFromVerifiedExternalFinancialState: Boolean = true,
        legalInformationSeparatedFromLegalAdviceAndLegalAuthority: Boolean = true,
        legalGuidanceSeparatedFromRightsObligationsAndLegalDeterminations: Boolean = true,
        suppliedLegalEvidenceAndCitationSeparatedFromVerificationAndAuthority: Boolean = true,
        highStakesLegalSafetySeparatedFromExecutionAndEmergencyAuthority: Boolean = true,
    ): DevilFinanceLegalSecurityAuditEvidence =
        DevilFinanceLegalSecurityAuditEvidence(
            childGuardianSecurityAudit = childGuardianSecurityAudit,
            financialInformationSeparatedFromFinancialAuthorityAndAccountAccess =
                financialInformationSeparatedFromFinancialAuthorityAndAccountAccess,
            financialAnalysisSeparatedFromTransactionAndExecution =
                financialAnalysisSeparatedFromTransactionAndExecution,
            financialSafetySeparatedFromConstitutionalVerificationAndExecutionAuthorization =
                financialSafetySeparatedFromConstitutionalVerificationAndExecutionAuthorization,
            suppliedFinancialFactsSeparatedFromVerifiedExternalFinancialState =
                suppliedFinancialFactsSeparatedFromVerifiedExternalFinancialState,
            legalInformationSeparatedFromLegalAdviceAndLegalAuthority =
                legalInformationSeparatedFromLegalAdviceAndLegalAuthority,
            legalGuidanceSeparatedFromRightsObligationsAndLegalDeterminations =
                legalGuidanceSeparatedFromRightsObligationsAndLegalDeterminations,
            suppliedLegalEvidenceAndCitationSeparatedFromVerificationAndAuthority =
                suppliedLegalEvidenceAndCitationSeparatedFromVerificationAndAuthority,
            highStakesLegalSafetySeparatedFromExecutionAndEmergencyAuthority =
                highStakesLegalSafetySeparatedFromExecutionAndEmergencyAuthority,
        )

    private fun completeChildGuardianSecurityAudit(
        threatModel: DevilThreatModelResult = completeThreatModel(),
    ): DevilChildGuardianSecurityAuditResult =
        DevilChildGuardianSecurityAuditResult.create(
            evidence =
                completeChildGuardianSecurityAuditEvidence(
                    memorySecurity =
                        completeMemorySecurity(
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
                        ),
                ),
        )

    private fun completeChildGuardianSecurityAuditEvidence(
        memorySecurity: DevilMemorySecurityResult =
            completeMemorySecurity(
                dataProtection =
                    completeDataProtection(
                        capabilityAuthorizationHardening =
                            completeCapabilityAuthorizationHardening(
                                sessionHardening =
                                    completeSessionHardening(
                                        authenticationHardening =
                                            completeAuthenticationHardening(
                                                threatModel = completeThreatModel(),
                                            ),
                                    ),
                            ),
                    ),
            ),
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

    private fun completeMemorySecurity(
        dataProtection: DevilDataProtectionResult,
    ): DevilMemorySecurityResult =
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

    private fun completeDataProtection(
        capabilityAuthorizationHardening: DevilCapabilityAuthorizationHardeningResult,
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
                    capabilitySelectionCannotGrantAuthorization = true,
                    constitutionalAuthorizationSeparatedFromCapabilityAuthorization = true,
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

    private fun threatModelMissing(
        category: DevilThreatCategory,
    ): DevilThreatModelResult =
        DevilFullThreatModelCoordinator()
            .evaluate(
                DevilThreatModelEvidence(
                    identityAuthenticationThreatsCovered =
                        category != DevilThreatCategory.IDENTITY_AUTHENTICATION_SPOOFING,
                    sessionThreatsCovered = true,
                    authorizationThreatsCovered =
                        category != DevilThreatCategory.AUTHORIZATION_BYPASS_PRIVILEGE_ESCALATION,
                    deviceTrustThreatsCovered = true,
                    untrustedInputThreatsCovered = true,
                    dataMemoryThreatsCovered =
                        category != DevilThreatCategory.DATA_MEMORY_EXPOSURE,
                    capabilityExecutionThreatsCovered =
                        category != DevilThreatCategory.CAPABILITY_EXECUTION_MISUSE,
                ),
            )

    private fun stage282Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/securityhardening/DevilFinanceLegalSecurityAudit.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/securityhardening/DevilFinanceLegalSecurityAudit.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 282 source from: ${candidates.joinToString()}",
            )
    }
}
