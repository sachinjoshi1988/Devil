package com.devil.core.runtime.security

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.security.SecurityStage
import com.devil.core.model.security.SecurityStateRecord
import com.devil.core.model.security.SessionId
import com.devil.core.model.security.SessionRecord
import com.devil.core.model.security.SessionState
import com.devil.core.model.security.SessionValidityRequest
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage99SecurityIntegrationV2FoundationTest {

    @Test
    fun `resolved authorized valid matching session at session stage satisfies bounded integration`() {
        val traceId =
            TraceId.from(
                "trace-stage99-security-integration-001",
            )

        val identityId =
            IdentityId.from(
                "subject-stage99-security-integration",
            )

        val validity =
            validSession(
                traceId = traceId,
                identityId = identityId,
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                identity =
                    resolvedIdentity(
                        traceId = traceId,
                        identityId = identityId,
                    ),
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                sessionValidity = validity,
                securityState =
                    securityState(
                        SecurityStage.SESSION,
                    ),
            )

        assertEquals(
            SecurityIntegrationV2Status.SATISFIED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertEquals(
            identityId,
            record.identityId,
        )

        assertSame(
            requireNotNull(validity.request).session,
            record.session,
        )

        assertEquals(
            SecurityStage.SESSION,
            record.securityState.stage,
        )

        assertNull(result.error)
    }

    @Test
    fun `owner mode representation may satisfy integration without proving owner identity`() {
        val traceId =
            TraceId.from(
                "trace-stage99-security-integration-002",
            )

        val identityId =
            IdentityId.from(
                "subject-stage99-owner-mode",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                identity =
                    resolvedIdentity(
                        traceId = traceId,
                        identityId = identityId,
                    ),
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                sessionValidity =
                    validSession(
                        traceId = traceId,
                        identityId = identityId,
                    ),
                securityState =
                    securityState(
                        SecurityStage.OWNER_MODE,
                    ),
            )

        assertEquals(
            SecurityIntegrationV2Status.SATISFIED,
            result.status,
        )

        assertEquals(
            SecurityStage.OWNER_MODE,
            requireNotNull(result.record).securityState.stage,
        )

        assertNull(result.error)
    }

    @Test
    fun `high security representation may satisfy integration without approving protected action`() {
        val traceId =
            TraceId.from(
                "trace-stage99-security-integration-003",
            )

        val identityId =
            IdentityId.from(
                "subject-stage99-high-security",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                identity =
                    resolvedIdentity(
                        traceId = traceId,
                        identityId = identityId,
                    ),
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                sessionValidity =
                    validSession(
                        traceId = traceId,
                        identityId = identityId,
                    ),
                securityState =
                    securityState(
                        SecurityStage.HIGH_SECURITY_CONFIRMATION,
                    ),
            )

        assertEquals(
            SecurityIntegrationV2Status.SATISFIED,
            result.status,
        )

        assertEquals(
            SecurityStage.HIGH_SECURITY_CONFIRMATION,
            requireNotNull(result.record).securityState.stage,
        )

        assertNull(result.error)
    }

    @Test
    fun `matching valid session still defers before session security stage`() {
        val traceId =
            TraceId.from(
                "trace-stage99-security-integration-004",
            )

        val identityId =
            IdentityId.from(
                "subject-stage99-pre-session",
            )

        val earlyStages =
            listOf(
                SecurityStage.LOCKED,
                SecurityStage.WAKE,
                SecurityStage.AUTHENTICATION,
            )

        earlyStages.forEach { stage ->
            val result =
                coordinator().assess(
                    traceId = traceId,
                    identity =
                        resolvedIdentity(
                            traceId = traceId,
                            identityId = identityId,
                        ),
                    authorization =
                        authorized(
                            traceId = traceId,
                        ),
                    sessionValidity =
                        validSession(
                            traceId = traceId,
                            identityId = identityId,
                        ),
                    securityState =
                        securityState(
                            stage,
                        ),
                )

            assertEquals(
                SecurityIntegrationV2Status.DEFERRED,
                result.status,
            )

            assertNull(result.record)
            assertNull(result.error)
        }
    }

    @Test
    fun `valid session belonging to another identity remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage99-security-integration-005",
            )

        val resolvedIdentityId =
            IdentityId.from(
                "subject-stage99-resolved",
            )

        val sessionIdentityId =
            IdentityId.from(
                "subject-stage99-session-other",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                identity =
                    resolvedIdentity(
                        traceId = traceId,
                        identityId = resolvedIdentityId,
                    ),
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                sessionValidity =
                    validSession(
                        traceId = traceId,
                        identityId = sessionIdentityId,
                    ),
                securityState =
                    securityState(
                        SecurityStage.SESSION,
                    ),
            )

        assertEquals(
            SecurityIntegrationV2Status.DEFERRED,
            result.status,
        )

        assertNull(result.record)
        assertNull(result.error)
    }

    @Test
    fun `unresolved identity remains deferred without fabricated security evidence`() {
        val traceId =
            TraceId.from(
                "trace-stage99-security-integration-006",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                identity =
                    IdentityResult.create(
                        traceId = traceId,
                        status = IdentityStatus.UNRESOLVED,
                    ),
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                sessionValidity =
                    validSession(
                        traceId = traceId,
                        identityId =
                            IdentityId.from(
                                "subject-stage99-unresolved-session",
                            ),
                    ),
                securityState =
                    securityState(
                        SecurityStage.SESSION,
                    ),
            )

        assertEquals(
            SecurityIntegrationV2Status.DEFERRED,
            result.status,
        )

        assertNull(result.record)
        assertNull(result.error)
    }

    @Test
    fun `denied or deferred authorization remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage99-security-integration-007",
            )

        val identityId =
            IdentityId.from(
                "subject-stage99-authorization",
            )

        listOf(
            AuthorizationStatus.DENIED,
            AuthorizationStatus.DEFERRED,
        ).forEach { authorizationStatus ->
            val result =
                coordinator().assess(
                    traceId = traceId,
                    identity =
                        resolvedIdentity(
                            traceId = traceId,
                            identityId = identityId,
                        ),
                    authorization =
                        AuthorizationResult.create(
                            traceId = traceId,
                            status = authorizationStatus,
                        ),
                    sessionValidity =
                        validSession(
                            traceId = traceId,
                            identityId = identityId,
                        ),
                    securityState =
                        securityState(
                            SecurityStage.SESSION,
                        ),
                )

            assertEquals(
                SecurityIntegrationV2Status.DEFERRED,
                result.status,
            )

            assertNull(result.record)
            assertNull(result.error)
        }
    }

    @Test
    fun `invalid or deferred session validity remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage99-security-integration-008",
            )

        val identityId =
            IdentityId.from(
                "subject-stage99-validity",
            )

        val request =
            sessionRequest(
                traceId = traceId,
                identityId = identityId,
            )

        val invalid =
            SessionValidityResult.create(
                traceId = traceId,
                status = SessionValidityStatus.INVALID,
                request = request,
            )

        val deferred =
            SessionValidityResult.create(
                traceId = traceId,
                status = SessionValidityStatus.DEFERRED,
            )

        listOf(
            invalid,
            deferred,
        ).forEach { sessionValidity ->
            val result =
                coordinator().assess(
                    traceId = traceId,
                    identity =
                        resolvedIdentity(
                            traceId = traceId,
                            identityId = identityId,
                        ),
                    authorization =
                        authorized(
                            traceId = traceId,
                        ),
                    sessionValidity = sessionValidity,
                    securityState =
                        securityState(
                            SecurityStage.SESSION,
                        ),
                )

            assertEquals(
                SecurityIntegrationV2Status.DEFERRED,
                result.status,
            )

            assertNull(result.record)
            assertNull(result.error)
        }
    }

    @Test
    fun `identity failure has deterministic precedence over later failures`() {
        val traceId =
            TraceId.from(
                "trace-stage99-security-integration-009",
            )

        val identityError =
            error(
                traceId = traceId,
                code = "STAGE_99_IDENTITY_FAILURE",
                summary = "Synthetic Stage 99 identity failure.",
            )

        val authorizationError =
            error(
                traceId = traceId,
                code = "STAGE_99_AUTHORIZATION_FAILURE",
                summary = "Synthetic Stage 99 authorization failure.",
            )

        val sessionError =
            error(
                traceId = traceId,
                code = "STAGE_99_SESSION_FAILURE",
                summary = "Synthetic Stage 99 session failure.",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                identity =
                    IdentityResult.create(
                        traceId = traceId,
                        status = IdentityStatus.FAILED,
                        error = identityError,
                    ),
                authorization =
                    AuthorizationResult.create(
                        traceId = traceId,
                        status = AuthorizationStatus.FAILED,
                        error = authorizationError,
                    ),
                sessionValidity =
                    SessionValidityResult.create(
                        traceId = traceId,
                        status = SessionValidityStatus.FAILED,
                        error = sessionError,
                    ),
                securityState =
                    securityState(
                        SecurityStage.LOCKED,
                    ),
            )

        assertEquals(
            SecurityIntegrationV2Status.FAILED,
            result.status,
        )

        assertSame(
            identityError,
            result.error,
        )

        assertNull(result.record)
    }

    @Test
    fun `authorization failure precedes session validity failure`() {
        val traceId =
            TraceId.from(
                "trace-stage99-security-integration-010",
            )

        val identityId =
            IdentityId.from(
                "subject-stage99-failure-precedence",
            )

        val authorizationError =
            error(
                traceId = traceId,
                code = "STAGE_99_AUTHORIZATION_FAILURE_ONLY",
                summary = "Synthetic Stage 99 authorization failure.",
            )

        val sessionError =
            error(
                traceId = traceId,
                code = "STAGE_99_SESSION_FAILURE_LATER",
                summary = "Synthetic Stage 99 later session failure.",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                identity =
                    resolvedIdentity(
                        traceId = traceId,
                        identityId = identityId,
                    ),
                authorization =
                    AuthorizationResult.create(
                        traceId = traceId,
                        status = AuthorizationStatus.FAILED,
                        error = authorizationError,
                    ),
                sessionValidity =
                    SessionValidityResult.create(
                        traceId = traceId,
                        status = SessionValidityStatus.FAILED,
                        error = sessionError,
                    ),
                securityState =
                    securityState(
                        SecurityStage.SESSION,
                    ),
            )

        assertEquals(
            SecurityIntegrationV2Status.FAILED,
            result.status,
        )

        assertSame(
            authorizationError,
            result.error,
        )

        assertNull(result.record)
    }

    @Test
    fun `session validity failure preserves exact upstream failure`() {
        val traceId =
            TraceId.from(
                "trace-stage99-security-integration-011",
            )

        val identityId =
            IdentityId.from(
                "subject-stage99-session-failure",
            )

        val sessionError =
            error(
                traceId = traceId,
                code = "STAGE_99_SESSION_VALIDITY_FAILURE",
                summary = "Synthetic Stage 99 session-validity failure.",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                identity =
                    resolvedIdentity(
                        traceId = traceId,
                        identityId = identityId,
                    ),
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                sessionValidity =
                    SessionValidityResult.create(
                        traceId = traceId,
                        status = SessionValidityStatus.FAILED,
                        error = sessionError,
                    ),
                securityState =
                    securityState(
                        SecurityStage.SESSION,
                    ),
            )

        assertEquals(
            SecurityIntegrationV2Status.FAILED,
            result.status,
        )

        assertSame(
            sessionError,
            result.error,
        )

        assertNull(result.record)
    }

    @Test
    fun `cross trace identity authorization and session results are rejected`() {
        val traceId =
            TraceId.from(
                "trace-stage99-security-integration-012",
            )

        val otherTraceId =
            TraceId.from(
                "trace-stage99-security-integration-other",
            )

        val identityId =
            IdentityId.from(
                "subject-stage99-cross-trace",
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator().assess(
                traceId = traceId,
                identity =
                    resolvedIdentity(
                        traceId = otherTraceId,
                        identityId = identityId,
                    ),
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                sessionValidity =
                    validSession(
                        traceId = traceId,
                        identityId = identityId,
                    ),
                securityState =
                    securityState(
                        SecurityStage.SESSION,
                    ),
            )
        }

        assertFailsWith<IllegalArgumentException> {
            coordinator().assess(
                traceId = traceId,
                identity =
                    resolvedIdentity(
                        traceId = traceId,
                        identityId = identityId,
                    ),
                authorization =
                    authorized(
                        traceId = otherTraceId,
                    ),
                sessionValidity =
                    validSession(
                        traceId = traceId,
                        identityId = identityId,
                    ),
                securityState =
                    securityState(
                        SecurityStage.SESSION,
                    ),
            )
        }

        assertFailsWith<IllegalArgumentException> {
            coordinator().assess(
                traceId = traceId,
                identity =
                    resolvedIdentity(
                        traceId = traceId,
                        identityId = identityId,
                    ),
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                sessionValidity =
                    validSession(
                        traceId = otherTraceId,
                        identityId = identityId,
                    ),
                securityState =
                    securityState(
                        SecurityStage.SESSION,
                    ),
            )
        }
    }

    @Test
    fun `integration record rejects session belonging to another identity`() {
        assertFailsWith<IllegalArgumentException> {
            SecurityIntegrationV2Record.create(
                identityId =
                    IdentityId.from(
                        "subject-stage99-record-owner",
                    ),
                session =
                    session(
                        identityId =
                            IdentityId.from(
                                "subject-stage99-record-other",
                            ),
                    ),
                securityState =
                    securityState(
                        SecurityStage.SESSION,
                    ),
            )
        }
    }

    @Test
    fun `integration record rejects security position before session stage`() {
        val identityId =
            IdentityId.from(
                "subject-stage99-record-stage",
            )

        listOf(
            SecurityStage.LOCKED,
            SecurityStage.WAKE,
            SecurityStage.AUTHENTICATION,
        ).forEach { stage ->
            assertFailsWith<IllegalArgumentException> {
                SecurityIntegrationV2Record.create(
                    identityId = identityId,
                    session =
                        session(
                            identityId = identityId,
                        ),
                    securityState =
                        securityState(
                            stage,
                        ),
                )
            }
        }
    }

    private fun coordinator():
        SecurityIntegrationV2Coordinator {
        return SecurityIntegrationV2Coordinator()
    }

    private fun resolvedIdentity(
        traceId: TraceId,
        identityId: IdentityId,
    ): IdentityResult {
        return IdentityResult.create(
            traceId = traceId,
            status = IdentityStatus.RESOLVED,
            identityId = identityId,
        )
    }

    private fun authorized(
        traceId: TraceId,
    ): AuthorizationResult {
        return AuthorizationResult.create(
            traceId = traceId,
            status = AuthorizationStatus.AUTHORIZED,
        )
    }

    private fun validSession(
        traceId: TraceId,
        identityId: IdentityId,
    ): SessionValidityResult {
        val request =
            sessionRequest(
                traceId = traceId,
                identityId = identityId,
            )

        return SessionValidityResult.create(
            traceId = traceId,
            status = SessionValidityStatus.VALID,
            request = request,
        )
    }

    private fun sessionRequest(
        traceId: TraceId,
        identityId: IdentityId,
    ): SessionValidityRequest {
        return SessionValidityRequest.create(
            context =
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
                            OBSERVED_AT,
                        ),
                ),
            session =
                session(
                    identityId = identityId,
                ),
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    OBSERVED_AT,
                ),
        )
    }

    private fun session(
        identityId: IdentityId,
    ): SessionRecord {
        return SessionRecord.create(
            sessionId =
                SessionId.from(
                    "session-stage99-security-integration",
                ),
            subjectIdentityId = identityId,
            state =
                SessionState.ACTIVE,
            establishedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    ESTABLISHED_AT,
                ),
            expiresAt =
                DevilTimestamp.fromEpochMilliseconds(
                    EXPIRES_AT,
                ),
        )
    }

    private fun securityState(
        stage: SecurityStage,
    ): SecurityStateRecord {
        return SecurityStateRecord.create(
            stage = stage,
            rationale =
                "Bounded Stage 99 security-integration test state.",
        )
    }

    private fun error(
        traceId: TraceId,
        code: String,
        summary: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    code,
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    ERROR_AT,
                ),
            summary = summary,
        )
    }

    companion object {
        private const val ESTABLISHED_AT =
            1_754_000_196_000L

        private const val OBSERVED_AT =
            1_754_000_300_000L

        private const val EXPIRES_AT =
            1_754_003_796_000L

        private const val ERROR_AT =
            1_754_000_300_500L
    }
}
