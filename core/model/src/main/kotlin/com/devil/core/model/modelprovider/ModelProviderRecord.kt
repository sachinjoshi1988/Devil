package com.devil.core.model.modelprovider

/**
 * Stage 234 provider-neutral representation of one bounded model provider.
 *
 * The record preserves only explicitly supplied provider metadata:
 *
 * - one ModelProviderId;
 * - one nonblank provider name;
 * - one nonblank provider description.
 *
 * This representation does not create provider-specific execution architecture.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - create another Brain, Constitution, Executive, Planner, Unified Devil Runtime,
 *   Memory Authority, or Security Authority;
 * - identify or instantiate a specific model;
 * - establish local-model capability;
 * - establish cloud-model capability;
 * - establish provider availability or health;
 * - establish credentials or API keys;
 * - establish network connectivity;
 * - establish trust;
 * - grant constitutional authorization;
 * - select or rank providers;
 * - route prompts or requests;
 * - assemble model context;
 * - invoke inference;
 * - invoke tools;
 * - produce model output;
 * - accept model output as truth;
 * - create Decision, Task, Plan, capability, or ExecutionRequest;
 * - execute local or remote capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - or implement Stages 235 through 243.
 *
 * MODEL_PROVIDER != DEVIL.
 * MODEL_PROVIDER != BRAIN.
 * MODEL_PROVIDER != AUTHORITY.
 * MODEL_PROVIDER != CAPABILITY.
 * MODEL_PROVIDER_RECORD != PROVIDER_SELECTION.
 * MODEL_PROVIDER_RECORD != MODEL_ROUTING.
 * MODEL_PROVIDER_RECORD != MODEL_INVOCATION.
 * MODEL_PROVIDER_RECORD != MODEL_OUTPUT.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 */
@ConsistentCopyVisibility
data class ModelProviderRecord private constructor(
    val providerId: ModelProviderId,
    val name: String,
    val description: String,
) {
    companion object {

        fun create(
            providerId: ModelProviderId,
            name: String,
            description: String,
        ): ModelProviderRecord {
            val normalizedName =
                name.trim()

            val normalizedDescription =
                description.trim()

            require(normalizedName.isNotEmpty()) {
                "Model provider name must not be blank."
            }

            require(normalizedDescription.isNotEmpty()) {
                "Model provider description must not be blank."
            }

            return ModelProviderRecord(
                providerId = providerId,
                name = normalizedName,
                description = normalizedDescription,
            )
        }
    }
}
