package com.devil.core.runtime.modelprovider

/**
 * Stage 235 bounded Model Routing status.
 *
 * ROUTED means one exact AVAILABLE Stage 234 Model Provider Architecture result
 * has been preserved as the explicitly selected bounded routing destination.
 *
 * DEFERRED means Stage 235 cannot truthfully establish bounded model routing.
 *
 * Routing at this stage is structural selection only.
 *
 * MODEL_ROUTED != MODEL_INVOKED.
 * MODEL_ROUTED != INFERENCE_PERFORMED.
 * MODEL_ROUTED != MODEL_OUTPUT_AVAILABLE.
 * MODEL_ROUTED != PROVIDER_AVAILABLE.
 * MODEL_ROUTED != PROVIDER_HEALTHY.
 * MODEL_ROUTED != AUTHORIZATION.
 * MODEL_ROUTED != EXECUTION.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 */
enum class ModelRoutingStatus {
    ROUTED,
    DEFERRED,
}
