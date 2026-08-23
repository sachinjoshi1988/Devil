package com.devil.core.runtime.modelprovider

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage238ToolUsingIntelligenceTest {

    @Test
    fun `routed model context and existing capability produce prepared tool use with exact provenance`() {
        val routing = routedModel()
        val capability = capability()

        val result =
            ToolUsingIntelligenceCoordinator()
                .prepare(
                    routing = routing,
                    capability = capability,
                    toolUseIntentDescription =
                        "  Bounded model-domain association with an existing Devil capability.  ",
                )

        assertEquals(
            ToolUsingIntelligenceStatus.PREPARED,
            result.status,
        )
        assertSame(routing, result.routing)
        assertSame(
            routing.providerArchitecture,
            result.routing.providerArchitecture,
        )
        assertSame(
            routing.providerArchitecture.provider,
            result.routing.providerArchitecture.provider,
        )
        assertSame(capability, result.capability)
        assertEquals(
            "Bounded model-domain association with an existing Devil capability.",
            result.toolUseIntentDescription,
        )
    }

    @Test
    fun `blank tool use intent keeps Stage 238 deferred`() {
        val routing = routedModel()

        val result =
            ToolUsingIntelligenceCoordinator()
                .prepare(
                    routing = routing,
                    capability = capability(),
                    toolUseIntentDescription = "   ",
                )

        assertEquals(
            ToolUsingIntelligenceStatus.DEFERRED,
            result.status,
        )
        assertSame(routing, result.routing)
        assertNull(result.capability)
        assertNull(result.toolUseIntentDescription)
    }

    @Test
    fun `missing capability keeps Stage 238 deferred`() {
        val routing = routedModel()

        val result =
            ToolUsingIntelligenceCoordinator()
                .prepare(
                    routing = routing,
                    capability = null,
                    toolUseIntentDescription =
                        "Bounded tool-use intent.",
                )

        assertEquals(
            ToolUsingIntelligenceStatus.DEFERRED,
            result.status,
        )
        assertSame(routing, result.routing)
        assertNull(result.capability)
        assertNull(result.toolUseIntentDescription)
    }

    @Test
    fun `deferred routing keeps Stage 238 deferred`() {
        val routing =
            ModelRoutingResult.create(
                status = ModelRoutingStatus.DEFERRED,
                providerArchitecture =
                    availableProviderArchitecture(),
            )

        val result =
            ToolUsingIntelligenceCoordinator()
                .prepare(
                    routing = routing,
                    capability = capability(),
                    toolUseIntentDescription =
                        "Bounded tool-use intent.",
                )

        assertEquals(
            ToolUsingIntelligenceStatus.DEFERRED,
            result.status,
        )
        assertSame(routing, result.routing)
        assertNull(result.capability)
        assertNull(result.toolUseIntentDescription)
    }

    @Test
    fun `prepared result requires routed Stage 235 context`() {
        val routing =
            ModelRoutingResult.create(
                status = ModelRoutingStatus.DEFERRED,
                providerArchitecture =
                    availableProviderArchitecture(),
            )

        assertFailsWith<IllegalArgumentException> {
            ToolUsingIntelligenceResult.create(
                status = ToolUsingIntelligenceStatus.PREPARED,
                routing = routing,
                capability = capability(),
                toolUseIntentDescription =
                    "Bounded tool-use intent.",
            )
        }
    }

    @Test
    fun `prepared result preserves exact capability and normalizes intent`() {
        val routing = routedModel()
        val capability = capability()

        val result =
            ToolUsingIntelligenceResult.create(
                status = ToolUsingIntelligenceStatus.PREPARED,
                routing = routing,
                capability = capability,
                toolUseIntentDescription =
                    "  Normalized Stage 238 tool-use intent.  ",
            )

        assertSame(routing, result.routing)
        assertSame(capability, result.capability)
        assertEquals(
            "Normalized Stage 238 tool-use intent.",
            result.toolUseIntentDescription,
        )
    }

    @Test
    fun `prepared result rejects missing capability`() {
        assertFailsWith<IllegalArgumentException> {
            ToolUsingIntelligenceResult.create(
                status = ToolUsingIntelligenceStatus.PREPARED,
                routing = routedModel(),
                toolUseIntentDescription =
                    "Bounded tool-use intent.",
            )
        }
    }

    @Test
    fun `prepared result rejects blank tool use intent`() {
        assertFailsWith<IllegalArgumentException> {
            ToolUsingIntelligenceResult.create(
                status = ToolUsingIntelligenceStatus.PREPARED,
                routing = routedModel(),
                capability = capability(),
                toolUseIntentDescription = "   ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle capability`() {
        assertFailsWith<IllegalArgumentException> {
            ToolUsingIntelligenceResult.create(
                status = ToolUsingIntelligenceStatus.DEFERRED,
                routing = routedModel(),
                capability = capability(),
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle tool use metadata`() {
        assertFailsWith<IllegalArgumentException> {
            ToolUsingIntelligenceResult.create(
                status = ToolUsingIntelligenceStatus.DEFERRED,
                routing = routedModel(),
                toolUseIntentDescription =
                    "Must not be present.",
            )
        }
    }

    private fun routedModel(): ModelRoutingResult {
        return ModelRoutingCoordinator()
            .route(
                providerArchitecture =
                    availableProviderArchitecture(),
                routingRationale =
                    "Explicit bounded Stage 238 model routing destination.",
            )
    }

    private fun availableProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId = "provider:stage238:test",
                providerName = "Stage 238 Test Provider",
                providerDescription =
                    "Provider-neutral Stage 238 tool-using intelligence foundation.",
            )
    }

    private fun capability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage238-test",
                ),
            category =
                enumValues<CapabilityCategory>().first(),
            name =
                "Stage 238 Test Capability",
            description =
                "Existing Devil capability preserved for bounded Stage 238 tool-use preparation.",
        )
    }
}
