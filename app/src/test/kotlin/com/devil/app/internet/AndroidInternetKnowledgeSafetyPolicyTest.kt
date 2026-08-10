package com.devil.app.internet

import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AndroidInternetKnowledgeSafetyPolicyTest {

    private val policy =
        AndroidInternetKnowledgeSafetyPolicy()

    @Test
    fun `same-origin HTTPS document is eligible only for later analysis`() {
        val request =
            AndroidInternetKnowledgeRequest.create(
                "https://example.com/knowledge",
            )

        val document =
            AndroidInternetKnowledgeDocument.create(
                sourceUri =
                    URI(
                        "https://example.com/knowledge",
                    ),
                mediaType = "text/plain",
                content =
                    "External content remains untrusted data.",
            )

        val result =
            policy.evaluate(
                request = request,
                retrievalResult =
                    AndroidInternetKnowledgeResult.available(
                        document = document,
                    ),
            )

        assertEquals(
            AndroidInternetKnowledgeStatus.AVAILABLE,
            result.retrievalStatus,
        )
        assertEquals(
            AndroidInternetKnowledgeContentDisposition
                .ELIGIBLE_FOR_LATER_ANALYSIS,
            result.disposition,
        )
        assertEquals(
            request.uri,
            result.requestedUri,
        )
        assertEquals(
            document,
            result.retrievedDocument,
        )
    }

    @Test
    fun `different Internet origin remains retrieval only`() {
        val request =
            AndroidInternetKnowledgeRequest.create(
                "https://example.com/knowledge",
            )

        val document =
            AndroidInternetKnowledgeDocument.create(
                sourceUri =
                    URI(
                        "https://other.example/knowledge",
                    ),
                content =
                    "Content from another origin.",
            )

        val result =
            policy.evaluate(
                request = request,
                retrievalResult =
                    AndroidInternetKnowledgeResult.available(
                        document = document,
                    ),
            )

        assertEquals(
            AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
            result.disposition,
        )
        assertNotNull(result.retrievedDocument)
    }

    @Test
    fun `explicit different HTTPS port remains retrieval only`() {
        val request =
            AndroidInternetKnowledgeRequest.create(
                "https://example.com/knowledge",
            )

        val document =
            AndroidInternetKnowledgeDocument.create(
                sourceUri =
                    URI(
                        "https://example.com:8443/knowledge",
                    ),
                content =
                    "External content.",
            )

        val result =
            policy.evaluate(
                request = request,
                retrievalResult =
                    AndroidInternetKnowledgeResult.available(
                        document = document,
                    ),
            )

        assertEquals(
            AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
            result.disposition,
        )
    }

    @Test
    fun `unavailable retrieval remains retrieval only without document`() {
        val request =
            AndroidInternetKnowledgeRequest.create(
                "https://example.com/knowledge",
            )

        val result =
            policy.evaluate(
                request = request,
                retrievalResult =
                    AndroidInternetKnowledgeResult.unavailable(),
            )

        assertEquals(
            AndroidInternetKnowledgeStatus.UNAVAILABLE,
            result.retrievalStatus,
        )
        assertEquals(
            AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
            result.disposition,
        )
        assertNull(result.retrievedDocument)
    }

    @Test
    fun `failed retrieval remains retrieval only without document`() {
        val request =
            AndroidInternetKnowledgeRequest.create(
                "https://example.com/knowledge",
            )

        val result =
            policy.evaluate(
                request = request,
                retrievalResult =
                    AndroidInternetKnowledgeResult.failed(
                        error = "Network retrieval failed.",
                    ),
            )

        assertEquals(
            AndroidInternetKnowledgeStatus.FAILED,
            result.retrievalStatus,
        )
        assertEquals(
            AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
            result.disposition,
        )
        assertNull(result.retrievedDocument)
    }
}
