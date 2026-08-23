package com.devil.core.runtime.modelprovider

/**
 * Stage 236 bounded Local Model Foundation status.
 *
 * AVAILABLE means one exact ROUTED Stage 235 Model Routing result has been
 * associated with explicitly supplied bounded local-model metadata.
 *
 * DEFERRED means Stage 236 cannot truthfully claim bounded local-model
 * foundation availability.
 *
 * Local-model foundation at this stage is structural representation only.
 *
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
enum class LocalModelFoundationStatus {
    AVAILABLE,
    DEFERRED,
}
