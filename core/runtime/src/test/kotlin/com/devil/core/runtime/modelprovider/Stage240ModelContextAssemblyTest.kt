package com.devil.core.runtime.modelprovider

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage240ModelContextAssemblyTest {

    @Test
    fun `integrated Stage 239 context assembles Stage 240 model context with exact provenance`() {
        val reasoning = integratedReasoning()

        val result =
            ModelContextAssemblyCoordinator()
                .assemble(
                    structuredReasoning = reasoning,
                    modelContextObjective =
                        "  Preserve bounded model-context material for downstream interpretation.  ",
                    assembledContextDescription =
                        "  Provider-neutral structural model context without prompt transmission or inference.  ",
                )

        assertEquals(
            ModelContextAssemblyStatus.ASSEMBLED,
            result.status,
        )

        assertSame(
            reasoning,
            result.structuredReasoning,
        )

        assertSame(
            reasoning.toolUsingIntelligence,
            result.structuredReasoning.toolUsingIntelligence,
        )

        assertSame(
            reasoning.toolUsingIntelligence.routing,
            result.structuredReasoning.toolUsingIntelligence.routing,
        )

        assertSame(
            reasoning.toolUsingIntelligence.routing.providerArchitecture,
            result.structuredReasoning.toolUsingIntelligence.routing.providerArchitecture,
        )

        assertSame(
            reasoning.toolUsingIntelligence.capability,
            result.structuredReasoning.toolUsingIntelligence.capability,
        )

        assertEquals(
            "Preserve bounded model-context material for downstream interpretation.",
            result.modelContextObjective,
        )

        assertEquals(
            "Provider-neutral structural model context without prompt transmission or inference.",
            result.assembledContextDescription,
        )
    }

    @Test
    fun `blank model context objective keeps Stage 240 deferred`() {
        val reasoning = integratedReasoning()

        val result =
            ModelContextAssemblyCoordinator()
                .assemble(
                    structuredReasoning = reasoning,
                    modelContextObjective = "   ",
                    assembledContextDescription =
                        "Bounded model-context description.",
                )

        assertEquals(
            ModelContextAssemblyStatus.DEFERRED,
            result.status,
        )
        assertSame(reasoning, result.structuredReasoning)
        assertNull(result.modelContextObjective)
        assertNull(result.assembledContextDescription)
    }

    @Test
    fun `blank assembled context description keeps Stage 240 deferred`() {
        val reasoning = integratedReasoning()

        val result =
            ModelContextAssemblyCoordinator()
                .assemble(
                    structuredReasoning = reasoning,
                    modelContextObjective =
                        "Bounded model-context objective.",
                    assembledContextDescription = "   ",
                )

        assertEquals(
            ModelContextAssemblyStatus.DEFERRED,
            result.status,
        )
        assertSame(reasoning, result.structuredReasoning)
        assertNull(result.modelContextObjective)
        assertNull(result.assembledContextDescription)
    }

    @Test
    fun `deferred Stage 239 context keeps Stage 240 deferred`() {
        val reasoning =
            StructuredReasoningIntegrationResult.create(
                status =
                    StructuredReasoningIntegrationStatus.DEFERRED,
                toolUsingIntelligence = preparedToolUse(),
            )

        val result =
            ModelContextAssemblyCoordinator()
                .assemble(
                    structuredReasoning = reasoning,
                    modelContextObjective =
                        "Bounded model-context objective.",
                    assembledContextDescription =
                        "Bounded model-context description.",
                )

        assertEquals(
            ModelContextAssemblyStatus.DEFERRED,
            result.status,
        )
        assertSame(reasoning, result.structuredReasoning)
        assertNull(result.modelContextObjective)
        assertNull(result.assembledContextDescription)
    }

    @Test
    fun `assembled result requires integrated Stage 239 provenance`() {
        val deferredReasoning =
            StructuredReasoningIntegrationResult.create(
                status =
                    StructuredReasoningIntegrationStatus.DEFERRED,
                toolUsingIntelligence = preparedToolUse(),
            )

        assertFailsWith<IllegalArgumentException> {
            ModelContextAssemblyResult.create(
                status = ModelContextAssemblyStatus.ASSEMBLED,
                structuredReasoning = deferredReasoning,
                modelContextObjective =
                    "Bounded model-context objective.",
                assembledContextDescription =
                    "Bounded model-context description.",
            )
        }
    }

    @Test
    fun `assembled result preserves exact Stage 239 Stage 238 routing and capability provenance`() {
        val reasoning = integratedReasoning()

        val result =
            ModelContextAssemblyResult.create(
                status = ModelContextAssemblyStatus.ASSEMBLED,
                structuredReasoning = reasoning,
                modelContextObjective =
                    "Bounded model-context objective.",
                assembledContextDescription =
                    "Bounded model-context description.",
            )

        assertSame(
            reasoning,
            result.structuredReasoning,
        )
        assertSame(
            reasoning.toolUsingIntelligence,
            result.structuredReasoning.toolUsingIntelligence,
        )
        assertSame(
            reasoning.toolUsingIntelligence.routing,
            result.structuredReasoning.toolUsingIntelligence.routing,
        )
        assertSame(
            reasoning.toolUsingIntelligence.capability,
            result.structuredReasoning.toolUsingIntelligence.capability,
        )
    }

    @Test
    fun `assembled result normalizes model context metadata`() {
        val result =
            ModelContextAssemblyResult.create(
                status = ModelContextAssemblyStatus.ASSEMBLED,
                structuredReasoning = integratedReasoning(),
                modelContextObjective =
                    "  Normalized Stage 240 model-context objective.  ",
                assembledContextDescription =
                    "  Normalized Stage 240 assembled-context description.  ",
            )

        assertEquals(
            "Normalized Stage 240 model-context objective.",
            result.modelContextObjective,
        )

        assertEquals(
            "Normalized Stage 240 assembled-context description.",
            result.assembledContextDescription,
        )
    }

    @Test
    fun `assembled result rejects blank model context objective`() {
        assertFailsWith<IllegalArgumentException> {
            ModelContextAssemblyResult.create(
                status = ModelContextAssemblyStatus.ASSEMBLED,
                structuredReasoning = integratedReasoning(),
                modelContextObjective = "   ",
                assembledContextDescription =
                    "Bounded model-context description.",
            )
        }
    }

    @Test
    fun `assembled result rejects blank assembled context description`() {
        assertFailsWith<IllegalArgumentException> {
            ModelContextAssemblyResult.create(
                status = ModelContextAssemblyStatus.ASSEMBLED,
                structuredReasoning = integratedReasoning(),
                modelContextObjective =
                    "Bounded model-context objective.",
                assembledContextDescription = "   ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle model context objective`() {
        assertFailsWith<IllegalArgumentException> {
            ModelContextAssemblyResult.create(
                status = ModelContextAssemblyStatus.DEFERRED,
                structuredReasoning = integratedReasoning(),
                modelContextObjective =
                    "Must not be present.",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle assembled context metadata`() {
        assertFailsWith<IllegalArgumentException> {
            ModelContextAssemblyResult.create(
                status = ModelContextAssemblyStatus.DEFERRED,
                structuredReasoning = integratedReasoning(),
                assembledContextDescription =
                    "Must not be present.",
            )
        }
    }

    private fun integratedReasoning():
        StructuredReasoningIntegrationResult {
        return StructuredReasoningIntegrationCoordinator()
            .integrate(
                toolUsingIntelligence = preparedToolUse(),
                reasoningObjective =
                    "Bounded Stage 240 reasoning objective.",
                structuredReasoningDescription =
                    "Bounded Stage 240 structured-reasoning context.",
            )
    }

    private fun preparedToolUse(): ToolUsingIntelligenceResult {
        return ToolUsingIntelligenceCoordinator()
            .prepare(
                routing = routedModel(),
                capability = capability(),
                toolUseIntentDescription =
                    "Bounded Stage 240 upstream tool-use context.",
            )
    }

    private fun routedModel(): ModelRoutingResult {
        return ModelRoutingCoordinator()
            .route(
                providerArchitecture =
                    availableProviderArchitecture(),
                routingRationale =
                    "Explicit bounded Stage 240 model routing destination.",
            )
    }

    private fun availableProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId =
                    "provider:stage240:test",
                providerName =
                    "Stage 240 Test Provider",
                providerDescription =
                    "Provider-neutral Stage 240 context-assembly foundation.",
            )
    }

    private fun capability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage240-test",
                ),
            category =
                enumValues<CapabilityCategory>().first(),
            name =
                "Stage 240 Test Capability",
            description =
                "Existing Devil capability preserved through bounded Stage 240 model-context assembly.",
        )
    }
}
