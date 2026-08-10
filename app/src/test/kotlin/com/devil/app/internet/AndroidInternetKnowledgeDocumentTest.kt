package com.devil.app.internet

import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AndroidInternetKnowledgeDocumentTest {

    @Test
    fun `document preserves bounded HTTPS provenance and content`() {
        val document =
            AndroidInternetKnowledgeDocument.create(
                sourceUri =
                    URI(
                        "https://example.com/knowledge",
                    ),
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
                content = "   ",
            )
        }
    }
}
