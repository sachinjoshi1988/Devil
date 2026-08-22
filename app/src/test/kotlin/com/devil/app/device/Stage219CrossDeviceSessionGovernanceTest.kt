package com.devil.app.device

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.embodiment.CrossDeviceRelationshipRecord
import com.devil.core.model.embodiment.EmbodimentId
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.security.SessionId
import com.devil.core.model.security.SessionRecord
import com.devil.core.model.security.SessionState
import com.devil.core.model.security.SessionValidityRequest
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationResult
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationStatus
import com.devil.core.runtime.security.SessionValidityResult
import com.devil.core.runtime.security.SessionValidityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage219CrossDeviceSessionGovernanceTest {

    @Test
    fun `valid matching session becomes available and preserves exact provenance`() {
        val identity =
            IdentityId.from("identity:stage219:subject")

        val crossDeviceIdentity =
            availableCrossDeviceIdentity(identity)

        val session =
            session(identity)

        val validity =
            validSessionResult(session)

        val result =
            AndroidCrossDeviceSessionGovernanceCoordinator()
                .govern(
                    crossDeviceIdentity = crossDeviceIdentity,
                    session = session,
                    sessionValidity = validity,
                )

        assertEquals(
            AndroidCrossDeviceSessionGovernanceStatus.AVAILABLE,
            result.status,
        )
        assertSame(crossDeviceIdentity, result.crossDeviceIdentity)
        assertSame(session, result.session)
        assertSame(validity, result.sessionValidity)
    }

    @Test
    fun `invalid session validity remains deferred`() {
        val identity =
            IdentityId.from("identity:stage219:invalid")

        val crossDeviceIdentity =
            availableCrossDeviceIdentity(identity)

        val session =
            session(identity)

        val validity =
            SessionValidityResult.create(
                traceId = TraceId.from("trace-stage219-invalid"),
                status = SessionValidityStatus.INVALID,
                request =
                    SessionValidityRequest.create(
                    context = context(TraceId.from("trace-stage219-invalid")),
                        session = session,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(150L),
                    ),
            )

        val result =
            AndroidCrossDeviceSessionGovernanceCoordinator()
                .govern(
                    crossDeviceIdentity = crossDeviceIdentity,
                    session = session,
                    sessionValidity = validity,
                )

        assertEquals(
            AndroidCrossDeviceSessionGovernanceStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `deferred session validity remains deferred`() {
        val identity =
            IdentityId.from("identity:stage219:deferred")

        val result =
            AndroidCrossDeviceSessionGovernanceCoordinator()
                .govern(
                    crossDeviceIdentity =
                        availableCrossDeviceIdentity(identity),
                    session = session(identity),
                    sessionValidity =
                        SessionValidityResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-stage219-validity-deferred",
                                ),
                            status = SessionValidityStatus.DEFERRED,
                        ),
                )

        assertEquals(
            AndroidCrossDeviceSessionGovernanceStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `identity mismatch remains deferred`() {
        val crossDeviceIdentity =
            availableCrossDeviceIdentity(
                IdentityId.from("identity:stage219:one"),
            )

        val session =
            session(
                IdentityId.from("identity:stage219:two"),
            )

        val result =
            AndroidCrossDeviceSessionGovernanceCoordinator()
                .govern(
                    crossDeviceIdentity = crossDeviceIdentity,
                    session = session,
                    sessionValidity = validSessionResult(session),
                )

        assertEquals(
            AndroidCrossDeviceSessionGovernanceStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `available result rejects identity mismatch`() {
        val crossDeviceIdentity =
            availableCrossDeviceIdentity(
                IdentityId.from("identity:stage219:available-one"),
            )

        val session =
            session(
                IdentityId.from("identity:stage219:available-two"),
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidCrossDeviceSessionGovernanceResult.create(
                status =
                    AndroidCrossDeviceSessionGovernanceStatus.AVAILABLE,
                crossDeviceIdentity = crossDeviceIdentity,
                session = session,
                sessionValidity = validSessionResult(session),
            )
        }
    }

    @Test
    fun `available result rejects non valid session validity`() {
        val identity =
            IdentityId.from("identity:stage219:nonvalid")

        val session =
            session(identity)

        assertFailsWith<IllegalArgumentException> {
            AndroidCrossDeviceSessionGovernanceResult.create(
                status =
                    AndroidCrossDeviceSessionGovernanceStatus.AVAILABLE,
                crossDeviceIdentity =
                    availableCrossDeviceIdentity(identity),
                session = session,
                sessionValidity =
                    SessionValidityResult.create(
                        traceId =
                            TraceId.from(
                                "trace-stage219-nonvalid",
                            ),
                        status =
                            SessionValidityStatus.DEFERRED,
                    ),
            )
        }
    }

    @Test
    fun `available result requires exact session provenance from validity request`() {
        val identity =
            IdentityId.from("identity:stage219:exact")

        val validitySession =
            session(identity)

        val suppliedSession =
            SessionRecord.create(
                sessionId =
                    SessionId.from(
                        "session:stage219:other",
                    ),
                subjectIdentityId = identity,
                state = SessionState.ACTIVE,
                establishedAt =
                    DevilTimestamp.fromEpochMilliseconds(100L),
                expiresAt =
                    DevilTimestamp.fromEpochMilliseconds(200L),
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidCrossDeviceSessionGovernanceResult.create(
                status =
                    AndroidCrossDeviceSessionGovernanceStatus.AVAILABLE,
                crossDeviceIdentity =
                    availableCrossDeviceIdentity(identity),
                session = suppliedSession,
                sessionValidity =
                    validSessionResult(validitySession),
            )
        }
    }

    private fun availableCrossDeviceIdentity(
        identityId: IdentityId,
    ): AndroidCrossDeviceIdentityResult {
        val relationship =
            CrossDeviceRelationshipRepresentationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage219-relationship",
                    ),
                status =
                    CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
                relationship =
                    CrossDeviceRelationshipRecord.create(
                        sourceEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage219:source",
                            ),
                        targetEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage219:target",
                            ),
                        description =
                            "Bounded Stage 219 cross-device relationship.",
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
                    "session:stage219:${identityId.value}",
                ),
            subjectIdentityId = identityId,
            state = SessionState.ACTIVE,
            establishedAt =
                DevilTimestamp.fromEpochMilliseconds(100L),
            expiresAt =
                DevilTimestamp.fromEpochMilliseconds(200L),
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
                DevilTimestamp.fromEpochMilliseconds(150L),
        )
    }

    private fun validSessionResult(
        session: SessionRecord,
    ): SessionValidityResult {
        val traceId =
            TraceId.from(
                "trace-stage219-valid:${session.sessionId.value}",
            )

        return SessionValidityResult.create(
            traceId = traceId,
            status = SessionValidityStatus.VALID,
            request =
                SessionValidityRequest.create(
                    context = context(traceId),
                    session = session,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(150L),
                ),
        )
    }
}
