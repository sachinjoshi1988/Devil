package com.devil.app.internet

import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AndroidInternetKnowledgeSafetyResultTest {

    @Test
    fun `eligible result preserves bounded external document`() {
        val document =
            AndroidInternetKnowledgeDocument.create(
                sourceUri =
                    URI(
                        "https://example.com/source",
                    ),
                retrievedAt = stage74TestRetrievedAt(),
                content =
                    "Untrusted external text.",
            )

        val result =
            AndroidInternetKnowledgeSafetyResult.create(
                requestedUri =
                    URI(
                        "https://example.com/source",
                    ),
                retrievalStatus =
                    AndroidInternetKnowledgeStatus.AVAILABLE,
                disposition =
                    AndroidInternetKnowledgeContentDisposition
                        .ELIGIBLE_FOR_LATER_ANALYSIS,
                retrievedDocument = document,
            )

        assertEquals(
            document,
            result.retrievedDocument,
        )
    }

    @Test
    fun `eligible disposition rejects unavailable retrieval`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidInternetKnowledgeSafetyResult.create(
                requestedUri =
                    URI(
                        "https://example.com/source",
                    ),
                retrievalStatus =
                    AndroidInternetKnowledgeStatus.UNAVAILABLE,
                disposition =
                    AndroidInternetKnowledgeContentDisposition
                        .ELIGIBLE_FOR_LATER_ANALYSIS,
            )
        }
    }

    @Test
    fun `non-available result rejects preserved document`() {
        val document =
            AndroidInternetKnowledgeDocument.create(
                sourceUri =
                    URI(
                        "https://example.com/source",
                    ),
                retrievedAt = stage74TestRetrievedAt(),
                content =
                    "External text.",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidInternetKnowledgeSafetyResult.create(
                requestedUri =
                    URI(
                        "https://example.com/source",
                    ),
                retrievalStatus =
                    AndroidInternetKnowledgeStatus.UNAVAILABLE,
                disposition =
                    AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
                retrievedDocument = document,
            )
        }
    }
}
