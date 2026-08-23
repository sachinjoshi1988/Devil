package com.devil.core.runtime.modelprovider

/**
 * Stage 237 bounded Cloud Model Integration status.
 *
 * INTEGRATED means one exact ROUTED Stage 235 Model Routing result has been
 * associated with explicitly supplied bounded cloud-model integration metadata.
 *
 * DEFERRED means Stage 237 cannot truthfully claim bounded cloud-model
 * integration context.
 *
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
enum class CloudModelIntegrationStatus {
    INTEGRATED,
    DEFERRED,
}
