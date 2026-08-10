package com.devil.app.internet

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AndroidInternetKnowledgeRequestTest {

    @Test
    fun `request preserves normalized HTTPS destination`() {
        val request =
            AndroidInternetKnowledgeRequest.create(
                "  https://example.com/knowledge?q=devil  ",
            )

        assertEquals(
            "https://example.com/knowledge?q=devil",
            request.uri.toString(),
        )
    }

    @Test
    fun `request rejects insecure HTTP destination`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidInternetKnowledgeRequest.create(
                "http://example.com/",
            )
        }
    }

    @Test
    fun `request rejects destination without host`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidInternetKnowledgeRequest.create(
                "https:///knowledge",
            )
        }
    }

    @Test
    fun `request rejects embedded credentials`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidInternetKnowledgeRequest.create(
                "https://user:secret@example.com/knowledge",
            )
        }
    }

    @Test
    fun `request rejects URL fragment`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidInternetKnowledgeRequest.create(
                "https://example.com/knowledge#section",
            )
        }
    }
}
