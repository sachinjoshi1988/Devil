package com.devil.core.runtime.creative

/**
 * Stage 171 bounded Story-to-Animation Pipeline preparation status.
 *
 * PREPARED means one structurally valid provider-neutral production-pipeline
 * context was prepared from:
 *
 * - one exact Stage 88 StoryToAnimationRecord;
 * - one exact Stage 170 StoryToStoryboardRecord;
 * - one explicitly supplied ordered nonempty production-step sequence;
 * - one explicitly supplied production objective.
 *
 * PREPARED does not mean:
 *
 * - story generation occurred;
 * - storyboard generation occurred;
 * - shot planning was executed;
 * - character, location, or prop assets were approved or created;
 * - image, animation, audio, music, voice, or video generation occurred;
 * - editing or rendering occurred;
 * - a provider, model, generator, renderer, or capability was selected or invoked;
 * - execution was authorized or performed;
 * - constitutional Observation, Verification, or Outcome occurred;
 * - publishing was authorized;
 * - Stage 172–174 behavior was implemented.
 *
 * DEFERRED means no truthful Story-to-Animation Pipeline context was produced.
 *
 * PIPELINE_PREPARED != GENERATION.
 * PIPELINE_PREPARED != EXECUTION.
 * PLANNED_OBSERVATION != CONSTITUTIONAL_OBSERVATION.
 * PLANNED_VERIFICATION != CONSTITUTIONAL_VERIFICATION.
 */
enum class StoryToAnimationPipelinePreparationStatus {
    PREPARED,
    DEFERRED,
}
