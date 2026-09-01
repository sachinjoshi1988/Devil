package com.devil.app.authorization

import com.devil.app.authentication.Stage314OwnerSessionStore
import com.devil.core.model.authorization.AuthorizationAssessment
import com.devil.core.model.authorization.AuthorizationEvaluationRequest
import com.devil.core.model.authorization.AuthorizationEvaluationState
import com.devil.core.model.security.SessionValidityRequest
import com.devil.core.runtime.authorization.AuthorizationEvaluationResolver
import com.devil.core.runtime.security.DefaultSessionValidityAuthority
import com.devil.core.runtime.security.SessionValidityAuthority
import com.devil.core.runtime.security.SessionValidityStatus

/**
 * Stage 314 bounded owner-alpha constitutional authorization policy.
 *
 * This resolver permits bounded constitutional continuation only when:
 *
 * - one process-local owner-alpha session has already been established;
 * - that session belongs to the exact authorization-request subject; and
 * - Devil's existing Session Validity Authority evaluates that session as VALID
 *   at the authorization request's authoritative observation time.
 *
 * Identity and trust carried by AuthorizationEvaluationRequest remain upstream
 * constitutional evidence. Neither identity resolution nor trust classification
 * is treated as authentication or session validity.
 *
 * This resolver does not:
 *
 * - perform Android authentication;
 * - create, renew, extend, or persist sessions;
 * - infer authentication from identity or trust;
 * - declare SessionState.ACTIVE sufficient for validity;
 * - enter Owner Mode;
 * - authorize an individual capability;
 * - grant Android permission;
 * - create an ExecutionRequest;
 * - execute actions;
 * - or establish successful outcomes.
 *
 * SESSION_STORED != SESSION_VALID.
 * SESSION_VALID != AUTHORIZATION.
 * AUTHORIZED_CONTINUATION != CAPABILITY_AUTHORIZED.
 * AUTHORIZED_CONTINUATION != EXECUTION_APPROVED.
 */
class Stage314OwnerAuthorizationEvaluationResolver(
    private val sessionStore: Stage314OwnerSessionStore,
    private val sessionValidityAuthority: SessionValidityAuthority =
        DefaultSessionValidityAuthority(),
) : AuthorizationEvaluationResolver {

    override fun evaluate(
        request: AuthorizationEvaluationRequest,
    ): AuthorizationAssessment {
        val session =
            sessionStore.current()
                ?: return deferred(
                    request = request,
                    rationale =
                        "No Stage 314 owner-alpha authenticated session is established.",
                )

        if (session.subjectIdentityId != request.subjectIdentityId) {
            return deferred(
                request = request,
                rationale =
                    "The Stage 314 owner-alpha session does not belong to the authorization subject.",
            )
        }

        val validityRequest =
            SessionValidityRequest.create(
                context = request.context,
                session = session,
                observedAt = request.context.observedAt,
            )

        val validity =
            sessionValidityAuthority.evaluateValidity(
                validityRequest,
            )

        return if (
            validity.status ==
                SessionValidityStatus.VALID
        ) {
            AuthorizationAssessment.create(
                subjectIdentityId =
                    request.subjectIdentityId,
                state =
                    AuthorizationEvaluationState.AUTHORIZED,
                rationale =
                    "A matching Stage 314 owner-alpha authenticated session is constitutionally valid at the authoritative observation time.",
            )
        } else {
            deferred(
                request = request,
                rationale =
                    "The matching Stage 314 owner-alpha session is not constitutionally valid at the authoritative observation time.",
            )
        }
    }

    private fun deferred(
        request: AuthorizationEvaluationRequest,
        rationale: String,
    ): AuthorizationAssessment {
        return AuthorizationAssessment.create(
            subjectIdentityId =
                request.subjectIdentityId,
            state =
                AuthorizationEvaluationState.DEFERRED,
            rationale =
                rationale,
        )
    }
}
