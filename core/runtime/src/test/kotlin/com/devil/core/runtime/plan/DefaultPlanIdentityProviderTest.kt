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
import com.devil.core.model.plan.PlanId
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultPlanIdentityProviderTest {

    @Test
    fun `provide returns deterministic task-derived plan identity`() {
        val traceId = TraceId.from(
            "trace-default-plan-identity-provider-001",
        )
        val request = createRequest(
            traceId = traceId,
            taskId = TaskId.from(
                "task:trace-default-plan-identity-provider-001",
            ),
        )

        val result = DefaultPlanIdentityProvider().provide(
            traceId = traceId,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            PlanIdentityProvisionStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            PlanId.from(
                "plan:task:trace-default-plan-identity-provider-001",
            ),
            result.planId,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide is deterministic for the same task identity`() {
        val traceId = TraceId.from(
            "trace-default-plan-identity-provider-002",
        )
        val request = createRequest(
            traceId = traceId,
            taskId = TaskId.from(
                "task:trace-default-plan-identity-provider-002",
            ),
        )
        val provider = DefaultPlanIdentityProvider()

        val first = provider.provide(
            traceId = traceId,
            request = request,
        )
        val second = provider.provide(
            traceId = traceId,
            request = request,
        )

        assertEquals(first.planId, second.planId)
        assertEquals(
            PlanIdentityProvisionStatus.AVAILABLE,
            first.status,
        )
    }

    @Test
    fun `provide rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultPlanIdentityProvider().provide(
                traceId = TraceId.from(
                    "trace-default-plan-identity-provider-003",
                ),
                request = createRequest(
                    traceId = TraceId.from(
                        "trace-default-plan-identity-request-other",
                    ),
                    taskId = TaskId.from(
                        "task:trace-default-plan-identity-request-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
        taskId: TaskId,
    ): PlanCreationRequest {
        return PlanCreationRequest.create(
            task = TaskRecord.create(
                taskId = taskId,
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
                                    1_754_000_083_000L,
                                ),
                        ),
                        state = UnderstandingState.COMPLETE,
                        summary =
                            "Bounded understanding was produced.",
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
