package com.devil.core.runtime.modelprovider

/**
 * Stage 234 bounded Model Provider Architecture status.
 *
 * AVAILABLE means one structurally valid provider-neutral model-provider record
 * is represented.
 *
 * DEFERRED means Stage 234 cannot truthfully claim bounded model-provider
 * architecture availability.
 *
 * MODEL_PROVIDER_ARCHITECTURE_AVAILABLE != PROVIDER_AVAILABLE.
 * MODEL_PROVIDER_ARCHITECTURE_AVAILABLE != PROVIDER_HEALTHY.
 * MODEL_PROVIDER_ARCHITECTURE_AVAILABLE != PROVIDER_SELECTED.
 * MODEL_PROVIDER_ARCHITECTURE_AVAILABLE != MODEL_ROUTED.
 * MODEL_PROVIDER_ARCHITECTURE_AVAILABLE != MODEL_INVOKED.
 * MODEL_PROVIDER_ARCHITECTURE_AVAILABLE != MODEL_OUTPUT_AVAILABLE.
 * MODEL_PROVIDER_ARCHITECTURE_AVAILABLE != AUTHORIZATION.
 * MODEL_PROVIDER_ARCHITECTURE_AVAILABLE != EXECUTION.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 */
enum class ModelProviderArchitectureStatus {
    AVAILABLE,
    DEFERRED,
}
