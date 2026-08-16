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

class Stage102MemoryRepresentationPreparationGovernanceTest {

    @Test
    fun `committable authority plus established matching subject prepares representation`() {
        val traceId = TraceId.from("trace-stage-102-001")
        val subjectId = IdentityId.from("subject-stage-102")
        val authorityRequest = createAuthorityRequest(traceId)
        val request =
            createPreparationRequest(
                authorityRequest = authorityRequest,
                subjectIdentityId = subjectId,
            )

        val result =
            MemoryRepresentationPreparationCoordinator()
                .prepare(
                    traceId = traceId,
                    memoryAuthority =
                        MemoryAuthorityResult.create(
                            traceId = traceId,
                            status =
                                MemoryAuthorityStatus.COMMITTABLE,
                            request = authorityRequest,
                        ),
                    ownerContext =
                        createEstablishedOwnerContext(
                            traceId = traceId,
                            subjectIdentityId = subjectId,
                        ),
                    request = request,
                )

        assertEquals(
            MemoryRepresentationPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(request, result.request)
        assertEquals(request.memoryId, result.representation?.memoryId)
        assertEquals(
            request.subjectIdentityId,
            result.representation?.subjectIdentityId,
        )
        assertEquals(
            request.memoryClass,
            result.representation?.memoryClass,
        )
        assertEquals(
            request.sensitivity,
            result.representation?.sensitivity,
        )
        assertEquals(
            request.confidence,
            result.representation?.confidence,
        )
        assertEquals(
            request.retention,
            result.representation?.retention,
        )
        assertEquals(
            request.source,
            result.representation?.source,
        )
        assertEquals(
            request.ownerVisibleReason,
            result.representation?.ownerVisibleReason,
        )
        assertEquals(
            request.content,
            result.representation?.content,
        )
        assertNull(result.error)
    }

    @Test
    fun `non committable memory authority defers preparation`() {
        val traceId = TraceId.from("trace-stage-102-002")
        val subjectId = IdentityId.from("subject-stage-102")

        val result =
            MemoryRepresentationPreparationCoordinator()
                .prepare(
                    traceId = traceId,
                    memoryAuthority =
                        MemoryAuthorityResult.create(
                            traceId = traceId,
                            status = MemoryAuthorityStatus.DEFERRED,
                        ),
                    ownerContext =
                        createEstablishedOwnerContext(
                            traceId = traceId,
                            subjectIdentityId = subjectId,
                        ),
                    request =
                        createPreparationRequest(
                            authorityRequest =
                                createAuthorityRequest(traceId),
                            subjectIdentityId = subjectId,
                        ),
                )

        assertEquals(
            MemoryRepresentationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.representation)
        assertNull(result.error)
    }

    @Test
    fun `non established owner context defers preparation`() {
        val traceId = TraceId.from("trace-stage-102-003")
        val subjectId = IdentityId.from("subject-stage-102")
        val authorityRequest = createAuthorityRequest(traceId)

        val result =
            MemoryRepresentationPreparationCoordinator()
                .prepare(
                    traceId = traceId,
                    memoryAuthority =
                        MemoryAuthorityResult.create(
                            traceId = traceId,
                            status =
                                MemoryAuthorityStatus.COMMITTABLE,
                            request = authorityRequest,
                        ),
                    ownerContext =
                        OwnerMultiUserContextResult.create(
                            traceId = traceId,
                            status =
                                OwnerMultiUserContextStatus.DEFERRED,
                        ),
                    request =
                        createPreparationRequest(
                            authorityRequest = authorityRequest,
                            subjectIdentityId = subjectId,
                        ),
                )

        assertEquals(
            MemoryRepresentationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.representation)
    }

    @Test
    fun `subject mismatch defers without transforming identity`() {
        val traceId = TraceId.from("trace-stage-102-004")
        val authorityRequest = createAuthorityRequest(traceId)

        val result =
            MemoryRepresentationPreparationCoordinator()
                .prepare(
                    traceId = traceId,
                    memoryAuthority =
                        MemoryAuthorityResult.create(
                            traceId = traceId,
                            status =
                                MemoryAuthorityStatus.COMMITTABLE,
                            request = authorityRequest,
                        ),
                    ownerContext =
                        createEstablishedOwnerContext(
                            traceId = traceId,
                            subjectIdentityId =
                                IdentityId.from(
                                    "established-subject",
                                ),
                        ),
                    request =
                        createPreparationRequest(
                            authorityRequest = authorityRequest,
                            subjectIdentityId =
                                IdentityId.from(
                                    "different-subject",
                                ),
                        ),
                )

        assertEquals(
            MemoryRepresentationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.representation)
        assertNull(result.error)
    }

    @Test
    fun `different authority request is rejected even when authority is committable`() {
        val traceId = TraceId.from("trace-stage-102-005")
        val subjectId = IdentityId.from("subject-stage-102")

        val approvedRequest = createAuthorityRequest(traceId)
        val differentRequest = createAuthorityRequest(traceId, "different")

        assertFailsWith<IllegalArgumentException> {
            MemoryRepresentationPreparationCoordinator()
                .prepare(
                    traceId = traceId,
                    memoryAuthority =
                        MemoryAuthorityResult.create(
                            traceId = traceId,
                            status =
                                MemoryAuthorityStatus.COMMITTABLE,
                            request = approvedRequest,
                        ),
                    ownerContext =
                        createEstablishedOwnerContext(
                            traceId = traceId,
                            subjectIdentityId = subjectId,
                        ),
                    request =
                        createPreparationRequest(
                            authorityRequest = differentRequest,
                            subjectIdentityId = subjectId,
                        ),
                )
        }
    }

    @Test
    fun `memory authority failure propagates matching error`() {
        val traceId = TraceId.from("trace-stage-102-006")
        val error =
            createError(
                traceId = traceId,
                code = "MEMORY_AUTHORITY_STAGE_102_FAILED",
            )

        val result =
            MemoryRepresentationPreparationCoordinator()
                .prepare(
                    traceId = traceId,
                    memoryAuthority =
                        MemoryAuthorityResult.create(
                            traceId = traceId,
                            status = MemoryAuthorityStatus.FAILED,
                            error = error,
                        ),
                    ownerContext =
                        OwnerMultiUserContextResult.create(
                            traceId = traceId,
                            status =
                                OwnerMultiUserContextStatus.DEFERRED,
                        ),
                    request =
                        createPreparationRequest(
                            authorityRequest =
                                createAuthorityRequest(traceId),
                            subjectIdentityId =
                                IdentityId.from(
                                    "subject-stage-102",
                                ),
                        ),
                )

        assertEquals(
            MemoryRepresentationPreparationStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.request)
        assertNull(result.representation)
    }

    @Test
    fun `owner context failure propagates matching error`() {
        val traceId = TraceId.from("trace-stage-102-007")
        val error =
            createError(
                traceId = traceId,
                code = "OWNER_CONTEXT_STAGE_102_FAILED",
            )

        val result =
            MemoryRepresentationPreparationCoordinator()
                .prepare(
                    traceId = traceId,
                    memoryAuthority =
                        MemoryAuthorityResult.create(
                            traceId = traceId,
                            status = MemoryAuthorityStatus.DEFERRED,
                        ),
                    ownerContext =
                        OwnerMultiUserContextResult.create(
                            traceId = traceId,
                            status =
                                OwnerMultiUserContextStatus.FAILED,
                            error = error,
                        ),
                    request =
                        createPreparationRequest(
                            authorityRequest =
                                createAuthorityRequest(traceId),
                            subjectIdentityId =
                                IdentityId.from(
                                    "subject-stage-102",
                                ),
                        ),
                )

        assertEquals(
            MemoryRepresentationPreparationStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
    }

    @Test
    fun `memory authority trace mismatch is rejected`() {
        val request = createAuthorityRequest(TraceId.from("trace-stage-102-other"))

        assertFailsWith<IllegalArgumentException> {
            MemoryRepresentationPreparationCoordinator()
                .prepare(
                    traceId =
                        TraceId.from(
                            "trace-stage-102-primary",
                        ),
                    memoryAuthority =
                        MemoryAuthorityResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-stage-102-other",
                                ),
                            status =
                                MemoryAuthorityStatus.COMMITTABLE,
                            request = request,
                        ),
                    ownerContext =
                        OwnerMultiUserContextResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-stage-102-primary",
                                ),
                            status =
                                OwnerMultiUserContextStatus.DEFERRED,
                        ),
                    request =
                        createPreparationRequest(
                            authorityRequest = request,
                            subjectIdentityId =
                                IdentityId.from(
                                    "subject-stage-102",
                                ),
                        ),
                )
        }
    }

    @Test
    fun `owner context trace mismatch is rejected`() {
        val authorityRequest = createAuthorityRequest(TraceId.from("trace-stage-102-primary-owner"))

        assertFailsWith<IllegalArgumentException> {
            MemoryRepresentationPreparationCoordinator()
                .prepare(
                    traceId =
                        TraceId.from(
                            "trace-stage-102-primary-owner",
                        ),
                    memoryAuthority =
                        MemoryAuthorityResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-stage-102-primary-owner",
                                ),
                            status =
                                MemoryAuthorityStatus.COMMITTABLE,
                            request = authorityRequest,
                        ),
                    ownerContext =
                        OwnerMultiUserContextResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-stage-102-other-owner",
                                ),
                            status =
                                OwnerMultiUserContextStatus.DEFERRED,
                        ),
                    request =
                        createPreparationRequest(
                            authorityRequest = authorityRequest,
                            subjectIdentityId =
                                IdentityId.from(
                                    "subject-stage-102",
                                ),
                        ),
                )
        }
    }

    private fun createPreparationRequest(
        authorityRequest: MemoryAuthorityRequest,
        subjectIdentityId: IdentityId,
    ): MemoryRepresentationPreparationRequest {
        return MemoryRepresentationPreparationRequest.create(
            authorityRequest = authorityRequest,
            memoryId = MemoryId.from("memory-stage-102"),
            subjectIdentityId = subjectIdentityId,
            memoryClass = MemoryClass.PERSONAL,
            sensitivity = MemorySensitivity.PRIVATE,
            confidence = MemoryConfidence.from(88),
            retention = MemoryRetention.LONG_TERM,
            source =
                MemorySource.create(
                    sourceId = "stage-102-source",
                    sourceType = "explicit-test-source",
                ),
            ownerVisibleReason =
                OwnerVisibleMemoryReason.from(
                    "Explicit Stage 102 reason.",
                ),
            content =
                "Explicit Stage 102 logical-memory content.",
        )
    }

    private fun createEstablishedOwnerContext(
        traceId: TraceId,
        subjectIdentityId: IdentityId,
    ): OwnerMultiUserContextResult {
        val ownerIdentityId =
            IdentityId.from("owner-stage-102")

        val ownerContext =
            OwnerContext.create(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId = subjectIdentityId,
            )

        return OwnerMultiUserContextResult.create(
            traceId = traceId,
            status = OwnerMultiUserContextStatus.ESTABLISHED,
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
        suffix: String = "approved",
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
                                                                            traceId,
                                                                            suffix,
                                                                        ),
                                                                    capability =
                                                                        createCapability(
                                                                            suffix,
                                                                        ),
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
        suffix: String,
    ): PlanRecord {
        val context = createContext(traceId)

        val understanding =
            UnderstandingRecord.create(
                context = context,
                state = UnderstandingState.COMPLETE,
                summary =
                    "Stage 102 bounded understanding.",
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state = DecisionState.SELECTED,
                summary =
                    "Stage 102 bounded decision.",
            )

        val task =
            TaskRecord.create(
                taskId =
                    TaskId.from(
                        "task-stage-102-$suffix",
                    ),
                decision = decision,
                state = TaskState.CREATED,
                summary =
                    "Stage 102 bounded task.",
            )

        return PlanRecord.create(
            planId =
                PlanId.from(
                    "plan-stage-102-$suffix",
                ),
            task = task,
            state = PlanState.CREATED,
            summary =
                "Stage 102 bounded plan.",
        )
    }

    private fun createCapability(
        suffix: String,
    ): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage-102-$suffix",
                ),
            category = CapabilityCategory.ACTION,
            name = "Stage 102 capability $suffix",
            description =
                "Stage 102 bounded capability $suffix.",
        )
    }

    private fun createContext(
        traceId: TraceId,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = traceId,
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_755_000_102_300L,
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_755_000_102_400L,
                ),
            summary =
                "Stage 102 upstream dependency failed.",
        )
    }
}
