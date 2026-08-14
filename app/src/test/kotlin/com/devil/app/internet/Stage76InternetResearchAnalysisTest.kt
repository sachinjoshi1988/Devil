package com.devil.app.internet

import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage76InternetResearchAnalysisTest {

    @Test
    fun `admitted Internet research receives bounded descriptive analysis`() {
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
                    " External material says that a bounded research observation exists. ",
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

        val admission =
            AndroidInternetResearchAdmissionPolicy()
                .evaluate(
                    safety = safety,
                )

        val result =
            AndroidInternetResearchAnalysisPolicy()
                .analyze(
                    admission = admission,
                )

        assertEquals(
            AndroidInternetResearchAnalysisStatus.ANALYZED,
            result.status,
        )

        assertSame(
            admission,
            result.admission,
        )

        assertSame(
            document,
            result.admission.safety.retrievedDocument,
        )

        assertEquals(
            "External material says that a bounded research observation exists.",
            result.description,
        )
    }

    @Test
    fun `retrieval-only Internet material cannot receive Stage 76 analysis`() {
        val document =
            AndroidInternetKnowledgeDocument.create(
                sourceUri =
                    URI(
                        "https://other.example/research",
                    ),
                retrievedAt =
                    stage74TestRetrievedAt(),
                content =
                    "External material from a different origin.",
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
                    AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
                retrievedDocument = document,
            )

        val admission =
            AndroidInternetResearchAdmissionPolicy()
                .evaluate(
                    safety = safety,
                )

        val result =
            AndroidInternetResearchAnalysisPolicy()
                .analyze(
                    admission = admission,
                )

        assertEquals(
            AndroidInternetResearchAnalysisStatus.NOT_ANALYZED,
            result.status,
        )

        assertSame(
            admission,
            result.admission,
        )

        assertNull(
            result.description,
        )
    }

    @Test
    fun `unavailable retrieval cannot be promoted into research analysis`() {
        val safety =
            AndroidInternetKnowledgeSafetyResult.create(
                requestedUri =
                    URI(
                        "https://example.com/research",
                    ),
                retrievalStatus =
                    AndroidInternetKnowledgeStatus.UNAVAILABLE,
                disposition =
                    AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
            )

        val admission =
            AndroidInternetResearchAdmissionCoordinator()
                .evaluate(
                    safety = safety,
                )

        val result =
            AndroidInternetResearchAnalysisCoordinator()
                .analyze(
                    admission = admission,
                )

        assertEquals(
            AndroidInternetResearchAnalysisStatus.NOT_ANALYZED,
            result.status,
        )

        assertNull(
            result.description,
        )

        assertSame(
            admission,
            result.admission,
        )
    }
}
