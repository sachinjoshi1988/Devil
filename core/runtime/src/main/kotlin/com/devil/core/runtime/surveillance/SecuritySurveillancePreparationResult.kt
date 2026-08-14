package com.devil.core.runtime.surveillance

import com.devil.core.model.common.TraceId
import com.devil.core.model.surveillance.SecuritySurveillanceRecord

/**
 * Stable Stage 90 result of bounded Security Surveillance preparation.
 *
 * PREPARED requires exactly one SecuritySurveillanceRecord.
 *
 * DEFERRED must not contain a surveillance record.
 *
 * This result creates no identity authority, authentication, trust, authorization,
 * security-session state, threat determination, constitutional Decision, Task,
 * Plan, capability, execution request, Security Response, constitutional Observation,
 * Verification, Outcome, World Model mutation, constitutional Learning, Memory,
 * platform connection, CCTV connection, network-camera connection, or persistence authority.
 */
@ConsistentCopyVisibility
data class SecuritySurveillancePreparationResult private constructor(
    val traceId: TraceId,
    val status: SecuritySurveillancePreparationStatus,
    val record: SecuritySurveillanceRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: SecuritySurveillancePreparationStatus,
            record: SecuritySurveillanceRecord? = null,
        ): SecuritySurveillancePreparationResult {
            when (status) {
                SecuritySurveillancePreparationStatus.PREPARED -> {
                    require(record != null) {
                        "Prepared Security Surveillance results require one record."
                    }
                }

                SecuritySurveillancePreparationStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred Security Surveillance results must not contain a record."
                    }
                }
            }

            return SecuritySurveillancePreparationResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
