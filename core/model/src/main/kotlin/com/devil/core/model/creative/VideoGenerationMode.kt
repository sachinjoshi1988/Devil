package com.devil.core.model.creative

/**
 * Stage 173 provider-neutral Video Creation Assistance generation mode.
 *
 * TEXT_TO_VIDEO means the supplied text prompt is the primary creative-generation input.
 *
 * IMAGE_TO_VIDEO means a supplied image/reference-image context is intended to participate
 * in future governed video generation.
 *
 * This mode does not prove that any provider, model, capability, image bytes, or generated
 * video exists.
 *
 * GENERATION_MODE != PROVIDER.
 * GENERATION_MODE != EXECUTION.
 * IMAGE_TO_VIDEO != IMAGE_BYTES_INSPECTED.
 */
enum class VideoGenerationMode {
    TEXT_TO_VIDEO,
    IMAGE_TO_VIDEO,
}
