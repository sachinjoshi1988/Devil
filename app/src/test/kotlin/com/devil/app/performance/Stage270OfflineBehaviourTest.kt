package com.devil.app.performance

import com.devil.core.runtime.modelprovider.LocalModelFoundationCoordinator
import com.devil.core.runtime.modelprovider.LocalModelFoundationResult
import com.devil.core.runtime.modelprovider.LocalModelFoundationStatus
import com.devil.core.runtime.modelprovider.ModelProviderArchitectureCoordinator
import com.devil.core.runtime.modelprovider.ModelProviderArchitectureResult
import com.devil.core.runtime.modelprovider.ModelRoutingCoordinator
import com.devil.core.runtime.modelprovider.ModelRoutingResult
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 270 Offline Behaviour governance tests.
 *
 * Stage 270 evaluates explicitly supplied offline-operability evidence only.
 *
 * It must not become a model loader, inference runtime, network-fallback engine,
 * cache, persistence mechanism, execution path, or constitutional authority.
 */
class Stage270OfflineBehaviourTest {

    @Test
    fun `complete supplied offline prerequisites produce offline ready status`() {
        val foundation =
            availableLocalModelFoundation()

        val evidence =
            DevilOfflineBehaviourEvidence(
                localModelFoundation = foundation,
                modelFileAvailable = true,
                deviceCompatibilityEstablished = true,
                localRuntimeAvailable = true,
                offlineInvocationAvailable = true,
            )

        val result =
            DevilOfflineBehaviourCoordinator()
                .evaluate(
                    evidence = evidence,
                )

        assertEquals(
            DevilOfflineBehaviourStatus.OFFLINE_READY,
            result.status,
        )

        assertSame(
            evidence,
            result.evidence,
        )

        assertSame(
            foundation,
            result.evidence.localModelFoundation,
        )

        assertTrue(
            result.evidence.isComplete(),
        )
    }

    @Test
    fun `missing any supplied offline prerequisite remains not offline ready`() {
        val foundation =
            availableLocalModelFoundation()

        val evidence =
            DevilOfflineBehaviourEvidence(
                localModelFoundation = foundation,
                modelFileAvailable = true,
                deviceCompatibilityEstablished = true,
                localRuntimeAvailable = true,
                offlineInvocationAvailable = false,
            )

        val result =
            DevilOfflineBehaviourCoordinator()
                .evaluate(
                    evidence = evidence,
                )

        assertEquals(
            DevilOfflineBehaviourStatus.NOT_OFFLINE_READY,
            result.status,
        )

        assertSame(
            foundation,
            result.evidence.localModelFoundation,
        )

        assertFalse(
            result.evidence.isComplete(),
        )
    }

    @Test
    fun `deferred Stage 236 foundation cannot become offline ready`() {
        val routing =
            routedModel()

        val foundation =
            LocalModelFoundationCoordinator()
                .prepare(
                    routing = routing,
                    localModelId = "   ",
                    localModelDescription =
                        "Bounded local-model foundation.",
                )

        assertEquals(
            LocalModelFoundationStatus.DEFERRED,
            foundation.status,
        )

        val result =
            DevilOfflineBehaviourCoordinator()
                .evaluate(
                    evidence =
                        DevilOfflineBehaviourEvidence(
                            localModelFoundation = foundation,
                            modelFileAvailable = true,
                            deviceCompatibilityEstablished = true,
                            localRuntimeAvailable = true,
                            offlineInvocationAvailable = true,
                        ),
                )

        assertEquals(
            DevilOfflineBehaviourStatus.NOT_OFFLINE_READY,
            result.status,
        )

        assertSame(
            foundation,
            result.evidence.localModelFoundation,
        )
    }

    @Test
    fun `Stage 270 preserves exact Stage 236 provenance`() {
        val foundation =
            availableLocalModelFoundation()

        val result =
            DevilOfflineBehaviourResult.create(
                evidence =
                    DevilOfflineBehaviourEvidence(
                        localModelFoundation = foundation,
                        modelFileAvailable = true,
                        deviceCompatibilityEstablished = true,
                        localRuntimeAvailable = true,
                        offlineInvocationAvailable = true,
                    ),
            )

        assertSame(
            foundation,
            result.evidence.localModelFoundation,
        )

        assertSame(
            foundation.routing,
            result.evidence.localModelFoundation.routing,
        )

        assertSame(
            foundation.routing.providerArchitecture,
            result.evidence
                .localModelFoundation
                .routing
                .providerArchitecture,
        )

        assertSame(
            foundation.routing.providerArchitecture.provider,
            result.evidence
                .localModelFoundation
                .routing
                .providerArchitecture
                .provider,
        )
    }

    @Test
    fun `Stage 270 preserves offline and constitutional boundaries`() {
        val source =
            stage270Source()

        listOf(
            "OFFLINE_READY != MODEL_INVOKED.",
            "OFFLINE_READY != INFERENCE_PERFORMED.",
            "OFFLINE_READY != MODEL_OUTPUT_AVAILABLE.",
            "OFFLINE_READY != VERIFIED_OUTCOME.",
            "OFFLINE_READY != AUTHORIZATION.",
            "OFFLINE_READY != EXECUTION_APPROVAL.",
            "OFFLINE_READY != NETWORK_FALLBACK_EXECUTED.",
            "OFFLINE_READY != CACHE_AVAILABLE.",
            "OFFLINE_READY != MEMORY_PERSISTENCE.",
            "LOCAL_MODEL_FOUNDATION_AVAILABLE != OFFLINE_READY.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 270 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 270 stops before Stage 271 Crash Recovery`() {
        val source =
            stage270Source()

        assertTrue(
            source.contains(
                "Stage 271",
            ),
        )

        assertTrue(
            source.contains(
                "Crash Recovery",
            ),
        )
    }

    @Test
    fun `Stage 270 contains no operational model network cache persistence or authority wiring`() {
        val source =
            stage270Source()

        listOf(
            "HttpURLConnection",
            "HttpsURLConnection",
            "ConnectivityManager",
            "WorkManager",
            "JobScheduler",
            "FileInputStream",
            "FileOutputStream",
            "RoomDatabase",
            "SharedPreferences",
            "UnifiedDevilRuntime",
            "ExecutionRequest",
            "AuthorizationAuthority",
            "VerificationAuthority",
            "OutcomeAuthority",
        ).forEach { forbidden ->
            assertFalse(
                source.contains(forbidden),
                "Stage 270 must not introduce operational wiring: $forbidden",
            )
        }
    }

    private fun availableLocalModelFoundation():
        LocalModelFoundationResult {
        return LocalModelFoundationCoordinator()
            .prepare(
                routing = routedModel(),
                localModelId =
                    "local-model:stage270:test",
                localModelDescription =
                    "Bounded Stage 270 local-model foundation provenance.",
            )
    }

    private fun routedModel(): ModelRoutingResult {
        return ModelRoutingCoordinator()
            .route(
                providerArchitecture =
                    availableProviderArchitecture(),
                routingRationale =
                    "Explicit bounded Stage 270 routing destination.",
            )
    }

    private fun availableProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId =
                    "provider:stage270:test",
                providerName =
                    "Stage 270 Test Provider",
                providerDescription =
                    "Provider-neutral Stage 270 offline-behaviour fixture.",
            )
    }

    private fun stage270Source(): String {
        val candidates =
            listOf(
                File(
                    "app/src/main/kotlin/com/devil/app/performance/DevilOfflineBehaviour.kt",
                ),
                File(
                    "src/main/kotlin/com/devil/app/performance/DevilOfflineBehaviour.kt",
                ),
            )

        return candidates
            .firstOrNull { candidate ->
                candidate.isFile
            }
            ?.readText()
            ?: error(
                "Unable to locate Stage 270 source from: ${candidates.joinToString()}",
            )
    }
}
