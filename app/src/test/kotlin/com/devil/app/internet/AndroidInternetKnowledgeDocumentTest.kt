package com.devil.app.internet

import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AndroidInternetKnowledgeDocumentTest {

    @Test
    fun `document preserves bounded HTTPS retrieval provenance and content`() {
        val retrievedAt =
            stage74TestRetrievedAt()

        val document =
            AndroidInternetKnowledgeDocument.create(
                sourceUri =
                    URI(
                        "https://example.com/knowledge",
                    ),
                retrievedAt = retrievedAt,
                mediaType =
                    " text/plain ",
                content =
                    " External information. ",
            )

        assertEquals(
            "https://example.com/knowledge",
            document.sourceUri.toString(),
        )

        assertEquals(
            retrievedAt,
            document.retrievedAt,
        )

        assertEquals(
            "text/plain",
            document.mediaType,
        )

        assertEquals(
            "External information.",
            document.content,
        )
    }

    @Test
    fun `document rejects blank external content`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidInternetKnowledgeDocument.create(
                sourceUri =
                    URI(
                        "https://example.com/",
                    ),
                retrievedAt =
                    stage74TestRetrievedAt(),
                content = "   ",
            )
        }
    }
}
