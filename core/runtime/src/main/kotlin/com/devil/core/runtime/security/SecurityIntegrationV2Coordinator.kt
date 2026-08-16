package com.devil.core.runtime.security

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.security.SecurityStage
import com.devil.core.model.security.SecurityStateRecord
import com.devil.core.model.security.SessionRecord
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus

/**
 * Stage 99 — Security Integration V2.
 *
 * Represents whether the bounded security-integration prerequisites established
 * for this stage are mutually consistent.
 *
 * SATISFIED means only that:
 *
 * - constitutional identity resolution produced one IdentityId;
 * - constitutional authorization is AUTHORIZED;
 * - constitutional session validity is VALID;
 * - the valid session belongs to the same resolved identity; and
 * - the represented SecurityStage is SESSION, OWNER_MODE, or
 *   HIGH_SECURITY_CONFIRMATION.
 *
 * SATISFIED does not authenticate a subject, prove owner identity, establish
 * Owner Mode, approve High-Security Confirmation, create or renew a session,
 * mutate SecurityStage, grant Android permission, authorize a capability,
 * establish Executive readiness, approve execution, execute an action, observe
 * an effect, verify an effect, establish Outcome, or grant autonomy.
 *
 * DEFERRED means the bounded Stage 99 prerequisites are not currently satisfied.
 *
 * FAILED preserves one matching upstream failure without inventing a new one.
 */
enum class SecurityIntegrationV2Status {
    SATISFIED,
    DEFERRED,
    FAILED,
}

/**
 * Immutable evidence-preserving Stage 99 security-integration record.
 *
 * This record preserves one resolved identity, one already-valid session, and
 * one already-represented security position.
 *
 * The record does not establish where authentication, session establishment,
 * session validity, or security-stage evidence originated.
 *
 * In particular:
 *
 * SESSION_VALID != AUTHENTICATED_OWNER.
 * SECURITY_STAGE_OWNER_MODE != OWNER_IDENTITY_PROOF.
 * SECURITY_INTEGRATION_SATISFIED != OWNER_MODE_ENTRY.
 * SECURITY_INTEGRATION_SATISFIED != HIGH_SECURITY_CONFIRMATION_APPROVAL.
 * SECURITY_INTEGRATION_SATISFIED != EXECUTION_AUTHORITY.
 */
@ConsistentCopyVisibility
data class SecurityIntegrationV2Record private constructor(
    val identityId: IdentityId,
    val session: SessionRecord,
    val securityState: SecurityStateRecord,
) {
    companion object {

        fun create(
            identityId: IdentityId,
            session: SessionRecord,
            securityState: SecurityStateRecord,
        ): SecurityIntegrationV2Record {
            require(session.subjectIdentityId == identityId) {
                "Security Integration V2 requires session subject identity to match the resolved identity."
            }

            require(
                securityState.stage == SecurityStage.SESSION ||
                    securityState.stage == SecurityStage.OWNER_MODE ||
                    securityState.stage ==
                    SecurityStage.HIGH_SECURITY_CONFIRMATION,
            ) {
                "Security Integration V2 requires SESSION or a later represented security stage."
            }

            return SecurityIntegrationV2Record(
                identityId = identityId,
                session = session,
                securityState = securityState,
            )
        }
    }
}

/**
 * Structured Stage 99 Security Integration V2 result.
 *
 * A satisfied result contains exactly one bounded integration record.
 *
 * A deferred result contains neither record nor error.
 *
 * A failed result contains one matching upstream error.
 */
@ConsistentCopyVisibility
data class SecurityIntegrationV2Result private constructor(
    val traceId: TraceId,
    val status: SecurityIntegrationV2Status,
    val record: SecurityIntegrationV2Record?,
    val error: UniversalErrorRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: SecurityIntegrationV2Status,
            record: SecurityIntegrationV2Record? = null,
            error: UniversalErrorRecord? = null,
        ): SecurityIntegrationV2Result {
            when (status) {
                SecurityIntegrationV2Status.SATISFIED -> {
                    require(
                        record != null &&
                            error == null,
                    ) {
                        "Satisfied Security Integration V2 results require one record and must not contain an error."
                    }
                }

                SecurityIntegrationV2Status.DEFERRED -> {
                    require(
                        record == null &&
                            error == null,
                    ) {
                        "Deferred Security Integration V2 results must not contain a record or error."
                    }
                }

                SecurityIntegrationV2Status.FAILED -> {
                    require(
                        record == null &&
                            error != null,
                    ) {
                        "Failed Security Integration V2 results require an error and must not contain a record."
                    }
                }
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "Security Integration V2 result and error must use the same trace identity."
            }

            return SecurityIntegrationV2Result(
                traceId = traceId,
                status = status,
                record = record,
                error = error,
            )
        }
    }
}

/**
 * Stage 99 bounded Security Integration V2 coordinator.
 *
 * This coordinator consumes already-established constitutional identity,
 * authorization, session-validity, and security-position evidence.
 *
 * It does not:
 *
 * - resolve identity;
 * - authenticate a subject;
 * - prove owner identity;
 * - evaluate trust;
 * - grant constitutional authorization;
 * - create, renew, revoke, or mutate a session;
 * - advance or mutate SecurityStage;
 * - enter Owner Mode;
 * - approve High-Security Confirmation;
 * - invoke Android credentials or platform security;
 * - grant Android permission;
 * - authorize an individual capability;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - approve or perform execution;
 * - observe or verify effects;
 * - establish Outcome;
 * - grant Controlled Autonomy;
 * - or continue work autonomously.
 *
 * Failure propagation is deterministic:
 *
 * Identity -> Authorization -> Session Validity.
 *
 * Wake != authentication.
 * Session validity != owner identity.
 * SecurityStage.OWNER_MODE != owner identity proof.
 * Android permission != Devil authorization.
 *
 * STAGE_99_SECURITY_INTEGRATION != SECURITY_AUTHORITY_REPLACEMENT.
 */
class SecurityIntegrationV2Coordinator {

    fun assess(
        traceId: TraceId,
        identity: IdentityResult,
        authorization: AuthorizationResult,
        sessionValidity: SessionValidityResult,
        securityState: SecurityStateRecord,
    ): SecurityIntegrationV2Result {
        require(identity.traceId == traceId) {
            "Security Integration V2 trace and identity result must use the same trace identity."
        }

        require(authorization.traceId == traceId) {
            "Security Integration V2 trace and authorization result must use the same trace identity."
        }

        require(sessionValidity.traceId == traceId) {
            "Security Integration V2 trace and session validity result must use the same trace identity."
        }

        if (identity.status == IdentityStatus.FAILED) {
            return SecurityIntegrationV2Result.create(
                traceId = traceId,
                status = SecurityIntegrationV2Status.FAILED,
                error = requireNotNull(identity.error),
            )
        }

        if (authorization.status == AuthorizationStatus.FAILED) {
            return SecurityIntegrationV2Result.create(
                traceId = traceId,
                status = SecurityIntegrationV2Status.FAILED,
                error = requireNotNull(authorization.error),
            )
        }

        if (sessionValidity.status == SessionValidityStatus.FAILED) {
            return SecurityIntegrationV2Result.create(
                traceId = traceId,
                status = SecurityIntegrationV2Status.FAILED,
                error = requireNotNull(sessionValidity.error),
            )
        }

        if (
            identity.status != IdentityStatus.RESOLVED ||
            authorization.status != AuthorizationStatus.AUTHORIZED ||
            sessionValidity.status != SessionValidityStatus.VALID
        ) {
            return deferred(traceId)
        }

        val identityId =
            requireNotNull(identity.identityId)

        val validityRequest =
            requireNotNull(sessionValidity.request)

        val session =
            validityRequest.session

        if (session.subjectIdentityId != identityId) {
            return deferred(traceId)
        }

        return when (securityState.stage) {
            SecurityStage.LOCKED,
            SecurityStage.WAKE,
            SecurityStage.AUTHENTICATION,
            ->
                deferred(traceId)

            SecurityStage.SESSION,
            SecurityStage.OWNER_MODE,
            SecurityStage.HIGH_SECURITY_CONFIRMATION,
            ->
                SecurityIntegrationV2Result.create(
                    traceId = traceId,
                    status =
                        SecurityIntegrationV2Status.SATISFIED,
                    record =
                        SecurityIntegrationV2Record.create(
                            identityId = identityId,
                            session = session,
                            securityState = securityState,
                        ),
                )
        }
    }

    private fun deferred(
        traceId: TraceId,
    ): SecurityIntegrationV2Result {
        return SecurityIntegrationV2Result.create(
            traceId = traceId,
            status = SecurityIntegrationV2Status.DEFERRED,
        )
    }
}
