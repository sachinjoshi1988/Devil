package com.devil.core.runtime.modelprovider

/**
 * Stage 235 bounded Model Routing result.
 *
 * ROUTED preserves:
 *
 * - one exact AVAILABLE Stage 234 Model Provider Architecture result;
 * - therefore the exact provider-neutral ModelProviderRecord preserved by it;
 * - one normalized explicitly supplied bounded routing rationale.
 *
 * DEFERRED preserves the exact upstream Stage 234 architecture result without
 * claiming that model routing was established and contains no routing rationale.
 *
 * Stage 235 does not:
 *
 * - create another Devil intelligence;
 * - create another Brain, Constitution, Executive, Planner, Unified Devil Runtime,
 *   Memory Authority, or Security Authority;
 * - create or modify model-provider identity;
 * - establish provider availability, health, credentials, trust, or connectivity;
 * - instantiate a local model;
 * - integrate a cloud model;
 * - discover providers;
 * - rank multiple providers;
 * - infer a preferred provider;
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
 * - implement Stage 236 Local Model Foundation;
 * - or implement Stages 237 through 243.
 *
 * MODEL_PROVIDER != DEVIL.
 * MODEL_PROVIDER != BRAIN.
 * MODEL_PROVIDER != AUTHORITY.
 * MODEL_ROUTING != CAPABILITY_SELECTION.
 * MODEL_ROUTED != MODEL_INVOKED.
 * MODEL_ROUTED != INFERENCE_PERFORMED.
 * MODEL_ROUTED != MODEL_OUTPUT_AVAILABLE.
 * MODEL_ROUTED != PROVIDER_AVAILABLE.
 * MODEL_ROUTED != PROVIDER_HEALTHY.
 * MODEL_ROUTED != AUTHORIZATION.
 * MODEL_ROUTED != EXECUTION.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 */
@ConsistentCopyVisibility
data class ModelRoutingResult private constructor(
    val status: ModelRoutingStatus,
    val providerArchitecture: ModelProviderArchitectureResult,
    val routingRationale: String?,
) {
    companion object {

        fun create(
            status: ModelRoutingStatus,
            providerArchitecture: ModelProviderArchitectureResult,
            routingRationale: String? = null,
        ): ModelRoutingResult {
            return when (status) {
                ModelRoutingStatus.ROUTED -> {
                    require(
                        providerArchitecture.status ==
                            ModelProviderArchitectureStatus.AVAILABLE,
                    ) {
                        "Routed Stage 235 Model Routing requires available Stage 234 Model Provider Architecture."
                    }

                    requireNotNull(providerArchitecture.provider) {
                        "Routed Stage 235 Model Routing requires one preserved Stage 234 model-provider record."
                    }

                    val normalizedRationale =
                        requireNotNull(routingRationale)
                            .trim()

                    require(normalizedRationale.isNotEmpty()) {
                        "Stage 235 model routing rationale must not be blank."
                    }

                    ModelRoutingResult(
                        status = status,
                        providerArchitecture = providerArchitecture,
                        routingRationale = normalizedRationale,
                    )
                }

                ModelRoutingStatus.DEFERRED -> {
                    require(routingRationale == null) {
                        "Deferred Stage 235 Model Routing must not contain routing rationale metadata."
                    }

                    ModelRoutingResult(
                        status = status,
                        providerArchitecture = providerArchitecture,
                        routingRationale = null,
                    )
                }
            }
        }
    }
}
