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
import com.devil.core.runtime.execution.ExecutionAuthority
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.execution.ExecutionStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.observation.ObservationAuthority
import com.devil.core.runtime.observation.ObservationResult
import com.devil.core.runtime.observation.ObservationStatus
import com.devil.core.runtime.outcome.OutcomeAuthority
import com.devil.core.runtime.outcome.OutcomeResult
import com.devil.core.runtime.outcome.OutcomeStatus
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import com.devil.core.runtime.verification.VerificationAuthority
import com.devil.core.runtime.verification.VerificationResult
import com.devil.core.runtime.verification.VerificationStatus
import com.devil.core.runtime.worldmodel.WorldModelUpdateAuthority
import com.devil.core.runtime.worldmodel.WorldModelUpdateResult
import com.devil.core.runtime.worldmodel.WorldModelUpdateStatus
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
    fun `accept does not treat approved execution as later constitutional work`() {
        val input = createInput(
            "trace-runtime-world-model-002",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept does not treat observation as later constitutional work`() {
        val input = createInput(
            "trace-runtime-world-model-003",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
            observationAuthority =
                fixedObservationAuthority(
                    status = ObservationStatus.OBSERVED,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept does not treat verification as an established outcome or World Model update`() {
        val input = createInput(
            "trace-runtime-world-model-004",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
            observationAuthority =
                fixedObservationAuthority(
                    status = ObservationStatus.OBSERVED,
                ),
            verificationAuthority =
                fixedVerificationAuthority(
                    status = VerificationStatus.VERIFIED,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept does not treat established outcome as an applied World Model update`() {
        val input = createInput(
            "trace-runtime-world-model-005",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
            observationAuthority =
                fixedObservationAuthority(
                    status = ObservationStatus.OBSERVED,
                ),
            verificationAuthority =
                fixedVerificationAuthority(
                    status = VerificationStatus.VERIFIED,
                ),
            outcomeAuthority =
                fixedOutcomeAuthority(
                    status = OutcomeStatus.ESTABLISHED,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept maps applicable World Model update to accepted runtime result`() {
        val input = createInput(
            "trace-runtime-world-model-006",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
            observationAuthority =
                fixedObservationAuthority(
                    status = ObservationStatus.OBSERVED,
                ),
            verificationAuthority =
                fixedVerificationAuthority(
                    status = VerificationStatus.VERIFIED,
                ),
            outcomeAuthority =
                fixedOutcomeAuthority(
                    status = OutcomeStatus.ESTABLISHED,
                ),
            worldModelUpdateAuthority =
                fixedWorldModelUpdateAuthority(
                    status = WorldModelUpdateStatus.APPLICABLE,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.ACCEPTED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept maps failed execution through later authorities to rejected result`() {
        val input = createInput(
            "trace-runtime-world-model-007",
        )
        val error = createError(
            traceId = input.context.traceId,
            code = "UNIFIED_RUNTIME_EXECUTION_FAILED",
            summary = "Bounded execution evaluation failed.",
        )
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
    fun `accept maps failed observation through later authorities to rejected result`() {
        val input = createInput(
            "trace-runtime-world-model-008",
        )
        val error = createError(
            traceId = input.context.traceId,
            code = "UNIFIED_RUNTIME_OBSERVATION_FAILED",
            summary = "Bounded observation evaluation failed.",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
            observationAuthority =
                fixedObservationAuthority(
                    status = ObservationStatus.FAILED,
                    error = error,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.REJECTED, result.status)
        assertEquals(error, result.error)
    }

    @Test
    fun `accept maps failed verification through later authorities to rejected result`() {
        val input = createInput(
            "trace-runtime-world-model-009",
        )
        val error = createError(
            traceId = input.context.traceId,
            code = "UNIFIED_RUNTIME_VERIFICATION_FAILED",
            summary = "Bounded verification evaluation failed.",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
            observationAuthority =
                fixedObservationAuthority(
                    status = ObservationStatus.OBSERVED,
                ),
            verificationAuthority =
                fixedVerificationAuthority(
                    status = VerificationStatus.FAILED,
                    error = error,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.REJECTED, result.status)
        assertEquals(error, result.error)
    }

    @Test
    fun `accept maps failed outcome through World Model authority to rejected result`() {
        val input = createInput(
            "trace-runtime-world-model-010",
        )
        val error = createError(
            traceId = input.context.traceId,
            code = "UNIFIED_RUNTIME_OUTCOME_FAILED",
            summary = "Bounded outcome evaluation failed.",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
            observationAuthority =
                fixedObservationAuthority(
                    status = ObservationStatus.OBSERVED,
                ),
            verificationAuthority =
                fixedVerificationAuthority(
                    status = VerificationStatus.VERIFIED,
                ),
            outcomeAuthority =
                fixedOutcomeAuthority(
                    status = OutcomeStatus.FAILED,
                    error = error,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.REJECTED, result.status)
        assertEquals(error, result.error)
    }

    @Test
    fun `accept maps failed World Model update to rejected runtime result`() {
        val input = createInput(
            "trace-runtime-world-model-011",
        )
        val error = createError(
            traceId = input.context.traceId,
            code = "UNIFIED_RUNTIME_WORLD_MODEL_UPDATE_FAILED",
            summary = "Bounded World Model update evaluation failed.",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
            observationAuthority =
                fixedObservationAuthority(
                    status = ObservationStatus.OBSERVED,
                ),
            verificationAuthority =
                fixedVerificationAuthority(
                    status = VerificationStatus.VERIFIED,
                ),
            outcomeAuthority =
                fixedOutcomeAuthority(
                    status = OutcomeStatus.ESTABLISHED,
                ),
            worldModelUpdateAuthority =
                fixedWorldModelUpdateAuthority(
                    status = WorldModelUpdateStatus.FAILED,
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
            "trace-runtime-world-model-012",
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

    @Test
    fun `accept rejects observation result from a different trace`() {
        val input = createInput(
            "trace-runtime-world-model-013",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
            observationAuthority =
                fixedObservationAuthority(
                    status = ObservationStatus.DEFERRED,
                    traceId = TraceId.from(
                        "trace-runtime-observation-other",
                    ),
                ),
        )

        assertFailsWith<IllegalArgumentException> {
            runtime.accept(input)
        }
    }

    @Test
    fun `accept rejects verification result from a different trace`() {
        val input = createInput(
            "trace-runtime-world-model-014",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
            observationAuthority =
                fixedObservationAuthority(
                    status = ObservationStatus.OBSERVED,
                ),
            verificationAuthority =
                fixedVerificationAuthority(
                    status = VerificationStatus.DEFERRED,
                    traceId = TraceId.from(
                        "trace-runtime-verification-other",
                    ),
                ),
        )

        assertFailsWith<IllegalArgumentException> {
            runtime.accept(input)
        }
    }

    @Test
    fun `accept rejects outcome result from a different trace`() {
        val input = createInput(
            "trace-runtime-world-model-015",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
            observationAuthority =
                fixedObservationAuthority(
                    status = ObservationStatus.OBSERVED,
                ),
            verificationAuthority =
                fixedVerificationAuthority(
                    status = VerificationStatus.VERIFIED,
                ),
            outcomeAuthority =
                fixedOutcomeAuthority(
                    status = OutcomeStatus.DEFERRED,
                    traceId = TraceId.from(
                        "trace-runtime-outcome-other",
                    ),
                ),
        )

        assertFailsWith<IllegalArgumentException> {
            runtime.accept(input)
        }
    }

    @Test
    fun `accept rejects World Model update result from a different trace`() {
        val input = createInput(
            "trace-runtime-world-model-016",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            executionAuthority =
                fixedExecutionAuthority(
                    status = ExecutionStatus.APPROVED,
                ),
            observationAuthority =
                fixedObservationAuthority(
                    status = ObservationStatus.OBSERVED,
                ),
            verificationAuthority =
                fixedVerificationAuthority(
                    status = VerificationStatus.VERIFIED,
                ),
            outcomeAuthority =
                fixedOutcomeAuthority(
                    status = OutcomeStatus.ESTABLISHED,
                ),
            worldModelUpdateAuthority =
                fixedWorldModelUpdateAuthority(
                    status = WorldModelUpdateStatus.DEFERRED,
                    traceId = TraceId.from(
                        "trace-runtime-world-model-update-other",
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

    private fun fixedObservationAuthority(
        status: ObservationStatus,
        traceId: TraceId? = null,
        error: UniversalErrorRecord? = null,
    ): ObservationAuthority {
        return object : ObservationAuthority {
            override fun observe(
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
            ): ObservationResult {
                val resultTraceId =
                    traceId ?: context.traceId

                return when (status) {
                    ObservationStatus.OBSERVED ->
                        ObservationResult.create(
                            traceId = resultTraceId,
                            status = ObservationStatus.OBSERVED,
                            request = ObservationRequest.create(
                                execution =
                                    requireNotNull(execution.request),
                            ),
                        )

                    ObservationStatus.DEFERRED ->
                        ObservationResult.create(
                            traceId = resultTraceId,
                            status = ObservationStatus.DEFERRED,
                        )

                    ObservationStatus.FAILED ->
                        ObservationResult.create(
                            traceId = resultTraceId,
                            status = ObservationStatus.FAILED,
                            error = requireNotNull(error),
                        )
                }
            }
        }
    }

    private fun fixedVerificationAuthority(
        status: VerificationStatus,
        traceId: TraceId? = null,
        error: UniversalErrorRecord? = null,
    ): VerificationAuthority {
        return object : VerificationAuthority {
            override fun verify(
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
            ): VerificationResult {
                val resultTraceId =
                    traceId ?: context.traceId

                return when (status) {
                    VerificationStatus.VERIFIED ->
                        VerificationResult.create(
                            traceId = resultTraceId,
                            status = VerificationStatus.VERIFIED,
                            request = VerificationRequest.create(
                                observation =
                                    requireNotNull(
                                        observation.request,
                                    ),
                            ),
                        )

                    VerificationStatus.DEFERRED ->
                        VerificationResult.create(
                            traceId = resultTraceId,
                            status = VerificationStatus.DEFERRED,
                        )

                    VerificationStatus.FAILED ->
                        VerificationResult.create(
                            traceId = resultTraceId,
                            status = VerificationStatus.FAILED,
                            error = requireNotNull(error),
                        )
                }
            }
        }
    }

    private fun fixedOutcomeAuthority(
        status: OutcomeStatus,
        traceId: TraceId? = null,
        error: UniversalErrorRecord? = null,
    ): OutcomeAuthority {
        return object : OutcomeAuthority {
            override fun establish(
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
            ): OutcomeResult {
                val resultTraceId =
                    traceId ?: context.traceId

                return when (status) {
                    OutcomeStatus.ESTABLISHED ->
                        OutcomeResult.create(
                            traceId = resultTraceId,
                            status = OutcomeStatus.ESTABLISHED,
                            request = OutcomeRequest.create(
                                verification =
                                    requireNotNull(
                                        verification.request,
                                    ),
                            ),
                        )

                    OutcomeStatus.DEFERRED ->
                        OutcomeResult.create(
                            traceId = resultTraceId,
                            status = OutcomeStatus.DEFERRED,
                        )

                    OutcomeStatus.FAILED ->
                        OutcomeResult.create(
                            traceId = resultTraceId,
                            status = OutcomeStatus.FAILED,
                            error = requireNotNull(error),
                        )
                }
            }
        }
    }

    private fun fixedWorldModelUpdateAuthority(
        status: WorldModelUpdateStatus,
        traceId: TraceId? = null,
        error: UniversalErrorRecord? = null,
    ): WorldModelUpdateAuthority {
        return object : WorldModelUpdateAuthority {
            override fun evaluateUpdate(
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
            ): WorldModelUpdateResult {
                val resultTraceId =
                    traceId ?: context.traceId

                return when (status) {
                    WorldModelUpdateStatus.APPLICABLE ->
                        WorldModelUpdateResult.create(
                            traceId = resultTraceId,
                            status =
                                WorldModelUpdateStatus.APPLICABLE,
                            request =
                                WorldModelUpdateRequest.create(
                                    outcome =
                                        requireNotNull(
                                            outcome.request,
                                        ),
                                ),
                        )

                    WorldModelUpdateStatus.DEFERRED ->
                        WorldModelUpdateResult.create(
                            traceId = resultTraceId,
                            status =
                                WorldModelUpdateStatus.DEFERRED,
                        )

                    WorldModelUpdateStatus.FAILED ->
                        WorldModelUpdateResult.create(
                            traceId = resultTraceId,
                            status =
                                WorldModelUpdateStatus.FAILED,
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
                "task-runtime-world-model-update",
            ),
            decision = decision,
            state = TaskState.CREATED,
            summary =
                "A bounded constitutional task was created.",
        )

        val plan = PlanRecord.create(
            planId = PlanId.from(
                "plan-runtime-world-model-update",
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
        code: String,
        summary: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_145_500L,
                ),
            summary = summary,
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
                        1_754_000_145_000L,
                    ),
            ),
            content =
                "Please tell me the current phone status.",
        )
    }
}
