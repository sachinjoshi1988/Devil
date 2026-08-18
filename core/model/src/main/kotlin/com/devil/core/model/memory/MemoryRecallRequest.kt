package com.devil.core.model.memory

import com.devil.core.model.common.TraceId

/**
 * Immutable Stage 105 request for bounded constitutional logical-memory recall.
 *
 * This request may be created only from one already-established Stage 104
 * MemoryRecallEligibilityRecord.
 *
 * It preserves:
 *
 * - the constitutional trace identity belonging to the Stage 104 evaluation;
 * - the exact Stage 104 recall-eligibility record;
 * - and therefore the exact Stage 103 continuity record and Stage 101 logical-memory
 *   representation already preserved by that eligibility record.
 *
 * It does not introduce another MemoryId or independently identify a different
 * logical-memory item. The logical-memory identity remains the identity already
 * preserved through the Stage 104 eligibility record.
 *
 * Creating this request does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Memory Authority;
 * - create another logical Memory Domain;
 * - authenticate a subject;
 * - prove ownership;
 * - establish trust;
 * - grant authorization;
 * - establish Owner Mode;
 * - establish High-Security Confirmation;
 * - establish recall eligibility;
 * - read logical memory from storage;
 * - retrieve logical memory;
 * - recall logical memory;
 * - expose logical memory;
 * - disclose logical-memory content;
 * - establish privacy-disclosure permission;
 * - map MemorySensitivity to privacy-disclosure policy;
 * - create, commit, or persist logical memory;
 * - write logical memory to storage;
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
 * - execute an action;
 * - or establish verified success.
 *
 * RECALL_REQUEST != MEMORY_RECALL.
 * RECALL_REQUEST != STORAGE_READ.
 * RECALL_REQUEST != RETRIEVAL_SUCCESS.
 * RECALL_REQUEST != DISCLOSURE_PERMISSION.
 * RECALL_REQUEST != MEMORY_PERSISTENCE.
 */
@ConsistentCopyVisibility
data class MemoryRecallRequest private constructor(
    val traceId: TraceId,
    val eligibility: MemoryRecallEligibilityRecord,
) {
    companion object {

        fun create(
            traceId: TraceId,
            eligibility: MemoryRecallEligibilityRecord,
        ): MemoryRecallRequest {
            return MemoryRecallRequest(
                traceId = traceId,
                eligibility = eligibility,
            )
        }
    }
}
