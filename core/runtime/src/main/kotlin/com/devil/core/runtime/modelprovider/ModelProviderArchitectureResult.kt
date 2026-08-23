package com.devil.core.runtime.modelprovider

import com.devil.core.model.modelprovider.ModelProviderRecord

/**
 * Stage 234 bounded Model Provider Architecture result.
 *
 * AVAILABLE preserves exactly one provider-neutral ModelProviderRecord.
 *
 * DEFERRED contains no provider record.
 *
 * Stage 234 does not:
 *
 * - create another Devil intelligence;
 * - create another Brain, Constitution, Executive, Planner, Unified Devil Runtime,
 *   Memory Authority, or Security Authority;
 * - establish provider availability, health, credentials, or connectivity;
 * - establish local or cloud model integration;
 * - select or rank providers;
 * - implement Model Routing;
 * - invoke a provider or model;
 * - perform inference;
 * - assemble model context;
 * - invoke tools;
 * - establish structured reasoning;
 * - produce, interpret, verify, or trust model output;
 * - grant constitutional authorization;
 * - create Decision, Task, Plan, capability, or ExecutionRequest;
 * - execute local or remote capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - implement Stage 235 Model Routing;
 * - or implement Stages 236 through 243.
 *
 * MODEL_PROVIDER != DEVIL.
 * MODEL_PROVIDER != BRAIN.
 * MODEL_PROVIDER != AUTHORITY.
 * MODEL_PROVIDER_ARCHITECTURE != MODEL_ROUTING.
 * MODEL_PROVIDER_ARCHITECTURE != MODEL_INVOCATION.
 * MODEL_PROVIDER_ARCHITECTURE != MODEL_OUTPUT.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 */
@ConsistentCopyVisibility
data class ModelProviderArchitectureResult private constructor(
    val status: ModelProviderArchitectureStatus,
    val provider: ModelProviderRecord?,
) {
    companion object {

        fun create(
            status: ModelProviderArchitectureStatus,
            provider: ModelProviderRecord? = null,
        ): ModelProviderArchitectureResult {
            when (status) {
                ModelProviderArchitectureStatus.AVAILABLE -> {
                    require(provider != null) {
                        "Available Stage 234 Model Provider Architecture requires one model-provider record."
                    }
                }

                ModelProviderArchitectureStatus.DEFERRED -> {
                    require(provider == null) {
                        "Deferred Stage 234 Model Provider Architecture must not contain a model-provider record."
                    }
                }
            }

            return ModelProviderArchitectureResult(
                status = status,
                provider = provider,
            )
        }
    }
}
