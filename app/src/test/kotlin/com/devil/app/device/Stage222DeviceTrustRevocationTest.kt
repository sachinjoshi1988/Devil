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

class Stage222DeviceTrustRevocationTest {

    @Test
    fun `explicit trusted disposition preserves exact Stage 221 provenance`() {
        val upstream = availableMemoryContinuity()

        val result =
            AndroidDeviceTrustRevocationCoordinator()
                .integrate(
                    memoryContinuity = upstream,
                    disposition = AndroidDeviceTrustRevocationStatus.TRUSTED,
                )

        assertEquals(
            AndroidDeviceTrustRevocationStatus.TRUSTED,
            result.status,
        )
        assertSame(upstream, result.memoryContinuity)
    }

    @Test
    fun `explicit revoked disposition preserves exact Stage 221 provenance`() {
        val upstream = availableMemoryContinuity()

        val result =
            AndroidDeviceTrustRevocationCoordinator()
                .integrate(
                    memoryContinuity = upstream,
                    disposition = AndroidDeviceTrustRevocationStatus.REVOKED,
                )

        assertEquals(
            AndroidDeviceTrustRevocationStatus.REVOKED,
            result.status,
        )
        assertSame(upstream, result.memoryContinuity)
    }

    @Test
    fun `missing disposition keeps Stage 222 deferred`() {
        val upstream = availableMemoryContinuity()

        val result =
            AndroidDeviceTrustRevocationCoordinator()
                .integrate(
                    memoryContinuity = upstream,
                    disposition = null,
                )

        assertEquals(
            AndroidDeviceTrustRevocationStatus.DEFERRED,
            result.status,
        )
        assertSame(upstream, result.memoryContinuity)
    }

    @Test
    fun `explicit deferred disposition remains deferred`() {
        val upstream = availableMemoryContinuity()

        val result =
            AndroidDeviceTrustRevocationCoordinator()
                .integrate(
                    memoryContinuity = upstream,
                    disposition = AndroidDeviceTrustRevocationStatus.DEFERRED,
                )

        assertEquals(
            AndroidDeviceTrustRevocationStatus.DEFERRED,
            result.status,
        )
        assertSame(upstream, result.memoryContinuity)
    }

    @Test
    fun `deferred Stage 221 continuity cannot become trusted or revoked`() {
        val available = availableMemoryContinuity()

        val deferred =
            AndroidCrossDeviceMemoryContinuityResult.create(
                status = AndroidCrossDeviceMemoryContinuityStatus.DEFERRED,
                taskContinuity = available.taskContinuity,
                memoryContinuity = available.memoryContinuity,
            )

        val trustedAttempt =
            AndroidDeviceTrustRevocationCoordinator()
                .integrate(
                    memoryContinuity = deferred,
                    disposition = AndroidDeviceTrustRevocationStatus.TRUSTED,
                )

        val revokedAttempt =
            AndroidDeviceTrustRevocationCoordinator()
                .integrate(
                    memoryContinuity = deferred,
                    disposition = AndroidDeviceTrustRevocationStatus.REVOKED,
                )

        assertEquals(
            AndroidDeviceTrustRevocationStatus.DEFERRED,
            trustedAttempt.status,
        )
        assertEquals(
            AndroidDeviceTrustRevocationStatus.DEFERRED,
            revokedAttempt.status,
        )
        assertSame(deferred, trustedAttempt.memoryContinuity)
        assertSame(deferred, revokedAttempt.memoryContinuity)
    }

    @Test
    fun `result invariants reject trusted or revoked state without available Stage 221 continuity`() {
        val available = availableMemoryContinuity()

        val deferred =
            AndroidCrossDeviceMemoryContinuityResult.create(
                status = AndroidCrossDeviceMemoryContinuityStatus.DEFERRED,
                taskContinuity = available.taskContinuity,
                memoryContinuity = available.memoryContinuity,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidDeviceTrustRevocationResult.create(
                status = AndroidDeviceTrustRevocationStatus.TRUSTED,
                memoryContinuity = deferred,
            )
        }

        assertFailsWith<IllegalArgumentException> {
            AndroidDeviceTrustRevocationResult.create(
                status = AndroidDeviceTrustRevocationStatus.REVOKED,
                memoryContinuity = deferred,
            )
        }
    }

    private fun availableMemoryContinuity(): AndroidCrossDeviceMemoryContinuityResult {
        return AndroidCrossDeviceMemoryContinuityCoordinator()
            .integrate(
                taskContinuity = availableTaskContinuity(),
                memoryContinuity = establishedMemoryContinuity(),
            )
    }

    private fun establishedMemoryContinuity(): MemoryContinuityResult {
        val representation =
            LogicalMemoryRepresentation.create(
                memoryId =
                    MemoryId.from(
                        "memory-stage222-cross-device",
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
                        sourceId = "source-stage222",
                        sourceType = "bounded-existing-memory",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Preserve bounded Stage 222 upstream memory continuity.",
                    ),
                content =
                    "Existing bounded logical-memory context.",
            )

        return MemoryContinuityResult.create(
            traceId =
                TraceId.from(
                    "trace-stage222-memory",
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
                        "trace-stage222-relationship",
                    ),
                status =
                    CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
                relationship =
                    CrossDeviceRelationshipRecord.create(
                        sourceEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage222:source",
                            ),
                        targetEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage222:target",
                            ),
                        description =
                            "Bounded Stage 222 cross-device relationship.",
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
                        "session-stage222",
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
                "trace-stage222-session",
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
                    "task-stage222",
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
                                "trace-stage222-task",
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
                    "Preserve bounded device trust and revocation representation.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary =
                "Preserve bounded device trust and revocation representation.",
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
            "identity:stage222:subject"
    }
}
