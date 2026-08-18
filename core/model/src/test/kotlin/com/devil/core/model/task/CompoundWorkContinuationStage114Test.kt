package com.devil.core.model.task

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class CompoundWorkContinuationStage114Test {

    @Test
    fun `continuation record preserves exact request and candidate step`() {
        val request =
            request(
                trace = "trace-stage-114-model-001",
            )

        val record =
            CompoundWorkContinuationRecord.create(
                request = request,
                step = request.steps[1],
            )

        assertSame(
            request,
            record.request,
        )

        assertSame(
            request.steps[1],
            record.step,
        )
    }

    @Test
    fun `continuation record rejects foreign step`() {
        val request =
            request(
                trace = "trace-stage-114-model-002",
            )

        val foreign =
            CompoundWorkStep.create(
                position = 2,
                summary = "Foreign bounded step.",
            )

        assertFailsWith<IllegalArgumentException> {
            CompoundWorkContinuationRecord.create(
                request = request,
                step = foreign,
            )
        }
    }

    private fun request(
        trace: String,
    ): CompoundWorkRequest {
        val traceId =
            TraceId.from(trace)

        val understanding =
            UnderstandingRecord.create(
                context =
                    ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel =
                            ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_114_000L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Stage 114 bounded compound-work continuation understanding.",
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state = DecisionState.SELECTED,
                summary =
                    "Preserve one bounded compound-work goal.",
            )

        return CompoundWorkRequest.create(
            decision = decision,
            steps =
                listOf(
                    CompoundWorkStep.create(
                        position = 1,
                        summary = "First bounded compound step.",
                    ),
                    CompoundWorkStep.create(
                        position = 2,
                        summary = "Second bounded compound step.",
                    ),
                ),
        )
    }
}
