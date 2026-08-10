package com.devil.app.internet

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class Stage42InternetKnowledgeProductionCompositionTest {

    @Test
    fun `Devil application owns one bounded Stage 42 Internet Knowledge composition`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        assertTrue(
            source.contains(
                "val internetKnowledgeSource:",
            ),
        )

        assertTrue(
            source.contains(
                "DefaultAndroidInternetKnowledgeSource()",
            ),
        )

        assertTrue(
            source.contains(
                "val internetKnowledgeCoordinator:",
            ),
        )

        assertTrue(
            source.contains(
                "val internetKnowledgeSafetyCoordinator:",
            ),
        )

        assertTrue(
            source.contains(
                "source = internetKnowledgeSource",
            ),
        )
    }
}
