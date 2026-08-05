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
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultTaskIdentityProviderTest {

    @Test
    fun `provide returns unavailable without fabricating task identity`() {
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
            TaskIdentityProvisionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.taskId)
        assertNull(result.error)
    }

    @Test
    fun `provide consistently returns unavailable for selected decision`() {
        val traceId = TraceId.from(
            "trace-default-task-identity-provider-002",
        )

        val result = DefaultTaskIdentityProvider().provide(
            traceId = traceId,
            request = createRequest(traceId),
        )

        assertEquals(
            DecisionState.SELECTED,
            createRequest(traceId).decision.state,
        )
        assertEquals(
            TaskIdentityProvisionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.taskId)
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
