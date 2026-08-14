package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.CreativeMediaProjectRecord

/**
 * Stable Stage 87 result of bounded Creative Media project preparation.
 *
 * PREPARED requires exactly one CreativeMediaProjectRecord.
 *
 * DEFERRED must not contain a project.
 *
 * This result creates no Devil identity, Brain, runtime, identity authority,
 * trust, authentication, authorization, security session, Decision, Task,
 * Plan, capability, generator, execution, generated media, Observation,
 * Verification, Outcome, World Model mutation, constitutional Learning,
 * Memory, persistence authority, scene model, frame sequence, animation
 * timeline, or Story-to-Animation state.
 */
@ConsistentCopyVisibility
data class CreativeMediaProjectPreparationResult private constructor(
    val traceId: TraceId,
    val status: CreativeMediaProjectPreparationStatus,
    val project: CreativeMediaProjectRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: CreativeMediaProjectPreparationStatus,
            project: CreativeMediaProjectRecord? = null,
        ): CreativeMediaProjectPreparationResult {
            when (status) {
                CreativeMediaProjectPreparationStatus.PREPARED -> {
                    require(project != null) {
                        "Prepared Creative Media results require one project."
                    }
                }

                CreativeMediaProjectPreparationStatus.DEFERRED -> {
                    require(project == null) {
                        "Deferred Creative Media results must not contain a project."
                    }
                }
            }

            return CreativeMediaProjectPreparationResult(
                traceId = traceId,
                status = status,
                project = project,
            )
        }
    }
}
