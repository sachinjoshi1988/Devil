package com.devil.core.model.modelprovider

/**
 * Stable identifier for one bounded model-provider representation.
 *
 * A ModelProviderId identifies provider context only.
 *
 * It does not:
 *
 * - identify Devil;
 * - identify the Devil Brain;
 * - identify a model instance;
 * - prove provider availability;
 * - prove provider health;
 * - establish provider trust;
 * - grant authorization;
 * - select a provider;
 * - route a request;
 * - invoke a provider or model;
 * - establish model output;
 * - establish constitutional Verification;
 * - establish verified truth.
 *
 * MODEL_PROVIDER_ID != DEVIL_IDENTITY.
 * MODEL_PROVIDER_ID != MODEL_IDENTITY.
 * MODEL_PROVIDER_ID != PROVIDER_AVAILABILITY.
 * MODEL_PROVIDER_ID != AUTHORIZATION.
 * MODEL_PROVIDER_ID != MODEL_ROUTING.
 * MODEL_PROVIDER_ID != MODEL_EXECUTION.
 */
@ConsistentCopyVisibility
data class ModelProviderId private constructor(
    val value: String,
) {
    companion object {

        fun from(
            rawValue: String,
        ): ModelProviderId {
            val normalizedValue =
                rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Model provider identity must not be blank."
            }

            return ModelProviderId(
                value = normalizedValue,
            )
        }
    }
}
