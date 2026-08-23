package com.devil.core.runtime.modelprovider

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage243AHallucinationResistanceTest {

    @Test
    fun `verified Stage 242 output supports bounded hallucination resistance assessment with exact provenance`() {
        val verification = verifiedOutput()

        val result =
            ModelHallucinationResistanceCoordinator()
                .assess(
                    modelOutputVerification = verification,
                    resistanceBasisDescription =
                        "  Explicit bounded evidence for hallucination-resistance review.  ",
                    resistanceAssessmentDescription =
                        "  Model-domain resistance assessment without verified-truth promotion.  ",
                )

        assertEquals(
            ModelHallucinationResistanceStatus.ASSESSED,
            result.status,
        )

        assertSame(
            verification,
            result.modelOutputVerification,
        )

        assertSame(
            verification.interpretation,
            result.modelOutputVerification.interpretation,
        )

        assertSame(
            verification.interpretation.modelContext,
            result.modelOutputVerification.interpretation.modelContext,
        )

        assertSame(
            verification
                .interpretation
                .modelContext
                .structuredReasoning,
            result
                .modelOutputVerification
                .interpretation
                .modelContext
                .structuredReasoning,
        )

        assertSame(
            verification
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence,
            result
                .modelOutputVerification
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence,
        )

        assertEquals(
            "Explicit bounded evidence for hallucination-resistance review.",
            result.resistanceBasisDescription,
        )

        assertEquals(
            "Model-domain resistance assessment without verified-truth promotion.",
            result.resistanceAssessmentDescription,
        )
    }

    @Test
    fun `blank resistance basis keeps Stage 243A deferred`() {
        val verification = verifiedOutput()

        val result =
            ModelHallucinationResistanceCoordinator()
                .assess(
                    modelOutputVerification = verification,
                    resistanceBasisDescription = "   ",
                    resistanceAssessmentDescription =
                        "Bounded hallucination-resistance assessment.",
                )

        assertEquals(
            ModelHallucinationResistanceStatus.DEFERRED,
            result.status,
        )
        assertSame(verification, result.modelOutputVerification)
        assertNull(result.resistanceBasisDescription)
        assertNull(result.resistanceAssessmentDescription)
    }

    @Test
    fun `blank resistance assessment keeps Stage 243A deferred`() {
        val verification = verifiedOutput()

        val result =
            ModelHallucinationResistanceCoordinator()
                .assess(
                    modelOutputVerification = verification,
                    resistanceBasisDescription =
                        "Bounded hallucination-resistance basis.",
                    resistanceAssessmentDescription = "   ",
                )

        assertEquals(
            ModelHallucinationResistanceStatus.DEFERRED,
            result.status,
        )
        assertSame(verification, result.modelOutputVerification)
        assertNull(result.resistanceBasisDescription)
        assertNull(result.resistanceAssessmentDescription)
    }

    @Test
    fun `deferred Stage 242 output keeps Stage 243A deferred`() {
        val verification =
            ModelOutputVerificationResult.create(
                status = ModelOutputVerificationStatus.DEFERRED,
                interpretation = interpretedOutput(),
            )

        val result =
            ModelHallucinationResistanceCoordinator()
                .assess(
                    modelOutputVerification = verification,
                    resistanceBasisDescription =
                        "Bounded hallucination-resistance basis.",
                    resistanceAssessmentDescription =
                        "Bounded hallucination-resistance assessment.",
                )

        assertEquals(
            ModelHallucinationResistanceStatus.DEFERRED,
            result.status,
        )
        assertSame(verification, result.modelOutputVerification)
        assertNull(result.resistanceBasisDescription)
        assertNull(result.resistanceAssessmentDescription)
    }

    @Test
    fun `assessed result requires verified for review Stage 242 provenance`() {
        val deferredVerification =
            ModelOutputVerificationResult.create(
                status = ModelOutputVerificationStatus.DEFERRED,
                interpretation = interpretedOutput(),
            )

        assertFailsWith<IllegalArgumentException> {
            ModelHallucinationResistanceResult.create(
                status = ModelHallucinationResistanceStatus.ASSESSED,
                modelOutputVerification = deferredVerification,
                resistanceBasisDescription =
                    "Bounded hallucination-resistance basis.",
                resistanceAssessmentDescription =
                    "Bounded hallucination-resistance assessment.",
            )
        }
    }

    @Test
    fun `assessed result normalizes hallucination resistance metadata`() {
        val result =
            ModelHallucinationResistanceResult.create(
                status = ModelHallucinationResistanceStatus.ASSESSED,
                modelOutputVerification = verifiedOutput(),
                resistanceBasisDescription =
                    "  Normalized Stage 243A resistance basis.  ",
                resistanceAssessmentDescription =
                    "  Normalized Stage 243A resistance assessment.  ",
            )

        assertEquals(
            "Normalized Stage 243A resistance basis.",
            result.resistanceBasisDescription,
        )

        assertEquals(
            "Normalized Stage 243A resistance assessment.",
            result.resistanceAssessmentDescription,
        )
    }

    @Test
    fun `assessed result rejects blank resistance basis`() {
        assertFailsWith<IllegalArgumentException> {
            ModelHallucinationResistanceResult.create(
                status = ModelHallucinationResistanceStatus.ASSESSED,
                modelOutputVerification = verifiedOutput(),
                resistanceBasisDescription = "   ",
                resistanceAssessmentDescription =
                    "Bounded hallucination-resistance assessment.",
            )
        }
    }

    @Test
    fun `assessed result rejects blank resistance assessment`() {
        assertFailsWith<IllegalArgumentException> {
            ModelHallucinationResistanceResult.create(
                status = ModelHallucinationResistanceStatus.ASSESSED,
                modelOutputVerification = verifiedOutput(),
                resistanceBasisDescription =
                    "Bounded hallucination-resistance basis.",
                resistanceAssessmentDescription = "   ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle resistance basis metadata`() {
        assertFailsWith<IllegalArgumentException> {
            ModelHallucinationResistanceResult.create(
                status = ModelHallucinationResistanceStatus.DEFERRED,
                modelOutputVerification = verifiedOutput(),
                resistanceBasisDescription =
                    "Must not be present.",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle resistance assessment metadata`() {
        assertFailsWith<IllegalArgumentException> {
            ModelHallucinationResistanceResult.create(
                status = ModelHallucinationResistanceStatus.DEFERRED,
                modelOutputVerification = verifiedOutput(),
                resistanceAssessmentDescription =
                    "Must not be present.",
            )
        }
    }

    private fun verifiedOutput(): ModelOutputVerificationResult {
        return ModelOutputVerificationCoordinator()
            .verify(
                interpretation = interpretedOutput(),
                verificationBasisDescription =
                    "Explicit bounded Stage 243A model-output verification basis.",
                verificationAssessmentDescription =
                    "Model-domain Stage 243A verification preparation only.",
            )
    }

    private fun interpretedOutput(): ModelOutputInterpretationResult {
        return ModelOutputInterpretationCoordinator()
            .interpret(
                modelContext = assembledContext(),
                rawModelOutput =
                    "Explicitly supplied untrusted Stage 243A model output.",
                interpretationDescription =
                    "Bounded interpretation without truth promotion.",
            )
    }

    private fun assembledContext(): ModelContextAssemblyResult {
        return ModelContextAssemblyCoordinator()
            .assemble(
                structuredReasoning = integratedReasoning(),
                modelContextObjective =
                    "Bounded Stage 243A model-context objective.",
                assembledContextDescription =
                    "Bounded Stage 243A assembled model context.",
            )
    }

    private fun integratedReasoning():
        StructuredReasoningIntegrationResult {
        return StructuredReasoningIntegrationCoordinator()
            .integrate(
                toolUsingIntelligence = preparedToolUse(),
                reasoningObjective =
                    "Bounded Stage 243A reasoning objective.",
                structuredReasoningDescription =
                    "Bounded Stage 243A structured-reasoning context.",
            )
    }

    private fun preparedToolUse(): ToolUsingIntelligenceResult {
        return ToolUsingIntelligenceCoordinator()
            .prepare(
                routing = routedModel(),
                capability = capability(),
                toolUseIntentDescription =
                    "Bounded Stage 243A upstream tool-use context.",
            )
    }

    private fun routedModel(): ModelRoutingResult {
        return ModelRoutingCoordinator()
            .route(
                providerArchitecture =
                    availableProviderArchitecture(),
                routingRationale =
                    "Explicit bounded Stage 243A model routing destination.",
            )
    }

    private fun availableProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId =
                    "provider:stage243a:test",
                providerName =
                    "Stage 243A Test Provider",
                providerDescription =
                    "Provider-neutral Stage 243A hallucination-resistance foundation.",
            )
    }

    private fun capability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage243a-test",
                ),
            category =
                enumValues<CapabilityCategory>().first(),
            name =
                "Stage 243A Test Capability",
            description =
                "Existing Devil capability preserved through bounded Stage 243A hallucination-resistance assessment.",
        )
    }
}
