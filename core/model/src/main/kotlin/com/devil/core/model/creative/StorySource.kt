package com.devil.core.model.creative

/**
 * Immutable Stage 88 representation of one explicitly supplied story source.
 *
 * StorySource preserves only nonblank story content supplied to the
 * Story-to-Animation boundary.
 *
 * Constructing this value does not mean Devil:
 *
 * - inferred a story from conversation;
 * - understood the story;
 * - interpreted characters;
 * - inferred chronology;
 * - decomposed scenes;
 * - selected shots;
 * - created a storyboard;
 * - generated frames;
 * - created an animation timeline;
 * - selected a model or generator;
 * - selected a capability;
 * - executed anything;
 * - rendered media;
 * - created a file;
 * - established Observation, Verification, or Outcome;
 * - performed constitutional Learning;
 * - or created or committed Memory.
 *
 * STORY_SOURCE != STORY_UNDERSTANDING.
 * STORY_SOURCE != SCENE_DECOMPOSITION.
 * STORY_SOURCE != GENERATION_REQUEST.
 * STORY_SOURCE != EXECUTION.
 */
@ConsistentCopyVisibility
data class StorySource private constructor(
    val content: String,
) {
    companion object {

        fun from(rawContent: String): StorySource {
            val normalizedContent =
                rawContent.trim()

            require(normalizedContent.isNotEmpty()) {
                "Story source content must not be blank."
            }

            return StorySource(
                content = normalizedContent,
            )
        }
    }
}
