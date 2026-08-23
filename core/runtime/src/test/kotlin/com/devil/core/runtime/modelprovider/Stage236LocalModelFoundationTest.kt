package com.devil.core.runtime.modelprovider

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage236LocalModelFoundationTest {

    @Test
    fun `routed Stage 235 context produces available local model foundation with exact provenance`() {
        val routing =
            routedModel()

        val result =
            LocalModelFoundationCoordinator()
                .prepare(
                    routing = routing,
                    localModelId = "  local-model:stage236:test  ",
                    localModelDescription =
                        "  Bounded provider-neutral local-model foundation.  ",
                )

        assertEquals(
            LocalModelFoundationStatus.AVAILABLE,
            result.status,
        )

        assertSame(
            routing,
            result.routing,
        )

        assertSame(
            routing.providerArchitecture,
            result.routing.providerArchitecture,
        )

        assertSame(
            routing.providerArchitecture.provider,
            result.routing.providerArchitecture.provider,
        )

        assertEquals(
            "local-model:stage236:test",
            result.localModelId,
        )

        assertEquals(
            "Bounded provider-neutral local-model foundation.",
            result.localModelDescription,
        )
    }

    @Test
    fun `blank local model identifier keeps Stage 236 deferred`() {
        val routing =
            routedModel()

        val result =
            LocalModelFoundationCoordinator()
                .prepare(
                    routing = routing,
                    localModelId = "   ",
                    localModelDescription =
                        "Bounded local-model foundation.",
                )

        assertEquals(
            LocalModelFoundationStatus.DEFERRED,
            result.status,
        )

        assertSame(
            routing,
            result.routing,
        )

        assertNull(result.localModelId)
        assertNull(result.localModelDescription)
    }

    @Test
    fun `blank local model description keeps Stage 236 deferred`() {
        val routing =
            routedModel()

        val result =
            LocalModelFoundationCoordinator()
                .prepare(
                    routing = routing,
                    localModelId = "local-model:stage236:test",
                    localModelDescription = "   ",
                )

        assertEquals(
            LocalModelFoundationStatus.DEFERRED,
            result.status,
        )

        assertSame(
            routing,
            result.routing,
        )

        assertNull(result.localModelId)
        assertNull(result.localModelDescription)
    }

    @Test
    fun `deferred Stage 235 routing keeps Stage 236 deferred`() {
        val routing =
            ModelRoutingResult.create(
                status = ModelRoutingStatus.DEFERRED,
                providerArchitecture =
                    availableProviderArchitecture(),
            )

        val result =
            LocalModelFoundationCoordinator()
                .prepare(
                    routing = routing,
                    localModelId = "local-model:stage236:test",
                    localModelDescription =
                        "Bounded local-model foundation.",
                )

        assertEquals(
            LocalModelFoundationStatus.DEFERRED,
            result.status,
        )

        assertSame(
            routing,
            result.routing,
        )

        assertNull(result.localModelId)
        assertNull(result.localModelDescription)
    }

    @Test
    fun `available result requires routed Stage 235 context`() {
        val routing =
            ModelRoutingResult.create(
                status = ModelRoutingStatus.DEFERRED,
                providerArchitecture =
                    availableProviderArchitecture(),
            )

        assertFailsWith<IllegalArgumentException> {
            LocalModelFoundationResult.create(
                status = LocalModelFoundationStatus.AVAILABLE,
                routing = routing,
                localModelId = "local-model:stage236:test",
                localModelDescription =
                    "Bounded local-model foundation.",
            )
        }
    }

    @Test
    fun `available result preserves exact Stage 235 Stage 234 and provider provenance`() {
        val routing =
            routedModel()

        val result =
            LocalModelFoundationResult.create(
                status = LocalModelFoundationStatus.AVAILABLE,
                routing = routing,
                localModelId = "local-model:stage236:exact",
                localModelDescription =
                    "Exact bounded local-model foundation.",
            )

        assertSame(
            routing,
            result.routing,
        )

        assertSame(
            routing.providerArchitecture,
            result.routing.providerArchitecture,
        )

        assertSame(
            routing.providerArchitecture.provider,
            result.routing.providerArchitecture.provider,
        )
    }

    @Test
    fun `available result normalizes local model metadata`() {
        val result =
            LocalModelFoundationResult.create(
                status = LocalModelFoundationStatus.AVAILABLE,
                routing = routedModel(),
                localModelId =
                    "  local-model:stage236:normalized  ",
                localModelDescription =
                    "  Normalized bounded Stage 236 local model.  ",
            )

        assertEquals(
            "local-model:stage236:normalized",
            result.localModelId,
        )

        assertEquals(
            "Normalized bounded Stage 236 local model.",
            result.localModelDescription,
        )
    }

    @Test
    fun `available result rejects blank local model identifier`() {
        assertFailsWith<IllegalArgumentException> {
            LocalModelFoundationResult.create(
                status = LocalModelFoundationStatus.AVAILABLE,
                routing = routedModel(),
                localModelId = "   ",
                localModelDescription =
                    "Bounded local-model foundation.",
            )
        }
    }

    @Test
    fun `available result rejects blank local model description`() {
        assertFailsWith<IllegalArgumentException> {
            LocalModelFoundationResult.create(
                status = LocalModelFoundationStatus.AVAILABLE,
                routing = routedModel(),
                localModelId = "local-model:stage236:test",
                localModelDescription = "   ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle local model identity metadata`() {
        assertFailsWith<IllegalArgumentException> {
            LocalModelFoundationResult.create(
                status = LocalModelFoundationStatus.DEFERRED,
                routing = routedModel(),
                localModelId =
                    "local-model:stage236:must-not-exist",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle local model description metadata`() {
        assertFailsWith<IllegalArgumentException> {
            LocalModelFoundationResult.create(
                status = LocalModelFoundationStatus.DEFERRED,
                routing = routedModel(),
                localModelDescription =
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
                    "Explicit bounded Stage 236 routing destination.",
            )
    }

    private fun availableProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId =
                    "provider:stage236:test",
                providerName =
                    "Stage 236 Test Provider",
                providerDescription =
                    "Provider-neutral Stage 236 local-model foundation.",
            )
    }
}
