package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.LogicalMemoryRepresentation
import com.devil.core.model.memory.MemoryRepresentationPreparationRequest

/**
 * Immutable Stage 102 result of bounded logical-memory representation
 * preparation.
 *
 * A PREPARED result contains exactly:
 *
 * - the preparation request that preserved the exact Memory Authority request;
 * - and the LogicalMemoryRepresentation formed from that request.
 *
 * A DEFERRED result contains neither request, representation, nor error.
 *
 * A FAILED result contains one matching upstream error and no prepared
 * representation.
 *
 * This result does not commit, persist, store, expose, recall, delete,
 * synchronize, replicate, encrypt, or otherwise execute logical-memory state.
 */
@ConsistentCopyVisibility
data class MemoryRepresentationPreparationResult private constructor(
    val traceId: TraceId,
    val status: MemoryRepresentationPreparationStatus,
    val request: MemoryRepresentationPreparationRequest?,
    val representation: LogicalMemoryRepresentation?,
    val error: UniversalErrorRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: MemoryRepresentationPreparationStatus,
            request: MemoryRepresentationPreparationRequest? = null,
            representation: LogicalMemoryRepresentation? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryRepresentationPreparationResult {
            when (status) {
                MemoryRepresentationPreparationStatus.PREPARED -> {
                    require(request != null) {
                        "Prepared memory representation result requires one preparation request."
                    }

                    require(representation != null) {
                        "Prepared memory representation result requires one logical-memory representation."
                    }

                    require(error == null) {
                        "Prepared memory representation result must not contain an error."
                    }

                    require(
                        representation.memoryId ==
                            request.memoryId,
                    ) {
                        "Prepared memory representation identity must match the preparation request."
                    }

                    require(
                        representation.subjectIdentityId ==
                            request.subjectIdentityId,
                    ) {
                        "Prepared memory representation subject must match the preparation request."
                    }

                    require(
                        representation.memoryClass ==
                            request.memoryClass,
                    ) {
                        "Prepared memory representation class must match the preparation request."
                    }

                    require(
                        representation.sensitivity ==
                            request.sensitivity,
                    ) {
                        "Prepared memory representation sensitivity must match the preparation request."
                    }

                    require(
                        representation.confidence ==
                            request.confidence,
                    ) {
                        "Prepared memory representation confidence must match the preparation request."
                    }

                    require(
                        representation.retention ==
                            request.retention,
                    ) {
                        "Prepared memory representation retention must match the preparation request."
                    }

                    require(
                        representation.source ==
                            request.source,
                    ) {
                        "Prepared memory representation source must match the preparation request."
                    }

                    require(
                        representation.ownerVisibleReason ==
                            request.ownerVisibleReason,
                    ) {
                        "Prepared memory representation owner-visible reason must match the preparation request."
                    }

                    require(
                        representation.content ==
                            request.content,
                    ) {
                        "Prepared memory representation content must match the preparation request."
                    }
                }

                MemoryRepresentationPreparationStatus.DEFERRED -> {
                    require(request == null) {
                        "Deferred memory representation result must not contain a preparation request."
                    }

                    require(representation == null) {
                        "Deferred memory representation result must not contain a logical-memory representation."
                    }

                    require(error == null) {
                        "Deferred memory representation result must not contain an error."
                    }
                }

                MemoryRepresentationPreparationStatus.FAILED -> {
                    require(request == null) {
                        "Failed memory representation result must not contain a preparation request."
                    }

                    require(representation == null) {
                        "Failed memory representation result must not contain a logical-memory representation."
                    }

                    require(error != null) {
                        "Failed memory representation result requires one error."
                    }
                }
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "Memory representation preparation result and error must use the same trace identity."
            }

            return MemoryRepresentationPreparationResult(
                traceId = traceId,
                status = status,
                request = request,
                representation = representation,
                error = error,
            )
        }
    }
}
