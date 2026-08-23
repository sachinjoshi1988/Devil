package com.devil.core.runtime.modelprovider

import com.devil.core.model.modelprovider.ModelProviderId
import com.devil.core.model.modelprovider.ModelProviderRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage234ModelProviderArchitectureTest {

    @Test
    fun `explicit provider metadata produces available provider-neutral architecture`() {
        val result =
            ModelProviderArchitectureCoordinator()
                .prepare(
                    providerId = "  provider:stage234:test  ",
                    providerName = "  Stage 234 Test Provider  ",
                    providerDescription =
                        "  Provider-neutral structural model-provider context.  ",
                )

        assertEquals(
            ModelProviderArchitectureStatus.AVAILABLE,
            result.status,
        )

        val provider =
            requireNotNull(result.provider)

        assertEquals(
            "provider:stage234:test",
            provider.providerId.value,
        )
        assertEquals(
            "Stage 234 Test Provider",
            provider.name,
        )
        assertEquals(
            "Provider-neutral structural model-provider context.",
            provider.description,
        )
    }

    @Test
    fun `blank provider identifier keeps architecture deferred`() {
        val result =
            ModelProviderArchitectureCoordinator()
                .prepare(
                    providerId = "   ",
                    providerName = "Stage 234 Provider",
                    providerDescription = "Bounded provider context.",
                )

        assertEquals(
            ModelProviderArchitectureStatus.DEFERRED,
            result.status,
        )
        assertNull(result.provider)
    }

    @Test
    fun `blank provider name keeps architecture deferred`() {
        val result =
            ModelProviderArchitectureCoordinator()
                .prepare(
                    providerId = "provider:stage234:test",
                    providerName = "   ",
                    providerDescription = "Bounded provider context.",
                )

        assertEquals(
            ModelProviderArchitectureStatus.DEFERRED,
            result.status,
        )
        assertNull(result.provider)
    }

    @Test
    fun `blank provider description keeps architecture deferred`() {
        val result =
            ModelProviderArchitectureCoordinator()
                .prepare(
                    providerId = "provider:stage234:test",
                    providerName = "Stage 234 Provider",
                    providerDescription = "   ",
                )

        assertEquals(
            ModelProviderArchitectureStatus.DEFERRED,
            result.status,
        )
        assertNull(result.provider)
    }

    @Test
    fun `model provider id normalizes explicit identity`() {
        val providerId =
            ModelProviderId.from(
                "  provider:stage234:normalized  ",
            )

        assertEquals(
            "provider:stage234:normalized",
            providerId.value,
        )
    }

    @Test
    fun `model provider id rejects blank identity`() {
        assertFailsWith<IllegalArgumentException> {
            ModelProviderId.from("   ")
        }
    }

    @Test
    fun `provider record preserves exact provider identity and normalizes metadata`() {
        val providerId =
            ModelProviderId.from(
                "provider:stage234:record",
            )

        val provider =
            ModelProviderRecord.create(
                providerId = providerId,
                name = "  Provider Record  ",
                description =
                    "  Bounded Stage 234 provider representation.  ",
            )

        assertSame(
            providerId,
            provider.providerId,
        )
        assertEquals(
            "Provider Record",
            provider.name,
        )
        assertEquals(
            "Bounded Stage 234 provider representation.",
            provider.description,
        )
    }

    @Test
    fun `provider record rejects blank name`() {
        val providerId =
            ModelProviderId.from(
                "provider:stage234:blank-name",
            )

        assertFailsWith<IllegalArgumentException> {
            ModelProviderRecord.create(
                providerId = providerId,
                name = "   ",
                description = "Bounded provider description.",
            )
        }
    }

    @Test
    fun `provider record rejects blank description`() {
        val providerId =
            ModelProviderId.from(
                "provider:stage234:blank-description",
            )

        assertFailsWith<IllegalArgumentException> {
            ModelProviderRecord.create(
                providerId = providerId,
                name = "Stage 234 Provider",
                description = "   ",
            )
        }
    }

    @Test
    fun `available result preserves exact provider record`() {
        val provider =
            ModelProviderRecord.create(
                providerId =
                    ModelProviderId.from(
                        "provider:stage234:exact",
                    ),
                name = "Exact Provider",
                description =
                    "Exact bounded provider representation.",
            )

        val result =
            ModelProviderArchitectureResult.create(
                status =
                    ModelProviderArchitectureStatus.AVAILABLE,
                provider = provider,
            )

        assertSame(
            provider,
            result.provider,
        )
    }

    @Test
    fun `available result requires provider record`() {
        assertFailsWith<IllegalArgumentException> {
            ModelProviderArchitectureResult.create(
                status =
                    ModelProviderArchitectureStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `deferred result cannot contain provider record`() {
        val provider =
            ModelProviderRecord.create(
                providerId =
                    ModelProviderId.from(
                        "provider:stage234:deferred",
                    ),
                name = "Deferred Provider",
                description =
                    "Must not be present in deferred result.",
            )

        assertFailsWith<IllegalArgumentException> {
            ModelProviderArchitectureResult.create(
                status =
                    ModelProviderArchitectureStatus.DEFERRED,
                provider = provider,
            )
        }
    }
}
