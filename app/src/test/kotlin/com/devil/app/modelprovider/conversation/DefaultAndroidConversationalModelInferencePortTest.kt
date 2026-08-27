package com.devil.app.modelprovider.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferenceRequest
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferenceStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame

class DefaultAndroidConversationalModelInferencePortTest {

    @Test
    fun `missing configuration fails closed without invoking transport`() {
        var transportInvoked = false

        val port =
            DefaultAndroidConversationalModelInferencePort(
                configurationSource =
                    ConversationalModelConfigurationSource {
                        ConversationalModelConfigurationResult.unavailable()
                    },
                transport =
                    ConversationalModelTransport { request, _ ->
                        transportInvoked = true

                        ConversationalModelTransportResult.unavailable(
                            traceId = request.traceId,
                        )
                    },
            )

        val request =
            request(
                traceValue =
                    "trace-stage-313-android-inference-001",
            )

        val result =
            port.infer(
                request = request,
            )

        assertEquals(
            request.traceId,
            result.traceId,
        )

        assertEquals(
            ConversationalModelInferenceStatus.UNAVAILABLE,
            result.status,
        )

        assertNull(
            result.generatedOutput,
        )

        assertFalse(
            transportInvoked,
        )
    }

    @Test
    fun `available configuration and generated transport result preserve bounded evidence`() {
        val configuration =
            ConversationalModelConfiguration.create(
                endpoint =
                    "https://example.invalid/v1/conversation",
                modelId =
                    "stage-313-model",
                credential =
                    "stage313-test-credential",
            )

        var receivedRequest:
            ConversationalModelInferenceRequest? = null

        var receivedConfiguration:
            ConversationalModelConfiguration? = null

        val port =
            DefaultAndroidConversationalModelInferencePort(
                configurationSource =
                    ConversationalModelConfigurationSource {
                        ConversationalModelConfigurationResult.available(
                            configuration = configuration,
                        )
                    },
                transport =
                    ConversationalModelTransport {
                            request,
                            suppliedConfiguration,
                        ->

                        receivedRequest =
                            request

                        receivedConfiguration =
                            suppliedConfiguration

                        ConversationalModelTransportResult.generated(
                            traceId =
                                request.traceId,
                            generatedText =
                                "Bounded generated response.",
                        )
                    },
            )

        val request =
            request(
                traceValue =
                    "trace-stage-313-android-inference-002",
            )

        val result =
            port.infer(
                request = request,
            )

        assertSame(
            request,
            receivedRequest,
        )

        assertSame(
            configuration,
            receivedConfiguration,
        )

        assertEquals(
            request.traceId,
            result.traceId,
        )

        assertEquals(
            ConversationalModelInferenceStatus.AVAILABLE,
            result.status,
        )

        assertEquals(
            "Bounded generated response.",
            result.generatedOutput,
        )

        assertNull(
            result.errorDescription,
        )
    }

    @Test
    fun `unavailable transport remains unavailable`() {
        val configuration =
            ConversationalModelConfiguration.create(
                endpoint =
                    "https://example.invalid/v1/conversation",
                modelId =
                    "stage-313-model",
                credential =
                    "stage313-test-credential",
            )

        val port =
            DefaultAndroidConversationalModelInferencePort(
                configurationSource =
                    ConversationalModelConfigurationSource {
                        ConversationalModelConfigurationResult.available(
                            configuration = configuration,
                        )
                    },
                transport =
                    ConversationalModelTransport { request, _ ->
                        ConversationalModelTransportResult.unavailable(
                            traceId =
                                request.traceId,
                        )
                    },
            )

        val request =
            request(
                traceValue =
                    "trace-stage-313-android-inference-003",
            )

        val result =
            port.infer(
                request = request,
            )

        assertEquals(
            request.traceId,
            result.traceId,
        )

        assertEquals(
            ConversationalModelInferenceStatus.UNAVAILABLE,
            result.status,
        )

        assertNull(
            result.generatedOutput,
        )

        assertNull(
            result.errorDescription,
        )
    }

    @Test
    fun `transport result from another trace is rejected`() {
        val configuration =
            ConversationalModelConfiguration.create(
                endpoint =
                    "https://example.invalid/v1/conversation",
                modelId =
                    "stage-313-model",
                credential =
                    "stage313-test-credential",
            )

        val port =
            DefaultAndroidConversationalModelInferencePort(
                configurationSource =
                    ConversationalModelConfigurationSource {
                        ConversationalModelConfigurationResult.available(
                            configuration = configuration,
                        )
                    },
                transport =
                    ConversationalModelTransport { _, _ ->
                        ConversationalModelTransportResult.generated(
                            traceId =
                                TraceId.from(
                                    "trace-stage-313-android-inference-other",
                                ),
                            generatedText =
                                "Wrong trace.",
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            port.infer(
                request =
                    request(
                        traceValue =
                            "trace-stage-313-android-inference-004",
                    ),
            )
        }
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
                "Stage 313 bounded conversation.",
        )
    }
}
