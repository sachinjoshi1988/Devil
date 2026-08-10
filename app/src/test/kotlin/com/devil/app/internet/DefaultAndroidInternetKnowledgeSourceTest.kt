package com.devil.app.internet

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class DefaultAndroidInternetKnowledgeSourceTest {

    @Test
    fun `default HTTPS source satisfies Internet Knowledge source contract`() {
        val source: AndroidInternetKnowledgeSource =
            DefaultAndroidInternetKnowledgeSource()

        assertIs<DefaultAndroidInternetKnowledgeSource>(
            source,
        )
    }

    @Test
    fun `source rejects non-positive connection timeout`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultAndroidInternetKnowledgeSource(
                connectTimeoutMilliseconds = 0,
            )
        }
    }

    @Test
    fun `source rejects non-positive read timeout`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultAndroidInternetKnowledgeSource(
                readTimeoutMilliseconds = 0,
            )
        }
    }

    @Test
    fun `source rejects non-positive response limit`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultAndroidInternetKnowledgeSource(
                maximumResponseBytes = 0,
            )
        }
    }
}
