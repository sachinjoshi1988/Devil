package com.devil.core.runtime.task

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.task.TaskCreationRequest
import com.devil.core.model.task.TaskId
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultTaskIdentityProviderTest {

    @Test
    fun `provide returns trace-derived task identity`() {
        val traceId = TraceId.from(
            "trace-default-task-identity-provider-001",
        )
        val provider: TaskIdentityProvider =
            DefaultTaskIdentityProvider()

        val result = provider.provide(
            traceId = traceId,
            request = createRequest(traceId),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            TaskIdentityProvisionStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            TaskId.from(
                "task:trace-default-task-identity-provider-001",
            ),
            result.taskId,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide is deterministic for the same bounded trace`() {
        val traceId = TraceId.from(
            "trace-default-task-identity-provider-002",
        )
        val provider = DefaultTaskIdentityProvider()
        val request = createRequest(traceId)

        val first = provider.provide(
            traceId = traceId,
            request = request,
        )
        val second = provider.provide(
            traceId = traceId,
            request = request,
        )

        assertEquals(
            DecisionState.SELECTED,
            request.decision.state,
        )
        assertEquals(
            TaskIdentityProvisionStatus.AVAILABLE,
            first.status,
        )
        assertEquals(first.taskId, second.taskId)
        assertEquals(
            TaskId.from(
                "task:trace-default-task-identity-provider-002",
            ),
            first.taskId,
        )
    }

    @Test
    fun `provide produces distinct task identities for distinct traces`() {
        val firstTrace = TraceId.from(
            "trace-default-task-identity-provider-a",
        )
        val secondTrace = TraceId.from(
            "trace-default-task-identity-provider-b",
        )

        val provider = DefaultTaskIdentityProvider()

        val first = provider.provide(
            traceId = firstTrace,
            request = createRequest(firstTrace),
        )
        val second = provider.provide(
            traceId = secondTrace,
            request = createRequest(secondTrace),
        )

        assertEquals(
            TaskId.from(
                "task:trace-default-task-identity-provider-a",
            ),
            first.taskId,
        )
        assertEquals(
            TaskId.from(
                "task:trace-default-task-identity-provider-b",
            ),
            second.taskId,
        )
    }

    @Test
    fun `provide rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultTaskIdentityProvider().provide(
                traceId = TraceId.from(
                    "trace-default-task-identity-provider-003",
                ),
                request = createRequest(
                    TraceId.from(
                        "trace-default-task-identity-request-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): TaskCreationRequest {
        return TaskCreationRequest.create(
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
                    "Bounded constitutional decision was selected.",
            ),
        )
    }
}
