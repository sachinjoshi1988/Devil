package com.devil.app.observation

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents one bounded Android execution-observation result.
 *
 * OBSERVED preserves genuine AndroidObservationEvidence.
 *
 * DEFERRED contains neither evidence nor error.
 *
 * FAILED contains one matching error and no evidence.
 *
 * This result does not perform verification, establish an Outcome, claim task
 * completion, update world state, create logical memory, or report success.
 */
@ConsistentCopyVisibility
data class AndroidObservationResult private constructor(
    val traceId: TraceId,
    val status: AndroidObservationStatus,
    val evidence: AndroidObservationEvidence?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: AndroidObservationStatus,
            evidence: AndroidObservationEvidence? = null,
            error: UniversalErrorRecord? = null,
        ): AndroidObservationResult {
            when (status) {
                AndroidObservationStatus.OBSERVED -> {
                    require(evidence != null && error == null) {
                        "Observed Android results require evidence and must not contain an error."
                    }
                }

                AndroidObservationStatus.DEFERRED -> {
                    require(evidence == null && error == null) {
                        "Deferred Android observation results must not contain evidence or error."
                    }
                }

                AndroidObservationStatus.FAILED -> {
                    require(evidence == null && error != null) {
                        "Failed Android observation results require an error and must not contain evidence."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Android observation result and error must use the same trace identity."
            }

            return AndroidObservationResult(
                traceId = traceId,
                status = status,
                evidence = evidence,
                error = error,
            )
        }
    }
}
