package com.devil.app.internet

import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidInternetKnowledgeSafetyCoordinatorTest {

    @Test
    fun `coordinator preserves retrieval through bounded safety policy`() {
        val source =
            AndroidInternetKnowledgeSource { request ->
                AndroidInternetKnowledgeResult.available(
                    document =
                        AndroidInternetKnowledgeDocument.create(
                            sourceUri = request.uri,
                            mediaType = "text/plain",
                retrievedAt = stage74TestRetrievedAt(),
                            content =
                                "This external content remains untrusted.",
                        ),
                )
            }

        val coordinator =
            AndroidInternetKnowledgeSafetyCoordinator(
                knowledgeCoordinator =
                    AndroidInternetKnowledgeCoordinator(
                        source = source,
                    ),
            )

        val request =
            AndroidInternetKnowledgeRequest.create(
                "https://example.com/reference",
            )

        val result =
            coordinator.retrieveAndAssess(
                request = request,
            )

        assertEquals(
            AndroidInternetKnowledgeContentDisposition
                .ELIGIBLE_FOR_LATER_ANALYSIS,
            result.disposition,
        )

        assertEquals(
            URI(
                "https://example.com/reference",
            ),
            result.retrievedDocument?.sourceUri,
        )
    }

    @Test
    fun `coordinator does not fabricate Internet content when source is unavailable`() {
        val source =
            AndroidInternetKnowledgeSource {
                AndroidInternetKnowledgeResult.unavailable()
            }

        val coordinator =
            AndroidInternetKnowledgeSafetyCoordinator(
                knowledgeCoordinator =
                    AndroidInternetKnowledgeCoordinator(
                        source = source,
                    ),
            )

        val result =
            coordinator.retrieveAndAssess(
                request =
                    AndroidInternetKnowledgeRequest.create(
                        "https://example.com/reference",
                    ),
            )

        assertEquals(
            AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
            result.disposition,
        )

        assertEquals(
            null,
            result.retrievedDocument,
        )
    }
}
