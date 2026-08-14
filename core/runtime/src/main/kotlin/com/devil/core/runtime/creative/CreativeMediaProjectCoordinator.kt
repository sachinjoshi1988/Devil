package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.CreativeMediaMedium
import com.devil.core.model.creative.CreativeMediaObjective
import com.devil.core.model.creative.CreativeMediaProjectId
import com.devil.core.model.creative.CreativeMediaProjectRecord

/**
 * Stage 87 bounded Creative Media Foundation coordinator.
 *
 * This coordinator prepares one Creative Media project from explicitly
 * supplied structured inputs.
 *
 * Inputs are:
 *
 * - constitutional TraceId;
 * - already-created CreativeMediaProjectId;
 * - explicit target-medium description;
 * - explicit creative objective.
 *
 * It does not infer creative intent from raw conversation.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Executive;
 * - create another Planner;
 * - create another Unified Devil Runtime;
 * - create Creative Media-specific Memory or Security authorities;
 * - resolve or infer identity;
 * - authenticate a subject;
 * - establish trust;
 * - grant authorization;
 * - establish or validate a security session;
 * - enter Owner Mode;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - register, select, authorize, or activate capabilities;
 * - establish capability availability, health, or readiness;
 * - select or invoke an image, audio, video, text, or multimodal model;
 * - select or invoke a renderer or generator;
 * - invoke UnifiedDevilRuntime;
 * - create execution requests;
 * - execute actions;
 * - generate Creative Media;
 * - render content;
 * - create assets;
 * - create or write files;
 * - communicate with platform APIs;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - propose, commit, or persist Memory;
 * - persist Creative Media state;
 * - decompose stories into scenes;
 * - create scene records;
 * - create frame sequences;
 * - create animation timelines;
 * - or perform Story-to-Animation.
 *
 * CREATIVE_MEDIA = DOMAIN_OF_THE_ONE_DEVIL_INTELLIGENCE.
 * CREATIVE_MEDIA != ANOTHER_INTELLIGENCE.
 * MEDIUM != CAPABILITY.
 * PROJECT_PREPARED != GENERATED.
 * PROJECT_PREPARED != EXECUTION.
 * CREATIVE_MEDIA != STORY_TO_ANIMATION.
 */
class CreativeMediaProjectCoordinator {

    fun prepare(
        traceId: TraceId,
        projectId: CreativeMediaProjectId,
        medium: String,
        objective: String,
    ): CreativeMediaProjectPreparationResult {
        if (
            medium.isBlank() ||
            objective.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val creativeObjective =
            CreativeMediaObjective.create(
                medium =
                    CreativeMediaMedium.from(
                        medium,
                    ),
                objective = objective,
            )

        val project =
            CreativeMediaProjectRecord.create(
                projectId = projectId,
                objective = creativeObjective,
            )

        return CreativeMediaProjectPreparationResult.create(
            traceId = traceId,
            status =
                CreativeMediaProjectPreparationStatus.PREPARED,
            project = project,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): CreativeMediaProjectPreparationResult {
        return CreativeMediaProjectPreparationResult.create(
            traceId = traceId,
            status =
                CreativeMediaProjectPreparationStatus.DEFERRED,
        )
    }
}
