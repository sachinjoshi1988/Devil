package com.devil.core.runtime.modelprovider

/**
 * Stage 237 bounded Cloud Model Integration result.
 *
 * INTEGRATED preserves:
 *
 * - one exact ROUTED Stage 235 Model Routing result;
 * - therefore the exact Stage 234 provider architecture and provider provenance;
 * - one normalized explicitly supplied cloud-model identifier;
 * - one normalized explicitly supplied cloud-model description;
 * - one normalized explicitly supplied remote-service integration description.
 *
 * DEFERRED preserves the exact upstream Stage 235 routing result and contains
 * no cloud-model integration metadata.
 *
 * Stage 237 does not:
 *
 * - create another Devil intelligence, Brain, Constitution, Executive, Planner,
 *   Unified Devil Runtime, Memory Authority, or Security Authority;
 * - create or modify provider identity or routing;
 * - establish provider availability, health, trust, credentials, API keys, or connectivity;
 * - open network connections or send HTTP requests;
 * - instantiate, invoke, or execute a cloud model;
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
 * - implement Stage 238 Tool-Using Intelligence;
 * - or implement Stages 239 through 243.
 *
 * MODEL_PROVIDER != DEVIL.
 * MODEL_PROVIDER != BRAIN.
 * MODEL_PROVIDER != AUTHORITY.
 * CLOUD_MODEL_INTEGRATION != MODEL_ROUTING.
 * CLOUD_MODEL_INTEGRATION != NETWORK_CONNECTION_ESTABLISHED.
 * CLOUD_MODEL_INTEGRATION != CREDENTIALS_AVAILABLE.
 * CLOUD_MODEL_INTEGRATION != API_KEY_AVAILABLE.
 * CLOUD_MODEL_INTEGRATION != PROVIDER_AVAILABLE.
 * CLOUD_MODEL_INTEGRATION != PROVIDER_HEALTHY.
 * CLOUD_MODEL_INTEGRATION != MODEL_INVOKED.
 * CLOUD_MODEL_INTEGRATION != INFERENCE_PERFORMED.
 * CLOUD_MODEL_INTEGRATION != MODEL_OUTPUT_AVAILABLE.
 * CLOUD_MODEL_INTEGRATION != AUTHORIZATION.
 * CLOUD_MODEL_INTEGRATION != EXECUTION.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 */
@ConsistentCopyVisibility
data class CloudModelIntegrationResult private constructor(
    val status: CloudModelIntegrationStatus,
    val routing: ModelRoutingResult,
    val cloudModelId: String?,
    val cloudModelDescription: String?,
    val remoteServiceDescription: String?,
) {
    companion object {

        fun create(
            status: CloudModelIntegrationStatus,
            routing: ModelRoutingResult,
            cloudModelId: String? = null,
            cloudModelDescription: String? = null,
            remoteServiceDescription: String? = null,
        ): CloudModelIntegrationResult {
            return when (status) {
                CloudModelIntegrationStatus.INTEGRATED -> {
                    require(routing.status == ModelRoutingStatus.ROUTED) {
                        "Integrated Stage 237 Cloud Model requires routed Stage 235 Model Routing."
                    }

                    val normalizedId =
                        requireNotNull(cloudModelId).trim()
                    val normalizedDescription =
                        requireNotNull(cloudModelDescription).trim()
                    val normalizedService =
                        requireNotNull(remoteServiceDescription).trim()

                    require(normalizedId.isNotEmpty()) {
                        "Stage 237 cloud-model identifier must not be blank."
                    }
                    require(normalizedDescription.isNotEmpty()) {
                        "Stage 237 cloud-model description must not be blank."
                    }
                    require(normalizedService.isNotEmpty()) {
                        "Stage 237 remote-service integration description must not be blank."
                    }

                    CloudModelIntegrationResult(
                        status = status,
                        routing = routing,
                        cloudModelId = normalizedId,
                        cloudModelDescription = normalizedDescription,
                        remoteServiceDescription = normalizedService,
                    )
                }

                CloudModelIntegrationStatus.DEFERRED -> {
                    require(cloudModelId == null) {
                        "Deferred Stage 237 Cloud Model Integration must not contain cloud-model identity metadata."
                    }
                    require(cloudModelDescription == null) {
                        "Deferred Stage 237 Cloud Model Integration must not contain cloud-model description metadata."
                    }
                    require(remoteServiceDescription == null) {
                        "Deferred Stage 237 Cloud Model Integration must not contain remote-service metadata."
                    }

                    CloudModelIntegrationResult(
                        status = status,
                        routing = routing,
                        cloudModelId = null,
                        cloudModelDescription = null,
                        remoteServiceDescription = null,
                    )
                }
            }
        }
    }
}
