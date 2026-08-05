package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultCapabilityRegistryTest {

    @Test
    fun `obtain returns unavailable without fabricating registrations`() {
        val traceId = TraceId.from(
            "trace-default-capability-registry-001",
        )
        val registry: CapabilityRegistry =
            DefaultCapabilityRegistry()

        val result = registry.obtain(
            traceId = traceId,
            request = createRequest(traceId),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilityRegistryStatus.UNAVAILABLE,
            result.status,
        )
        assertEquals(emptyList(), result.capabilities)
        assertNull(result.error)
    }

    @Test
    fun `obtain consistently preserves empty constitutional registry`() {
        val traceId = TraceId.from(
            "trace-default-capability-registry-002",
        )

        val result = DefaultCapabilityRegistry().obtain(
            traceId = traceId,
            request = createRequest(traceId),
        )

        assertEquals(
            CapabilityRegistryStatus.UNAVAILABLE,
            result.status,
        )
        assertEquals(emptyList(), result.capabilities)
    }

    @Test
    fun `obtain rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultCapabilityRegistry().obtain(
                traceId = TraceId.from(
                    "trace-default-capability-registry-003",
                ),
                request = createRequest(
                    TraceId.from(
                        "trace-default-capability-registry-request-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): CapabilitySelectionRequest {
        return CapabilitySelectionRequest.create(
            plan = PlanRecord.create(
                planId = PlanId.from(
                    "plan-default-capability-registry-001",
                ),
                task = TaskRecord.create(
                    taskId = TaskId.from(
                        "task-default-capability-registry-001",
                    ),
                    decision = DecisionRecord.create(
                        understanding =
                            UnderstandingRecord.create(
                                context =
                                    ContextEnvelope.create(
                                        traceId = traceId,
                                        schemaVersion =
                                            SchemaVersion.from(1),
                                        source =
                                            ContextSource.TEXT,
                                        trustLevel =
                                            ContextTrustLevel.VERIFIED,
                                        securityLevel =
                                            ContextSecurityLevel.RESTRICTED,
                                        observedAt =
                                            DevilTimestamp
                                                .fromEpochMilliseconds(
                                                    1_754_000_090_000L,
                                                ),
                                    ),
                                state =
                                    UnderstandingState.COMPLETE,
                                summary =
                                    "Bounded understanding was produced.",
                            ),
                        state = DecisionState.SELECTED,
                        summary =
                            "A constitutional decision was selected.",
                    ),
                    state = TaskState.CREATED,
                    summary =
                        "A bounded constitutional task was created.",
                ),
                state = PlanState.CREATED,
                summary =
                    "Use the constitutionally approved capability path.",
            ),
        )
    }
}
