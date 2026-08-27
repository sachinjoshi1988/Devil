package com.devil.core.runtime.trust

import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.trust.TrustAssessment

/**
 * Represents the structured result of trust evaluation.
 *
 * An evaluated result may preserve:
 *
 * - one bounded subject TrustAssessment; and/or
 * - one ContextTrustLevel when that context-provenance representation is
 *   independently justified by an existing caller.
 *
 * Subject trust and context trust remain distinct.
 *
 * SUBJECT_TRUST != CONTEXT_TRUST.
 * TRUST_ASSESSMENT != AUTHENTICATION.
 * TRUST_ASSESSMENT != AUTHORIZATION.
 * TRUST_ASSESSMENT != OWNER_MODE.
 * TRUST_ASSESSMENT != EXECUTION.
 *
 * A deferred result contains no trust representation or error.
 * A failed result contains one matching error.
 */
@ConsistentCopyVisibility
data class TrustResult private constructor(
    val traceId: TraceId,
    val status: TrustStatus,
    val trustLevel: ContextTrustLevel?,
    val assessment: TrustAssessment?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: TrustStatus,
            trustLevel: ContextTrustLevel? = null,
            assessment: TrustAssessment? = null,
            error: UniversalErrorRecord? = null,
        ): TrustResult {
            when (status) {
                TrustStatus.EVALUATED -> {
                    require(
                        (trustLevel != null || assessment != null) &&
                            error == null,
                    ) {
                        "Evaluated trust results require at least one bounded trust representation and must not contain an error."
                    }
                }

                TrustStatus.DEFERRED -> {
                    require(
                        trustLevel == null &&
                            assessment == null &&
                            error == null,
                    ) {
                        "Deferred trust results must not contain trust representations or error."
                    }
                }

                TrustStatus.FAILED -> {
                    require(
                        trustLevel == null &&
                            assessment == null &&
                            error != null,
                    ) {
                        "Failed trust results require an error and must not contain trust representations."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Trust result and error must use the same trace identity."
            }

            return TrustResult(
                traceId = traceId,
                status = status,
                trustLevel = trustLevel,
                assessment = assessment,
                error = error,
            )
        }
    }
}
