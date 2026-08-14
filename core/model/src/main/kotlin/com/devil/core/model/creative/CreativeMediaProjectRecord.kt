package com.devil.core.model.creative

/**
 * Immutable Stage 87 representation of one bounded Creative Media project.
 *
 * The record preserves only explicitly supplied Creative Media metadata:
 *
 * - one Creative Media project identity;
 * - and one bounded Creative Media objective.
 *
 * This record deliberately contains no:
 *
 * - Brain;
 * - Constitution;
 * - Executive;
 * - Planner;
 * - separate Creative Media intelligence;
 * - Creative Media-specific Unified Devil Runtime;
 * - Creative Media-specific Memory Authority;
 * - Creative Media-specific Security Authority;
 * - subject authentication;
 * - trust assessment;
 * - authorization;
 * - security session;
 * - Decision;
 * - Task;
 * - Plan;
 * - capability binding;
 * - generation model;
 * - generation request;
 * - execution request;
 * - rendered media;
 * - generated asset;
 * - file path;
 * - file bytes;
 * - Observation;
 * - Verification;
 * - Outcome;
 * - World Model mutation;
 * - constitutional Learning result;
 * - Memory commitment;
 * - persistence authority;
 * - scene decomposition;
 * - frame sequence;
 * - animation timeline;
 * - or Story-to-Animation representation.
 *
 * CREATIVE_MEDIA = DOMAIN_OF_ONE_DEVIL_INTELLIGENCE.
 * CREATIVE_MEDIA_PROJECT != ANOTHER_INTELLIGENCE.
 * PROJECT_PREPARATION != MEDIA_GENERATION.
 * PROJECT_PREPARATION != EXECUTION.
 * CREATIVE_MEDIA != STORY_TO_ANIMATION.
 */
@ConsistentCopyVisibility
data class CreativeMediaProjectRecord private constructor(
    val projectId: CreativeMediaProjectId,
    val objective: CreativeMediaObjective,
) {
    companion object {

        fun create(
            projectId: CreativeMediaProjectId,
            objective: CreativeMediaObjective,
        ): CreativeMediaProjectRecord {
            return CreativeMediaProjectRecord(
                projectId = projectId,
                objective = objective,
            )
        }
    }
}
