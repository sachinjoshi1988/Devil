package com.devil.core.runtime.modelprovider

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage237CloudModelIntegrationTest {

    @Test
    fun `routed Stage 235 context produces integrated cloud model context with exact provenance`() {
        val routing = routedModel()

        val result =
            CloudModelIntegrationCoordinator()
                .integrate(
                    routing = routing,
                    cloudModelId = "  cloud-model:stage237:test  ",
                    cloudModelDescription = "  Bounded cloud-model context.  ",
                    remoteServiceDescription = "  Provider-neutral remote-service integration context.  ",
                )

        assertEquals(CloudModelIntegrationStatus.INTEGRATED, result.status)
        assertSame(routing, result.routing)
        assertSame(routing.providerArchitecture, result.routing.providerArchitecture)
        assertSame(
            routing.providerArchitecture.provider,
            result.routing.providerArchitecture.provider,
        )
        assertEquals("cloud-model:stage237:test", result.cloudModelId)
        assertEquals("Bounded cloud-model context.", result.cloudModelDescription)
        assertEquals(
            "Provider-neutral remote-service integration context.",
            result.remoteServiceDescription,
        )
    }

    @Test
    fun `blank cloud model identifier keeps Stage 237 deferred`() {
        val result =
            CloudModelIntegrationCoordinator()
                .integrate(
                    routing = routedModel(),
                    cloudModelId = "   ",
                    cloudModelDescription = "Bounded cloud-model context.",
                    remoteServiceDescription = "Bounded remote-service context.",
                )

        assertEquals(CloudModelIntegrationStatus.DEFERRED, result.status)
        assertNull(result.cloudModelId)
        assertNull(result.cloudModelDescription)
        assertNull(result.remoteServiceDescription)
    }

    @Test
    fun `blank cloud model description keeps Stage 237 deferred`() {
        val result =
            CloudModelIntegrationCoordinator()
                .integrate(
                    routing = routedModel(),
                    cloudModelId = "cloud-model:stage237:test",
                    cloudModelDescription = "   ",
                    remoteServiceDescription = "Bounded remote-service context.",
                )

        assertEquals(CloudModelIntegrationStatus.DEFERRED, result.status)
        assertNull(result.cloudModelId)
        assertNull(result.cloudModelDescription)
        assertNull(result.remoteServiceDescription)
    }

    @Test
    fun `blank remote service description keeps Stage 237 deferred`() {
        val result =
            CloudModelIntegrationCoordinator()
                .integrate(
                    routing = routedModel(),
                    cloudModelId = "cloud-model:stage237:test",
                    cloudModelDescription = "Bounded cloud-model context.",
                    remoteServiceDescription = "   ",
                )

        assertEquals(CloudModelIntegrationStatus.DEFERRED, result.status)
        assertNull(result.cloudModelId)
        assertNull(result.cloudModelDescription)
        assertNull(result.remoteServiceDescription)
    }

    @Test
    fun `deferred Stage 235 routing keeps Stage 237 deferred`() {
        val routing =
            ModelRoutingResult.create(
                status = ModelRoutingStatus.DEFERRED,
                providerArchitecture = availableProviderArchitecture(),
            )

        val result =
            CloudModelIntegrationCoordinator()
                .integrate(
                    routing = routing,
                    cloudModelId = "cloud-model:stage237:test",
                    cloudModelDescription = "Bounded cloud-model context.",
                    remoteServiceDescription = "Bounded remote-service context.",
                )

        assertEquals(CloudModelIntegrationStatus.DEFERRED, result.status)
        assertSame(routing, result.routing)
        assertNull(result.cloudModelId)
        assertNull(result.cloudModelDescription)
        assertNull(result.remoteServiceDescription)
    }

    @Test
    fun `integrated result requires routed Stage 235 context`() {
        val routing =
            ModelRoutingResult.create(
                status = ModelRoutingStatus.DEFERRED,
                providerArchitecture = availableProviderArchitecture(),
            )

        assertFailsWith<IllegalArgumentException> {
            CloudModelIntegrationResult.create(
                status = CloudModelIntegrationStatus.INTEGRATED,
                routing = routing,
                cloudModelId = "cloud-model:stage237:test",
                cloudModelDescription = "Bounded cloud-model context.",
                remoteServiceDescription = "Bounded remote-service context.",
            )
        }
    }

    @Test
    fun `integrated result normalizes cloud metadata`() {
        val result =
            CloudModelIntegrationResult.create(
                status = CloudModelIntegrationStatus.INTEGRATED,
                routing = routedModel(),
                cloudModelId = "  cloud-model:stage237:normalized  ",
                cloudModelDescription = "  Normalized cloud model.  ",
                remoteServiceDescription = "  Normalized remote service.  ",
            )

        assertEquals("cloud-model:stage237:normalized", result.cloudModelId)
        assertEquals("Normalized cloud model.", result.cloudModelDescription)
        assertEquals("Normalized remote service.", result.remoteServiceDescription)
    }

    @Test
    fun `deferred result cannot smuggle cloud integration metadata`() {
        assertFailsWith<IllegalArgumentException> {
            CloudModelIntegrationResult.create(
                status = CloudModelIntegrationStatus.DEFERRED,
                routing = routedModel(),
                cloudModelId = "cloud-model:stage237:must-not-exist",
            )
        }
    }

    private fun routedModel(): ModelRoutingResult {
        return ModelRoutingCoordinator()
            .route(
                providerArchitecture = availableProviderArchitecture(),
                routingRationale = "Explicit bounded Stage 237 routing destination.",
            )
    }

    private fun availableProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId = "provider:stage237:test",
                providerName = "Stage 237 Test Provider",
                providerDescription =
                    "Provider-neutral Stage 237 cloud-model integration foundation.",
            )
    }
}
