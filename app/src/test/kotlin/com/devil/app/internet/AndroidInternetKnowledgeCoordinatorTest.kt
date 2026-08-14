package com.devil.app.internet

import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidInternetKnowledgeCoordinatorTest {

    @Test
    fun `coordinator forwards explicit request to bounded source unchanged`() {
        val request =
            AndroidInternetKnowledgeRequest.create(
                "https://example.com/knowledge",
            )

        val expectedDocument =
            AndroidInternetKnowledgeDocument.create(
                sourceUri =
                    URI(
                        "https://example.com/knowledge",
                    ),
                retrievedAt = stage74TestRetrievedAt(),
                content =
                    "Bounded external knowledge.",
            )

        var receivedRequest:
            AndroidInternetKnowledgeRequest? = null

        val coordinator =
            AndroidInternetKnowledgeCoordinator(
                source =
                    AndroidInternetKnowledgeSource { suppliedRequest ->
                        receivedRequest = suppliedRequest

                        AndroidInternetKnowledgeResult.available(
                            document = expectedDocument,
                        )
                    },
            )

        val result =
            coordinator.retrieve(
                request = request,
            )

        assertEquals(
            request,
            receivedRequest,
        )

        assertEquals(
            AndroidInternetKnowledgeStatus.AVAILABLE,
            result.status,
        )

        assertEquals(
            expectedDocument,
            result.document,
        )
    }

    @Test
    fun `coordinator preserves unavailable source result without fabrication`() {
        val coordinator =
            AndroidInternetKnowledgeCoordinator(
                source =
                    AndroidInternetKnowledgeSource {
                        AndroidInternetKnowledgeResult.unavailable()
                    },
            )

        val result =
            coordinator.retrieve(
                request =
                    AndroidInternetKnowledgeRequest.create(
                        "https://example.com/",
                    ),
            )

        assertEquals(
            AndroidInternetKnowledgeStatus.UNAVAILABLE,
            result.status,
        )
    }
}
