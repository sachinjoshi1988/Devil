package com.devil.app.device

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.embodiment.CrossDeviceRelationshipRecord
import com.devil.core.model.embodiment.EmbodimentId
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.security.SessionId
import com.devil.core.model.security.SessionRecord
import com.devil.core.model.security.SessionState
import com.devil.core.model.security.SessionValidityRequest
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationResult
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationStatus
import com.devil.core.runtime.security.SessionValidityResult
import com.devil.core.runtime.security.SessionValidityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage220CrossDeviceTaskContinuityTest {

    @Test
    fun `available session governance and exact task become available`() {
        val sessionGovernance =
            availableSessionGovernance()

        val task =
            task()

        val result =
            AndroidCrossDeviceTaskContinuityCoordinator()
                .integrate(
                    sessionGovernance = sessionGovernance,
                    task = task,
                )

        assertEquals(
            AndroidCrossDeviceTaskContinuityStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            sessionGovernance,
            result.sessionGovernance,
        )
        assertSame(
            task,
            result.task,
        )
    }

    @Test
    fun `deferred session governance keeps task continuity deferred`() {
        val identity =
            IdentityId.from(
                "identity:stage220:deferred",
            )

        val crossDeviceIdentity =
            availableCrossDeviceIdentity(identity)

        val session =
            session(identity)

        val deferredSessionGovernance =
            AndroidCrossDeviceSessionGovernanceCoordinator()
                .govern(
                    crossDeviceIdentity = crossDeviceIdentity,
                    session = session,
                    sessionValidity =
                        SessionValidityResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-stage220:deferred",
                                ),
                            status =
                                SessionValidityStatus.DEFERRED,
                        ),
                )

        val task =
            task()

        val result =
            AndroidCrossDeviceTaskContinuityCoordinator()
                .integrate(
                    sessionGovernance = deferredSessionGovernance,
                    task = task,
                )

        assertEquals(
            AndroidCrossDeviceTaskContinuityStatus.DEFERRED,
            result.status,
        )
        assertSame(
            deferredSessionGovernance,
            result.sessionGovernance,
        )
        assertSame(
            task,
            result.task,
        )
    }

    @Test
    fun `available result requires available Stage 219 session governance`() {
        val identity =
            IdentityId.from(
                "identity:stage220:invalid",
            )

        val session =
            session(identity)

        val deferredSessionGovernance =
            AndroidCrossDeviceSessionGovernanceCoordinator()
                .govern(
                    crossDeviceIdentity =
                        availableCrossDeviceIdentity(identity),
                    session = session,
                    sessionValidity =
                        SessionValidityResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-stage220:invalid",
                                ),
                            status =
                                SessionValidityStatus.DEFERRED,
                        ),
                )

        assertFailsWith<IllegalArgumentException> {
            AndroidCrossDeviceTaskContinuityResult.create(
                status =
                    AndroidCrossDeviceTaskContinuityStatus.AVAILABLE,
                sessionGovernance = deferredSessionGovernance,
                task = task(),
            )
        }
    }

    @Test
    fun `deferred result preserves exact task without claiming continuation`() {
        val identity =
            IdentityId.from(
                "identity:stage220:preserved",
            )

        val session =
            session(identity)

        val deferredSessionGovernance =
            AndroidCrossDeviceSessionGovernanceCoordinator()
                .govern(
                    crossDeviceIdentity =
                        availableCrossDeviceIdentity(identity),
                    session = session,
                    sessionValidity =
                        SessionValidityResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-stage220:preserved",
                                ),
                            status =
                                SessionValidityStatus.DEFERRED,
                        ),
                )

        val task =
            task()

        val result =
            AndroidCrossDeviceTaskContinuityResult.create(
                status =
                    AndroidCrossDeviceTaskContinuityStatus.DEFERRED,
                sessionGovernance = deferredSessionGovernance,
                task = task,
            )

        assertEquals(
            AndroidCrossDeviceTaskContinuityStatus.DEFERRED,
            result.status,
        )
        assertSame(
            task,
            result.task,
        )
    }

    private fun availableSessionGovernance():
        AndroidCrossDeviceSessionGovernanceResult {
        val identity =
            IdentityId.from(
                "identity:stage220:subject",
            )

        val session =
            session(identity)

        return AndroidCrossDeviceSessionGovernanceCoordinator()
            .govern(
                crossDeviceIdentity =
                    availableCrossDeviceIdentity(identity),
                session = session,
                sessionValidity =
                    validSessionResult(session),
            )
    }

    private fun availableCrossDeviceIdentity(
        identityId: IdentityId,
    ): AndroidCrossDeviceIdentityResult {
        val relationship =
            CrossDeviceRelationshipRepresentationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage220:relationship",
                    ),
                status =
                    CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
                relationship =
                    CrossDeviceRelationshipRecord.create(
                        sourceEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage220:source",
                            ),
                        targetEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage220:target",
                            ),
                        description =
                            "Bounded Stage 220 cross-device relationship.",
                    ),
            )

        return AndroidCrossDeviceIdentityCoordinator()
            .integrate(
                relationshipRepresentation = relationship,
                identityId = identityId,
            )
    }

    private fun session(
        identityId: IdentityId,
    ): SessionRecord {
        return SessionRecord.create(
            sessionId =
                SessionId.from(
                    "session:stage220:${identityId.value}",
                ),
            subjectIdentityId = identityId,
            state = SessionState.ACTIVE,
            establishedAt =
                DevilTimestamp.fromEpochMilliseconds(100L),
            expiresAt =
                DevilTimestamp.fromEpochMilliseconds(200L),
        )
    }

    private fun validSessionResult(
        session: SessionRecord,
    ): SessionValidityResult {
        val traceId =
            TraceId.from(
                "trace-stage220:valid:${session.sessionId.value}",
            )

        return SessionValidityResult.create(
            traceId = traceId,
            status = SessionValidityStatus.VALID,
            request =
                SessionValidityRequest.create(
                    context =
                        ContextEnvelope.create(
                            traceId = traceId,
                            schemaVersion = SchemaVersion.from(1),
                            source = ContextSource.SYSTEM,
                            trustLevel = ContextTrustLevel.UNVERIFIED,
                            securityLevel =
                                ContextSecurityLevel.RESTRICTED,
                            observedAt =
                                DevilTimestamp.fromEpochMilliseconds(
                                    150L,
                                ),
                        ),
                    session = session,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(150L),
                ),
        )
    }

    private fun task(): TaskRecord {
        val decision =
            decision()

        return TaskRecord.create(
            taskId =
                TaskId.from(
                    "task:stage220",
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
                                "trace-stage220:task",
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
                    "Preserve bounded cross-device task continuity.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary =
                "Preserve bounded cross-device task continuity.",
        )
    }
}
