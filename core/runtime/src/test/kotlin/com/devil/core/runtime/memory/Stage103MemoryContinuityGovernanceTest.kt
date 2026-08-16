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
import com.devil.core.model.owner.OwnerContext
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
import com.devil.core.runtime.owner.OwnerMultiUserContextRecord
import com.devil.core.runtime.owner.OwnerMultiUserContextResult
import com.devil.core.runtime.owner.OwnerMultiUserContextStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage103MemoryContinuityGovernanceTest {

    @Test
    fun `prepared representation plus established matching subject establishes continuity`() {
        val traceId =
            TraceId.from(
                "trace-stage-103-001",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage-103",
            )

        val preparation =
            createPreparedRepresentation(
                traceId = traceId,
                subjectIdentityId =
                    subjectIdentityId,
            )

        val representation =
            requireNotNull(
                preparation.representation,
            )

        val result =
            MemoryContinuityCoordinator()
                .establish(
                    traceId = traceId,
                    preparation = preparation,
                    ownerContext =
                        createEstablishedOwnerContext(
                            traceId = traceId,
                            subjectIdentityId =
                                subjectIdentityId,
                        ),
                )

        assertEquals(
            MemoryContinuityStatus.ESTABLISHED,
            result.status,
        )

        assertNull(
            result.error,
        )

        assertSame(
            representation,
            result.record?.representation,
        )

        assertEquals(
            representation.memoryId,
            result.record
                ?.representation
                ?.memoryId,
        )

        assertEquals(
            representation.subjectIdentityId,
            result.record
                ?.representation
                ?.subjectIdentityId,
        )

        assertEquals(
            representation.memoryClass,
            result.record
                ?.representation
                ?.memoryClass,
        )

        assertEquals(
            representation.sensitivity,
            result.record
                ?.representation
                ?.sensitivity,
        )

        assertEquals(
            representation.confidence,
            result.record
                ?.representation
                ?.confidence,
        )

        assertEquals(
            representation.retention,
            result.record
                ?.representation
                ?.retention,
        )

        assertEquals(
            representation.source,
            result.record
                ?.representation
                ?.source,
        )

        assertEquals(
            representation.ownerVisibleReason,
            result.record
                ?.representation
                ?.ownerVisibleReason,
        )

        assertEquals(
            representation.content,
            result.record
                ?.representation
                ?.content,
        )
    }

    @Test
    fun `non prepared representation defers continuity`() {
        val traceId =
            TraceId.from(
                "trace-stage-103-002",
            )

        val result =
            MemoryContinuityCoordinator()
                .establish(
                    traceId = traceId,
                    preparation =
                        MemoryRepresentationPreparationResult.create(
                            traceId = traceId,
                            status =
                                MemoryRepresentationPreparationStatus.DEFERRED,
                        ),
                    ownerContext =
                        OwnerMultiUserContextResult.create(
                            traceId = traceId,
                            status =
                                OwnerMultiUserContextStatus.DEFERRED,
                        ),
                )

        assertEquals(
            MemoryContinuityStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.record,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `non established owner context defers continuity`() {
        val traceId =
            TraceId.from(
                "trace-stage-103-003",
            )

        val preparation =
            createPreparedRepresentation(
                traceId = traceId,
                subjectIdentityId =
                    IdentityId.from(
                        "subject-stage-103",
                    ),
            )

        val result =
            MemoryContinuityCoordinator()
                .establish(
                    traceId = traceId,
                    preparation = preparation,
                    ownerContext =
                        OwnerMultiUserContextResult.create(
                            traceId = traceId,
                            status =
                                OwnerMultiUserContextStatus.DEFERRED,
                        ),
                )

        assertEquals(
            MemoryContinuityStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.record,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `subject mismatch defers without transforming identity`() {
        val traceId =
            TraceId.from(
                "trace-stage-103-004",
            )

        val preparation =
            createPreparedRepresentation(
                traceId = traceId,
                subjectIdentityId =
                    IdentityId.from(
                        "memory-subject-stage-103",
                    ),
            )

        val result =
            MemoryContinuityCoordinator()
                .establish(
                    traceId = traceId,
                    preparation = preparation,
                    ownerContext =
                        createEstablishedOwnerContext(
                            traceId = traceId,
                            subjectIdentityId =
                                IdentityId.from(
                                    "current-subject-stage-103",
                                ),
                        ),
                )

        assertEquals(
            MemoryContinuityStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.record,
        )

        assertNull(
            result.error,
        )

        assertEquals(
            IdentityId.from(
                "memory-subject-stage-103",
            ),
            preparation.representation
                ?.subjectIdentityId,
        )
    }

    @Test
    fun `preparation failure propagates matching error`() {
        val traceId =
            TraceId.from(
                "trace-stage-103-005",
            )

        val error =
            createError(
                traceId = traceId,
                code =
                    "MEMORY_PREPARATION_STAGE_103_FAILED",
            )

        val result =
            MemoryContinuityCoordinator()
                .establish(
                    traceId = traceId,
                    preparation =
                        MemoryRepresentationPreparationResult.create(
                            traceId = traceId,
                            status =
                                MemoryRepresentationPreparationStatus.FAILED,
                            error = error,
                        ),
                    ownerContext =
                        OwnerMultiUserContextResult.create(
                            traceId = traceId,
                            status =
                                OwnerMultiUserContextStatus.DEFERRED,
                        ),
                )

        assertEquals(
            MemoryContinuityStatus.FAILED,
            result.status,
        )

        assertEquals(
            error,
            result.error,
        )

        assertNull(
            result.record,
        )
    }

    @Test
    fun `owner context failure propagates matching error`() {
        val traceId =
            TraceId.from(
                "trace-stage-103-006",
            )

        val error =
            createError(
                traceId = traceId,
                code =
                    "OWNER_CONTEXT_STAGE_103_FAILED",
            )

        val result =
            MemoryContinuityCoordinator()
                .establish(
                    traceId = traceId,
                    preparation =
                        MemoryRepresentationPreparationResult.create(
                            traceId = traceId,
                            status =
                                MemoryRepresentationPreparationStatus.DEFERRED,
                        ),
                    ownerContext =
                        OwnerMultiUserContextResult.create(
                            traceId = traceId,
                            status =
                                OwnerMultiUserContextStatus.FAILED,
                            error = error,
                        ),
                )

        assertEquals(
            MemoryContinuityStatus.FAILED,
            result.status,
        )

        assertEquals(
            error,
            result.error,
        )

        assertNull(
            result.record,
        )
    }

    @Test
    fun `preparation trace mismatch is rejected`() {
        val preparationTraceId =
            TraceId.from(
                "trace-stage-103-preparation-other",
            )

        assertFailsWith<IllegalArgumentException> {
            MemoryContinuityCoordinator()
                .establish(
                    traceId =
                        TraceId.from(
                            "trace-stage-103-preparation-primary",
                        ),
                    preparation =
                        createPreparedRepresentation(
                            traceId =
                                preparationTraceId,
                            subjectIdentityId =
                                IdentityId.from(
                                    "subject-stage-103",
                                ),
                        ),
                    ownerContext =
                        OwnerMultiUserContextResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-stage-103-preparation-primary",
                                ),
                            status =
                                OwnerMultiUserContextStatus.DEFERRED,
                        ),
                )
        }
    }

    @Test
    fun `owner context trace mismatch is rejected`() {
        val traceId =
            TraceId.from(
                "trace-stage-103-owner-primary",
            )

        assertFailsWith<IllegalArgumentException> {
            MemoryContinuityCoordinator()
                .establish(
                    traceId = traceId,
                    preparation =
                        createPreparedRepresentation(
                            traceId = traceId,
                            subjectIdentityId =
                                IdentityId.from(
                                    "subject-stage-103",
                                ),
                        ),
                    ownerContext =
                        OwnerMultiUserContextResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-stage-103-owner-other",
                                ),
                            status =
                                OwnerMultiUserContextStatus.DEFERRED,
                        ),
                )
        }
    }

    private fun createPreparedRepresentation(
        traceId: TraceId,
        subjectIdentityId: IdentityId,
    ): MemoryRepresentationPreparationResult {
        val authorityRequest =
            createAuthorityRequest(
                traceId = traceId,
            )

        val request =
            MemoryRepresentationPreparationRequest.create(
                authorityRequest =
                    authorityRequest,
                memoryId =
                    MemoryId.from(
                        "memory-stage-103",
                    ),
                subjectIdentityId =
                    subjectIdentityId,
                memoryClass =
                    MemoryClass.PERSONAL,
                sensitivity =
                    MemorySensitivity.PRIVATE,
                confidence =
                    MemoryConfidence.from(89),
                retention =
                    MemoryRetention.LONG_TERM,
                source =
                    MemorySource.create(
                        sourceId =
                            "source-stage-103",
                        sourceType =
                            "explicit-stage-103-test-source",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Explicit Stage 103 continuity reason.",
                    ),
                content =
                    "Explicit Stage 103 continuity content.",
            )

        val representation =
            LogicalMemoryRepresentation.create(
                memoryId =
                    request.memoryId,
                subjectIdentityId =
                    request.subjectIdentityId,
                memoryClass =
                    request.memoryClass,
                sensitivity =
                    request.sensitivity,
                confidence =
                    request.confidence,
                retention =
                    request.retention,
                source =
                    request.source,
                ownerVisibleReason =
                    request.ownerVisibleReason,
                content =
                    request.content,
            )

        return MemoryRepresentationPreparationResult.create(
            traceId = traceId,
            status =
                MemoryRepresentationPreparationStatus.PREPARED,
            request = request,
            representation =
                representation,
        )
    }

    private fun createEstablishedOwnerContext(
        traceId: TraceId,
        subjectIdentityId: IdentityId,
    ): OwnerMultiUserContextResult {
        val ownerContext =
            OwnerContext.create(
                ownerIdentityId =
                    IdentityId.from(
                        "owner-stage-103",
                    ),
                subjectIdentityId =
                    subjectIdentityId,
            )

        return OwnerMultiUserContextResult.create(
            traceId = traceId,
            status =
                OwnerMultiUserContextStatus.ESTABLISHED,
            record =
                OwnerMultiUserContextRecord.create(
                    ownerContext = ownerContext,
                    currentSubjectIdentityId =
                        subjectIdentityId,
                ),
        )
    }

    private fun createAuthorityRequest(
        traceId: TraceId,
    ): MemoryAuthorityRequest {
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
                                                                    plan =
                                                                        createPlan(
                                                                            traceId =
                                                                                traceId,
                                                                        ),
                                                                    capability =
                                                                        createCapability(),
                                                                ),
                                                        ),
                                                ),
                                        ),
                                ),
                        ),
                ),
        )
    }

    private fun createPlan(
        traceId: TraceId,
    ): PlanRecord {
        val context =
            ContextEnvelope.create(
                traceId = traceId,
                schemaVersion =
                    SchemaVersion.from(1),
                source =
                    ContextSource.TEST,
                trustLevel =
                    ContextTrustLevel.VERIFIED,
                securityLevel =
                    ContextSecurityLevel.RESTRICTED,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_755_000_103_200L,
                    ),
            )

        val understanding =
            UnderstandingRecord.create(
                context = context,
                state =
                    UnderstandingState.COMPLETE,
                summary =
                    "Stage 103 bounded understanding.",
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state =
                    DecisionState.SELECTED,
                summary =
                    "Stage 103 bounded decision.",
            )

        val task =
            TaskRecord.create(
                taskId =
                    TaskId.from(
                        "task-stage-103",
                    ),
                decision = decision,
                state =
                    TaskState.CREATED,
                summary =
                    "Stage 103 bounded task.",
            )

        return PlanRecord.create(
            planId =
                PlanId.from(
                    "plan-stage-103",
                ),
            task = task,
            state =
                PlanState.CREATED,
            summary =
                "Stage 103 bounded plan.",
        )
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage-103",
                ),
            category =
                CapabilityCategory.ACTION,
            name =
                "Stage 103 bounded capability",
            description =
                "Stage 103 test fixture capability.",
        )
    }

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    code,
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_755_000_103_300L,
                ),
            summary =
                "Stage 103 bounded continuity upstream failure.",
        )
    }
}
