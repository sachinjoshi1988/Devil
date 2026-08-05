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
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultPlanCreationRequestProviderTest {

    @Test
    fun `provide returns available request for created task`() {
        val task = createCreatedTask(
            traceValue = "trace-plan-provider-001",
            state = TaskState.CREATED,
        )
        val provider: PlanCreationRequestProvider =
            DefaultPlanCreationRequestProvider()

        val result = provider.provide(task)

        assertEquals(task.traceId, result.traceId)
        assertEquals(
            PlanCreationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            requireNotNull(task.task),
            requireNotNull(result.request).task,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for waiting task`() {
        val result =
            DefaultPlanCreationRequestProvider().provide(
                createCreatedTask(
                    traceValue = "trace-plan-provider-002",
                    state = TaskState.WAITING,
                ),
            )

        assertEquals(
            PlanCreationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for active task`() {
        val result =
            DefaultPlanCreationRequestProvider().provide(
                createCreatedTask(
                    traceValue = "trace-plan-provider-003",
                    state = TaskState.ACTIVE,
                ),
            )

        assertEquals(
            PlanCreationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for completed task`() {
        val result =
            DefaultPlanCreationRequestProvider().provide(
                createCreatedTask(
                    traceValue = "trace-plan-provider-004",
                    state = TaskState.COMPLETED,
                ),
            )

        assertEquals(
            PlanCreationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for cancelled task`() {
        val result =
            DefaultPlanCreationRequestProvider().provide(
                createCreatedTask(
                    traceValue = "trace-plan-provider-005",
                    state = TaskState.CANCELLED,
                ),
            )

        assertEquals(
            PlanCreationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for failed task`() {
        val result =
            DefaultPlanCreationRequestProvider().provide(
                createCreatedTask(
                    traceValue = "trace-plan-provider-006",
                    state = TaskState.FAILED,
                ),
            )

        assertEquals(
            PlanCreationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred task authority result`() {
        val traceId = TraceId.from("trace-plan-provider-007")

        val result =
            DefaultPlanCreationRequestProvider().provide(
                TaskAuthorityResult.create(
                    traceId = traceId,
                    status = TaskAuthorityStatus.DEFERRED,
                ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            PlanCreationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed task error`() {
        val traceId = TraceId.from("trace-plan-provider-008")
        val error = createError(traceId)

        val result =
            DefaultPlanCreationRequestProvider().provide(
                TaskAuthorityResult.create(
                    traceId = traceId,
                    status = TaskAuthorityStatus.FAILED,
                    error = error,
                ),
            )

        assertEquals(PlanCreationRequestStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    private fun createCreatedTask(
        traceValue: String,
        state: TaskState,
    ): TaskAuthorityResult {
        val traceId = TraceId.from(traceValue)

        return TaskAuthorityResult.create(
            traceId = traceId,
            status = TaskAuthorityStatus.CREATED,
            task = TaskRecord.create(
                taskId = TaskId.from("task-plan-provider-001"),
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
                                    1_754_000_081_000L,
                                ),
                        ),
                        state = UnderstandingState.COMPLETE,
                        summary =
                            "Bounded understanding was produced.",
                    ),
                    state = DecisionState.SELECTED,
                    summary =
                        "A constitutional decision was selected.",
                ),
                state = state,
                summary = "A bounded task was created.",
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from("TASK_FAILED"),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_081_500L,
                ),
            summary = "Task creation failed.",
        )
    }
}
