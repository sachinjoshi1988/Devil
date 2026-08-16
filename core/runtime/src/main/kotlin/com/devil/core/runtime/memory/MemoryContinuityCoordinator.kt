package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryContinuityRecord
import com.devil.core.runtime.owner.OwnerMultiUserContextResult
import com.devil.core.runtime.owner.OwnerMultiUserContextStatus

/**
 * Stage 103 bounded coordinator for constitutional logical-memory continuity.
 *
 * This coordinator bridges:
 *
 * Stage 102 PREPARED representation
 * -> established Stage 100 subject context
 * -> MemoryContinuityRecord
 *
 * It preserves the exact LogicalMemoryRepresentation already established by
 * Stage 102. It does not duplicate, reinterpret, infer, or mutate Stage 101
 * logical-memory metadata.
 *
 * Continuity may be established only when:
 *
 * - the supplied Stage 102 result belongs to the same constitutional trace;
 * - the supplied Stage 100 owner / multi-user context belongs to that trace;
 * - Stage 102 status is PREPARED;
 * - Stage 100 owner / multi-user context status is ESTABLISHED;
 * - the Stage 102 result contains its exact prepared representation;
 * - and that representation's subject identity matches the current subject
 *   identity preserved by Stage 100.
 *
 * This coordinator does not:
 *
 * - create another Memory Authority;
 * - replace Memory Authority;
 * - change Memory Authority policy;
 * - authenticate the subject;
 * - prove ownership;
 * - grant authorization;
 * - enter Owner Mode;
 * - establish High-Security Confirmation;
 * - create a Memory Proposal;
 * - approve memory;
 * - commit logical memory;
 * - persist logical memory;
 * - write to storage;
 * - create a database;
 * - use a filesystem;
 * - invoke Android storage;
 * - invoke cloud storage;
 * - expose logical memory;
 * - make memory available for recall;
 * - recall memory;
 * - delete memory;
 * - enforce retention;
 * - mutate memory content;
 * - mutate memory metadata;
 * - recalculate confidence;
 * - change source provenance;
 * - select a storage destination;
 * - apply encryption;
 * - replicate data;
 * - restore cross-session state;
 * - modify the Stage 49 runtime ordering;
 * - modify the Unified Devil Runtime;
 * - authorize a capability;
 * - execute an action;
 * - or establish verified success.
 *
 * MEMORY_CONTINUITY != MEMORY_COMMITMENT.
 * MEMORY_CONTINUITY != MEMORY_PERSISTENCE.
 * MEMORY_CONTINUITY != STORAGE_SUCCESS.
 * MEMORY_CONTINUITY != RECALL_AVAILABILITY.
 * MEMORY_CONTINUITY != MEMORY_RECALL.
 * SUBJECT_CONTINUITY != AUTHENTICATION.
 */
class MemoryContinuityCoordinator {

    fun establish(
        traceId: TraceId,
        preparation: MemoryRepresentationPreparationResult,
        ownerContext: OwnerMultiUserContextResult,
    ): MemoryContinuityResult {
        require(preparation.traceId == traceId) {
            "Memory continuity and representation preparation result must use the same trace identity."
        }

        require(ownerContext.traceId == traceId) {
            "Memory continuity and owner / multi-user context must use the same trace identity."
        }

        if (
            preparation.status ==
            MemoryRepresentationPreparationStatus.FAILED
        ) {
            return MemoryContinuityResult.create(
                traceId = traceId,
                status = MemoryContinuityStatus.FAILED,
                error =
                    requireNotNull(
                        preparation.error,
                    ),
            )
        }

        if (
            ownerContext.status ==
            OwnerMultiUserContextStatus.FAILED
        ) {
            return MemoryContinuityResult.create(
                traceId = traceId,
                status = MemoryContinuityStatus.FAILED,
                error =
                    requireNotNull(
                        ownerContext.error,
                    ),
            )
        }

        if (
            preparation.status !=
            MemoryRepresentationPreparationStatus.PREPARED
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

        val representation =
            requireNotNull(
                preparation.representation,
            )

        val establishedOwnerContext =
            requireNotNull(
                ownerContext.record,
            )

        if (
            representation.subjectIdentityId !=
            establishedOwnerContext.currentSubjectIdentityId
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        return MemoryContinuityResult.create(
            traceId = traceId,
            status = MemoryContinuityStatus.ESTABLISHED,
            record =
                MemoryContinuityRecord.create(
                    representation = representation,
                ),
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): MemoryContinuityResult {
        return MemoryContinuityResult.create(
            traceId = traceId,
            status = MemoryContinuityStatus.DEFERRED,
        )
    }
}
