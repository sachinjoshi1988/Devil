package com.devil.app.memory

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
import com.devil.core.model.learning.LearningRequest
import com.devil.core.model.memory.MemoryAuthorityRequest
import com.devil.core.model.memory.MemoryCommitmentRequest
import com.devil.core.model.memory.MemoryPersistenceRequest
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
import com.devil.core.runtime.memory.MemoryPersistenceResult
import com.devil.core.runtime.memory.MemoryPersistenceStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAndroidMemoryPersistenceCoordinatorTest {

    @Test
    fun `persist defers core deferred result without invoking store`() {
        val traceId =
            TraceId.from(
                "trace-android-memory-coordinator-001",
            )
        var invoked = false

        val coordinator =
            DefaultAndroidMemoryPersistenceCoordinator(
                store =
                    object : AndroidMemoryPersistenceStore {
                        override fun persist(
                            traceId: TraceId,
                            request: MemoryPersistenceRequest,
                        ): AndroidMemoryPersistenceResult {
                            invoked = true

                            return AndroidMemoryPersistenceResult.create(
                                traceId = traceId,
                                status =
                                    AndroidMemoryPersistenceStatus.PERSISTED,
                            )
                        }
                    },
            )

        val result =
            coordinator.persist(
                MemoryPersistenceResult.create(
                    traceId = traceId,
                    status =
                        MemoryPersistenceStatus.DEFERRED,
                ),
            )

        assertEquals(false, invoked)
        assertEquals(
            AndroidMemoryPersistenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `persist sends genuinely persistable request to bounded Android store`() {
        val request =
            createRequest(
                "trace-android-memory-coordinator-002",
            )
        val traceId =
            request.commitmentRequest
                .authorityRequest
                .proposal
                .learning
                .worldModelUpdate
                .outcome
                .verification
                .observation
                .execution
                .plan
                .task
                .decision
                .understanding
                .context
                .traceId

        val coordinator =
            DefaultAndroidMemoryPersistenceCoordinator(
                store =
                    object : AndroidMemoryPersistenceStore {
                        override fun persist(
                            traceId: TraceId,
                            request: MemoryPersistenceRequest,
                        ): AndroidMemoryPersistenceResult {
                            assertEquals(
                                traceId,
                                request.commitmentRequest
                                    .authorityRequest
                                    .proposal
                                    .learning
                                    .worldModelUpdate
                                    .outcome
                                    .verification
                                    .observation
                                    .execution
                                    .plan
                                    .task
                                    .decision
                                    .understanding
                                    .context
                                    .traceId,
                            )

                            return AndroidMemoryPersistenceResult.create(
                                traceId = traceId,
                                status =
                                    AndroidMemoryPersistenceStatus.PERSISTED,
                            )
                        }
                    },
            )

        val result =
            coordinator.persist(
                MemoryPersistenceResult.create(
                    traceId = traceId,
                    status =
                        MemoryPersistenceStatus.PERSISTABLE,
                    request = request,
                ),
            )

        assertEquals(
            AndroidMemoryPersistenceStatus.PERSISTED,
            result.status,
        )
    }

    @Test
    fun `default store defers persistable request instead of fabricating durable storage`() {
        val request =
            createRequest(
                "trace-android-memory-coordinator-003",
            )
        val traceId =
            request.commitmentRequest
                .authorityRequest
                .proposal
                .learning
                .worldModelUpdate
                .outcome
                .verification
                .observation
                .execution
                .plan
                .task
                .decision
                .understanding
                .context
                .traceId

        val result =
            DefaultAndroidMemoryPersistenceCoordinator()
                .persist(
                    MemoryPersistenceResult.create(
                        traceId = traceId,
                        status =
                            MemoryPersistenceStatus.PERSISTABLE,
                        request = request,
                    ),
                )

        assertEquals(
            AndroidMemoryPersistenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `persist preserves failed core persistence error`() {
        val traceId =
            TraceId.from(
                "trace-android-memory-coordinator-004",
            )
        val error = createError(traceId)

        val result =
            DefaultAndroidMemoryPersistenceCoordinator()
                .persist(
                    MemoryPersistenceResult.create(
                        traceId = traceId,
                        status =
                            MemoryPersistenceStatus.FAILED,
                        error = error,
                    ),
                )

        assertEquals(
            AndroidMemoryPersistenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
    }

    @Test
    fun `persist rejects Android store result from different trace`() {
        val request =
            createRequest(
                "trace-android-memory-coordinator-005",
            )
        val traceId =
            request.commitmentRequest
                .authorityRequest
                .proposal
                .learning
                .worldModelUpdate
                .outcome
                .verification
                .observation
                .execution
                .plan
                .task
                .decision
                .understanding
                .context
                .traceId

        val coordinator =
            DefaultAndroidMemoryPersistenceCoordinator(
                store =
                    object : AndroidMemoryPersistenceStore {
                        override fun persist(
                            traceId: TraceId,
                            request: MemoryPersistenceRequest,
                        ): AndroidMemoryPersistenceResult {
                            return AndroidMemoryPersistenceResult.create(
                                traceId =
                                    TraceId.from(
                                        "trace-android-memory-store-other",
                                    ),
                                status =
                                    AndroidMemoryPersistenceStatus.DEFERRED,
                            )
                        }
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.persist(
                MemoryPersistenceResult.create(
                    traceId = traceId,
                    status =
                        MemoryPersistenceStatus.PERSISTABLE,
                    request = request,
                ),
            )
        }
    }

    private fun createRequest(
        traceValue: String,
    ): MemoryPersistenceRequest {
        val context =
            ContextEnvelope.create(
                traceId = TraceId.from(traceValue),
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEST,
                trustLevel =
                    ContextTrustLevel.VERIFIED,
                securityLevel =
                    ContextSecurityLevel.RESTRICTED,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_302_000L,
                    ),
            )

        val understanding =
            UnderstandingRecord.create(
                context = context,
                state = UnderstandingState.COMPLETE,
                summary =
                    "Bounded Android memory persistence test understanding.",
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state = DecisionState.SELECTED,
                summary =
                    "Evaluate bounded Android memory persistence.",
            )

        val task =
            TaskRecord.create(
                taskId =
                    TaskId.from(
                        "task-android-memory-persistence",
                    ),
                decision = decision,
                state = TaskState.CREATED,
                summary =
                    "Prepare the bounded memory persistence path.",
            )

        val plan =
            PlanRecord.create(
                planId =
                    PlanId.from(
                        "plan-android-memory-persistence",
                    ),
                task = task,
                state = PlanState.CREATED,
                summary =
                    "Preserve the single Memory Authority path.",
            )

        val execution =
            ExecutionRequest.create(
                plan = plan,
                capability =
                    CapabilityContract.create(
                        capabilityId =
                            CapabilityId.from(
                                "capability-android-memory-persistence",
                            ),
                        category =
                            CapabilityCategory.KNOWLEDGE,
                        name =
                            "Android Memory Persistence Test Capability",
                        description =
                            "Represents a bounded test capability without actual Android storage.",
                    ),
            )

        return MemoryPersistenceRequest.create(
            commitmentRequest =
                MemoryCommitmentRequest.create(
                    authorityRequest =
                        MemoryAuthorityRequest.create(
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
                                                                                execution,
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

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "ANDROID_MEMORY_PERSISTENCE_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_302_500L,
                ),
            summary =
                "Bounded Android memory persistence failed.",
        )
    }
}
