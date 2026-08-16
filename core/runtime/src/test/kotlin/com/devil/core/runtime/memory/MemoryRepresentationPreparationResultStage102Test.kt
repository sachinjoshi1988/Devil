package com.devil.core.runtime.memory

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
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.execution.ExecutionRequest
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.learning.LearningRequest
import com.devil.core.model.memory.LogicalMemoryRepresentation
import com.devil.core.model.memory.MemoryAuthorityRequest
import com.devil.core.model.memory.MemoryClass
import com.devil.core.model.memory.MemoryConfidence
import com.devil.core.model.memory.MemoryId
import com.devil.core.model.memory.MemoryProposalRequest
import com.devil.core.model.memory.MemoryRepresentationPreparationRequest
import com.devil.core.model.memory.MemoryRetention
import com.devil.core.model.memory.MemorySensitivity
import com.devil.core.model.memory.MemorySource
import com.devil.core.model.memory.OwnerVisibleMemoryReason
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class MemoryRepresentationPreparationResultStage102Test {

    @Test
    fun `prepared result preserves exact request and representation`() {
        val traceId = TraceId.from("trace-stage-102-result")
        val request = createPreparationRequest()
        val representation = createRepresentation(request)

        val result =
            MemoryRepresentationPreparationResult.create(
                traceId = traceId,
                status = MemoryRepresentationPreparationStatus.PREPARED,
                request = request,
                representation = representation,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(request, result.request)
        assertEquals(representation, result.representation)
        assertNull(result.error)
    }

    @Test
    fun `prepared result rejects representation with different memory identity`() {
        val request = createPreparationRequest()

        val differentRepresentation =
            LogicalMemoryRepresentation.create(
                memoryId = MemoryId.from("different-memory"),
                subjectIdentityId = request.subjectIdentityId,
                memoryClass = request.memoryClass,
                sensitivity = request.sensitivity,
                confidence = request.confidence,
                retention = request.retention,
                source = request.source,
                ownerVisibleReason = request.ownerVisibleReason,
                content = request.content,
            )

        assertFailsWith<IllegalArgumentException> {
            MemoryRepresentationPreparationResult.create(
                traceId = TraceId.from("trace-stage-102-result"),
                status = MemoryRepresentationPreparationStatus.PREPARED,
                request = request,
                representation = differentRepresentation,
            )
        }
    }

    @Test
    fun `deferred result contains no prepared state`() {
        val result =
            MemoryRepresentationPreparationResult.create(
                traceId = TraceId.from("trace-stage-102-deferred"),
                status = MemoryRepresentationPreparationStatus.DEFERRED,
            )

        assertNull(result.request)
        assertNull(result.representation)
        assertNull(result.error)
    }

    @Test
    fun `failed result requires matching upstream error`() {
        val traceId = TraceId.from("trace-stage-102-failed")
        val error = createError(traceId)

        val result =
            MemoryRepresentationPreparationResult.create(
                traceId = traceId,
                status = MemoryRepresentationPreparationStatus.FAILED,
                error = error,
            )

        assertEquals(error, result.error)
        assertNull(result.request)
        assertNull(result.representation)
    }

    @Test
    fun `failed result rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryRepresentationPreparationResult.create(
                traceId = TraceId.from("trace-stage-102-primary"),
                status = MemoryRepresentationPreparationStatus.FAILED,
                error =
                    createError(
                        TraceId.from("trace-stage-102-other"),
                    ),
            )
        }
    }

    private fun createRepresentation(
        request: MemoryRepresentationPreparationRequest,
    ): LogicalMemoryRepresentation {
        return LogicalMemoryRepresentation.create(
            memoryId = request.memoryId,
            subjectIdentityId = request.subjectIdentityId,
            memoryClass = request.memoryClass,
            sensitivity = request.sensitivity,
            confidence = request.confidence,
            retention = request.retention,
            source = request.source,
            ownerVisibleReason = request.ownerVisibleReason,
            content = request.content,
        )
    }

    private fun createPreparationRequest(): MemoryRepresentationPreparationRequest {
        return MemoryRepresentationPreparationRequest.create(
            authorityRequest = createAuthorityRequest(),
            memoryId = MemoryId.from("memory-stage-102-result"),
            subjectIdentityId = IdentityId.from("subject-stage-102-result"),
            memoryClass = MemoryClass.SEMANTIC,
            sensitivity = MemorySensitivity.PRIVATE,
            confidence = MemoryConfidence.from(90),
            retention = MemoryRetention.LONG_TERM,
            source =
                MemorySource.create(
                    sourceId = "source-stage-102-result",
                    sourceType = "test",
                ),
            ownerVisibleReason =
                OwnerVisibleMemoryReason.from(
                    "Stage 102 test reason.",
                ),
            content = "Stage 102 bounded memory content.",
        )
    }

    private fun createAuthorityRequest(): MemoryAuthorityRequest {
        return MemoryAuthorityRequest.create(
            proposal =
                MemoryProposalRequest.create(
                    learning =
                        LearningRequest.create(
                            worldModelUpdate =
                                WorldModelUpdateRequest.create(
                                    outcome =
                                        OutcomeRequest.create(
                                            verification =
                                                VerificationRequest.create(
                                                    observation =
                                                        ObservationRequest.create(
                                                            execution =
                                                                ExecutionRequest.create(
                                                                    plan = createPlan(),
                                                                    capability = createCapability(),
                                                                ),
                                                        ),
                                                ),
                                        ),
                                ),
                        ),
                ),
        )
    }

    private fun createPlan(): PlanRecord {
        val context = createContext()

        val understanding =
            UnderstandingRecord.create(
                context = context,
                state = UnderstandingState.COMPLETE,
                summary = "Bounded understanding.",
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state = DecisionState.SELECTED,
                summary = "Bounded decision.",
            )

        val task =
            TaskRecord.create(
                taskId = TaskId.from("task-stage-102-result"),
                decision = decision,
                state = TaskState.CREATED,
                summary = "Bounded task.",
            )

        return PlanRecord.create(
            planId = PlanId.from("plan-stage-102-result"),
            task = task,
            state = PlanState.CREATED,
            summary = "Bounded plan.",
        )
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from("capability-stage-102-result"),
            category = CapabilityCategory.ACTION,
            name = "Stage 102 result capability",
            description = "Bounded Stage 102 result-test capability.",
        )
    }

    private fun createContext(): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from("trace-stage-102-result-context"),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_755_000_102_100L,
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "STAGE_102_PREPARATION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_755_000_102_200L,
                ),
            summary =
                "Stage 102 bounded preparation failed.",
        )
    }
}
