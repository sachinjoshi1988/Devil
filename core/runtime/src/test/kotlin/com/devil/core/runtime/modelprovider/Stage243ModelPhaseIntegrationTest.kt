package com.devil.core.runtime.modelprovider

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage243ModelPhaseIntegrationTest {

    @Test
    fun `completed Stage 242 integrates Stage 243 with exact full provenance`() {
        val verification = completedVerification()

        val result =
            ModelPhaseIntegrationCoordinator()
                .integrate(
                    modelOutputVerification = verification,
                    integrationObjective =
                        "  Preserve the completed bounded model-provider phase.  ",
                    integrationDescription =
                        "  Structural Stage 234 through Stage 243 integration without constitutional authority transfer.  ",
                )

        assertEquals(
            ModelPhaseIntegrationStatus.INTEGRATED,
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
            result.modelOutputVerification
                .interpretation
                .modelContext,
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

        assertSame(
            verification
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .routing,
            result
                .modelOutputVerification
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .routing,
        )

        assertSame(
            verification
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .routing
                .providerArchitecture,
            result
                .modelOutputVerification
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .routing
                .providerArchitecture,
        )

        assertSame(
            verification
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .capability,
            result
                .modelOutputVerification
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .capability,
        )

        assertEquals(
            "Preserve the completed bounded model-provider phase.",
            result.integrationObjective,
        )

        assertEquals(
            "Structural Stage 234 through Stage 243 integration without constitutional authority transfer.",
            result.integrationDescription,
        )
    }

    @Test
    fun `blank integration objective keeps Stage 243 deferred`() {
        val verification = completedVerification()

        val result =
            ModelPhaseIntegrationCoordinator()
                .integrate(
                    modelOutputVerification = verification,
                    integrationObjective = "   ",
                    integrationDescription =
                        "Bounded model-phase integration description.",
                )

        assertEquals(
            ModelPhaseIntegrationStatus.DEFERRED,
            result.status,
        )
        assertSame(verification, result.modelOutputVerification)
        assertNull(result.integrationObjective)
        assertNull(result.integrationDescription)
    }

    @Test
    fun `blank integration description keeps Stage 243 deferred`() {
        val verification = completedVerification()

        val result =
            ModelPhaseIntegrationCoordinator()
                .integrate(
                    modelOutputVerification = verification,
                    integrationObjective =
                        "Bounded model-phase integration objective.",
                    integrationDescription = "   ",
                )

        assertEquals(
            ModelPhaseIntegrationStatus.DEFERRED,
            result.status,
        )
        assertSame(verification, result.modelOutputVerification)
        assertNull(result.integrationObjective)
        assertNull(result.integrationDescription)
    }

    @Test
    fun `non completed Stage 242 keeps Stage 243 deferred`() {
        val verification =
            ModelOutputVerificationResult.create(
                status = ModelOutputVerificationStatus.DEFERRED,
                interpretation = interpretedOutput(),
            )

        val result =
            ModelPhaseIntegrationCoordinator()
                .integrate(
                    modelOutputVerification = verification,
                    integrationObjective =
                        "Bounded model-phase integration objective.",
                    integrationDescription =
                        "Bounded model-phase integration description.",
                )

        assertEquals(
            ModelPhaseIntegrationStatus.DEFERRED,
            result.status,
        )
        assertSame(verification, result.modelOutputVerification)
        assertNull(result.integrationObjective)
        assertNull(result.integrationDescription)
    }

    @Test
    fun `integrated result requires completed Stage 242 provenance`() {
        val deferredVerification =
            ModelOutputVerificationResult.create(
                status = ModelOutputVerificationStatus.DEFERRED,
                interpretation = interpretedOutput(),
            )

        assertFailsWith<IllegalArgumentException> {
            ModelPhaseIntegrationResult.create(
                status = ModelPhaseIntegrationStatus.INTEGRATED,
                modelOutputVerification = deferredVerification,
                integrationObjective =
                    "Bounded model-phase integration objective.",
                integrationDescription =
                    "Bounded model-phase integration description.",
            )
        }
    }

    @Test
    fun `integrated result normalizes Stage 243 metadata`() {
        val result =
            ModelPhaseIntegrationResult.create(
                status = ModelPhaseIntegrationStatus.INTEGRATED,
                modelOutputVerification = completedVerification(),
                integrationObjective =
                    "  Normalized Stage 243 integration objective.  ",
                integrationDescription =
                    "  Normalized Stage 243 integration description.  ",
            )

        assertEquals(
            "Normalized Stage 243 integration objective.",
            result.integrationObjective,
        )

        assertEquals(
            "Normalized Stage 243 integration description.",
            result.integrationDescription,
        )
    }

    @Test
    fun `integrated result rejects blank integration objective`() {
        assertFailsWith<IllegalArgumentException> {
            ModelPhaseIntegrationResult.create(
                status = ModelPhaseIntegrationStatus.INTEGRATED,
                modelOutputVerification = completedVerification(),
                integrationObjective = "   ",
                integrationDescription =
                    "Bounded model-phase integration description.",
            )
        }
    }

    @Test
    fun `integrated result rejects blank integration description`() {
        assertFailsWith<IllegalArgumentException> {
            ModelPhaseIntegrationResult.create(
                status = ModelPhaseIntegrationStatus.INTEGRATED,
                modelOutputVerification = completedVerification(),
                integrationObjective =
                    "Bounded model-phase integration objective.",
                integrationDescription = "   ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle integration objective`() {
        assertFailsWith<IllegalArgumentException> {
            ModelPhaseIntegrationResult.create(
                status = ModelPhaseIntegrationStatus.DEFERRED,
                modelOutputVerification = completedVerification(),
                integrationObjective =
                    "Must not be present.",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle integration description`() {
        assertFailsWith<IllegalArgumentException> {
            ModelPhaseIntegrationResult.create(
                status = ModelPhaseIntegrationStatus.DEFERRED,
                modelOutputVerification = completedVerification(),
                integrationDescription =
                    "Must not be present.",
            )
        }
    }

    @Test
    fun `Stage 243 preserves exact Stage 242 through Stage 234 object identity`() {
        val verification = completedVerification()

        val result =
            ModelPhaseIntegrationResult.create(
                status = ModelPhaseIntegrationStatus.INTEGRATED,
                modelOutputVerification = verification,
                integrationObjective =
                    "Bounded Stage 243 provenance objective.",
                integrationDescription =
                    "Bounded Stage 243 provenance description.",
            )

        assertSame(
            verification,
            result.modelOutputVerification,
        )

        val expectedToolUse =
            verification
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence

        val actualToolUse =
            result
                .modelOutputVerification
                .interpretation
                .modelContext
                .structuredReasoning
                .toolUsingIntelligence

        assertSame(expectedToolUse, actualToolUse)
        assertSame(expectedToolUse.routing, actualToolUse.routing)
        assertSame(
            expectedToolUse.routing.providerArchitecture,
            actualToolUse.routing.providerArchitecture,
        )
        assertSame(
            expectedToolUse.capability,
            actualToolUse.capability,
        )
    }

    private fun completedVerification(): ModelOutputVerificationResult {
        return ModelOutputVerificationCoordinator()
            .verify(
                interpretation = interpretedOutput(),
                verificationBasisDescription =
                    "Explicit bounded Stage 243 model-output assessment basis.",
                verificationAssessmentDescription =
                    "Model-domain assessment only; not constitutional Verification or verified truth.",
            )
    }

    private fun interpretedOutput(): ModelOutputInterpretationResult {
        return ModelOutputInterpretationCoordinator()
            .interpret(
                modelContext = assembledContext(),
                rawModelOutput =
                    "Bounded Stage 243 supplied model-output representation.",
                interpretationDescription =
                    "Bounded Stage 243 structural model-output interpretation.",
            )
    }

    private fun assembledContext(): ModelContextAssemblyResult {
        return ModelContextAssemblyCoordinator()
            .assemble(
                structuredReasoning = integratedReasoning(),
                modelContextObjective =
                    "Bounded Stage 243 model-context objective.",
                assembledContextDescription =
                    "Bounded Stage 243 assembled model context.",
            )
    }

    private fun integratedReasoning():
        StructuredReasoningIntegrationResult {
        return StructuredReasoningIntegrationCoordinator()
            .integrate(
                toolUsingIntelligence = preparedToolUse(),
                reasoningObjective =
                    "Bounded Stage 243 reasoning objective.",
                structuredReasoningDescription =
                    "Bounded Stage 243 structured-reasoning context.",
            )
    }

    private fun preparedToolUse(): ToolUsingIntelligenceResult {
        return ToolUsingIntelligenceCoordinator()
            .prepare(
                routing = routedModel(),
                capability = capability(),
                toolUseIntentDescription =
                    "Bounded Stage 243 upstream tool-use context.",
            )
    }

    private fun routedModel(): ModelRoutingResult {
        return ModelRoutingCoordinator()
            .route(
                providerArchitecture =
                    availableProviderArchitecture(),
                routingRationale =
                    "Explicit bounded Stage 243 model routing destination.",
            )
    }

    private fun availableProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId =
                    "provider:stage243:test",
                providerName =
                    "Stage 243 Test Provider",
                providerDescription =
                    "Provider-neutral Stage 243 model-phase integration foundation.",
            )
    }

    private fun capability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage243-test",
                ),
            category =
                enumValues<CapabilityCategory>().first(),
            name =
                "Stage 243 Test Capability",
            description =
                "Existing Devil capability preserved through bounded Stage 243 model-phase integration.",
        )
    }
}
