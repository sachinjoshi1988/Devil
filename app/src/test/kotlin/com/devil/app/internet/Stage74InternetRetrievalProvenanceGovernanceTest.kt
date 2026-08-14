package com.devil.app.internet

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Stage74InternetRetrievalProvenanceGovernanceTest {

    @Test
    fun `production Internet source establishes retrieval time at network boundary`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/internet/DefaultAndroidInternetKnowledgeSource.kt",
            ).readText()

        assertTrue(
            source.contains(
                "AndroidInternetRetrievalTimeProvider",
            ),
        )

        assertTrue(
            source.contains(
                "retrievalTimeProvider.observedAt()",
            ),
        )

        assertTrue(
            source.contains(
                "AndroidInternetKnowledgeDocument.create(",
            ),
        )
    }

    @Test
    fun `Internet document model does not read the platform clock`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/internet/AndroidInternetKnowledgeDocument.kt",
            ).readText()

        assertFalse(
            source.contains(
                "System.currentTimeMillis()",
            ),
        )
    }
}
