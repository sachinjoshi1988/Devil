package com.devil.app.execution

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents one bounded Android execution-attempt result.
 *
 * ATTEMPTED preserves the capability identity whose platform action was
 * genuinely attempted.
 *
 * It does not claim execution success, observation, verification, or outcome.
 *
 * DEFERRED contains no capability identity or error because no platform action
 * was attempted.
 *
 * FAILED contains one matching error and no capability identity.
 */
@ConsistentCopyVisibility
data class AndroidExecutionAttemptResult private constructor(
    val traceId: TraceId,
    val status: AndroidExecutionAttemptStatus,
    val capabilityId: CapabilityId?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: AndroidExecutionAttemptStatus,
            capabilityId: CapabilityId? = null,
            error: UniversalErrorRecord? = null,
        ): AndroidExecutionAttemptResult {
            when (status) {
                AndroidExecutionAttemptStatus.ATTEMPTED -> {
                    require(capabilityId != null && error == null) {
                        "Attempted Android execution results require a capability identity and must not contain an error."
                    }
                }

                AndroidExecutionAttemptStatus.DEFERRED -> {
                    require(capabilityId == null && error == null) {
                        "Deferred Android execution results must not contain a capability identity or error."
                    }
                }

                AndroidExecutionAttemptStatus.FAILED -> {
                    require(capabilityId == null && error != null) {
                        "Failed Android execution results require an error and must not contain a capability identity."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Android execution result and error must use the same trace identity."
            }

            return AndroidExecutionAttemptResult(
                traceId = traceId,
                status = status,
                capabilityId = capabilityId,
                error = error,
            )
        }
    }
}
