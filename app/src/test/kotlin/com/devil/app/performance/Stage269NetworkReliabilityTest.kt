package com.devil.app.performance

import com.devil.app.internet.AndroidInternetKnowledgeDocument
import com.devil.app.internet.AndroidInternetKnowledgeResult
import java.io.File
import java.net.URI
import com.devil.core.model.common.DevilTimestamp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 269 Network Reliability governance tests.
 *
 * Stage 269 evaluates existing retrieval evidence only and must not become a
 * retry engine, connectivity controller, offline subsystem, or authority.
 */
class Stage269NetworkReliabilityTest {

    @Test
    fun `available retrieval with complete bounded evidence becomes reliable`() {
        val retrieval =
            AndroidInternetKnowledgeResult.available(
                document =
                    AndroidInternetKnowledgeDocument.create(
                        sourceUri = URI("https://example.com/knowledge"),
                        retrievedAt = DevilTimestamp.fromEpochMilliseconds(1_755_734_400_000L),
                        mediaType = "text/plain",
                        content = "bounded external content",
                    ),
            )

        val evidence =
            DevilNetworkReliabilityEvidence(
                retrievalResult = retrieval,
                connectTimeoutBounded = true,
                readTimeoutBounded = true,
                connectionCleanupBounded = true,
            )

        val result =
            DevilNetworkReliabilityCoordinator()
                .evaluate(evidence)

        assertEquals(
            DevilNetworkReliabilityStatus.RELIABLE,
            result.status,
        )
        assertSame(evidence, result.evidence)
        assertSame(retrieval, result.evidence.retrievalResult)
    }

    @Test
    fun `unavailable retrieval remains degraded without inventing disconnection`() {
        val retrieval =
            AndroidInternetKnowledgeResult.unavailable()

        val evidence =
            completeEvidence(
                retrieval = retrieval,
            )

        val result =
            DevilNetworkReliabilityCoordinator()
                .evaluate(evidence)

        assertEquals(
            DevilNetworkReliabilityStatus.DEGRADED,
            result.status,
        )
        assertSame(retrieval, result.evidence.retrievalResult)
    }

    @Test
    fun `failed retrieval remains degraded without retry`() {
        val retrieval =
            AndroidInternetKnowledgeResult.failed(
                error = "Network retrieval failed.",
            )

        val result =
            DevilNetworkReliabilityCoordinator()
                .evaluate(
                    completeEvidence(
                        retrieval = retrieval,
                    ),
                )

        assertEquals(
            DevilNetworkReliabilityStatus.DEGRADED,
            result.status,
        )
        assertSame(retrieval, result.evidence.retrievalResult)
    }

    @Test
    fun `incomplete reliability evidence remains degraded`() {
        val retrieval =
            AndroidInternetKnowledgeResult.available(
                document =
                    AndroidInternetKnowledgeDocument.create(
                        sourceUri = URI("https://example.com/knowledge"),
                        retrievedAt = DevilTimestamp.fromEpochMilliseconds(1_755_734_400_000L),
                        mediaType = "text/plain",
                        content = "bounded external content",
                    ),
            )

        val evidence =
            completeEvidence(
                retrieval = retrieval,
            ).copy(
                connectionCleanupBounded = false,
            )

        val result =
            DevilNetworkReliabilityCoordinator()
                .evaluate(evidence)

        assertEquals(
            DevilNetworkReliabilityStatus.DEGRADED,
            result.status,
        )
        assertFalse(result.evidence.isComplete())
    }

    @Test
    fun `Stage 269 preserves network and constitutional boundaries`() {
        val source = productionSource()

        for (
            boundary in
                listOf(
                    "NETWORK_RELIABLE != CONNECTIVITY_GUARANTEED.",
                    "NETWORK_RELIABLE != RETRY_AUTHORIZED.",
                    "NETWORK_RELIABLE != RECONNECTED.",
                    "NETWORK_RELIABLE != OFFLINE_READY.",
                    "NETWORK_RELIABILITY != VERIFIED_OUTCOME.",
                    "NETWORK_RELIABILITY != AUTHORIZATION.",
                    "NETWORK_RELIABILITY != EXECUTION_APPROVAL.",
                    "FAILED_RETRIEVAL != RETRY_EXECUTED.",
                    "UNAVAILABLE_RETRIEVAL != NETWORK_DISCONNECTED.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 269 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 269 stops before Stage 270 Offline Behaviour`() {
        val source = productionSource()

        assertTrue(
            source.contains(
                "Stage 269 does not implement Stage 270 Offline Behaviour",
            ),
        )
    }

    @Test
    fun `Stage 269 contains no network execution or recovery wiring`() {
        val source = productionSource()

        for (
            forbidden in
                listOf(
                    "HttpURLConnection(",
                    "HttpsURLConnection(",
                    "openConnection(",
                    "ConnectivityManager",
                    "WorkManager",
                    "JobScheduler",
                    "UnifiedDevilRuntime",
                    "ExecutionRequest(",
                    "AuthorizationAuthority",
                    "RecoveryRequestCoordinator",
                )
        ) {
            assertFalse(
                source.contains(forbidden),
                "Stage 269 must not introduce operational wiring: $forbidden",
            )
        }
    }

    private fun completeEvidence(
        retrieval: AndroidInternetKnowledgeResult,
    ): DevilNetworkReliabilityEvidence =
        DevilNetworkReliabilityEvidence(
            retrievalResult = retrieval,
            connectTimeoutBounded = true,
            readTimeoutBounded = true,
            connectionCleanupBounded = true,
        )

    private fun productionSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/performance/DevilNetworkReliability.kt",
            "src/main/kotlin/com/devil/app/performance/DevilNetworkReliability.kt",
        )

    private fun readSource(
        vararg candidates: String,
    ): String =
        candidates
            .asSequence()
            .map(::File)
            .firstOrNull(File::isFile)
            ?.readText()
            ?: error(
                "Unable to locate Stage 269 source from: ${candidates.joinToString()}",
            )
}
