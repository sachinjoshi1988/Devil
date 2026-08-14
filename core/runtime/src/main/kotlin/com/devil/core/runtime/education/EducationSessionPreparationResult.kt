package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationSessionRecord

/**
 * Stable Stage 85 result of bounded education-session preparation.
 *
 * PREPARED requires one EducationSessionRecord.
 *
 * DEFERRED must not contain an education session.
 *
 * This result creates no identity authority, trust, authentication,
 * authorization, security session, child classification, guardian authority,
 * Decision, Task, Plan, capability, execution, Observation, Verification,
 * Outcome, constitutional Learning, Memory, or persistence authority.
 */
@ConsistentCopyVisibility
data class EducationSessionPreparationResult private constructor(
    val traceId: TraceId,
    val status: EducationSessionPreparationStatus,
    val session: EducationSessionRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: EducationSessionPreparationStatus,
            session: EducationSessionRecord? = null,
        ): EducationSessionPreparationResult {
            when (status) {
                EducationSessionPreparationStatus.PREPARED -> {
                    require(session != null) {
                        "Prepared education-session results require one session."
                    }
                }

                EducationSessionPreparationStatus.DEFERRED -> {
                    require(session == null) {
                        "Deferred education-session results must not contain a session."
                    }
                }
            }

            return EducationSessionPreparationResult(
                traceId = traceId,
                status = status,
                session = session,
            )
        }
    }
}
