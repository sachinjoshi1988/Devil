package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryRecallEligibilityRecord

/**
 * Immutable Stage 104 result of bounded logical-memory recall-eligibility
 * evaluation.
 *
 * ELIGIBLE contains exactly one MemoryRecallEligibilityRecord.
 *
 * DEFERRED contains neither record nor error.
 *
 * FAILED contains one matching upstream error and no eligibility record.
 *
 * This result performs no storage read, retrieval, recall, exposure,
 * disclosure, persistence, deletion, synchronization, replication,
 * decryption, execution, or external communication.
 */
@ConsistentCopyVisibility
data class MemoryRecallEligibilityResult private constructor(
    val traceId: TraceId,
    val status: MemoryRecallEligibilityStatus,
    val record: MemoryRecallEligibilityRecord?,
    val error: UniversalErrorRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: MemoryRecallEligibilityStatus,
            record: MemoryRecallEligibilityRecord? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryRecallEligibilityResult {
            when (status) {
                MemoryRecallEligibilityStatus.ELIGIBLE -> {
                    require(record != null) {
                        "Eligible memory recall result requires one recall-eligibility record."
                    }

                    require(error == null) {
                        "Eligible memory recall result must not contain an error."
                    }
                }

                MemoryRecallEligibilityStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred memory recall eligibility result must not contain an eligibility record."
                    }

                    require(error == null) {
                        "Deferred memory recall eligibility result must not contain an error."
                    }
                }

                MemoryRecallEligibilityStatus.FAILED -> {
                    require(record == null) {
                        "Failed memory recall eligibility result must not contain an eligibility record."
                    }

                    require(error != null) {
                        "Failed memory recall eligibility result requires one error."
                    }
                }
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "Memory recall eligibility result and error must use the same trace identity."
            }

            return MemoryRecallEligibilityResult(
                traceId = traceId,
                status = status,
                record = record,
                error = error,
            )
        }
    }
}
