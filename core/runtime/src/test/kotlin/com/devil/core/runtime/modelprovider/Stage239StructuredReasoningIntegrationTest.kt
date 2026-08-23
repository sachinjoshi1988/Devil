package com.devil.core.runtime.modelprovider

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage239StructuredReasoningIntegrationTest {

    @Test
    fun `prepared Stage 238 context produces integrated structured reasoning with exact provenance`() {
        val toolUse = preparedToolUse()

        val result =
            StructuredReasoningIntegrationCoordinator()
                .integrate(
                    toolUsingIntelligence = toolUse,
                    reasoningObjective =
                        "  Evaluate one bounded model-domain reasoning objective.  ",
                    structuredReasoningDescription =
                        "  Provider-neutral structured reasoning context without constitutional decision authority.  ",
                )

        assertEquals(
            StructuredReasoningIntegrationStatus.INTEGRATED,
            result.status,
        )

        assertSame(
            toolUse,
            result.toolUsingIntelligence,
        )

        assertSame(
            toolUse.routing,
            result.toolUsingIntelligence.routing,
        )

        assertSame(
            toolUse.routing.providerArchitecture,
            result.toolUsingIntelligence.routing.providerArchitecture,
        )

        assertSame(
            toolUse.capability,
            result.toolUsingIntelligence.capability,
        )

        assertEquals(
            "Evaluate one bounded model-domain reasoning objective.",
            result.reasoningObjective,
        )

        assertEquals(
            "Provider-neutral structured reasoning context without constitutional decision authority.",
            result.structuredReasoningDescription,
        )
    }

    @Test
    fun `blank reasoning objective keeps Stage 239 deferred`() {
        val toolUse = preparedToolUse()

        val result =
            StructuredReasoningIntegrationCoordinator()
                .integrate(
                    toolUsingIntelligence = toolUse,
                    reasoningObjective = "   ",
                    structuredReasoningDescription =
                        "Bounded structured reasoning context.",
                )

        assertEquals(
            StructuredReasoningIntegrationStatus.DEFERRED,
            result.status,
        )
        assertSame(toolUse, result.toolUsingIntelligence)
        assertNull(result.reasoningObjective)
        assertNull(result.structuredReasoningDescription)
    }

    @Test
    fun `blank structured reasoning description keeps Stage 239 deferred`() {
        val toolUse = preparedToolUse()

        val result =
            StructuredReasoningIntegrationCoordinator()
                .integrate(
                    toolUsingIntelligence = toolUse,
                    reasoningObjective =
                        "Bounded reasoning objective.",
                    structuredReasoningDescription = "   ",
                )

        assertEquals(
            StructuredReasoningIntegrationStatus.DEFERRED,
            result.status,
        )
        assertSame(toolUse, result.toolUsingIntelligence)
        assertNull(result.reasoningObjective)
        assertNull(result.structuredReasoningDescription)
    }

    @Test
    fun `deferred Stage 238 context keeps Stage 239 deferred`() {
        val routing = routedModel()

        val toolUse =
            ToolUsingIntelligenceResult.create(
                status = ToolUsingIntelligenceStatus.DEFERRED,
                routing = routing,
            )

        val result =
            StructuredReasoningIntegrationCoordinator()
                .integrate(
                    toolUsingIntelligence = toolUse,
                    reasoningObjective =
                        "Bounded reasoning objective.",
                    structuredReasoningDescription =
                        "Bounded structured reasoning context.",
                )

        assertEquals(
            StructuredReasoningIntegrationStatus.DEFERRED,
            result.status,
        )
        assertSame(toolUse, result.toolUsingIntelligence)
        assertNull(result.reasoningObjective)
        assertNull(result.structuredReasoningDescription)
    }

    @Test
    fun `integrated result requires prepared Stage 238 provenance`() {
        val deferredToolUse =
            ToolUsingIntelligenceResult.create(
                status = ToolUsingIntelligenceStatus.DEFERRED,
                routing = routedModel(),
            )

        assertFailsWith<IllegalArgumentException> {
            StructuredReasoningIntegrationResult.create(
                status =
                    StructuredReasoningIntegrationStatus.INTEGRATED,
                toolUsingIntelligence = deferredToolUse,
                reasoningObjective =
                    "Bounded reasoning objective.",
                structuredReasoningDescription =
                    "Bounded structured reasoning context.",
            )
        }
    }

    @Test
    fun `integrated result preserves exact Stage 238 routing and capability provenance`() {
        val toolUse = preparedToolUse()

        val result =
            StructuredReasoningIntegrationResult.create(
                status =
                    StructuredReasoningIntegrationStatus.INTEGRATED,
                toolUsingIntelligence = toolUse,
                reasoningObjective =
                    "Bounded reasoning objective.",
                structuredReasoningDescription =
                    "Bounded structured reasoning context.",
            )

        assertSame(
            toolUse,
            result.toolUsingIntelligence,
        )
        assertSame(
            toolUse.routing,
            result.toolUsingIntelligence.routing,
        )
        assertSame(
            toolUse.routing.providerArchitecture,
            result.toolUsingIntelligence.routing.providerArchitecture,
        )
        assertSame(
            toolUse.capability,
            result.toolUsingIntelligence.capability,
        )
    }

    @Test
    fun `integrated result normalizes reasoning metadata`() {
        val result =
            StructuredReasoningIntegrationResult.create(
                status =
                    StructuredReasoningIntegrationStatus.INTEGRATED,
                toolUsingIntelligence = preparedToolUse(),
                reasoningObjective =
                    "  Normalized reasoning objective.  ",
                structuredReasoningDescription =
                    "  Normalized structured reasoning description.  ",
            )

        assertEquals(
            "Normalized reasoning objective.",
            result.reasoningObjective,
        )
        assertEquals(
            "Normalized structured reasoning description.",
            result.structuredReasoningDescription,
        )
    }

    @Test
    fun `integrated result rejects blank reasoning objective`() {
        assertFailsWith<IllegalArgumentException> {
            StructuredReasoningIntegrationResult.create(
                status =
                    StructuredReasoningIntegrationStatus.INTEGRATED,
                toolUsingIntelligence = preparedToolUse(),
                reasoningObjective = "   ",
                structuredReasoningDescription =
                    "Bounded structured reasoning context.",
            )
        }
    }

    @Test
    fun `integrated result rejects blank structured reasoning description`() {
        assertFailsWith<IllegalArgumentException> {
            StructuredReasoningIntegrationResult.create(
                status =
                    StructuredReasoningIntegrationStatus.INTEGRATED,
                toolUsingIntelligence = preparedToolUse(),
                reasoningObjective =
                    "Bounded reasoning objective.",
                structuredReasoningDescription = "   ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle reasoning objective`() {
        assertFailsWith<IllegalArgumentException> {
            StructuredReasoningIntegrationResult.create(
                status =
                    StructuredReasoningIntegrationStatus.DEFERRED,
                toolUsingIntelligence = preparedToolUse(),
                reasoningObjective =
                    "Must not be present.",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle structured reasoning metadata`() {
        assertFailsWith<IllegalArgumentException> {
            StructuredReasoningIntegrationResult.create(
                status =
                    StructuredReasoningIntegrationStatus.DEFERRED,
                toolUsingIntelligence = preparedToolUse(),
                structuredReasoningDescription =
                    "Must not be present.",
            )
        }
    }

    private fun preparedToolUse(): ToolUsingIntelligenceResult {
        return ToolUsingIntelligenceCoordinator()
            .prepare(
                routing = routedModel(),
                capability = capability(),
                toolUseIntentDescription =
                    "Bounded Stage 239 upstream tool-use context.",
            )
    }

    private fun routedModel(): ModelRoutingResult {
        return ModelRoutingCoordinator()
            .route(
                providerArchitecture =
                    availableProviderArchitecture(),
                routingRationale =
                    "Explicit bounded Stage 239 model routing destination.",
            )
    }

    private fun availableProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId =
                    "provider:stage239:test",
                providerName =
                    "Stage 239 Test Provider",
                providerDescription =
                    "Provider-neutral Stage 239 structured-reasoning foundation.",
            )
    }

    private fun capability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage239-test",
                ),
            category =
                enumValues<CapabilityCategory>().first(),
            name =
                "Stage 239 Test Capability",
            description =
                "Existing Devil capability preserved through bounded Stage 239 structured reasoning.",
        )
    }
}
