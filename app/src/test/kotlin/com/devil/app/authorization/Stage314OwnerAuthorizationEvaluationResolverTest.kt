package com.devil.app.authorization

import com.devil.app.authentication.Stage314OwnerSessionEstablishmentCoordinator
import com.devil.app.authentication.Stage314OwnerSessionStore
import com.devil.core.model.authorization.AuthorizationEvaluationRequest
import com.devil.core.model.authorization.AuthorizationEvaluationState
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.trust.SubjectTrustLevel
import com.devil.core.model.trust.TrustAssessment
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage314OwnerAuthorizationEvaluationResolverTest {

    @Test
    fun `resolver defers without established session`() {
        val resolver =
            Stage314OwnerAuthorizationEvaluationResolver(
                sessionStore =
                    Stage314OwnerSessionStore(),
            )

        assertEquals(
            AuthorizationEvaluationState.DEFERRED,
            resolver.evaluate(
                createRequest(
                    observedAtMilliseconds =
                        1_777_000_000_000L,
                ),
            ).state,
        )
    }

    @Test
    fun `matching valid authenticated session authorizes continuation`() {
        val store =
            establishSession(
                subjectIdentityId =
                    ownerIdentity(),
                establishedAtMilliseconds =
                    1_777_000_000_000L,
                validityDurationMilliseconds =
                    60_000L,
            )

        val resolver =
            Stage314OwnerAuthorizationEvaluationResolver(
                sessionStore = store,
            )

        assertEquals(
            AuthorizationEvaluationState.AUTHORIZED,
            resolver.evaluate(
                createRequest(
                    observedAtMilliseconds =
                        1_777_000_030_000L,
                ),
            ).state,
        )
    }

    @Test
    fun `expired authenticated session cannot authorize continuation`() {
        val store =
            establishSession(
                subjectIdentityId =
                    ownerIdentity(),
                establishedAtMilliseconds =
                    1_777_000_000_000L,
                validityDurationMilliseconds =
                    60_000L,
            )

        val resolver =
            Stage314OwnerAuthorizationEvaluationResolver(
                sessionStore = store,
            )

        assertEquals(
            AuthorizationEvaluationState.DEFERRED,
            resolver.evaluate(
                createRequest(
                    observedAtMilliseconds =
                        1_777_000_060_000L,
                ),
            ).state,
        )
    }

    @Test
    fun `session for another subject cannot authorize`() {
        val store =
            establishSession(
                subjectIdentityId =
                    IdentityId.from(
                        "different-subject",
                    ),
                establishedAtMilliseconds =
                    1_777_000_000_000L,
                validityDurationMilliseconds =
                    60_000L,
            )

        val resolver =
            Stage314OwnerAuthorizationEvaluationResolver(
                sessionStore = store,
            )

        assertEquals(
            AuthorizationEvaluationState.DEFERRED,
            resolver.evaluate(
                createRequest(
                    observedAtMilliseconds =
                        1_777_000_030_000L,
                ),
            ).state,
        )
    }

    @Test
    fun `trusted identity alone cannot authorize without session`() {
        val resolver =
            Stage314OwnerAuthorizationEvaluationResolver(
                sessionStore =
                    Stage314OwnerSessionStore(),
            )

        val request =
            createRequest(
                observedAtMilliseconds =
                    1_777_000_000_000L,
                trustLevel =
                    SubjectTrustLevel.TRUSTED,
            )

        assertEquals(
            SubjectTrustLevel.TRUSTED,
            request.trustAssessment.level,
        )

        assertEquals(
            AuthorizationEvaluationState.DEFERRED,
            resolver.evaluate(request).state,
        )
    }

    @Test
    fun `session cannot authorize before its establishment time`() {
        val store =
            establishSession(
                subjectIdentityId =
                    ownerIdentity(),
                establishedAtMilliseconds =
                    1_777_000_010_000L,
                validityDurationMilliseconds =
                    60_000L,
            )

        val resolver =
            Stage314OwnerAuthorizationEvaluationResolver(
                sessionStore = store,
            )

        assertEquals(
            AuthorizationEvaluationState.DEFERRED,
            resolver.evaluate(
                createRequest(
                    observedAtMilliseconds =
                        1_777_000_009_999L,
                ),
            ).state,
        )
    }

    private fun establishSession(
        subjectIdentityId: IdentityId,
        establishedAtMilliseconds: Long,
        validityDurationMilliseconds: Long,
    ): Stage314OwnerSessionStore {
        val store =
            Stage314OwnerSessionStore()

        Stage314OwnerSessionEstablishmentCoordinator(
            sessionStore = store,
            sessionIdProvider = {
                "stage314-owner-session-authorization-test"
            },
            timeProvider = {
                establishedAtMilliseconds
            },
        ).establish(
            subjectIdentityId =
                subjectIdentityId,
            validityDurationMilliseconds =
                validityDurationMilliseconds,
        )

        return store
    }

    private fun createRequest(
        observedAtMilliseconds: Long,
        trustLevel: SubjectTrustLevel =
            SubjectTrustLevel.UNESTABLISHED,
    ): AuthorizationEvaluationRequest {
        val identityId =
            ownerIdentity()

        return AuthorizationEvaluationRequest.create(
            context =
                ContextEnvelope.create(
                    traceId =
                        TraceId.from(
                            "trace-stage314-owner-authorization",
                        ),
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
                            observedAtMilliseconds,
                        ),
                ),
            subjectIdentityId =
                identityId,
            trustAssessment =
                TrustAssessment.create(
                    subjectIdentityId =
                        identityId,
                    level =
                        trustLevel,
                    rationale =
                        "Bounded upstream trust evidence.",
                ),
        )
    }

    private fun ownerIdentity(): IdentityId {
        return IdentityId.from(
            "android-primary-local-subject",
        )
    }
}
