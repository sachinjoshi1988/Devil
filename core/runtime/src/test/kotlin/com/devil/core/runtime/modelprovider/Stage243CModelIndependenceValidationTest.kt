package com.devil.core.runtime.modelprovider

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.reliability.RecoveryAttemptBudget
import com.devil.core.model.reliability.RecoveryStrategy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage243CModelIndependenceValidationTest {

    @Test
    fun `prepared Stage 243B context validates structural model independence with different provider`() {
        val recovery = preparedFailureRecovery()
        val alternateProvider = alternateProviderArchitecture()

        val result =
            ModelIndependenceValidationCoordinator()
                .validate(
                    modelFailureRecovery = recovery,
                    alternateProviderArchitecture = alternateProvider,
                    validationBasisDescription =
                        "  Explicit alternate provider representation for bounded independence validation.  ",
                    validationAssessmentDescription =
                        "  Structural provider independence only without provider switching or invocation.  ",
                )

        assertEquals(
            ModelIndependenceValidationStatus.VALIDATED,
            result.status,
        )

        assertSame(
            recovery,
            result.modelFailureRecovery,
        )

        assertSame(
            alternateProvider,
            result.alternateProviderArchitecture,
        )

        assertSame(
            recovery.hallucinationResistance,
            result.modelFailureRecovery.hallucinationResistance,
        )

        assertSame(
            recovery.hallucinationResistance.modelOutputVerification,
            result.modelFailureRecovery
                .hallucinationResistance
                .modelOutputVerification,
        )

        assertEquals(
            "Explicit alternate provider representation for bounded independence validation.",
            result.validationBasisDescription,
        )

        assertEquals(
            "Structural provider independence only without provider switching or invocation.",
            result.validationAssessmentDescription,
        )
    }

    @Test
    fun `non prepared Stage 243B context keeps Stage 243C deferred`() {
        val recovery =
            ModelFailureRecoveryResult.create(
                status = ModelFailureRecoveryStatus.DEFERRED,
                hallucinationResistance = assessedHallucinationResistance(),
            )

        val alternateProvider = alternateProviderArchitecture()

        val result =
            ModelIndependenceValidationCoordinator()
                .validate(
                    modelFailureRecovery = recovery,
                    alternateProviderArchitecture = alternateProvider,
                    validationBasisDescription =
                        "Bounded independence validation basis.",
                    validationAssessmentDescription =
                        "Bounded independence validation assessment.",
                )

        assertEquals(
            ModelIndependenceValidationStatus.DEFERRED,
            result.status,
        )

        assertSame(recovery, result.modelFailureRecovery)
        assertSame(
            alternateProvider,
            result.alternateProviderArchitecture,
        )
        assertNull(result.validationBasisDescription)
        assertNull(result.validationAssessmentDescription)
    }

    @Test
    fun `unavailable alternate provider keeps Stage 243C deferred`() {
        val alternateProvider =
            ModelProviderArchitectureResult.create(
                status = ModelProviderArchitectureStatus.DEFERRED,
            )

        val result =
            ModelIndependenceValidationCoordinator()
                .validate(
                    modelFailureRecovery = preparedFailureRecovery(),
                    alternateProviderArchitecture = alternateProvider,
                    validationBasisDescription =
                        "Bounded independence validation basis.",
                    validationAssessmentDescription =
                        "Bounded independence validation assessment.",
                )

        assertEquals(
            ModelIndependenceValidationStatus.DEFERRED,
            result.status,
        )
        assertSame(
            alternateProvider,
            result.alternateProviderArchitecture,
        )
        assertNull(result.validationBasisDescription)
        assertNull(result.validationAssessmentDescription)
    }

    @Test
    fun `same provider identity keeps Stage 243C deferred`() {
        val recovery = preparedFailureRecovery()

        val originalProvider =
            requireNotNull(
                recovery
                    .hallucinationResistance
                    .modelOutputVerification
                    .interpretation
                    .modelContext
                    .structuredReasoning
                    .toolUsingIntelligence
                    .routing
                    .providerArchitecture
                    .provider,
            )

        val sameProviderArchitecture =
            ModelProviderArchitectureCoordinator()
                .prepare(
                    providerId = originalProvider.providerId.value,
                    providerName = "Alternate Representation",
                    providerDescription =
                        "Different metadata with the same provider identity.",
                )

        val result =
            ModelIndependenceValidationCoordinator()
                .validate(
                    modelFailureRecovery = recovery,
                    alternateProviderArchitecture = sameProviderArchitecture,
                    validationBasisDescription =
                        "Bounded independence validation basis.",
                    validationAssessmentDescription =
                        "Bounded independence validation assessment.",
                )

        assertEquals(
            ModelIndependenceValidationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.validationBasisDescription)
        assertNull(result.validationAssessmentDescription)
    }

    @Test
    fun `blank validation basis keeps Stage 243C deferred`() {
        val result =
            ModelIndependenceValidationCoordinator()
                .validate(
                    modelFailureRecovery = preparedFailureRecovery(),
                    alternateProviderArchitecture = alternateProviderArchitecture(),
                    validationBasisDescription = "   ",
                    validationAssessmentDescription =
                        "Bounded independence validation assessment.",
                )

        assertEquals(
            ModelIndependenceValidationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.validationBasisDescription)
        assertNull(result.validationAssessmentDescription)
    }

    @Test
    fun `blank validation assessment keeps Stage 243C deferred`() {
        val result =
            ModelIndependenceValidationCoordinator()
                .validate(
                    modelFailureRecovery = preparedFailureRecovery(),
                    alternateProviderArchitecture = alternateProviderArchitecture(),
                    validationBasisDescription =
                        "Bounded independence validation basis.",
                    validationAssessmentDescription = "   ",
                )

        assertEquals(
            ModelIndependenceValidationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.validationBasisDescription)
        assertNull(result.validationAssessmentDescription)
    }

    @Test
    fun `validated result requires prepared Stage 243B provenance`() {
        val deferredRecovery =
            ModelFailureRecoveryResult.create(
                status = ModelFailureRecoveryStatus.DEFERRED,
                hallucinationResistance = assessedHallucinationResistance(),
            )

        assertFailsWith<IllegalArgumentException> {
            ModelIndependenceValidationResult.create(
                status = ModelIndependenceValidationStatus.VALIDATED,
                modelFailureRecovery = deferredRecovery,
                alternateProviderArchitecture = alternateProviderArchitecture(),
                validationBasisDescription =
                    "Bounded independence validation basis.",
                validationAssessmentDescription =
                    "Bounded independence validation assessment.",
            )
        }
    }

    @Test
    fun `validated result requires available alternate provider`() {
        assertFailsWith<IllegalArgumentException> {
            ModelIndependenceValidationResult.create(
                status = ModelIndependenceValidationStatus.VALIDATED,
                modelFailureRecovery = preparedFailureRecovery(),
                alternateProviderArchitecture =
                    ModelProviderArchitectureResult.create(
                        status = ModelProviderArchitectureStatus.DEFERRED,
                    ),
                validationBasisDescription =
                    "Bounded independence validation basis.",
                validationAssessmentDescription =
                    "Bounded independence validation assessment.",
            )
        }
    }

    @Test
    fun `validated result rejects same original and alternate provider identity`() {
        val recovery = preparedFailureRecovery()

        val originalProvider =
            requireNotNull(
                recovery
                    .hallucinationResistance
                    .modelOutputVerification
                    .interpretation
                    .modelContext
                    .structuredReasoning
                    .toolUsingIntelligence
                    .routing
                    .providerArchitecture
                    .provider,
            )

        val sameProviderArchitecture =
            ModelProviderArchitectureCoordinator()
                .prepare(
                    providerId = originalProvider.providerId.value,
                    providerName = "Same Identity Provider",
                    providerDescription =
                        "Same provider identity must not prove independence.",
                )

        assertFailsWith<IllegalArgumentException> {
            ModelIndependenceValidationResult.create(
                status = ModelIndependenceValidationStatus.VALIDATED,
                modelFailureRecovery = recovery,
                alternateProviderArchitecture = sameProviderArchitecture,
                validationBasisDescription =
                    "Bounded independence validation basis.",
                validationAssessmentDescription =
                    "Bounded independence validation assessment.",
            )
        }
    }

    @Test
    fun `validated result normalizes validation metadata`() {
        val result =
            ModelIndependenceValidationResult.create(
                status = ModelIndependenceValidationStatus.VALIDATED,
                modelFailureRecovery = preparedFailureRecovery(),
                alternateProviderArchitecture = alternateProviderArchitecture(),
                validationBasisDescription =
                    "  Normalized Stage 243C validation basis.  ",
                validationAssessmentDescription =
                    "  Normalized Stage 243C validation assessment.  ",
            )

        assertEquals(
            "Normalized Stage 243C validation basis.",
            result.validationBasisDescription,
        )

        assertEquals(
            "Normalized Stage 243C validation assessment.",
            result.validationAssessmentDescription,
        )
    }

    @Test
    fun `validated result rejects blank validation basis`() {
        assertFailsWith<IllegalArgumentException> {
            ModelIndependenceValidationResult.create(
                status = ModelIndependenceValidationStatus.VALIDATED,
                modelFailureRecovery = preparedFailureRecovery(),
                alternateProviderArchitecture = alternateProviderArchitecture(),
                validationBasisDescription = "   ",
                validationAssessmentDescription =
                    "Bounded independence validation assessment.",
            )
        }
    }

    @Test
    fun `validated result rejects blank validation assessment`() {
        assertFailsWith<IllegalArgumentException> {
            ModelIndependenceValidationResult.create(
                status = ModelIndependenceValidationStatus.VALIDATED,
                modelFailureRecovery = preparedFailureRecovery(),
                alternateProviderArchitecture = alternateProviderArchitecture(),
                validationBasisDescription =
                    "Bounded independence validation basis.",
                validationAssessmentDescription = "   ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle validation basis`() {
        assertFailsWith<IllegalArgumentException> {
            ModelIndependenceValidationResult.create(
                status = ModelIndependenceValidationStatus.DEFERRED,
                modelFailureRecovery = preparedFailureRecovery(),
                alternateProviderArchitecture = alternateProviderArchitecture(),
                validationBasisDescription =
                    "Must not be present.",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle validation assessment`() {
        assertFailsWith<IllegalArgumentException> {
            ModelIndependenceValidationResult.create(
                status = ModelIndependenceValidationStatus.DEFERRED,
                modelFailureRecovery = preparedFailureRecovery(),
                alternateProviderArchitecture = alternateProviderArchitecture(),
                validationAssessmentDescription =
                    "Must not be present.",
            )
        }
    }

    private fun preparedFailureRecovery(): ModelFailureRecoveryResult {
        return ModelFailureRecoveryCoordinator()
            .prepare(
                hallucinationResistance =
                    assessedHallucinationResistance(),
                recoveryStrategy =
                    RecoveryStrategy.RETRY_SAME_OPERATION,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 2,
                    ),
                recoveryRationale =
                    "Bounded Stage 243C recovery preparation only.",
            )
    }

    private fun assessedHallucinationResistance():
        ModelHallucinationResistanceResult {
        return ModelHallucinationResistanceCoordinator()
            .assess(
                modelOutputVerification = verifiedOutput(),
                resistanceBasisDescription =
                    "Explicit bounded Stage 243C hallucination-resistance basis.",
                resistanceAssessmentDescription =
                    "Model-domain Stage 243C resistance assessment only.",
            )
    }

    private fun verifiedOutput():
        ModelOutputVerificationResult {
        return ModelOutputVerificationCoordinator()
            .verify(
                interpretation = interpretedOutput(),
                verificationBasisDescription =
                    "Explicit bounded Stage 243C model-output verification basis.",
                verificationAssessmentDescription =
                    "Model-domain Stage 243C verification preparation only.",
            )
    }

    private fun interpretedOutput():
        ModelOutputInterpretationResult {
        return ModelOutputInterpretationCoordinator()
            .interpret(
                modelContext = assembledContext(),
                rawModelOutput =
                    "Explicitly supplied untrusted Stage 243C model output.",
                interpretationDescription =
                    "Bounded interpretation without truth promotion.",
            )
    }

    private fun assembledContext(): ModelContextAssemblyResult {
        return ModelContextAssemblyCoordinator()
            .assemble(
                structuredReasoning = integratedReasoning(),
                modelContextObjective =
                    "Bounded Stage 243C model-context objective.",
                assembledContextDescription =
                    "Bounded Stage 243C assembled model context.",
            )
    }

    private fun integratedReasoning():
        StructuredReasoningIntegrationResult {
        return StructuredReasoningIntegrationCoordinator()
            .integrate(
                toolUsingIntelligence = preparedToolUse(),
                reasoningObjective =
                    "Bounded Stage 243C reasoning objective.",
                structuredReasoningDescription =
                    "Bounded Stage 243C structured-reasoning context.",
            )
    }

    private fun preparedToolUse(): ToolUsingIntelligenceResult {
        return ToolUsingIntelligenceCoordinator()
            .prepare(
                routing = routedModel(),
                capability = capability(),
                toolUseIntentDescription =
                    "Bounded Stage 243C upstream tool-use context.",
            )
    }

    private fun routedModel(): ModelRoutingResult {
        return ModelRoutingCoordinator()
            .route(
                providerArchitecture = originalProviderArchitecture(),
                routingRationale =
                    "Explicit bounded Stage 243C original model routing destination.",
            )
    }

    private fun originalProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId =
                    "provider:stage243c:original",
                providerName =
                    "Stage 243C Original Provider",
                providerDescription =
                    "Original provider-neutral Stage 243C model foundation.",
            )
    }

    private fun alternateProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId =
                    "provider:stage243c:alternate",
                providerName =
                    "Stage 243C Alternate Provider",
                providerDescription =
                    "Different provider-neutral Stage 243C model foundation.",
            )
    }

    private fun capability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage243c-test",
                ),
            category =
                enumValues<CapabilityCategory>().first(),
            name =
                "Stage 243C Test Capability",
            description =
                "Existing Devil capability preserved through bounded Stage 243C Model Independence Validation.",
        )
    }
}
