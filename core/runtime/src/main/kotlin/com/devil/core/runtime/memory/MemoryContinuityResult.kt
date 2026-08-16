package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryContinuityRecord

/**
 * Immutable Stage 103 result of bounded logical-memory continuity evaluation.
 *
 * ESTABLISHED contains exactly one MemoryContinuityRecord.
 *
 * DEFERRED contains neither record nor error.
 *
 * FAILED contains one matching upstream error and no continuity record.
 *
 * This result does not commit, persist, store, expose, recall, delete,
 * synchronize, replicate, encrypt, restore, or otherwise execute
 * logical-memory state.
 */
@ConsistentCopyVisibility
data class MemoryContinuityResult private constructor(
    val traceId: TraceId,
    val status: MemoryContinuityStatus,
    val record: MemoryContinuityRecord?,
    val error: UniversalErrorRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: MemoryContinuityStatus,
            record: MemoryContinuityRecord? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryContinuityResult {
            when (status) {
                MemoryContinuityStatus.ESTABLISHED -> {
                    require(record != null) {
                        "Established memory continuity result requires one continuity record."
                    }

                    require(error == null) {
                        "Established memory continuity result must not contain an error."
                    }
                }

                MemoryContinuityStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred memory continuity result must not contain a continuity record."
                    }

                    require(error == null) {
                        "Deferred memory continuity result must not contain an error."
                    }
                }

                MemoryContinuityStatus.FAILED -> {
                    require(record == null) {
                        "Failed memory continuity result must not contain a continuity record."
                    }

                    require(error != null) {
                        "Failed memory continuity result requires one error."
                    }
                }
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "Memory continuity result and error must use the same trace identity."
            }

            return MemoryContinuityResult(
                traceId = traceId,
                status = status,
                record = record,
                error = error,
            )
        }
    }
}
