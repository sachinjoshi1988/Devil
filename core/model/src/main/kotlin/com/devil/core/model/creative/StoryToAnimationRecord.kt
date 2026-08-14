package com.devil.core.model.creative

/**
 * Immutable Stage 88 representation of one bounded Story-to-Animation
 * preparation beneath one existing Stage 87 Creative Media project.
 *
 * The existing CreativeMediaProjectRecord remains the parent Creative Media
 * project and therefore preserves the original Stage 87 project identity and
 * objective.
 *
 * Stage 88 adds only:
 *
 * - one explicitly supplied StorySource;
 * - and one explicitly supplied ordered nonempty scene sequence.
 *
 * Scene decomposition is supplied to this boundary. This record does not infer
 * scenes from story prose.
 *
 * Scene positions must be contiguous and begin at one.
 *
 * This record deliberately contains no:
 *
 * - another Devil intelligence;
 * - another Brain;
 * - another Constitution;
 * - another Executive;
 * - another Planner;
 * - another Unified Devil Runtime;
 * - Story-to-Animation-specific Memory Authority;
 * - Story-to-Animation-specific Security Authority;
 * - identity authority;
 * - trust;
 * - authentication;
 * - authorization;
 * - security session;
 * - constitutional Decision;
 * - Task;
 * - Plan;
 * - capability binding;
 * - model selection;
 * - renderer selection;
 * - generation request;
 * - execution request;
 * - shot plan;
 * - storyboard;
 * - frame sequence;
 * - keyframes;
 * - animation timeline;
 * - generated image;
 * - generated audio;
 * - generated video;
 * - rendered asset;
 * - file path;
 * - file bytes;
 * - Observation;
 * - Verification;
 * - Outcome;
 * - World Model mutation;
 * - constitutional Learning result;
 * - Memory commitment;
 * - or persistence authority.
 *
 * STORY_TO_ANIMATION = DOMAIN_OF_ONE_DEVIL_INTELLIGENCE.
 * STORY_TO_ANIMATION != ANOTHER_INTELLIGENCE.
 * STORY != SCENE_DECOMPOSITION.
 * SCENE_SEQUENCE != FRAME_SEQUENCE.
 * SCENE_SEQUENCE != ANIMATION_TIMELINE.
 * PREPARATION != GENERATION.
 * PREPARATION != EXECUTION.
 */
@ConsistentCopyVisibility
data class StoryToAnimationRecord private constructor(
    val creativeProject: CreativeMediaProjectRecord,
    val story: StorySource,
    val scenes: List<AnimationSceneRecord>,
) {
    companion object {

        fun create(
            creativeProject: CreativeMediaProjectRecord,
            story: StorySource,
            scenes: List<AnimationSceneRecord>,
        ): StoryToAnimationRecord {
            require(scenes.isNotEmpty()) {
                "Story-to-Animation preparation requires at least one supplied scene."
            }

            scenes.forEachIndexed { index, scene ->
                val expectedPosition =
                    index + 1

                require(scene.position == expectedPosition) {
                    "Story-to-Animation scenes must use contiguous ordered positions beginning at one."
                }
            }

            return StoryToAnimationRecord(
                creativeProject = creativeProject,
                story = story,
                scenes = scenes.toList(),
            )
        }
    }
}
