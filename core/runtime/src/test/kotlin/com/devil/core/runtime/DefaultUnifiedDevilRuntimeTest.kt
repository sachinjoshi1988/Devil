package com.devil.core.runtime

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.execution.ExecutionRequest
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.execution.ExecutionAuthority
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.execution.ExecutionStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultUnifiedDevilRuntimeTest {

    @Test
    fun `accept coordinates conversation input through one runtime path`() {
        val input = createInput()
        val runtime: UnifiedDevilRuntime =
            DefaultUnifiedDevilRuntime()

        val result = runtime.accept(input)

        assertEquals(
            input.context.traceId,
            result.traceId,
        )
        assertEquals(
            RuntimeStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `accept maps approved execution evaluation to accepted runtime result`() {
        val input = createInput(
            "trace-runtime-execution-002",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.ACCEPTED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept maps failed execution evaluation to rejected runtime result`() {
        val input = createInput(
            "trace-runtime-execution-003",
        )
        val error = createError(input.context.traceId)
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.FAILED,
                    error = error,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.REJECTED, result.status)
        assertEquals(error, result.error)
    }

    @Test
    fun `accept rejects execution result from a different trace`() {
        val input = createInput(
            "trace-runtime-execution-004",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.DEFERRED,
                    traceId = TraceId.from(
                        "trace-runtime-execution-other",
                    ),
                ),
        )

        assertFailsWith<IllegalArgumentException> {
            runtime.accept(input)
        }
    }

    private fun fixedExecutionAuthority(
        status: ExecutionStatus,
        traceId: TraceId? = null,
        error: UniversalErrorRecord? = null,
    ): ExecutionAuthority {
        return object : ExecutionAuthority {
            override fun evaluate(
                context: ContextEnvelope,
                identity: IdentityResult,
                trust: TrustResult,
                authorization: AuthorizationResult,
                understanding: UnderstandingAuthorityResult,
                decision: DecisionAuthorityResult,
                task: TaskAuthorityResult,
                plan: PlanAuthorityResult,
                capability: CapabilitySelectionResult,
                readiness: ExecutiveReadinessResult,
            ): ExecutionResult {
                val resultTraceId =
                    traceId ?: context.traceId

                return when (status) {
                    ExecutionStatus.APPROVED ->
                        ExecutionResult.create(
                            traceId = resultTraceId,
                            status = ExecutionStatus.APPROVED,
                            request =
                                createExecutionRequest(context),
                        )

                    ExecutionStatus.DEFERRED ->
                        ExecutionResult.create(
                            traceId = resultTraceId,
                            status = ExecutionStatus.DEFERRED,
                        )

                    ExecutionStatus.FAILED ->
                        ExecutionResult.create(
                            traceId = resultTraceId,
                            status = ExecutionStatus.FAILED,
                            error = requireNotNull(error),
                        )
                }
            }
        }
    }

    private fun createExecutionRequest(
        context: ContextEnvelope,
    ): ExecutionRequest {
        val understanding = UnderstandingRecord.create(
            context = context,
            state = UnderstandingState.COMPLETE,
            summary =
                "Bounded understanding was produced.",
        )

        val decision = DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary =
                "A constitutional decision was selected.",
        )

        val task = TaskRecord.create(
            taskId = TaskId.from(
                "task-runtime-execution",
            ),
            decision = decision,
            state = TaskState.CREATED,
            summary =
                "A bounded constitutional task was created.",
        )

        val plan = PlanRecord.create(
            planId = PlanId.from(
                "plan-runtime-execution",
            ),
            task = task,
            state = PlanState.CREATED,
            summary =
                "Use the constitutionally approved capability path.",
        )

        val capability = CapabilityContract.create(
            capabilityId = CapabilityId.from(
                "capability-runtime-test",
            ),
            category = CapabilityCategory.ACTION,
            name = "Runtime Test Capability",
            description =
                "Represents one bounded test capability without platform execution.",
        )

        return ExecutionRequest.create(
            plan = plan,
            capability = capability,
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "UNIFIED_RUNTIME_EXECUTION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_109_500L,
                ),
            summary =
                "Bounded execution evaluation failed.",
        )
    }

    private fun createInput(
        traceValue: String =
            "trace-runtime-conversation-001",
    ): ConversationInput {
        return ConversationInput.create(
            context = ContextEnvelope.create(
                traceId = TraceId.from(traceValue),
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEST,
                trustLevel =
                    ContextTrustLevel.VERIFIED,
                securityLevel =
                    ContextSecurityLevel.RESTRICTED,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_109_000L,
                    ),
            ),
            content =
                "Please tell me the current phone status.",
        )
    }
}
