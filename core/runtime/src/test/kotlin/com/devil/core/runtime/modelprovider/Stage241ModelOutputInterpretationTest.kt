package com.devil.core.runtime.modelprovider

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage241ModelOutputInterpretationTest {

    @Test
    fun `assembled Stage 240 context interprets explicitly supplied model output with exact provenance`() {
        val context = assembledContext()

        val result =
            ModelOutputInterpretationCoordinator()
                .interpret(
                    modelContext = context,
                    rawModelOutput =
                        "  Candidate provider output for bounded Stage 241 interpretation.  ",
                    interpretationDescription =
                        "  Structural interpretation only without verified-truth promotion.  ",
                )

        assertEquals(
            ModelOutputInterpretationStatus.INTERPRETED,
            result.status,
        )

        assertSame(
            context,
            result.modelContext,
        )

        assertSame(
            context.structuredReasoning,
            result.modelContext.structuredReasoning,
        )

        assertSame(
            context.structuredReasoning.toolUsingIntelligence,
            result.modelContext.structuredReasoning.toolUsingIntelligence,
        )

        assertSame(
            context.structuredReasoning.toolUsingIntelligence.routing,
            result.modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .routing,
        )

        assertSame(
            context.structuredReasoning.toolUsingIntelligence.routing.providerArchitecture,
            result.modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .routing
                .providerArchitecture,
        )

        assertSame(
            context.structuredReasoning.toolUsingIntelligence.capability,
            result.modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .capability,
        )

        assertEquals(
            "Candidate provider output for bounded Stage 241 interpretation.",
            result.rawModelOutput,
        )

        assertEquals(
            "Structural interpretation only without verified-truth promotion.",
            result.interpretationDescription,
        )
    }

    @Test
    fun `blank raw model output keeps Stage 241 deferred`() {
        val context = assembledContext()

        val result =
            ModelOutputInterpretationCoordinator()
                .interpret(
                    modelContext = context,
                    rawModelOutput = "   ",
                    interpretationDescription =
                        "Bounded interpretation description.",
                )

        assertEquals(
            ModelOutputInterpretationStatus.DEFERRED,
            result.status,
        )
        assertSame(context, result.modelContext)
        assertNull(result.rawModelOutput)
        assertNull(result.interpretationDescription)
    }

    @Test
    fun `blank interpretation description keeps Stage 241 deferred`() {
        val context = assembledContext()

        val result =
            ModelOutputInterpretationCoordinator()
                .interpret(
                    modelContext = context,
                    rawModelOutput =
                        "Bounded supplied model output.",
                    interpretationDescription = "   ",
                )

        assertEquals(
            ModelOutputInterpretationStatus.DEFERRED,
            result.status,
        )
        assertSame(context, result.modelContext)
        assertNull(result.rawModelOutput)
        assertNull(result.interpretationDescription)
    }

    @Test
    fun `deferred Stage 240 context keeps Stage 241 deferred`() {
        val context =
            ModelContextAssemblyResult.create(
                status = ModelContextAssemblyStatus.DEFERRED,
                structuredReasoning = integratedReasoning(),
            )

        val result =
            ModelOutputInterpretationCoordinator()
                .interpret(
                    modelContext = context,
                    rawModelOutput =
                        "Bounded supplied model output.",
                    interpretationDescription =
                        "Bounded interpretation description.",
                )

        assertEquals(
            ModelOutputInterpretationStatus.DEFERRED,
            result.status,
        )
        assertSame(context, result.modelContext)
        assertNull(result.rawModelOutput)
        assertNull(result.interpretationDescription)
    }

    @Test
    fun `interpreted result requires assembled Stage 240 provenance`() {
        val deferredContext =
            ModelContextAssemblyResult.create(
                status = ModelContextAssemblyStatus.DEFERRED,
                structuredReasoning = integratedReasoning(),
            )

        assertFailsWith<IllegalArgumentException> {
            ModelOutputInterpretationResult.create(
                status = ModelOutputInterpretationStatus.INTERPRETED,
                modelContext = deferredContext,
                rawModelOutput =
                    "Bounded supplied model output.",
                interpretationDescription =
                    "Bounded interpretation description.",
            )
        }
    }

    @Test
    fun `interpreted result preserves exact Stage 240 through Stage 234 and capability provenance`() {
        val context = assembledContext()

        val result =
            ModelOutputInterpretationResult.create(
                status = ModelOutputInterpretationStatus.INTERPRETED,
                modelContext = context,
                rawModelOutput =
                    "Bounded supplied model output.",
                interpretationDescription =
                    "Bounded interpretation description.",
            )

        assertSame(
            context,
            result.modelContext,
        )

        assertSame(
            context.structuredReasoning,
            result.modelContext.structuredReasoning,
        )

        assertSame(
            context.structuredReasoning.toolUsingIntelligence,
            result.modelContext.structuredReasoning.toolUsingIntelligence,
        )

        assertSame(
            context.structuredReasoning.toolUsingIntelligence.routing,
            result.modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .routing,
        )

        assertSame(
            context.structuredReasoning.toolUsingIntelligence.routing.providerArchitecture,
            result.modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .routing
                .providerArchitecture,
        )

        assertSame(
            context.structuredReasoning.toolUsingIntelligence.capability,
            result.modelContext
                .structuredReasoning
                .toolUsingIntelligence
                .capability,
        )
    }

    @Test
    fun `interpreted result normalizes supplied model output metadata`() {
        val result =
            ModelOutputInterpretationResult.create(
                status = ModelOutputInterpretationStatus.INTERPRETED,
                modelContext = assembledContext(),
                rawModelOutput =
                    "  Normalized Stage 241 model output.  ",
                interpretationDescription =
                    "  Normalized Stage 241 interpretation.  ",
            )

        assertEquals(
            "Normalized Stage 241 model output.",
            result.rawModelOutput,
        )

        assertEquals(
            "Normalized Stage 241 interpretation.",
            result.interpretationDescription,
        )
    }

    @Test
    fun `interpreted result rejects blank raw model output`() {
        assertFailsWith<IllegalArgumentException> {
            ModelOutputInterpretationResult.create(
                status = ModelOutputInterpretationStatus.INTERPRETED,
                modelContext = assembledContext(),
                rawModelOutput = "   ",
                interpretationDescription =
                    "Bounded interpretation description.",
            )
        }
    }

    @Test
    fun `interpreted result rejects blank interpretation description`() {
        assertFailsWith<IllegalArgumentException> {
            ModelOutputInterpretationResult.create(
                status = ModelOutputInterpretationStatus.INTERPRETED,
                modelContext = assembledContext(),
                rawModelOutput =
                    "Bounded supplied model output.",
                interpretationDescription = "   ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle model output metadata`() {
        assertFailsWith<IllegalArgumentException> {
            ModelOutputInterpretationResult.create(
                status = ModelOutputInterpretationStatus.DEFERRED,
                modelContext = assembledContext(),
                rawModelOutput =
                    "Must not be present.",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle interpretation metadata`() {
        assertFailsWith<IllegalArgumentException> {
            ModelOutputInterpretationResult.create(
                status = ModelOutputInterpretationStatus.DEFERRED,
                modelContext = assembledContext(),
                interpretationDescription =
                    "Must not be present.",
            )
        }
    }

    private fun assembledContext(): ModelContextAssemblyResult {
        return ModelContextAssemblyCoordinator()
            .assemble(
                structuredReasoning = integratedReasoning(),
                modelContextObjective =
                    "Bounded Stage 241 model-context objective.",
                assembledContextDescription =
                    "Bounded Stage 241 upstream assembled model context.",
            )
    }

    private fun integratedReasoning():
        StructuredReasoningIntegrationResult {
        return StructuredReasoningIntegrationCoordinator()
            .integrate(
                toolUsingIntelligence = preparedToolUse(),
                reasoningObjective =
                    "Bounded Stage 241 reasoning objective.",
                structuredReasoningDescription =
                    "Bounded Stage 241 structured-reasoning context.",
            )
    }

    private fun preparedToolUse():
        ToolUsingIntelligenceResult {
        return ToolUsingIntelligenceCoordinator()
            .prepare(
                routing = routedModel(),
                capability = capability(),
                toolUseIntentDescription =
                    "Bounded Stage 241 upstream tool-use context.",
            )
    }

    private fun routedModel(): ModelRoutingResult {
        return ModelRoutingCoordinator()
            .route(
                providerArchitecture =
                    availableProviderArchitecture(),
                routingRationale =
                    "Explicit bounded Stage 241 model routing destination.",
            )
    }

    private fun availableProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId =
                    "provider:stage241:test",
                providerName =
                    "Stage 241 Test Provider",
                providerDescription =
                    "Provider-neutral Stage 241 model-output interpretation foundation.",
            )
    }

    private fun capability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage241-test",
                ),
            category =
                enumValues<CapabilityCategory>().first(),
            name =
                "Stage 241 Test Capability",
            description =
                "Existing Devil capability preserved through bounded Stage 241 model-output interpretation.",
        )
    }
}
