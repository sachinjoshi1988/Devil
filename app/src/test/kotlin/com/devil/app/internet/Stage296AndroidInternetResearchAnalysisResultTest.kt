package com.devil.app.internet

import java.net.URI
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

/**
 * Stage 296 direct unit coverage for the existing Stage 76
 * AndroidInternetResearchAnalysisResult contract.
 *
 * Factory invariants only. No production behavior is modified.
 * Stage 297 Integration Test Completion is out of scope.
 */
class Stage296AndroidInternetResearchAnalysisResultTest {

    @Test
    fun `not analyzed result preserves retrieval only admission without description`() {
        val admission = retrievalOnlyAdmission()

        val result =
            AndroidInternetResearchAnalysisResult.create(
                status = AndroidInternetResearchAnalysisStatus.NOT_ANALYZED,
                admission = admission,
            )

        assertSame(admission, result.admission)
        assertNull(result.description)
    }

    @Test
    fun `not analyzed result rejects analysis description`() {
        val admission = retrievalOnlyAdmission()

        assertFailsWith<IllegalArgumentException> {
            AndroidInternetResearchAnalysisResult.create(
                status = AndroidInternetResearchAnalysisStatus.NOT_ANALYZED,
                admission = admission,
                description = "Must not be retained.",
            )
        }
    }

    private fun retrievalOnlyAdmission(): AndroidInternetResearchAdmissionResult {
        val safety =
            AndroidInternetKnowledgeSafetyResult.create(
                requestedUri = URI("https://example.com/research"),
                retrievalStatus = AndroidInternetKnowledgeStatus.UNAVAILABLE,
                disposition =
                    AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
            )

        return AndroidInternetResearchAdmissionResult.create(
            status = AndroidInternetResearchAdmissionStatus.RETRIEVAL_ONLY,
            safety = safety,
        )
    }
}
