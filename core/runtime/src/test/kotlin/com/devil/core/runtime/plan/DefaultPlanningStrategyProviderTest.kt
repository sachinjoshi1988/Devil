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
import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingSemantics
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultPlanningStrategyProviderTest {

    @Test
    fun `provide creates bounded open-target planning strategy`() {
        val traceId = TraceId.from(
            "trace-default-planning-strategy-provider-001",
        )

        val result = DefaultPlanningStrategyProvider().provide(
            traceId = traceId,
            request = createOpenTargetRequest(
                traceId = traceId,
                target = "camera",
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            PlanningStrategyProvisionStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            "Prepare the bounded plan for opening target: camera.",
            result.strategy,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide preserves understood target without executing it`() {
        val traceId = TraceId.from(
            "trace-default-planning-strategy-provider-002",
        )

        val result = DefaultPlanningStrategyProvider().provide(
            traceId = traceId,
            request = createOpenTargetRequest(
                traceId = traceId,
                target = "the camera",
            ),
        )

        assertEquals(
            "Prepare the bounded plan for opening target: the camera.",
            result.strategy,
        )
    }

    @Test
    fun `provide returns unavailable when complete understanding lacks semantics`() {
        val traceId = TraceId.from(
            "trace-default-planning-strategy-provider-003",
        )

        val result = DefaultPlanningStrategyProvider().provide(
            traceId = traceId,
            request = createRequestWithoutSemantics(traceId),
        )

        assertEquals(
            PlanningStrategyProvisionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.strategy)
        assertNull(result.error)
    }

    @Test
    fun `provide rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultPlanningStrategyProvider().provide(
                traceId = TraceId.from(
                    "trace-default-planning-strategy-provider-004",
                ),
                request = createOpenTargetRequest(
                    traceId = TraceId.from(
                        "trace-default-planning-strategy-request-other",
                    ),
                    target = "camera",
                ),
            )
        }
    }

    private fun createOpenTargetRequest(
        traceId: TraceId,
        target: String,
    ): PlanCreationRequest {
        return createRequest(
            traceId = traceId,
            semantics = UnderstandingSemantics.create(
                intent = UnderstandingIntent.OPEN_TARGET,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning = "open target",
                target = target,
            ),
        )
    }

    private fun createRequestWithoutSemantics(
        traceId: TraceId,
    ): PlanCreationRequest {
        return createRequest(
            traceId = traceId,
            semantics = null,
        )
    }

    private fun createRequest(
        traceId: TraceId,
        semantics: UnderstandingSemantics?,
    ): PlanCreationRequest {
        return PlanCreationRequest.create(
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task:${traceId.value}",
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
                            "Bounded semantic understanding was produced.",
                        semantics = semantics,
                    ),
                    state = DecisionState.SELECTED,
                    summary =
                        "Bounded constitutional decision was selected.",
                ),
                state = TaskState.CREATED,
                summary =
                    "Bounded constitutional task was created.",
            ),
        )
    }
}
