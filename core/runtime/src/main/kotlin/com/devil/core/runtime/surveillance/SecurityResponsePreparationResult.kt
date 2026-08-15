package com.devil.core.runtime.surveillance

import com.devil.core.model.common.TraceId
import com.devil.core.model.surveillance.SecurityResponseRecord

/**
 * Stable Stage 91 result of bounded Security Response preparation.
 *
 * PREPARED requires exactly one SecurityResponseRecord.
 *
 * DEFERRED must not contain a Security Response record.
 *
 * This result creates no identity authority, authentication, trust,
 * authorization, threat determination, criminal-status determination,
 * constitutional Decision, Task, Plan, capability, ExecutionRequest,
 * execution approval, platform action, notification, alarm, lock operation,
 * emergency-service communication, constitutional Observation, Verification,
 * Outcome, World Model mutation, constitutional Learning, Memory, or
 * persistence authority.
 */
@ConsistentCopyVisibility
data class SecurityResponsePreparationResult private constructor(
    val traceId: TraceId,
    val status: SecurityResponsePreparationStatus,
    val record: SecurityResponseRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: SecurityResponsePreparationStatus,
            record: SecurityResponseRecord? = null,
        ): SecurityResponsePreparationResult {
            when (status) {
                SecurityResponsePreparationStatus.PREPARED -> {
                    require(record != null) {
                        "Prepared Security Response results require one record."
                    }
                }

                SecurityResponsePreparationStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred Security Response results must not contain a record."
                    }
                }
            }

            return SecurityResponsePreparationResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
