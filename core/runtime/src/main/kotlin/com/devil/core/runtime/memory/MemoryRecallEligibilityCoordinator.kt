package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryRecallEligibilityRecord
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
import com.devil.core.runtime.owner.OwnerMultiUserContextResult
import com.devil.core.runtime.owner.OwnerMultiUserContextStatus

/**
 * Stage 104 bounded coordinator for constitutional logical-memory recall
 * eligibility.
 *
 * Flow:
 *
 * Stage 103 ESTABLISHED memory continuity
 * -> explicit constitutional AUTHORIZED result
 * -> ESTABLISHED Stage 100 current-subject context
 * -> MemoryRecallEligibilityRecord
 *
 * This coordinator preserves the exact Stage 103 MemoryContinuityRecord and the
 * exact LogicalMemoryRepresentation already contained by that record.
 *
 * Eligibility may be established only when:
 *
 * - every supplied upstream result belongs to the same constitutional trace;
 * - Stage 103 continuity is ESTABLISHED;
 * - constitutional authorization is AUTHORIZED;
 * - Stage 100 owner / multi-user context is ESTABLISHED;
 * - the Stage 103 continuity record exists;
 * - the Stage 100 current-subject record exists;
 * - and the logical-memory subject matches the established current subject.
 *
 * Authorization is an explicit prerequisite here because continuity alone does
 * not grant access authority.
 *
 * Authorization does not establish privacy-disclosure permission.
 *
 * This coordinator does not:
 *
 * - create another Memory Authority;
 * - replace Memory Authority;
 * - authenticate a subject;
 * - prove ownership;
 * - establish trust;
 * - grant authorization;
 * - establish Owner Mode;
 * - establish High-Security Confirmation;
 * - create, commit, or persist logical memory;
 * - read from storage;
 * - retrieve logical memory;
 * - recall logical memory;
 * - expose logical memory;
 * - disclose logical-memory content;
 * - derive privacy-disclosure treatment;
 * - convert MemorySensitivity into disclosure permission;
 * - delete logical memory;
 * - enforce retention;
 * - reconstruct logical-memory metadata;
 * - mutate logical-memory metadata;
 * - mutate logical-memory content;
 * - recalculate confidence;
 * - reinterpret provenance;
 * - invoke a database;
 * - invoke a filesystem;
 * - invoke Android storage;
 * - invoke cloud storage;
 * - invoke a network service;
 * - modify the Stage 49 runtime ordering;
 * - modify the Unified Devil Runtime;
 * - authorize a capability;
 * - execute an action;
 * - or establish verified success.
 *
 * RECALL_ELIGIBILITY != MEMORY_RECALL.
 * RECALL_ELIGIBILITY != STORAGE_READ.
 * RECALL_ELIGIBILITY != DISCLOSURE_PERMISSION.
 * AUTHORIZATION != PRIVACY_DISCLOSURE_PERMISSION.
 * MEMORY_SENSITIVITY != PRIVACY_DISCLOSURE_POLICY.
 */
class MemoryRecallEligibilityCoordinator {

    fun evaluate(
        traceId: TraceId,
        continuity: MemoryContinuityResult,
        authorization: AuthorizationResult,
        ownerContext: OwnerMultiUserContextResult,
    ): MemoryRecallEligibilityResult {
        require(continuity.traceId == traceId) {
            "Memory recall eligibility and memory continuity must use the same trace identity."
        }

        require(authorization.traceId == traceId) {
            "Memory recall eligibility and authorization must use the same trace identity."
        }

        require(ownerContext.traceId == traceId) {
            "Memory recall eligibility and owner / multi-user context must use the same trace identity."
        }

        if (
            continuity.status ==
            MemoryContinuityStatus.FAILED
        ) {
            return MemoryRecallEligibilityResult.create(
                traceId = traceId,
                status = MemoryRecallEligibilityStatus.FAILED,
                error =
                    requireNotNull(
                        continuity.error,
                    ),
            )
        }

        if (
            authorization.status ==
            AuthorizationStatus.FAILED
        ) {
            return MemoryRecallEligibilityResult.create(
                traceId = traceId,
                status = MemoryRecallEligibilityStatus.FAILED,
                error =
                    requireNotNull(
                        authorization.error,
                    ),
            )
        }

        if (
            ownerContext.status ==
            OwnerMultiUserContextStatus.FAILED
        ) {
            return MemoryRecallEligibilityResult.create(
                traceId = traceId,
                status = MemoryRecallEligibilityStatus.FAILED,
                error =
                    requireNotNull(
                        ownerContext.error,
                    ),
            )
        }

        if (
            continuity.status !=
            MemoryContinuityStatus.ESTABLISHED
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        if (
            authorization.status !=
            AuthorizationStatus.AUTHORIZED
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        if (
            ownerContext.status !=
            OwnerMultiUserContextStatus.ESTABLISHED
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val continuityRecord =
            requireNotNull(
                continuity.record,
            )

        val establishedOwnerContext =
            requireNotNull(
                ownerContext.record,
            )

        if (
            continuityRecord
                .representation
                .subjectIdentityId !=
            establishedOwnerContext.currentSubjectIdentityId
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        return MemoryRecallEligibilityResult.create(
            traceId = traceId,
            status = MemoryRecallEligibilityStatus.ELIGIBLE,
            record =
                MemoryRecallEligibilityRecord.create(
                    continuity = continuityRecord,
                ),
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): MemoryRecallEligibilityResult {
        return MemoryRecallEligibilityResult.create(
            traceId = traceId,
            status = MemoryRecallEligibilityStatus.DEFERRED,
        )
    }
}
