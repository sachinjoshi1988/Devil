package com.devil.app.outcome

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents one bounded Android outcome result.
 *
 * ESTABLISHED preserves genuine AndroidOutcomeEvidence.
 *
 * DEFERRED contains neither evidence nor error.
 *
 * FAILED contains one matching error and no evidence.
 *
 * An established Android outcome remains distinct from task completion, plan
 * completion, World Model update, learning, memory, and persistence.
 */
@ConsistentCopyVisibility
data class AndroidOutcomeResult private constructor(
    val traceId: TraceId,
    val status: AndroidOutcomeStatus,
    val evidence: AndroidOutcomeEvidence?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: AndroidOutcomeStatus,
            evidence: AndroidOutcomeEvidence? = null,
            error: UniversalErrorRecord? = null,
        ): AndroidOutcomeResult {
            when (status) {
                AndroidOutcomeStatus.ESTABLISHED -> {
                    require(evidence != null && error == null) {
                        "Established Android outcomes require evidence and must not contain an error."
                    }
                }

                AndroidOutcomeStatus.DEFERRED -> {
                    require(evidence == null && error == null) {
                        "Deferred Android outcomes must not contain evidence or error."
                    }
                }

                AndroidOutcomeStatus.FAILED -> {
                    require(evidence == null && error != null) {
                        "Failed Android outcomes require an error and must not contain evidence."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Android outcome result and error must use the same trace identity."
            }

            return AndroidOutcomeResult(
                traceId = traceId,
                status = status,
                evidence = evidence,
                error = error,
            )
        }
    }
}
