package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.CreativeProjectWorkspaceRecord
import com.devil.core.model.creative.CreativeWorkspaceAssetRecord
import com.devil.core.model.creative.CreativeWorkspaceAssetType
import com.devil.core.model.creative.VideoCreationAssistanceRecord

/**
 * Stage 174 bounded Creative Project Workspace coordinator.
 *
 * This coordinator preserves one exact existing Stage 173 Video Creation
 * Assistance context and prepares explicitly supplied creative-production
 * continuity metadata around it.
 *
 * It does not:
 *
 * - inspect or generate media;
 * - generate or verify creative assets;
 * - verify character, location, shot, or episode continuity;
 * - write files or workspace state;
 * - invoke databases, cloud storage, providers, models, or capabilities;
 * - create an ExecutionRequest;
 * - execute anything;
 * - create Memory Proposal, Memory Authority approval, Memory commitment,
 *   or Memory persistence;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - or authorize publishing.
 *
 * CREATIVE_PROJECT_WORKSPACE != ANOTHER_INTELLIGENCE.
 * CREATIVE_WORKSPACE != MEMORY.
 * WORKSPACE_PREPARED != WORKSPACE_PERSISTED.
 * WORKSPACE_RECORD != STORAGE_SUCCESS.
 * SUPPLIED_CONTINUITY != VERIFIED_VISUAL_CONSISTENCY.
 * SUPPLIED_SHOT_STATE != OBSERVED_SHOT_STATE.
 * PREPARED != EXECUTION.
 * GENERATED != VERIFIED.
 */
class CreativeProjectWorkspaceCoordinator {

    fun prepare(
        traceId: TraceId,
        videoCreationAssistance: VideoCreationAssistanceRecord,
        suppliedAssets: List<SuppliedCreativeWorkspaceAsset>,
        suppliedShotStateDescription: String,
        suppliedEpisodeContinuityDescription: String,
        workspaceObjective: String,
    ): CreativeProjectWorkspacePreparationResult {
        if (
            suppliedAssets.any {
                it.name.isBlank() ||
                    it.suppliedContinuityDescription.isBlank()
            } ||
            suppliedShotStateDescription.isBlank() ||
            suppliedEpisodeContinuityDescription.isBlank() ||
            workspaceObjective.isBlank()
        ) {
            return deferred(traceId)
        }

        val assets =
            suppliedAssets.mapIndexed { index, supplied ->
                CreativeWorkspaceAssetRecord.create(
                    position = index,
                    type = supplied.type,
                    name = supplied.name,
                    suppliedContinuityDescription =
                        supplied.suppliedContinuityDescription,
                )
            }

        val workspace =
            CreativeProjectWorkspaceRecord.create(
                videoCreationAssistance = videoCreationAssistance,
                assets = assets,
                suppliedShotStateDescription =
                    suppliedShotStateDescription,
                suppliedEpisodeContinuityDescription =
                    suppliedEpisodeContinuityDescription,
                workspaceObjective =
                    workspaceObjective,
            )

        return CreativeProjectWorkspacePreparationResult.create(
            traceId = traceId,
            status =
                CreativeProjectWorkspacePreparationStatus.PREPARED,
            workspace = workspace,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): CreativeProjectWorkspacePreparationResult {
        return CreativeProjectWorkspacePreparationResult.create(
            traceId = traceId,
            status =
                CreativeProjectWorkspacePreparationStatus.DEFERRED,
        )
    }
}

/**
 * Stage 174 caller-supplied workspace asset metadata.
 *
 * This is input metadata only and is not a generated or persisted asset.
 */
data class SuppliedCreativeWorkspaceAsset(
    val type: CreativeWorkspaceAssetType,
    val name: String,
    val suppliedContinuityDescription: String,
)
