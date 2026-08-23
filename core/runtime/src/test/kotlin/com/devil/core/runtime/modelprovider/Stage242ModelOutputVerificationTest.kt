package com.devil.core.runtime.modelprovider

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage242ModelOutputVerificationTest {

    @Test
    fun `interpreted Stage 241 output produces bounded Stage 242 verification with exact provenance`() {
        val interpretation = interpretedOutput()

        val result =
            ModelOutputVerificationCoordinator()
                .verify(
                    interpretation = interpretation,
                    verificationBasisDescription =
                        "  Explicit bounded verification basis supplied for review.  ",
                    verificationAssessmentDescription =
                        "  Model output remains untrusted pending constitutional verification.  ",
                )

        assertEquals(
            ModelOutputVerificationStatus.VERIFIED_FOR_REVIEW,
            result.status,
        )

        assertSame(
            interpretation,
            result.interpretation,
        )

        assertSame(
            interpretation.modelContext,
            result.interpretation.modelContext,
        )

        assertSame(
            interpretation.modelContext.structuredReasoning,
            result.interpretation.modelContext.structuredReasoning,
        )

        assertSame(
            interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence,
            result
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence,
        )

        assertSame(
            interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .routing,
            result
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .routing,
        )

        assertSame(
            interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .capability,
            result
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .capability,
        )

        assertEquals(
            "Explicit bounded verification basis supplied for review.",
            result.verificationBasisDescription,
        )

        assertEquals(
            "Model output remains untrusted pending constitutional verification.",
            result.verificationAssessmentDescription,
        )
    }

    @Test
    fun `blank verification basis keeps Stage 242 deferred`() {
        val interpretation = interpretedOutput()

        val result =
            ModelOutputVerificationCoordinator()
                .verify(
                    interpretation = interpretation,
                    verificationBasisDescription = "   ",
                    verificationAssessmentDescription =
                        "Bounded verification assessment.",
                )

        assertEquals(
            ModelOutputVerificationStatus.DEFERRED,
            result.status,
        )
        assertSame(interpretation, result.interpretation)
        assertNull(result.verificationBasisDescription)
        assertNull(result.verificationAssessmentDescription)
    }

    @Test
    fun `blank verification assessment keeps Stage 242 deferred`() {
        val interpretation = interpretedOutput()

        val result =
            ModelOutputVerificationCoordinator()
                .verify(
                    interpretation = interpretation,
                    verificationBasisDescription =
                        "Bounded verification basis.",
                    verificationAssessmentDescription = "   ",
                )

        assertEquals(
            ModelOutputVerificationStatus.DEFERRED,
            result.status,
        )
        assertSame(interpretation, result.interpretation)
        assertNull(result.verificationBasisDescription)
        assertNull(result.verificationAssessmentDescription)
    }

    @Test
    fun `deferred Stage 241 output keeps Stage 242 deferred`() {
        val interpretation =
            ModelOutputInterpretationResult.create(
                status = ModelOutputInterpretationStatus.DEFERRED,
                modelContext = assembledContext(),
            )

        val result =
            ModelOutputVerificationCoordinator()
                .verify(
                    interpretation = interpretation,
                    verificationBasisDescription =
                        "Bounded verification basis.",
                    verificationAssessmentDescription =
                        "Bounded verification assessment.",
                )

        assertEquals(
            ModelOutputVerificationStatus.DEFERRED,
            result.status,
        )
        assertSame(interpretation, result.interpretation)
        assertNull(result.verificationBasisDescription)
        assertNull(result.verificationAssessmentDescription)
    }

    @Test
    fun `verified for review result requires interpreted Stage 241 provenance`() {
        val deferredInterpretation =
            ModelOutputInterpretationResult.create(
                status = ModelOutputInterpretationStatus.DEFERRED,
                modelContext = assembledContext(),
            )

        assertFailsWith<IllegalArgumentException> {
            ModelOutputVerificationResult.create(
                status =
                    ModelOutputVerificationStatus.VERIFIED_FOR_REVIEW,
                interpretation = deferredInterpretation,
                verificationBasisDescription =
                    "Bounded verification basis.",
                verificationAssessmentDescription =
                    "Bounded verification assessment.",
            )
        }
    }

    @Test
    fun `verified for review result preserves exact Stage 241 through Stage 234 provenance`() {
        val interpretation = interpretedOutput()

        val result =
            ModelOutputVerificationResult.create(
                status =
                    ModelOutputVerificationStatus.VERIFIED_FOR_REVIEW,
                interpretation = interpretation,
                verificationBasisDescription =
                    "Bounded verification basis.",
                verificationAssessmentDescription =
                    "Bounded verification assessment.",
            )

        assertSame(
            interpretation,
            result.interpretation,
        )

        assertSame(
            interpretation.modelContext,
            result.interpretation.modelContext,
        )

        assertSame(
            interpretation.modelContext.structuredReasoning,
            result.interpretation.modelContext.structuredReasoning,
        )

        assertSame(
            interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .routing
                .providerArchitecture,
            result
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .routing
                .providerArchitecture,
        )

        assertSame(
            interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .capability,
            result
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .capability,
        )
    }

    @Test
    fun `verified for review result normalizes verification metadata`() {
        val result =
            ModelOutputVerificationResult.create(
                status =
                    ModelOutputVerificationStatus.VERIFIED_FOR_REVIEW,
                interpretation = interpretedOutput(),
                verificationBasisDescription =
                    "  Normalized Stage 242 verification basis.  ",
                verificationAssessmentDescription =
                    "  Normalized Stage 242 verification assessment.  ",
            )

        assertEquals(
            "Normalized Stage 242 verification basis.",
            result.verificationBasisDescription,
        )

        assertEquals(
            "Normalized Stage 242 verification assessment.",
            result.verificationAssessmentDescription,
        )
    }

    @Test
    fun `verified for review result rejects blank verification basis`() {
        assertFailsWith<IllegalArgumentException> {
            ModelOutputVerificationResult.create(
                status =
                    ModelOutputVerificationStatus.VERIFIED_FOR_REVIEW,
                interpretation = interpretedOutput(),
                verificationBasisDescription = "   ",
                verificationAssessmentDescription =
                    "Bounded verification assessment.",
            )
        }
    }

    @Test
    fun `verified for review result rejects blank verification assessment`() {
        assertFailsWith<IllegalArgumentException> {
            ModelOutputVerificationResult.create(
                status =
                    ModelOutputVerificationStatus.VERIFIED_FOR_REVIEW,
                interpretation = interpretedOutput(),
                verificationBasisDescription =
                    "Bounded verification basis.",
                verificationAssessmentDescription = "   ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle verification basis metadata`() {
        assertFailsWith<IllegalArgumentException> {
            ModelOutputVerificationResult.create(
                status = ModelOutputVerificationStatus.DEFERRED,
                interpretation = interpretedOutput(),
                verificationBasisDescription =
                    "Must not be present.",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle verification assessment metadata`() {
        assertFailsWith<IllegalArgumentException> {
            ModelOutputVerificationResult.create(
                status = ModelOutputVerificationStatus.DEFERRED,
                interpretation = interpretedOutput(),
                verificationAssessmentDescription =
                    "Must not be present.",
            )
        }
    }

    private fun interpretedOutput():
        ModelOutputInterpretationResult {
        return ModelOutputInterpretationCoordinator()
            .interpret(
                modelContext = assembledContext(),
                rawModelOutput =
                    "Explicitly supplied untrusted Stage 242 model output.",
                interpretationDescription =
                    "Bounded interpretation without truth promotion.",
            )
    }

    private fun assembledContext(): ModelContextAssemblyResult {
        return ModelContextAssemblyCoordinator()
            .assemble(
                structuredReasoning = integratedReasoning(),
                modelContextObjective =
                    "Bounded Stage 242 model-context objective.",
                assembledContextDescription =
                    "Bounded Stage 242 assembled model context.",
            )
    }

    private fun integratedReasoning():
        StructuredReasoningIntegrationResult {
        return StructuredReasoningIntegrationCoordinator()
            .integrate(
                toolUsingIntelligence = preparedToolUse(),
                reasoningObjective =
                    "Bounded Stage 242 reasoning objective.",
                structuredReasoningDescription =
                    "Bounded Stage 242 structured-reasoning context.",
            )
    }

    private fun preparedToolUse(): ToolUsingIntelligenceResult {
        return ToolUsingIntelligenceCoordinator()
            .prepare(
                routing = routedModel(),
                capability = capability(),
                toolUseIntentDescription =
                    "Bounded Stage 242 upstream tool-use context.",
            )
    }

    private fun routedModel(): ModelRoutingResult {
        return ModelRoutingCoordinator()
            .route(
                providerArchitecture =
                    availableProviderArchitecture(),
                routingRationale =
                    "Explicit bounded Stage 242 model routing destination.",
            )
    }

    private fun availableProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId =
                    "provider:stage242:test",
                providerName =
                    "Stage 242 Test Provider",
                providerDescription =
                    "Provider-neutral Stage 242 model-output verification foundation.",
            )
    }

    private fun capability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage242-test",
                ),
            category =
                enumValues<CapabilityCategory>().first(),
            name =
                "Stage 242 Test Capability",
            description =
                "Existing Devil capability preserved through bounded Stage 242 model-output verification.",
        )
    }
}
