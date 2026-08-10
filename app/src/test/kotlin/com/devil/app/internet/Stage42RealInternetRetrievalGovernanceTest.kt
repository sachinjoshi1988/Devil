package com.devil.app.internet

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Stage42RealInternetRetrievalGovernanceTest {

    @Test
    fun `real source remains bounded HTTPS retrieval rather than autonomous browsing`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/internet/DefaultAndroidInternetKnowledgeSource.kt",
            ).readText()

        assertTrue(
            source.contains(
                "HttpsURLConnection",
            ),
        )

        assertTrue(
            source.contains(
                "instanceFollowRedirects = false",
            ),
        )

        assertTrue(
            source.contains(
                "maximumResponseBytes",
            ),
        )

        assertTrue(
            source.contains(
                "Accept-Encoding",
            ),
        )

        assertTrue(
            source.contains(
                "isForbiddenAddress",
            ),
        )

        assertTrue(
            source.contains(
                "connection.disconnect()",
            ),
        )

        assertFalse(
            source.contains(
                "WebView",
            ),
        )

        assertFalse(
            source.contains(
                "ConversationInput(",
            ),
        )

        assertFalse(
            source.contains(
                "DefaultUnifiedDevilRuntime(",
            ),
        )
    }
}
