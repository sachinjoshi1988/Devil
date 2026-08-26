package com.devil.app.internet

import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

/**
 * Stage 296 direct unit coverage for the existing Stage 75
 * AndroidInternetResearchAdmissionResult contract.
 *
 * This test surface validates existing factory invariants only.
 *
 * Stage 296 does not modify Internet research production behavior,
 * establish trust or truth, grant authorization, perform execution,
 * create Learning or Memory, establish Verification or Outcome,
 * or implement Stage 297 Integration Test Completion.
 */
class Stage296AndroidInternetResearchAdmissionResultTest {

    @Test
    fun `retrieval only result preserves exact safety evidence`() {
        val safety =
            AndroidInternetKnowledgeSafetyResult.create(
                requestedUri = URI("https://example.com/research"),
                retrievalStatus = AndroidInternetKnowledgeStatus.UNAVAILABLE,
                disposition =
                    AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
            )

        val result =
            AndroidInternetResearchAdmissionResult.create(
                status = AndroidInternetResearchAdmissionStatus.RETRIEVAL_ONLY,
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
    fun `admitted result rejects retrieval only safety evidence`() {
        val safety =
            AndroidInternetKnowledgeSafetyResult.create(
                requestedUri = URI("https://example.com/research"),
                retrievalStatus = AndroidInternetKnowledgeStatus.UNAVAILABLE,
                disposition =
                    AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidInternetResearchAdmissionResult.create(
                status = AndroidInternetResearchAdmissionStatus.ADMITTED,
                safety = safety,
            )
        }
    }
}
