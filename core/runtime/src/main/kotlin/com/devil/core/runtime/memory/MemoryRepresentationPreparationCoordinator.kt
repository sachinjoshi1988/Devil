package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.LogicalMemoryRepresentation
import com.devil.core.model.memory.MemoryRepresentationPreparationRequest
import com.devil.core.runtime.owner.OwnerMultiUserContextResult
import com.devil.core.runtime.owner.OwnerMultiUserContextStatus

/**
 * Stage 102 bounded coordinator for constitutional logical-memory
 * representation preparation.
 *
 * This coordinator bridges:
 *
 * Memory Authority result
 * -> explicitly supplied Stage 101 metadata
 * -> established Stage 100 subject context
 * -> LogicalMemoryRepresentation
 *
 * It does so without modifying or replacing the existing Memory Authority,
 * Memory Commitment Authority, Memory Persistence Authority, or Unified Devil
 * Runtime.
 *
 * Preparation is allowed only when:
 *
 * - the supplied MemoryAuthorityResult belongs to the same trace;
 * - the supplied Stage 100 owner / multi-user context belongs to the same trace;
 * - Memory Authority status is COMMITTABLE;
 * - the preparation request preserves the exact MemoryAuthorityRequest returned
 *   by that COMMITTABLE result;
 * - Stage 100 owner / multi-user context is ESTABLISHED;
 * - and the explicitly supplied subject identity matches the current subject
 *   identity preserved by Stage 100.
 *
 * Every memory metadata field remains explicitly supplied by the caller.
 * This coordinator does not infer memory class, sensitivity, confidence,
 * retention, source, owner-visible reason, content, subject identity, or
 * MemoryId.
 *
 * This coordinator does not:
 *
 * - create another Memory Authority;
 * - change Memory Authority policy;
 * - reinterpret COMMITTABLE as committed;
 * - authenticate a subject;
 * - prove ownership;
 * - grant authorization;
 * - enter Owner Mode;
 * - establish High-Security Confirmation;
 * - commit logical memory;
 * - persist logical memory;
 * - write to storage;
 * - expose or recall logical memory;
 * - delete logical memory;
 * - enforce retention;
 * - select storage;
 * - apply encryption;
 * - replicate data;
 * - mutate World Model state;
 * - perform Learning;
 * - modify the Stage 49 runtime ordering;
 * - authorize a capability;
 * - create an ExecutionRequest;
 * - execute an action;
 * - or establish verified success.
 *
 * MEMORY_AUTHORITY_COMMITTABLE != MEMORY_COMMITMENT.
 * REPRESENTATION_PREPARED != MEMORY_COMMITMENT.
 * REPRESENTATION_PREPARED != MEMORY_PERSISTENCE.
 * SUBJECT_MATCH != AUTHENTICATION.
 * EXPLICIT_METADATA != INFERRED_METADATA.
 */
class MemoryRepresentationPreparationCoordinator {

    fun prepare(
        traceId: TraceId,
        memoryAuthority: MemoryAuthorityResult,
        ownerContext: OwnerMultiUserContextResult,
        request: MemoryRepresentationPreparationRequest,
    ): MemoryRepresentationPreparationResult {
        require(memoryAuthority.traceId == traceId) {
            "Memory representation preparation and Memory Authority result must use the same trace identity."
        }

        require(ownerContext.traceId == traceId) {
            "Memory representation preparation and owner / multi-user context must use the same trace identity."
        }

        if (
            memoryAuthority.status ==
            MemoryAuthorityStatus.FAILED
        ) {
            return MemoryRepresentationPreparationResult.create(
                traceId = traceId,
                status =
                    MemoryRepresentationPreparationStatus.FAILED,
                error =
                    requireNotNull(
                        memoryAuthority.error,
                    ),
            )
        }

        if (
            ownerContext.status ==
            OwnerMultiUserContextStatus.FAILED
        ) {
            return MemoryRepresentationPreparationResult.create(
                traceId = traceId,
                status =
                    MemoryRepresentationPreparationStatus.FAILED,
                error =
                    requireNotNull(
                        ownerContext.error,
                    ),
            )
        }

        if (
            memoryAuthority.status !=
            MemoryAuthorityStatus.COMMITTABLE
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

        val approvedAuthorityRequest =
            requireNotNull(
                memoryAuthority.request,
            )

        require(
            request.authorityRequest ==
                approvedAuthorityRequest,
        ) {
            "Memory representation preparation must preserve the exact Memory Authority request approved as COMMITTABLE."
        }

        val establishedOwnerContext =
            requireNotNull(
                ownerContext.record,
            )

        if (
            request.subjectIdentityId !=
            establishedOwnerContext.currentSubjectIdentityId
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val representation =
            LogicalMemoryRepresentation.create(
                memoryId = request.memoryId,
                subjectIdentityId =
                    request.subjectIdentityId,
                memoryClass = request.memoryClass,
                sensitivity = request.sensitivity,
                confidence = request.confidence,
                retention = request.retention,
                source = request.source,
                ownerVisibleReason =
                    request.ownerVisibleReason,
                content = request.content,
            )

        return MemoryRepresentationPreparationResult.create(
            traceId = traceId,
            status =
                MemoryRepresentationPreparationStatus.PREPARED,
            request = request,
            representation = representation,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): MemoryRepresentationPreparationResult {
        return MemoryRepresentationPreparationResult.create(
            traceId = traceId,
            status =
                MemoryRepresentationPreparationStatus.DEFERRED,
        )
    }
}
