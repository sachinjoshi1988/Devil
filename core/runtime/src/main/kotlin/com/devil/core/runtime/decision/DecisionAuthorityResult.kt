package com.devil.core.runtime.decision

import com.devil.core.model.common.TraceId
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the structured operational result of decision selection.
 *
 * A produced result contains a DecisionRecord. A deferred result contains
 * neither decision nor error. A failed result contains a matching error.
 */
@ConsistentCopyVisibility
data class DecisionAuthorityResult private constructor(
    val traceId: TraceId,
    val status: DecisionAuthorityStatus,
    val decision: DecisionRecord?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: DecisionAuthorityStatus,
            decision: DecisionRecord? = null,
            error: UniversalErrorRecord? = null,
        ): DecisionAuthorityResult {
            when (status) {
                DecisionAuthorityStatus.PRODUCED -> {
                    require(decision != null && error == null) {
                        "Produced decision results require a record and must not contain an error."
                    }
                }

                DecisionAuthorityStatus.DEFERRED -> {
                    require(decision == null && error == null) {
                        "Deferred decision results must not contain a record or error."
                    }
                }

                DecisionAuthorityStatus.FAILED -> {
                    require(decision == null && error != null) {
                        "Failed decision results require an error and must not contain a record."
                    }
                }
            }

            require(
                decision == null ||
                    decision.understanding.context.traceId == traceId,
            ) {
                "Decision result and record must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Decision result and error must use the same trace identity."
            }

            return DecisionAuthorityResult(
                traceId = traceId,
                status = status,
                decision = decision,
                error = error,
            )
        }
    }
}
