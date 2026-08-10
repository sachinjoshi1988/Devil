package com.devil.app.internet

import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AndroidInternetKnowledgeResultTest {

    @Test
    fun `available result preserves genuine external document`() {
        val document =
            AndroidInternetKnowledgeDocument.create(
                sourceUri =
                    URI(
                        "https://example.com/",
                    ),
                content =
                    "External knowledge.",
            )

        val result =
            AndroidInternetKnowledgeResult.available(
                document = document,
            )

        assertEquals(
            AndroidInternetKnowledgeStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            document,
            result.document,
        )
        assertNull(result.error)
    }

    @Test
    fun `unavailable result contains no fabricated document`() {
        val result =
            AndroidInternetKnowledgeResult.unavailable()

        assertEquals(
            AndroidInternetKnowledgeStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.document)
        assertNull(result.error)
    }

    @Test
    fun `failed result preserves bounded error only`() {
        val result =
            AndroidInternetKnowledgeResult.failed(
                error =
                    " Network retrieval failed. ",
            )

        assertEquals(
            AndroidInternetKnowledgeStatus.FAILED,
            result.status,
        )
        assertNull(result.document)
        assertEquals(
            "Network retrieval failed.",
            result.error,
        )
    }
}
