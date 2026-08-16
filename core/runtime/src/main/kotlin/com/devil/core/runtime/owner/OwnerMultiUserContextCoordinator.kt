package com.devil.core.runtime.owner

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.owner.OwnerContext
import com.devil.core.model.owner.OwnerProfileSnapshot
import com.devil.core.model.owner.OwnerRelationship
import com.devil.core.runtime.security.SecurityIntegrationV2Result
import com.devil.core.runtime.security.SecurityIntegrationV2Status

/**
 * Stage 100 — Owner & Multi-User Context Foundation.
 *
 * Represents whether one bounded owner / current-subject context can be
 * established consistently from already-existing constitutional evidence.
 *
 * ESTABLISHED means only that:
 *
 * - Stage 99 Security Integration V2 is SATISFIED;
 * - its resolved current-subject identity matches the explicitly supplied
 *   OwnerContext subject identity;
 * - the explicitly supplied OwnerContext preserves one owner identity; and
 * - when an OwnerProfileSnapshot is supplied, that snapshot belongs to the
 *   same configured owner identity.
 *
 * ESTABLISHED does not authenticate the current subject, prove ownership,
 * establish that the current subject is the owner, enter Owner Mode, establish
 * trust, grant authorization, establish guardian authority, apply child policy,
 * create or modify Memory, grant capability authority, establish Executive
 * readiness, approve execution, execute an action, observe or verify an effect,
 * establish Outcome, or grant autonomy.
 *
 * DEFERRED means the bounded Stage 100 prerequisites are not currently
 * consistent or available.
 *
 * FAILED preserves one matching upstream Stage 99 failure.
 */
enum class OwnerMultiUserContextStatus {
    ESTABLISHED,
    DEFERRED,
    FAILED,
}

/**
 * Immutable Stage 100 owner / multi-user context record.
 *
 * The record preserves:
 *
 * - the explicitly supplied owner / subject identity pairing;
 * - the current subject identity already preserved by Stage 99; and
 * - an optional descriptive Stage 43 relationship already present in an
 *   explicitly supplied OwnerProfileSnapshot.
 *
 * A null relationship means only that no matching descriptive relationship was
 * supplied. It must never be replaced by an invented relationship.
 *
 * In particular:
 *
 * OWNER_IDENTITY_MATCH != AUTHENTICATION.
 * OWNER_IDENTITY_MATCH != OWNERSHIP_PROOF.
 * SELF_RELATIONSHIP != AUTHENTICATED_OWNER.
 * FAMILY_RELATIONSHIP != GUARDIAN_AUTHORITY.
 * OWNER_CONTEXT != OWNER_MODE.
 * OWNER_CONTEXT != AUTHORIZATION.
 */
@ConsistentCopyVisibility
data class OwnerMultiUserContextRecord private constructor(
    val ownerContext: OwnerContext,
    val currentSubjectIdentityId: IdentityId,
    val relationship: OwnerRelationship?,
) {
    companion object {

        fun create(
            ownerContext: OwnerContext,
            currentSubjectIdentityId: IdentityId,
            relationship: OwnerRelationship? = null,
        ): OwnerMultiUserContextRecord {
            require(
                ownerContext.subjectIdentityId ==
                    currentSubjectIdentityId,
            ) {
                "Owner / multi-user context requires the supplied subject identity to match the current subject identity."
            }

            require(
                relationship == null ||
                    relationship.ownerIdentityId ==
                    ownerContext.ownerIdentityId,
            ) {
                "Owner / multi-user relationship must belong to the supplied owner identity."
            }

            require(
                relationship == null ||
                    relationship.subjectIdentityId ==
                    currentSubjectIdentityId,
            ) {
                "Owner / multi-user relationship must belong to the current subject identity."
            }

            return OwnerMultiUserContextRecord(
                ownerContext = ownerContext,
                currentSubjectIdentityId =
                    currentSubjectIdentityId,
                relationship = relationship,
            )
        }
    }
}

/**
 * Structured Stage 100 Owner & Multi-User Context result.
 *
 * An ESTABLISHED result contains exactly one bounded context record.
 *
 * A DEFERRED result contains neither record nor error.
 *
 * A FAILED result contains one matching upstream error.
 */
@ConsistentCopyVisibility
data class OwnerMultiUserContextResult private constructor(
    val traceId: TraceId,
    val status: OwnerMultiUserContextStatus,
    val record: OwnerMultiUserContextRecord?,
    val error: UniversalErrorRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: OwnerMultiUserContextStatus,
            record: OwnerMultiUserContextRecord? = null,
            error: UniversalErrorRecord? = null,
        ): OwnerMultiUserContextResult {
            when (status) {
                OwnerMultiUserContextStatus.ESTABLISHED -> {
                    require(
                        record != null &&
                            error == null,
                    ) {
                        "Established Owner / Multi-User Context results require one record and must not contain an error."
                    }
                }

                OwnerMultiUserContextStatus.DEFERRED -> {
                    require(
                        record == null &&
                            error == null,
                    ) {
                        "Deferred Owner / Multi-User Context results must not contain a record or error."
                    }
                }

                OwnerMultiUserContextStatus.FAILED -> {
                    require(
                        record == null &&
                            error != null,
                    ) {
                        "Failed Owner / Multi-User Context results require an error and must not contain a record."
                    }
                }
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "Owner / Multi-User Context result and error must use the same trace identity."
            }

            return OwnerMultiUserContextResult(
                traceId = traceId,
                status = status,
                record = record,
                error = error,
            )
        }
    }
}

/**
 * Stage 100 bounded Owner & Multi-User Context coordinator.
 *
 * This coordinator consumes already-established Stage 99 security integration
 * evidence plus explicitly supplied Stage 3 / Stage 43 owner-domain context.
 *
 * It does not:
 *
 * - resolve identity;
 * - authenticate a subject;
 * - prove owner identity or ownership;
 * - infer an owner from profile fields;
 * - infer a relationship;
 * - establish relationship authenticity;
 * - evaluate trust;
 * - grant constitutional authorization;
 * - create, renew, validate, revoke, or mutate a security session;
 * - advance SecurityStage;
 * - enter Owner Mode;
 * - approve High-Security Confirmation;
 * - create or modify OwnerProfileSnapshot;
 * - create another identity system;
 * - create another user-specific Devil intelligence;
 * - establish guardian authority;
 * - infer child classification;
 * - apply child or guardian policy;
 * - commit or persist logical Memory;
 * - authorize a capability;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - approve or perform execution;
 * - observe or verify effects;
 * - establish Outcome;
 * - grant Controlled Autonomy;
 * - or continue work autonomously.
 *
 * One Devil intelligence may represent different subjects.
 * Different subjects do not create different Devil intelligences.
 *
 * OwnerContext
 * != authentication
 * != ownership proof
 * != Owner Mode
 * != authorization.
 *
 * OwnerRelationship
 * != authentication
 * != guardian authority
 * != authorization.
 *
 * STAGE_100_OWNER_MULTI_USER_CONTEXT
 * != IDENTITY_AUTHORITY_REPLACEMENT.
 *
 * STAGE_100_OWNER_MULTI_USER_CONTEXT
 * != SECURITY_AUTHORITY_REPLACEMENT.
 */
class OwnerMultiUserContextCoordinator {

    fun assess(
        traceId: TraceId,
        securityIntegration: SecurityIntegrationV2Result,
        ownerContext: OwnerContext,
        ownerProfileSnapshot: OwnerProfileSnapshot? = null,
    ): OwnerMultiUserContextResult {
        require(
            securityIntegration.traceId == traceId,
        ) {
            "Owner / Multi-User Context trace and Security Integration V2 result must use the same trace identity."
        }

        if (
            securityIntegration.status ==
            SecurityIntegrationV2Status.FAILED
        ) {
            return OwnerMultiUserContextResult.create(
                traceId = traceId,
                status = OwnerMultiUserContextStatus.FAILED,
                error =
                    requireNotNull(
                        securityIntegration.error,
                    ),
            )
        }

        if (
            securityIntegration.status !=
            SecurityIntegrationV2Status.SATISFIED
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val securityRecord =
            requireNotNull(
                securityIntegration.record,
            )

        val currentSubjectIdentityId =
            securityRecord.identityId

        if (
            ownerContext.subjectIdentityId !=
            currentSubjectIdentityId
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        if (
            ownerProfileSnapshot != null &&
            ownerProfileSnapshot.profile.ownerIdentityId !=
            ownerContext.ownerIdentityId
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val relationship =
            ownerProfileSnapshot
                ?.relationships
                ?.firstOrNull {
                    it.subjectIdentityId ==
                        currentSubjectIdentityId
                }

        return OwnerMultiUserContextResult.create(
            traceId = traceId,
            status =
                OwnerMultiUserContextStatus.ESTABLISHED,
            record =
                OwnerMultiUserContextRecord.create(
                    ownerContext = ownerContext,
                    currentSubjectIdentityId =
                        currentSubjectIdentityId,
                    relationship = relationship,
                ),
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): OwnerMultiUserContextResult {
        return OwnerMultiUserContextResult.create(
            traceId = traceId,
            status =
                OwnerMultiUserContextStatus.DEFERRED,
        )
    }
}
