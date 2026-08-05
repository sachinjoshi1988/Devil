package com.devil.core.runtime.plan

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.plan.PlanCreationRequest
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultPlanningStrategyProviderTest {

    @Test
    fun `provide returns unavailable without fabricating strategy`() {
        val traceId = TraceId.from(
            "trace-default-planning-strategy-provider-001",
        )
        val provider: PlanningStrategyProvider =
            DefaultPlanningStrategyProvider()

        val result = provider.provide(
            traceId = traceId,
            request = createRequest(traceId),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            PlanningStrategyProvisionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.strategy)
        assertNull(result.error)
    }

    @Test
    fun `provide does not copy task summary as strategy`() {
        val traceId = TraceId.from(
            "trace-default-planning-strategy-provider-002",
        )
        val request = createRequest(traceId)

        val result = DefaultPlanningStrategyProvider().provide(
            traceId = traceId,
            request = request,
        )

        assertEquals(
            "Open the camera application.",
            request.task.summary,
        )
        assertEquals(
            PlanningStrategyProvisionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.strategy)
    }

    @Test
    fun `provide rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultPlanningStrategyProvider().provide(
                traceId = TraceId.from(
                    "trace-default-planning-strategy-provider-003",
                ),
                request = createRequest(
                    TraceId.from(
                        "trace-default-planning-strategy-request-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): PlanCreationRequest {
        return PlanCreationRequest.create(
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-default-planning-strategy-001",
                ),
                decision = DecisionRecord.create(
                    understanding = UnderstandingRecord.create(
                        context = ContextEnvelope.create(
                            traceId = traceId,
                            schemaVersion = SchemaVersion.from(1),
                            source = ContextSource.TEXT,
                            trustLevel =
                                ContextTrustLevel.VERIFIED,
                            securityLevel =
                                ContextSecurityLevel.RESTRICTED,
                            observedAt =
                                DevilTimestamp.fromEpochMilliseconds(
                                    1_754_000_082_000L,
                                ),
                        ),
                        state = UnderstandingState.COMPLETE,
                        summary =
                            "Open the camera application.",
                    ),
                    state = DecisionState.SELECTED,
                    summary = "Open the camera application.",
                ),
                state = TaskState.CREATED,
                summary = "Open the camera application.",
            ),
        )
    }
}
