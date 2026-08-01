package com.devil.core.runtime.constitution

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the structured result of constitutional context validation.
 *
 * Valid results contain no error. Invalid results require a matching
 * UniversalErrorRecord from the same trace.
 */
@ConsistentCopyVisibility
data class ConstitutionValidationResult private constructor(
    val traceId: TraceId,
    val status: ConstitutionValidationStatus,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ConstitutionValidationStatus,
            error: UniversalErrorRecord? = null,
        ): ConstitutionValidationResult {
            require(
                (status == ConstitutionValidationStatus.INVALID) == (error != null),
            ) {
                "Invalid validation results must contain an error and valid results must not."
            }

            require(error == null || error.traceId == traceId) {
                "Validation result and error must use the same trace identity."
            }

            return ConstitutionValidationResult(
                traceId = traceId,
                status = status,
                error = error,
            )
        }
    }
}
