package com.devil.core.runtime.understanding

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.understanding.UnderstandingRecord

/**
 * Represents the structured operational result of understanding.
 *
 * A produced result contains an UnderstandingRecord. A deferred result contains
 * neither record nor error. A failed result contains a matching error.
 */
@ConsistentCopyVisibility
data class UnderstandingAuthorityResult private constructor(
    val traceId: TraceId,
    val status: UnderstandingAuthorityStatus,
    val understanding: UnderstandingRecord?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: UnderstandingAuthorityStatus,
            understanding: UnderstandingRecord? = null,
            error: UniversalErrorRecord? = null,
        ): UnderstandingAuthorityResult {
            when (status) {
                UnderstandingAuthorityStatus.PRODUCED -> {
                    require(understanding != null && error == null) {
                        "Produced understanding results require a record and must not contain an error."
                    }
                }

                UnderstandingAuthorityStatus.DEFERRED -> {
                    require(understanding == null && error == null) {
                        "Deferred understanding results must not contain a record or error."
                    }
                }

                UnderstandingAuthorityStatus.FAILED -> {
                    require(understanding == null && error != null) {
                        "Failed understanding results require an error and must not contain a record."
                    }
                }
            }

            require(
                understanding == null ||
                    understanding.context.traceId == traceId,
            ) {
                "Understanding result and record must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Understanding result and error must use the same trace identity."
            }

            return UnderstandingAuthorityResult(
                traceId = traceId,
                status = status,
                understanding = understanding,
                error = error,
            )
        }
    }
}
