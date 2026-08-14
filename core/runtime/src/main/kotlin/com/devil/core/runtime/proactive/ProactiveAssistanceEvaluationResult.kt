package com.devil.core.runtime.proactive

import com.devil.core.model.common.TraceId
import com.devil.core.model.proactive.ProactiveAssistanceRecord

/**
 * Stable Stage 80 result of bounded proactive-assistance governance.
 *
 * ELIGIBLE_FOR_PRESENTATION preserves one ProactiveAssistanceRecord.
 *
 * Preserving this result does not present, notify, speak, authorize,
 * schedule, execute, persist, learn, or establish an Outcome.
 */
@ConsistentCopyVisibility
data class ProactiveAssistanceEvaluationResult private constructor(
    val traceId: TraceId,
    val status: ProactiveAssistanceEvaluationStatus,
    val record: ProactiveAssistanceRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: ProactiveAssistanceEvaluationStatus,
            record: ProactiveAssistanceRecord? = null,
        ): ProactiveAssistanceEvaluationResult {
            when (status) {
                ProactiveAssistanceEvaluationStatus.ELIGIBLE_FOR_PRESENTATION -> {
                    require(record != null) {
                        "Presentation-eligible proactive assistance requires one record."
                    }
                }

                ProactiveAssistanceEvaluationStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred proactive assistance must not contain a record."
                    }
                }
            }

            require(
                record == null ||
                    record.decision.understanding.context.traceId == traceId,
            ) {
                "Proactive-assistance result and fresh Decision must use the same trace identity."
            }

            return ProactiveAssistanceEvaluationResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
