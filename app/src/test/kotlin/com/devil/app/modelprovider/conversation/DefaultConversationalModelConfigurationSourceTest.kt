package com.devil.app.modelprovider.conversation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class DefaultConversationalModelConfigurationSourceTest {

    @Test
    fun `complete explicitly supplied configuration becomes available`() {
        val endpoint =
            "https://example.invalid/v1/conversation"

        val modelId =
            "bounded-conversation-model"

        val credential =
            "stage313-test-credential"

        val source: ConversationalModelConfigurationSource =
            DefaultConversationalModelConfigurationSource(
                endpointProvider = { endpoint },
                modelIdProvider = { modelId },
                credentialProvider = { credential },
            )

        val result = source.resolve()

        assertEquals(
            ConversationalModelConfigurationStatus.AVAILABLE,
            result.status,
        )

        val configuration =
            requireNotNull(result.configuration)

        assertEquals(endpoint, configuration.endpoint)
        assertEquals(modelId, configuration.modelId)
        assertEquals(credential, configuration.credential)
    }

    @Test
    fun `missing endpoint fails closed`() {
        val result =
            source(
                endpoint = null,
            ).resolve()

        assertEquals(
            ConversationalModelConfigurationStatus.UNAVAILABLE,
            result.status,
        )

        assertNull(result.configuration)
    }

    @Test
    fun `missing model identifier fails closed`() {
        val result =
            source(
                modelId = null,
            ).resolve()

        assertEquals(
            ConversationalModelConfigurationStatus.UNAVAILABLE,
            result.status,
        )

        assertNull(result.configuration)
    }

    @Test
    fun `missing credential fails closed`() {
        val result =
            source(
                credential = null,
            ).resolve()

        assertEquals(
            ConversationalModelConfigurationStatus.UNAVAILABLE,
            result.status,
        )

        assertNull(result.configuration)
    }

    @Test
    fun `blank values fail closed without fabricated defaults`() {
        listOf(
            source(endpoint = "   "),
            source(modelId = "   "),
            source(credential = "   "),
        ).forEach { source ->
            val result = source.resolve()

            assertEquals(
                ConversationalModelConfigurationStatus.UNAVAILABLE,
                result.status,
            )

            assertNull(result.configuration)
        }
    }

    @Test
    fun `valid supplied values are preserved exactly`() {
        val endpoint =
            "  https://example.invalid/custom-path  "

        val modelId =
            "  exact-model-id  "

        val credential =
            "  exact-test-credential  "

        val result =
            source(
                endpoint = endpoint,
                modelId = modelId,
                credential = credential,
            ).resolve()

        val configuration =
            requireNotNull(result.configuration)

        assertEquals(endpoint, configuration.endpoint)
        assertEquals(modelId, configuration.modelId)
        assertEquals(credential, configuration.credential)
    }

    @Test
    fun `available result preserves exact configuration object`() {
        val configuration =
            ConversationalModelConfiguration.create(
                endpoint = "https://example.invalid/v1/conversation",
                modelId = "bounded-model",
                credential = "test-credential",
            )

        val result =
            ConversationalModelConfigurationResult.available(
                configuration = configuration,
            )

        assertSame(
            configuration,
            result.configuration,
        )
    }

    private fun source(
        endpoint: String? =
            "https://example.invalid/v1/conversation",
        modelId: String? =
            "bounded-conversation-model",
        credential: String? =
            "stage313-test-credential",
    ): ConversationalModelConfigurationSource {
        return DefaultConversationalModelConfigurationSource(
            endpointProvider = { endpoint },
            modelIdProvider = { modelId },
            credentialProvider = { credential },
        )
    }
}
