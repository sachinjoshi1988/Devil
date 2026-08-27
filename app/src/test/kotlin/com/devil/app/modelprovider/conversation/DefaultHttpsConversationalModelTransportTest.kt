package com.devil.app.modelprovider.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferenceRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultHttpsConversationalModelTransportTest {

    @Test
    fun `constructor rejects non-positive connection timeout`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultHttpsConversationalModelTransport(
                connectTimeoutMilliseconds = 0,
            )
        }

        assertFailsWith<IllegalArgumentException> {
            DefaultHttpsConversationalModelTransport(
                connectTimeoutMilliseconds = -1,
            )
        }
    }

    @Test
    fun `constructor rejects non-positive read timeout`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultHttpsConversationalModelTransport(
                readTimeoutMilliseconds = 0,
            )
        }

        assertFailsWith<IllegalArgumentException> {
            DefaultHttpsConversationalModelTransport(
                readTimeoutMilliseconds = -1,
            )
        }
    }

    @Test
    fun `constructor rejects non-positive response byte limit`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultHttpsConversationalModelTransport(
                maximumResponseBytes = 0,
            )
        }

        assertFailsWith<IllegalArgumentException> {
            DefaultHttpsConversationalModelTransport(
                maximumResponseBytes = -1,
            )
        }
    }

    @Test
    fun `non https endpoint fails closed and preserves trace identity`() {
        val request =
            request(
                traceValue =
                    "trace-stage-313-https-transport-http",
            )

        val result =
            transport().invoke(
                request = request,
                configuration =
                    configuration(
                        endpoint =
                            "http://example.invalid/v1/conversation",
                    ),
            )

        assertUnavailable(
            request = request,
            result = result,
        )
    }

    @Test
    fun `localhost endpoint fails closed and preserves trace identity`() {
        val request =
            request(
                traceValue =
                    "trace-stage-313-https-transport-localhost",
            )

        val result =
            transport().invoke(
                request = request,
                configuration =
                    configuration(
                        endpoint =
                            "https://localhost/v1/conversation",
                    ),
            )

        assertUnavailable(
            request = request,
            result = result,
        )
    }

    @Test
    fun `localhost subdomain fails closed and preserves trace identity`() {
        val request =
            request(
                traceValue =
                    "trace-stage-313-https-transport-localhost-subdomain",
            )

        val result =
            transport().invoke(
                request = request,
                configuration =
                    configuration(
                        endpoint =
                            "https://model.localhost/v1/conversation",
                    ),
            )

        assertUnavailable(
            request = request,
            result = result,
        )
    }

    @Test
    fun `loopback ipv4 endpoint fails closed and preserves trace identity`() {
        val request =
            request(
                traceValue =
                    "trace-stage-313-https-transport-loopback-ipv4",
            )

        val result =
            transport().invoke(
                request = request,
                configuration =
                    configuration(
                        endpoint =
                            "https://127.0.0.1/v1/conversation",
                    ),
            )

        assertUnavailable(
            request = request,
            result = result,
        )
    }

    @Test
    fun `private ipv4 endpoint fails closed and preserves trace identity`() {
        val request =
            request(
                traceValue =
                    "trace-stage-313-https-transport-private-ipv4",
            )

        val result =
            transport().invoke(
                request = request,
                configuration =
                    configuration(
                        endpoint =
                            "https://192.168.1.1/v1/conversation",
                    ),
            )

        assertUnavailable(
            request = request,
            result = result,
        )
    }

    @Test
    fun `link local ipv4 endpoint fails closed and preserves trace identity`() {
        val request =
            request(
                traceValue =
                    "trace-stage-313-https-transport-link-local",
            )

        val result =
            transport().invoke(
                request = request,
                configuration =
                    configuration(
                        endpoint =
                            "https://169.254.1.1/v1/conversation",
                    ),
            )

        assertUnavailable(
            request = request,
            result = result,
        )
    }

    @Test
    fun `unspecified ipv4 endpoint fails closed and preserves trace identity`() {
        val request =
            request(
                traceValue =
                    "trace-stage-313-https-transport-unspecified",
            )

        val result =
            transport().invoke(
                request = request,
                configuration =
                    configuration(
                        endpoint =
                            "https://0.0.0.0/v1/conversation",
                    ),
            )

        assertUnavailable(
            request = request,
            result = result,
        )
    }

    @Test
    fun `uri user info endpoint fails closed without exposing generated output`() {
        val request =
            request(
                traceValue =
                    "trace-stage-313-https-transport-user-info",
            )

        val result =
            transport().invoke(
                request = request,
                configuration =
                    configuration(
                        endpoint =
                            "https://user:secret@example.invalid/v1/conversation",
                    ),
            )

        assertUnavailable(
            request = request,
            result = result,
        )
    }

    @Test
    fun `fragment endpoint fails closed without generated output`() {
        val request =
            request(
                traceValue =
                    "trace-stage-313-https-transport-fragment",
            )

        val result =
            transport().invoke(
                request = request,
                configuration =
                    configuration(
                        endpoint =
                            "https://example.invalid/v1/conversation#fragment",
                    ),
            )

        assertUnavailable(
            request = request,
            result = result,
        )
    }

    @Test
    fun `malformed endpoint fails closed and preserves trace identity`() {
        val request =
            request(
                traceValue =
                    "trace-stage-313-https-transport-malformed",
            )

        val result =
            transport().invoke(
                request = request,
                configuration =
                    configuration(
                        endpoint =
                            "not a valid uri",
                    ),
            )

        assertUnavailable(
            request = request,
            result = result,
        )
    }

    @Test
    fun `unresolvable https destination fails closed without fabricated output`() {
        val request =
            request(
                traceValue =
                    "trace-stage-313-https-transport-unresolvable",
            )

        val result =
            transport().invoke(
                request = request,
                configuration =
                    configuration(
                        endpoint =
                            "https://stage313-model.invalid/v1/conversation",
                    ),
            )

        assertUnavailable(
            request = request,
            result = result,
        )
    }

    private fun transport():
        DefaultHttpsConversationalModelTransport {
        return DefaultHttpsConversationalModelTransport(
            connectTimeoutMilliseconds = 1_000,
            readTimeoutMilliseconds = 1_000,
            maximumResponseBytes = 4_096,
        )
    }

    private fun configuration(
        endpoint: String,
    ): ConversationalModelConfiguration {
        return ConversationalModelConfiguration.create(
            endpoint = endpoint,
            modelId = "stage-313-model",
            credential = "stage313-test-credential",
        )
    }

    private fun request(
        traceValue: String,
    ): ConversationalModelInferenceRequest {
        return ConversationalModelInferenceRequest.create(
            traceId =
                TraceId.from(
                    traceValue,
                ),
            content =
                "Stage 313 bounded HTTPS transport test.",
        )
    }

    private fun assertUnavailable(
        request: ConversationalModelInferenceRequest,
        result: ConversationalModelTransportResult,
    ) {
        assertEquals(
            request.traceId,
            result.traceId,
        )

        assertEquals(
            ConversationalModelTransportStatus.UNAVAILABLE,
            result.status,
        )

        assertNull(
            result.generatedText,
        )
    }
}
