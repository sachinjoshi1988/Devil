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

class DefaultPlanIdentityProviderTest {

    @Test
    fun `provide returns unavailable without fabricating plan identity`() {
        val traceId = TraceId.from(
            "trace-default-plan-identity-provider-001",
        )
        val provider: PlanIdentityProvider =
            DefaultPlanIdentityProvider()

        val result = provider.provide(
            traceId = traceId,
            request = createRequest(traceId),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            PlanIdentityProvisionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.planId)
        assertNull(result.error)
    }

    @Test
    fun `provide remains independent from planning strategy availability`() {
        val traceId = TraceId.from(
            "trace-default-plan-identity-provider-002",
        )

        val result = DefaultPlanIdentityProvider().provide(
            traceId = traceId,
            request = createRequest(traceId),
        )

        assertEquals(
            PlanIdentityProvisionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.planId)
    }

    @Test
    fun `provide rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultPlanIdentityProvider().provide(
                traceId = TraceId.from(
                    "trace-default-plan-identity-provider-003",
                ),
                request = createRequest(
                    TraceId.from(
                        "trace-default-plan-identity-request-other",
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
                    "task-default-plan-identity-001",
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
                                    1_754_000_083_000L,
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
