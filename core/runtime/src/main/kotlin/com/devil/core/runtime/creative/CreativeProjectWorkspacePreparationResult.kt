package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.CreativeProjectWorkspaceRecord

/**
 * Stable Stage 174 result of bounded Creative Project Workspace preparation.
 *
 * PREPARED requires exactly one CreativeProjectWorkspaceRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no generation, execution, storage success,
 * constitutional Memory, Observation, Verification, Outcome, World Model
 * mutation, Learning, or publishing authorization.
 */
@ConsistentCopyVisibility
data class CreativeProjectWorkspacePreparationResult private constructor(
    val traceId: TraceId,
    val status: CreativeProjectWorkspacePreparationStatus,
    val workspace: CreativeProjectWorkspaceRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: CreativeProjectWorkspacePreparationStatus,
            workspace: CreativeProjectWorkspaceRecord? = null,
        ): CreativeProjectWorkspacePreparationResult {
            when (status) {
                CreativeProjectWorkspacePreparationStatus.PREPARED -> {
                    require(workspace != null) {
                        "Prepared Creative Project Workspace results require one workspace."
                    }
                }

                CreativeProjectWorkspacePreparationStatus.DEFERRED -> {
                    require(workspace == null) {
                        "Deferred Creative Project Workspace results must not contain a workspace."
                    }
                }
            }

            return CreativeProjectWorkspacePreparationResult(
                traceId = traceId,
                status = status,
                workspace = workspace,
            )
        }
    }
}
