package com.devil.core.runtime.proactive

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

/**
 * Stage 296 direct unit coverage for the existing Stage 80
 * ProactiveAssistanceEvaluationResult contract.
 *
 * Factory invariants only. No production behavior is modified.
 * Stage 297 Integration Test Completion is out of scope.
 */
class Stage296ProactiveAssistanceEvaluationResultTest {

    @Test
    fun `deferred result preserves trace and contains no record`() {
        val traceId =
            TraceId.from(
                "trace-stage296-proactive-deferred",
            )

        val result =
            ProactiveAssistanceEvaluationResult.create(
                traceId = traceId,
                status = ProactiveAssistanceEvaluationStatus.DEFERRED,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ProactiveAssistanceEvaluationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)
    }

    @Test
    fun `presentation eligible result rejects missing record`() {
        assertFailsWith<IllegalArgumentException> {
            ProactiveAssistanceEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage296-proactive-invalid",
                    ),
                status =
                    ProactiveAssistanceEvaluationStatus
                        .ELIGIBLE_FOR_PRESENTATION,
            )
        }
    }
}
