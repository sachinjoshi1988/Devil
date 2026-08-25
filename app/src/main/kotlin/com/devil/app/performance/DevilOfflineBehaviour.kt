package com.devil.app.performance

import com.devil.core.runtime.modelprovider.LocalModelFoundationResult
import com.devil.core.runtime.modelprovider.LocalModelFoundationStatus

/**
 * Stage 270 Offline Behaviour.
 *
 * This bounded contract evaluates explicitly supplied offline-operability evidence
 * while preserving the exact Stage 236 Local Model Foundation result as upstream
 * provenance.
 *
 * Stage 236 remains authoritative for local-model foundation representation.
 *
 * OFFLINE_READY != MODEL_INVOKED.
 * OFFLINE_READY != INFERENCE_PERFORMED.
 * OFFLINE_READY != MODEL_OUTPUT_AVAILABLE.
 * OFFLINE_READY != VERIFIED_OUTCOME.
 * OFFLINE_READY != AUTHORIZATION.
 * OFFLINE_READY != EXECUTION_APPROVAL.
 * OFFLINE_READY != NETWORK_FALLBACK_EXECUTED.
 * OFFLINE_READY != CACHE_AVAILABLE.
 * OFFLINE_READY != MEMORY_PERSISTENCE.
 * LOCAL_MODEL_FOUNDATION_AVAILABLE != OFFLINE_READY.
 *
 * Stage 270 does not download, install, locate, open, map, or load model files.
 * It does not instantiate a local inference runtime, execute inference, perform
 * network fallback, create caching, persist memory, or implement Stage 271
 * Crash Recovery or any later Phase-S reliability behavior.
 */
enum class DevilOfflineBehaviourStatus {
    OFFLINE_READY,
    NOT_OFFLINE_READY,
}

/**
 * Explicitly supplied Stage 270 offline-operability evidence.
 *
 * These flags describe supplied evidence only. They do not independently perform
 * filesystem inspection, hardware inspection, model loading, runtime creation,
 * inference, network switching, execution, Observation, Verification, or Outcome.
 */
data class DevilOfflineBehaviourEvidence(
    val localModelFoundation: LocalModelFoundationResult,
    val modelFileAvailable: Boolean,
    val deviceCompatibilityEstablished: Boolean,
    val localRuntimeAvailable: Boolean,
    val offlineInvocationAvailable: Boolean,
) {
    fun isComplete(): Boolean =
        localModelFoundation.status ==
            LocalModelFoundationStatus.AVAILABLE &&
            modelFileAvailable &&
            deviceCompatibilityEstablished &&
            localRuntimeAvailable &&
            offlineInvocationAvailable
}

/**
 * Bounded Stage 270 Offline Behaviour result.
 *
 * OFFLINE_READY means only that every explicitly required Stage 270
 * offline-operability prerequisite was supplied and the exact Stage 236 Local
 * Model Foundation result is AVAILABLE.
 *
 * It does not mean a model was invoked, inference occurred, output was produced,
 * an action was authorized, an execution occurred, or an Outcome was verified.
 */
@ConsistentCopyVisibility
data class DevilOfflineBehaviourResult private constructor(
    val status: DevilOfflineBehaviourStatus,
    val evidence: DevilOfflineBehaviourEvidence,
) {
    companion object {
        fun create(
            evidence: DevilOfflineBehaviourEvidence,
        ): DevilOfflineBehaviourResult =
            DevilOfflineBehaviourResult(
                status =
                    if (evidence.isComplete()) {
                        DevilOfflineBehaviourStatus.OFFLINE_READY
                    } else {
                        DevilOfflineBehaviourStatus.NOT_OFFLINE_READY
                    },
                evidence = evidence,
            )
    }
}

/**
 * Stage 270 bounded Offline Behaviour coordinator.
 *
 * It evaluates explicitly supplied evidence only and preserves the exact Stage 236
 * Local Model Foundation object carried by that evidence.
 *
 * It does not inspect files, inspect hardware, load models, instantiate runtimes,
 * perform inference, retry or reconnect networking, switch network transports,
 * cache responses, execute capabilities, grant authorization, or establish
 * constitutional Observation, Verification, or Outcome.
 */
class DevilOfflineBehaviourCoordinator {
    fun evaluate(
        evidence: DevilOfflineBehaviourEvidence,
    ): DevilOfflineBehaviourResult =
        DevilOfflineBehaviourResult.create(
            evidence = evidence,
        )
}
