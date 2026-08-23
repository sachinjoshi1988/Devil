package com.devil.core.runtime.modelprovider

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage235ModelRoutingTest {

    @Test
    fun `available Stage 234 provider architecture routes with exact provenance`() {
        val architecture =
            availableProviderArchitecture()

        val result =
            ModelRoutingCoordinator()
                .route(
                    providerArchitecture = architecture,
                    routingRationale =
                        "  Explicit bounded routing destination for Stage 235.  ",
                )

        assertEquals(
            ModelRoutingStatus.ROUTED,
            result.status,
        )
        assertSame(
            architecture,
            result.providerArchitecture,
        )
        assertSame(
            architecture.provider,
            result.providerArchitecture.provider,
        )
        assertEquals(
            "Explicit bounded routing destination for Stage 235.",
            result.routingRationale,
        )
    }

    @Test
    fun `blank routing rationale keeps Stage 235 deferred`() {
        val architecture =
            availableProviderArchitecture()

        val result =
            ModelRoutingCoordinator()
                .route(
                    providerArchitecture = architecture,
                    routingRationale = "   ",
                )

        assertEquals(
            ModelRoutingStatus.DEFERRED,
            result.status,
        )
        assertSame(
            architecture,
            result.providerArchitecture,
        )
        assertNull(result.routingRationale)
    }

    @Test
    fun `deferred Stage 234 provider architecture keeps Stage 235 deferred`() {
        val architecture =
            ModelProviderArchitectureResult.create(
                status =
                    ModelProviderArchitectureStatus.DEFERRED,
            )

        val result =
            ModelRoutingCoordinator()
                .route(
                    providerArchitecture = architecture,
                    routingRationale =
                        "Explicit bounded routing destination.",
                )

        assertEquals(
            ModelRoutingStatus.DEFERRED,
            result.status,
        )
        assertSame(
            architecture,
            result.providerArchitecture,
        )
        assertNull(result.routingRationale)
    }

    @Test
    fun `routed result requires available Stage 234 provider architecture`() {
        val architecture =
            ModelProviderArchitectureResult.create(
                status =
                    ModelProviderArchitectureStatus.DEFERRED,
            )

        assertFailsWith<IllegalArgumentException> {
            ModelRoutingResult.create(
                status = ModelRoutingStatus.ROUTED,
                providerArchitecture = architecture,
                routingRationale =
                    "Explicit bounded routing destination.",
            )
        }
    }

    @Test
    fun `routed result preserves exact Stage 234 provider architecture and provider`() {
        val architecture =
            availableProviderArchitecture()

        val result =
            ModelRoutingResult.create(
                status = ModelRoutingStatus.ROUTED,
                providerArchitecture = architecture,
                routingRationale =
                    "Bounded Stage 235 routing rationale.",
            )

        assertSame(
            architecture,
            result.providerArchitecture,
        )
        assertSame(
            architecture.provider,
            result.providerArchitecture.provider,
        )
    }

    @Test
    fun `routed result normalizes routing rationale`() {
        val result =
            ModelRoutingResult.create(
                status = ModelRoutingStatus.ROUTED,
                providerArchitecture =
                    availableProviderArchitecture(),
                routingRationale =
                    "  Bounded normalized Stage 235 rationale.  ",
            )

        assertEquals(
            "Bounded normalized Stage 235 rationale.",
            result.routingRationale,
        )
    }

    @Test
    fun `routed result rejects blank routing rationale`() {
        assertFailsWith<IllegalArgumentException> {
            ModelRoutingResult.create(
                status = ModelRoutingStatus.ROUTED,
                providerArchitecture =
                    availableProviderArchitecture(),
                routingRationale = "   ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle routing rationale metadata`() {
        assertFailsWith<IllegalArgumentException> {
            ModelRoutingResult.create(
                status = ModelRoutingStatus.DEFERRED,
                providerArchitecture =
                    availableProviderArchitecture(),
                routingRationale =
                    "Must not be present.",
            )
        }
    }

    private fun availableProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId =
                    "provider:stage235:test",
                providerName =
                    "Stage 235 Test Provider",
                providerDescription =
                    "Provider-neutral Stage 235 routing foundation.",
            )
    }
}
