package com.devil.core.runtime.modelprovider

/**
 * Stage 236 bounded Local Model Foundation result.
 *
 * AVAILABLE preserves:
 *
 * - one exact ROUTED Stage 235 Model Routing result;
 * - therefore the exact Stage 234 provider architecture and provider provenance;
 * - one normalized explicitly supplied local-model identifier;
 * - one normalized explicitly supplied local-model description.
 *
 * DEFERRED preserves the exact upstream Stage 235 routing result without
 * claiming local-model foundation availability and contains no local-model
 * metadata.
 *
 * Stage 236 does not:
 *
 * - create another Devil intelligence;
 * - create another Brain, Constitution, Executive, Planner, Unified Devil Runtime,
 *   Memory Authority, or Security Authority;
 * - create or modify provider identity;
 * - create or modify Model Routing;
 * - establish provider availability, health, credentials, trust, or connectivity;
 * - download, install, locate, open, map, or load model files;
 * - instantiate or initialize a model runtime;
 * - establish CPU, GPU, NPU, RAM, storage, or device compatibility;
 * - establish offline readiness or offline operation;
 * - invoke a provider or model;
 * - perform inference;
 * - create or send prompts;
 * - assemble model context;
 * - invoke tools;
 * - establish structured reasoning;
 * - produce, interpret, accept, verify, or trust model output;
 * - grant constitutional authorization;
 * - create Decision, Task, Plan, capability, or ExecutionRequest;
 * - execute local or remote capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - implement Stage 237 Cloud Model Integration;
 * - or implement Stages 238 through 243.
 *
 * MODEL_PROVIDER != DEVIL.
 * MODEL_PROVIDER != BRAIN.
 * MODEL_PROVIDER != AUTHORITY.
 * LOCAL_MODEL_FOUNDATION != MODEL_ROUTING.
 * LOCAL_MODEL_FOUNDATION_AVAILABLE != MODEL_LOADED.
 * LOCAL_MODEL_FOUNDATION_AVAILABLE != MODEL_FILE_PRESENT.
 * LOCAL_MODEL_FOUNDATION_AVAILABLE != DEVICE_COMPATIBLE.
 * LOCAL_MODEL_FOUNDATION_AVAILABLE != OFFLINE_OPERATION_PROVEN.
 * LOCAL_MODEL_FOUNDATION_AVAILABLE != MODEL_INVOKED.
 * LOCAL_MODEL_FOUNDATION_AVAILABLE != INFERENCE_PERFORMED.
 * LOCAL_MODEL_FOUNDATION_AVAILABLE != MODEL_OUTPUT_AVAILABLE.
 * LOCAL_MODEL_FOUNDATION_AVAILABLE != AUTHORIZATION.
 * LOCAL_MODEL_FOUNDATION_AVAILABLE != EXECUTION.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 */
@ConsistentCopyVisibility
data class LocalModelFoundationResult private constructor(
    val status: LocalModelFoundationStatus,
    val routing: ModelRoutingResult,
    val localModelId: String?,
    val localModelDescription: String?,
) {
    companion object {

        fun create(
            status: LocalModelFoundationStatus,
            routing: ModelRoutingResult,
            localModelId: String? = null,
            localModelDescription: String? = null,
        ): LocalModelFoundationResult {
            return when (status) {
                LocalModelFoundationStatus.AVAILABLE -> {
                    require(routing.status == ModelRoutingStatus.ROUTED) {
                        "Available Stage 236 Local Model Foundation requires routed Stage 235 Model Routing."
                    }

                    val normalizedId =
                        requireNotNull(localModelId)
                            .trim()

                    val normalizedDescription =
                        requireNotNull(localModelDescription)
                            .trim()

                    require(normalizedId.isNotEmpty()) {
                        "Stage 236 local-model identifier must not be blank."
                    }

                    require(normalizedDescription.isNotEmpty()) {
                        "Stage 236 local-model description must not be blank."
                    }

                    LocalModelFoundationResult(
                        status = status,
                        routing = routing,
                        localModelId = normalizedId,
                        localModelDescription = normalizedDescription,
                    )
                }

                LocalModelFoundationStatus.DEFERRED -> {
                    require(localModelId == null) {
                        "Deferred Stage 236 Local Model Foundation must not contain local-model identity metadata."
                    }

                    require(localModelDescription == null) {
                        "Deferred Stage 236 Local Model Foundation must not contain local-model description metadata."
                    }

                    LocalModelFoundationResult(
                        status = status,
                        routing = routing,
                        localModelId = null,
                        localModelDescription = null,
                    )
                }
            }
        }
    }
}
