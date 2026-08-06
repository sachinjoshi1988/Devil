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
import com.devil.core.model.learning.LearningRequest
import com.devil.core.model.memory.MemoryProposalRequest
import com.devil.core.model.observation.ObservationRequest
import com.devil.core.model.outcome.OutcomeRequest
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.model.verification.VerificationRequest
import com.devil.core.model.worldmodel.WorldModelUpdateRequest
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.learning.LearningResult
import com.devil.core.runtime.memory.MemoryProposalAuthority
import com.devil.core.runtime.memory.MemoryProposalResult
import com.devil.core.runtime.memory.MemoryProposalStatus
import com.devil.core.runtime.observation.ObservationResult
import com.devil.core.runtime.outcome.OutcomeResult
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import com.devil.core.runtime.verification.VerificationResult
import com.devil.core.runtime.worldmodel.WorldModelUpdateResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultUnifiedDevilRuntimeTest {

    @Test
    fun `accept coordinates conversation input through one constitutional runtime path`() {
        val input = createInput(
            "trace-runtime-memory-proposal-001",
        )

        val result = DefaultUnifiedDevilRuntime().accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept does not treat learning as an approved memory proposal`() {
        val input = createInput(
            "trace-runtime-memory-proposal-002",
        )

        val result = DefaultUnifiedDevilRuntime().accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept maps proposable memory result to accepted runtime result`() {
        val input = createInput(
            "trace-runtime-memory-proposal-003",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            memoryProposalAuthority =
                fixedMemoryProposalAuthority(
                    status = MemoryProposalStatus.PROPOSABLE,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.ACCEPTED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept maps deferred memory proposal to deferred runtime result`() {
        val input = createInput(
            "trace-runtime-memory-proposal-004",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            memoryProposalAuthority =
                fixedMemoryProposalAuthority(
                    status = MemoryProposalStatus.DEFERRED,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept maps failed memory proposal to rejected runtime result`() {
        val input = createInput(
            "trace-runtime-memory-proposal-005",
        )
        val error = createError(
            traceId = input.context.traceId,
            code = "UNIFIED_RUNTIME_MEMORY_PROPOSAL_FAILED",
            summary =
                "Bounded constitutional memory proposal evaluation failed.",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            memoryProposalAuthority =
                fixedMemoryProposalAuthority(
                    status = MemoryProposalStatus.FAILED,
                    error = error,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.REJECTED, result.status)
        assertEquals(error, result.error)
    }

    @Test
    fun `accept rejects memory proposal result from another trace`() {
        val input = createInput(
            "trace-runtime-memory-proposal-006",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            memoryProposalAuthority =
                fixedMemoryProposalAuthority(
                    status = MemoryProposalStatus.DEFERRED,
                    traceId = TraceId.from(
                        "trace-runtime-memory-proposal-other",
                    ),
                ),
        )

        assertFailsWith<IllegalArgumentException> {
            runtime.accept(input)
        }
    }

    private fun fixedMemoryProposalAuthority(
        status: MemoryProposalStatus,
        traceId: TraceId? = null,
        error: UniversalErrorRecord? = null,
    ): MemoryProposalAuthority {
        return object : MemoryProposalAuthority {
            override fun evaluateProposal(
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
                execution: ExecutionResult,
                observation: ObservationResult,
                verification: VerificationResult,
                outcome: OutcomeResult,
                worldModelUpdate: WorldModelUpdateResult,
                learning: LearningResult,
            ): MemoryProposalResult {
                val resultTraceId =
                    traceId ?: context.traceId

                return when (status) {
                    MemoryProposalStatus.PROPOSABLE ->
                        MemoryProposalResult.create(
                            traceId = resultTraceId,
                            status = MemoryProposalStatus.PROPOSABLE,
                            request =
                                createMemoryProposalRequest(
                                    context = context,
                                ),
                        )

                    MemoryProposalStatus.DEFERRED ->
                        MemoryProposalResult.create(
                            traceId = resultTraceId,
                            status = MemoryProposalStatus.DEFERRED,
                        )

                    MemoryProposalStatus.FAILED ->
                        MemoryProposalResult.create(
                            traceId = resultTraceId,
                            status = MemoryProposalStatus.FAILED,
                            error = requireNotNull(error),
                        )
                }
            }
        }
    }

    private fun createMemoryProposalRequest(
        context: ContextEnvelope,
    ): MemoryProposalRequest {
        return MemoryProposalRequest.create(
            learning = LearningRequest.create(
                worldModelUpdate =
                    WorldModelUpdateRequest.create(
                        outcome = OutcomeRequest.create(
                            verification =
                                VerificationRequest.create(
                                    observation =
                                        ObservationRequest.create(
                                            execution =
                                                createExecutionRequest(
                                                    context = context,
                                                ),
                                        ),
                                ),
                        ),
                    ),
            ),
        )
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
                "task-runtime-memory-proposal",
            ),
            decision = decision,
            state = TaskState.CREATED,
            summary =
                "A bounded constitutional task was created.",
        )

        val plan = PlanRecord.create(
            planId = PlanId.from(
                "plan-runtime-memory-proposal",
            ),
            task = task,
            state = PlanState.CREATED,
            summary =
                "Use the constitutionally approved capability path.",
        )

        val capability = CapabilityContract.create(
            capabilityId = CapabilityId.from(
                "capability-runtime-memory-proposal",
            ),
            category = CapabilityCategory.ACTION,
            name = "Runtime Memory Proposal Test Capability",
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
        code: String,
        summary: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_163_500L,
                ),
            summary = summary,
        )
    }

    private fun createInput(
        traceValue: String,
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
                        1_754_000_163_000L,
                    ),
            ),
            content =
                "Please tell me the current phone status.",
        )
    }
}
