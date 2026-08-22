package com.devil.app.device

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.embodiment.CrossDeviceRelationshipRecord
import com.devil.core.model.embodiment.EmbodimentId
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.memory.LogicalMemoryRepresentation
import com.devil.core.model.memory.MemoryClass
import com.devil.core.model.memory.MemoryConfidence
import com.devil.core.model.memory.MemoryContinuityRecord
import com.devil.core.model.memory.MemoryId
import com.devil.core.model.memory.MemoryRetention
import com.devil.core.model.memory.MemorySensitivity
import com.devil.core.model.memory.MemorySource
import com.devil.core.model.memory.OwnerVisibleMemoryReason
import com.devil.core.model.security.SessionId
import com.devil.core.model.security.SessionRecord
import com.devil.core.model.security.SessionState
import com.devil.core.model.security.SessionValidityRequest
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationResult
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationStatus
import com.devil.core.runtime.memory.MemoryContinuityResult
import com.devil.core.runtime.memory.MemoryContinuityStatus
import com.devil.core.runtime.security.SessionValidityResult
import com.devil.core.runtime.security.SessionValidityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage221CrossDeviceMemoryContinuityTest {

    @Test
    fun `available task and established memory continuity become available`() {
        val taskContinuity = availableTaskContinuity()
        val memoryContinuity = establishedMemoryContinuity()

        val result =
            AndroidCrossDeviceMemoryContinuityCoordinator()
                .integrate(
                    taskContinuity = taskContinuity,
                    memoryContinuity = memoryContinuity,
                )

        assertEquals(
            AndroidCrossDeviceMemoryContinuityStatus.AVAILABLE,
            result.status,
        )
        assertSame(taskContinuity, result.taskContinuity)
        assertSame(memoryContinuity, result.memoryContinuity)
        assertSame(
            memoryContinuity.record,
            result.memoryContinuity.record,
        )
    }

    @Test
    fun `deferred task continuity keeps memory continuity deferred at stage 221`() {
        val available = availableTaskContinuity()

        val taskContinuity =
            AndroidCrossDeviceTaskContinuityResult.create(
                status = AndroidCrossDeviceTaskContinuityStatus.DEFERRED,
                sessionGovernance = available.sessionGovernance,
                task = available.task,
            )

        val memoryContinuity = establishedMemoryContinuity()

        val result =
            AndroidCrossDeviceMemoryContinuityCoordinator()
                .integrate(
                    taskContinuity = taskContinuity,
                    memoryContinuity = memoryContinuity,
                )

        assertEquals(
            AndroidCrossDeviceMemoryContinuityStatus.DEFERRED,
            result.status,
        )
        assertSame(taskContinuity, result.taskContinuity)
        assertSame(memoryContinuity, result.memoryContinuity)
    }

    @Test
    fun `deferred memory continuity keeps stage 221 deferred`() {
        val taskContinuity = availableTaskContinuity()

        val memoryContinuity =
            MemoryContinuityResult.create(
                traceId = TraceId.from("trace-stage221-memory-deferred"),
                status = MemoryContinuityStatus.DEFERRED,
            )

        val result =
            AndroidCrossDeviceMemoryContinuityCoordinator()
                .integrate(
                    taskContinuity = taskContinuity,
                    memoryContinuity = memoryContinuity,
                )

        assertEquals(
            AndroidCrossDeviceMemoryContinuityStatus.DEFERRED,
            result.status,
        )
        assertSame(taskContinuity, result.taskContinuity)
        assertSame(memoryContinuity, result.memoryContinuity)
    }

    @Test
    fun `available result rejects deferred task continuity`() {
        val available = availableTaskContinuity()

        val deferred =
            AndroidCrossDeviceTaskContinuityResult.create(
                status = AndroidCrossDeviceTaskContinuityStatus.DEFERRED,
                sessionGovernance = available.sessionGovernance,
                task = available.task,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidCrossDeviceMemoryContinuityResult.create(
                status = AndroidCrossDeviceMemoryContinuityStatus.AVAILABLE,
                taskContinuity = deferred,
                memoryContinuity = establishedMemoryContinuity(),
            )
        }
    }

    @Test
    fun `available result rejects non established memory continuity`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidCrossDeviceMemoryContinuityResult.create(
                status = AndroidCrossDeviceMemoryContinuityStatus.AVAILABLE,
                taskContinuity = availableTaskContinuity(),
                memoryContinuity =
                    MemoryContinuityResult.create(
                        traceId =
                            TraceId.from(
                                "trace-stage221-memory-not-established",
                            ),
                        status = MemoryContinuityStatus.DEFERRED,
                    ),
            )
        }
    }

    @Test
    fun `established memory representation provenance remains exact`() {
        val memoryContinuity = establishedMemoryContinuity()
        val originalRepresentation =
            requireNotNull(memoryContinuity.record).representation

        val result =
            AndroidCrossDeviceMemoryContinuityCoordinator()
                .integrate(
                    taskContinuity = availableTaskContinuity(),
                    memoryContinuity = memoryContinuity,
                )

        assertSame(
            originalRepresentation,
            requireNotNull(result.memoryContinuity.record).representation,
        )
    }

    private fun establishedMemoryContinuity(): MemoryContinuityResult {
        val representation =
            LogicalMemoryRepresentation.create(
                memoryId =
                    MemoryId.from(
                        "memory-stage221-cross-device",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        SUBJECT_ID,
                    ),
                memoryClass = MemoryClass.WORKING,
                sensitivity = MemorySensitivity.PRIVATE,
                confidence = MemoryConfidence.from(80),
                retention = MemoryRetention.SESSION,
                source =
                    MemorySource.create(
                        sourceId = "source-stage221",
                        sourceType = "bounded-existing-memory",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Preserve bounded logical-memory continuity without synchronization.",
                    ),
                content =
                    "Existing bounded logical-memory context.",
            )

        return MemoryContinuityResult.create(
            traceId =
                TraceId.from(
                    "trace-stage221-memory",
                ),
            status = MemoryContinuityStatus.ESTABLISHED,
            record =
                MemoryContinuityRecord.create(
                    representation = representation,
                ),
        )
    }

    private fun availableTaskContinuity(): AndroidCrossDeviceTaskContinuityResult {
        val identity =
            IdentityId.from(
                SUBJECT_ID,
            )

        val relationship =
            CrossDeviceRelationshipRepresentationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage221-relationship",
                    ),
                status =
                    CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
                relationship =
                    CrossDeviceRelationshipRecord.create(
                        sourceEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage221:source",
                            ),
                        targetEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage221:target",
                            ),
                        description =
                            "Bounded Stage 221 cross-device relationship.",
                    ),
            )

        val crossDeviceIdentity =
            AndroidCrossDeviceIdentityResult.create(
                status = AndroidCrossDeviceIdentityStatus.AVAILABLE,
                relationshipRepresentation = relationship,
                identityId = identity,
            )

        val session =
            SessionRecord.create(
                sessionId =
                    SessionId.from(
                        "session-stage221",
                    ),
                subjectIdentityId = identity,
                state = SessionState.ACTIVE,
                establishedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        100L,
                    ),
                expiresAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        200L,
                    ),
            )

        val sessionTrace =
            TraceId.from(
                "trace-stage221-session",
            )

        val sessionValidity =
            SessionValidityResult.create(
                traceId = sessionTrace,
                status = SessionValidityStatus.VALID,
                request =
                    SessionValidityRequest.create(
                        context = context(sessionTrace),
                        session = session,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                150L,
                            ),
                    ),
            )

        val sessionGovernance =
            AndroidCrossDeviceSessionGovernanceResult.create(
                status =
                    AndroidCrossDeviceSessionGovernanceStatus.AVAILABLE,
                crossDeviceIdentity = crossDeviceIdentity,
                session = session,
                sessionValidity = sessionValidity,
            )

        return AndroidCrossDeviceTaskContinuityResult.create(
            status = AndroidCrossDeviceTaskContinuityStatus.AVAILABLE,
            sessionGovernance = sessionGovernance,
            task = task(),
        )
    }

    private fun task(): TaskRecord {
        val decision = decision()

        return TaskRecord.create(
            taskId =
                TaskId.from(
                    "task-stage221",
                ),
            decision = decision,
            state = TaskState.CREATED,
            summary = decision.summary,
        )
    }

    private fun decision(): DecisionRecord {
        val understanding =
            UnderstandingRecord.create(
                context =
                    ContextEnvelope.create(
                        traceId =
                            TraceId.from(
                                "trace-stage221-task",
                            ),
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel = ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                150L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Preserve bounded cross-device memory continuity.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary =
                "Preserve bounded cross-device memory continuity.",
        )
    }

    private fun context(
        traceId: TraceId,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = traceId,
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.SYSTEM,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    150L,
                ),
        )
    }

    private companion object {
        const val SUBJECT_ID: String =
            "identity:stage221:subject"
    }
}
