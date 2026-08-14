package com.devil.app.internet

import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class Stage75InternetResearchAdmissionTest {

    @Test
    fun `eligible Internet safety result becomes admitted without gaining trust or truth semantics`() {
        val document =
            AndroidInternetKnowledgeDocument.create(
                sourceUri =
                    URI(
                        "https://example.com/research",
                    ),
                retrievedAt =
                    stage74TestRetrievedAt(),
                mediaType = "text/plain",
                content =
                    "External research material remains untrusted data.",
            )

        val safety =
            AndroidInternetKnowledgeSafetyResult.create(
                requestedUri =
                    URI(
                        "https://example.com/research",
                    ),
                retrievalStatus =
                    AndroidInternetKnowledgeStatus.AVAILABLE,
                disposition =
                    AndroidInternetKnowledgeContentDisposition
                        .ELIGIBLE_FOR_LATER_ANALYSIS,
                retrievedDocument = document,
            )

        val result =
            AndroidInternetResearchAdmissionPolicy()
                .evaluate(
                    safety = safety,
                )

        assertEquals(
            AndroidInternetResearchAdmissionStatus.ADMITTED,
            result.status,
        )

        assertSame(
            safety,
            result.safety,
        )

        assertSame(
            document,
            result.safety.retrievedDocument,
        )
    }

    @Test
    fun `retrieval-only Internet safety result cannot become admitted`() {
        val document =
            AndroidInternetKnowledgeDocument.create(
                sourceUri =
                    URI(
                        "https://other.example/research",
                    ),
                retrievedAt =
                    stage74TestRetrievedAt(),
                content =
                    "External material from another origin.",
            )

        val safety =
            AndroidInternetKnowledgeSafetyResult.create(
                requestedUri =
                    URI(
                        "https://example.com/research",
                    ),
                retrievalStatus =
                    AndroidInternetKnowledgeStatus.AVAILABLE,
                disposition =
                    AndroidInternetKnowledgeContentDisposition
                        .RETRIEVAL_ONLY,
                retrievedDocument = document,
            )

        val result =
            AndroidInternetResearchAdmissionPolicy()
                .evaluate(
                    safety = safety,
                )

        assertEquals(
            AndroidInternetResearchAdmissionStatus.RETRIEVAL_ONLY,
            result.status,
        )

        assertSame(
            safety,
            result.safety,
        )
    }

    @Test
    fun `unavailable Internet retrieval remains retrieval only`() {
        val safety =
            AndroidInternetKnowledgeSafetyResult.create(
                requestedUri =
                    URI(
                        "https://example.com/research",
                    ),
                retrievalStatus =
                    AndroidInternetKnowledgeStatus.UNAVAILABLE,
                disposition =
                    AndroidInternetKnowledgeContentDisposition
                        .RETRIEVAL_ONLY,
            )

        val result =
            AndroidInternetResearchAdmissionCoordinator()
                .evaluate(
                    safety = safety,
                )

        assertEquals(
            AndroidInternetResearchAdmissionStatus.RETRIEVAL_ONLY,
            result.status,
        )

        assertSame(
            safety,
            result.safety,
        )
    }
}
