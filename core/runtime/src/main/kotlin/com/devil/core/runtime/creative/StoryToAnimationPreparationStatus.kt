package com.devil.core.runtime.creative

/**
 * Stage 88 bounded Story-to-Animation preparation status.
 *
 * PREPARED means one structurally valid StoryToAnimationRecord was constructed
 * from:
 *
 * - an existing Stage 87 Creative Media project;
 * - explicitly supplied story content;
 * - and explicitly supplied ordered scene summaries.
 *
 * PREPARED does not mean:
 *
 * - Devil inferred the story;
 * - Devil autonomously decomposed the story;
 * - story understanding was established;
 * - the scene sequence is semantically correct;
 * - authorization exists;
 * - a constitutional Decision exists;
 * - a Task or Plan exists;
 * - a capability was registered or selected;
 * - a model was selected;
 * - a renderer was selected;
 * - generation was requested;
 * - a storyboard exists;
 * - frames exist;
 * - an animation timeline exists;
 * - execution is approved;
 * - animation was generated;
 * - media was rendered;
 * - a file exists;
 * - Observation occurred;
 * - Verification occurred;
 * - an Outcome occurred;
 * - constitutional Learning occurred;
 * - Memory was committed;
 * - or Story-to-Animation state was persisted.
 *
 * DEFERRED means no truthful bounded Story-to-Animation representation was
 * produced.
 *
 * PREPARED != AUTHORIZED.
 * PREPARED != GENERATED.
 * PREPARED != EXECUTED.
 * GENERATED != VERIFIED_OUTCOME.
 */
enum class StoryToAnimationPreparationStatus {
    PREPARED,
    DEFERRED,
}
