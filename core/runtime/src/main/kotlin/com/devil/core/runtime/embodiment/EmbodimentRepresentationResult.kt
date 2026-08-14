package com.devil.core.runtime.embodiment

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.EmbodimentRecord

/**
 * Stable Stage 81 result of bounded embodiment representation.
 *
 * REPRESENTED preserves one EmbodimentRecord.
 *
 * The result creates no capability registration, availability, trust,
 * authentication, authorization, session, platform permission, execution,
 * observation, verification, Outcome, Memory, or persistence authority.
 */
@ConsistentCopyVisibility
data class EmbodimentRepresentationResult private constructor(
    val traceId: TraceId,
    val status: EmbodimentRepresentationStatus,
    val embodiment: EmbodimentRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: EmbodimentRepresentationStatus,
            embodiment: EmbodimentRecord? = null,
        ): EmbodimentRepresentationResult {
            when (status) {
                EmbodimentRepresentationStatus.REPRESENTED -> {
                    require(embodiment != null) {
                        "Represented embodiment results require one embodiment."
                    }
                }

                EmbodimentRepresentationStatus.DEFERRED -> {
                    require(embodiment == null) {
                        "Deferred embodiment results must not contain an embodiment."
                    }
                }
            }

            return EmbodimentRepresentationResult(
                traceId = traceId,
                status = status,
                embodiment = embodiment,
            )
        }
    }
}
