package com.devil.core.runtime.modelprovider.conversation

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ConversationalModelInferenceContractTest {

    @Test
    fun `request normalizes bounded conversational content`() {
        val traceId = TraceId.from("trace-stage313-inference-001")

        val request =
            ConversationalModelInferenceRequest.create(
                traceId = traceId,
                content = "  Explain this clearly.  ",
            )

        assertEquals(traceId, request.traceId)
        assertEquals("Explain this clearly.", request.content)
    }

    @Test
    fun `request rejects blank conversational content`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationalModelInferenceRequest.create(
                traceId = TraceId.from("trace-stage313-inference-002"),
                content = "   ",
            )
        }
    }

    @Test
    fun `available inference preserves trace and normalized generated output`() {
        val traceId = TraceId.from("trace-stage313-inference-003")

        val result =
            ConversationalModelInferenceResult.available(
                traceId = traceId,
                generatedOutput = "  Generated model text.  ",
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationalModelInferenceStatus.AVAILABLE,
            result.status,
        )
        assertEquals("Generated model text.", result.generatedOutput)
        assertNull(result.errorDescription)
    }

    @Test
    fun `available inference rejects blank generated output`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationalModelInferenceResult.available(
                traceId = TraceId.from("trace-stage313-inference-004"),
                generatedOutput = "   ",
            )
        }
    }

    @Test
    fun `unavailable inference contains neither output nor failure`() {
        val traceId = TraceId.from("trace-stage313-inference-005")

        val result =
            ConversationalModelInferenceResult.unavailable(
                traceId = traceId,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationalModelInferenceStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.generatedOutput)
        assertNull(result.errorDescription)
    }

    @Test
    fun `failed inference contains normalized failure and no generated output`() {
        val traceId = TraceId.from("trace-stage313-inference-006")

        val result =
            ConversationalModelInferenceResult.failed(
                traceId = traceId,
                errorDescription = "  Provider boundary failed.  ",
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationalModelInferenceStatus.FAILED,
            result.status,
        )
        assertNull(result.generatedOutput)
        assertEquals(
            "Provider boundary failed.",
            result.errorDescription,
        )
    }

    @Test
    fun `failed inference rejects blank failure description`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationalModelInferenceResult.failed(
                traceId = TraceId.from("trace-stage313-inference-007"),
                errorDescription = "   ",
            )
        }
    }

    @Test
    fun `generated assistant response preserves exact available inference trace and text`() {
        val traceId = TraceId.from("trace-stage313-inference-008")
        val inference =
            ConversationalModelInferenceResult.available(
                traceId = traceId,
                generatedOutput = "Bounded generated response.",
            )

        val response =
            GeneratedAssistantResponse.from(
                inference = inference,
            )

        assertEquals(traceId, response.traceId)
        assertEquals(
            "Bounded generated response.",
            response.content,
        )
    }

    @Test
    fun `unavailable inference cannot become generated assistant response`() {
        val inference =
            ConversationalModelInferenceResult.unavailable(
                traceId = TraceId.from("trace-stage313-inference-009"),
            )

        assertFailsWith<IllegalArgumentException> {
            GeneratedAssistantResponse.from(
                inference = inference,
            )
        }
    }

    @Test
    fun `failed inference cannot become generated assistant response`() {
        val inference =
            ConversationalModelInferenceResult.failed(
                traceId = TraceId.from("trace-stage313-inference-010"),
                errorDescription = "Inference failed.",
            )

        assertFailsWith<IllegalArgumentException> {
            GeneratedAssistantResponse.from(
                inference = inference,
            )
        }
    }

    @Test
    fun `provider neutral port preserves explicit implementation boundary`() {
        val traceId = TraceId.from("trace-stage313-inference-011")
        val request =
            ConversationalModelInferenceRequest.create(
                traceId = traceId,
                content = "Hello Devil.",
            )

        val port =
            ConversationalModelInferencePort { suppliedRequest ->
                ConversationalModelInferenceResult.available(
                    traceId = suppliedRequest.traceId,
                    generatedOutput = "Generated through test boundary.",
                )
            }

        val result = port.infer(request)

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationalModelInferenceStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            "Generated through test boundary.",
            result.generatedOutput,
        )
    }
}
